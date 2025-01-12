package com.gym.crm.application.cucumber.steps;

import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.User;
import com.gym.crm.application.repository.TraineeRepository;
import com.gym.crm.application.repository.UserRepository;
import io.cucumber.core.internal.com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class TraineeControllerStepDefs {

    private final WebApplicationContext context;
    private final UserRepository userRepository;
    private final TraineeRepository traineeRepository;
    private MockMvc mockMvc;
    private ResultActions resultActions;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Given("there is no trainee with username {string} in database")
    public void ensureTraineeDoesNotExist(String traineeUsername) {
        isTraineeDoesNotExistInDb(traineeUsername);
    }

    @Given("there is a trainee with username {string} in database already")
    public void ensureTraineeDoesExist(String traineeUsername) {
        isTraineeDoesExistInDb(traineeUsername);
    }

    @When("I send a POST request to {string} with the following data:")
    public void sendPostRequest(String url, List<Map<String, String>> data) throws Exception {
        String payload = createPayload(data);

        resultActions = mockMvc.perform(
                post(url)
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Then("the response status should be {int}")
    public void verifyResponseStatus(int expectedStatus) throws Exception {
        resultActions.andExpect(status().is(expectedStatus));
    }

    @Then("the response body should contain:")
    public void verifyResponseBody(Map<String, String> expectedFields) throws Exception {
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        assertNotNull(responseBody, "Response body is null");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> actualResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        checkResponse(expectedFields, actualResponse);
    }

    private void checkResponse(Map<String, String> expectedFields, Map<String, String> actualResponse) {
        expectedFields.forEach((field, expectedValue) -> {
            if ("(not empty)".equals(expectedValue)) {
                assertTrue(
                        actualResponse.containsKey(field) && !actualResponse.get(field).isEmpty(),
                        "Expected field " + field + " to be not empty"
                );
            } else {
                assertEquals(
                        expectedValue,
                        actualResponse.get(field),
                        "Mismatch in field: " + field
                );
            }
        });
    }

    private String createPayload(List<Map<String, String>> data) {
        return String.format(
                "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"dateOfBirth\":\"%s\",\"address\":\"%s\"}",
                data.get(0).get("firstName"), data.get(0).get("lastName"), data.get(0).get("dateOfBirth"), data.get(0).get("address")
        );
    }

    private void isTraineeDoesNotExistInDb(String traineeUsername) {
        if (traineeRepository.findByUserUsername(traineeUsername) != null) {
            throw new AssertionError("Trainee with username " + traineeUsername + " already exists in the database.");
        }
    }

    private void isTraineeDoesExistInDb(String traineeUsername) {
        if (traineeRepository.findByUserUsername(traineeUsername) == null) {
            throw new AssertionError("Trainee with username " + traineeUsername + " doesn't exist in the database.");
        }
    }

    private void createNewTrainee(String traineeUsername) {
        String[] traineeName = traineeUsername.split("\\.");
        User user = User.builder()
                .firstName(traineeName[0])
                .lastName(traineeName[1])
                .username(traineeUsername)
                .password("password")
                .isActive(true).build();
        userRepository.save(user);
        Trainee trainee = Trainee.builder()
                .id(user.getId())
                .user(user)
                .address("address")
                .dateOfBirth(LocalDate.of(2000, 11, 6)).build();
        traineeRepository.save(trainee);
    }
}
