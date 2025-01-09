package com.service.working.hours.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.working.hours.entity.Trainer;
import com.service.working.hours.entity.TrainingRecord;
import com.service.working.hours.repository.TrainerRepository;
import com.service.working.hours.repository.TrainingRecordRepository;
import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class WorkingHoursStepDef {

    private final WebApplicationContext context;

    private final MongoTemplate mongoTemplate;
    private final TrainerRepository trainerRepository;
    private final TrainingRecordRepository trainingRecordRepository;

    private final String url = "/api/v1/trainer/summary";

    private MockMvc mockMvc;
    private ResultActions resultActions;

    @Before
    public void start() {
        System.out.println("Connected to database: " + mongoTemplate.getDb().getName());
    }

    @After
    public void cleanup() {
        mongoTemplate.getDb().drop();
    }

    // TODO
//    @Given("the following trainer exists in the database:")
//    public void the_following_trainer_exists_in_the_database(io.cucumber.datatable.DataTable dataTable) {
//        // Write code here that turns the phrase above into concrete actions
//        // For automatic transformation, change DataTable to one of
//        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//        // Double, Byte, Short, Long, BigInteger or BigDecimal.
//        //
//        // For other transformations you can register a DataTableType.
////        throw new io.cucumber.java.PendingException();
//    }

    @When("I add a training for trainer {string} with duration {int} minutes")
    public void i_add_a_training_for_trainer_with_duration_minutes(String trainerUsername, int duration) throws Exception {
        LocalDate trainingDate = getTestTrainingDate();
        String[] trainerName = trainerUsername.split("\\.");
        boolean isActive = true;
        TrainerWorkloadRequest request = new TrainerWorkloadRequest().username(trainerUsername).firstName(trainerName[0]).lastName(trainerName[1]).isActive(isActive).trainingDate(trainingDate).trainingDuration(duration);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(request);

        resultActions = mockMvc.perform(post(url).content(json).contentType("application/json"));

        resultActions.andExpect(status().isOk());
    }

    @Then("the total duration for trainer {string} should be {int} minutes")
    public void the_total_duration_for_trainer_should_be_minutes(String trainerUsername, int expectedDuration) {
        LocalDate trainingDate = getTestTrainingDate();

        int actualDuration = trainingRecordRepository.findByTrainerUsernameAndYearAndMonth(
                        trainerUsername,
                        trainingDate.getYear(),
                        trainingDate.getMonthValue())
                .map(TrainingRecord::getDurationSummary).orElse(0);

        assertEquals(expectedDuration, actualDuration);
    }

    @Given("there is no trainer with username {string} in the database")
    public void there_is_no_trainer_with_username_in_the_database(String trainerUsername) {
        isTrainerNotExistsInDB(trainerUsername);
    }

    @Then("a new record for trainer {string} should exist")
    public void a_new_record_for_trainer_should_exist(String trainerUsername) {
        isTrainerExistsInDB(trainerUsername);
    }

    private LocalDate getTestTrainingDate() {
        return LocalDate.of(2025, 1, 1);
    }

    private Trainer createNewActiveTrainerWithUsername(String trainerUsername) {
        String[] trainerName = trainerUsername.split("\\.");

        return Trainer.builder().username(trainerUsername).firstName(trainerName[0]).lastName(trainerName[1]).isActive(true).build();
    }

    private void isTrainerNotExistsInDB(String trainerUsername) {
        if (trainerRepository.findById(trainerUsername).isPresent()) {
            throw new AssertionError("Trainer with username " + trainerUsername + " already exists in the database.");
        }
    }

    private void isTrainerExistsInDB(String trainerUsername) {
        if (trainerRepository.findById(trainerUsername).isEmpty()) {
            throw new AssertionError("Trainer with username " + trainerUsername + " not exists in the database.");
        }
    }
}

