package com.integration.test.service.cucumber.steps;

import com.integration.test.service.repository.PostgreSqlRepository;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class TrainingAddStepDefinitions {

    private static final String ADD_TRAINING_URL = "/api/v1/training/add";
    private static final String CHECK_DURATION_URL = "/api/v1/trainer/summary";

    private final PostgreSqlRepository postgreSqlRepository;
    private final MongoTemplate mongoTemplate;
    private final TestRestTemplate restTemplate;

    @Value("${testing.port.gym-crm}")
    private int gymCrmTestingPort;
    @Value("${testing.port.working-hours}")
    private int workingHoursTestingPort;

    private ResponseEntity<String> response;

    public TrainingAddStepDefinitions(PostgreSqlRepository postgreSqlRepository, MongoTemplate mongoTemplate, TestRestTemplate restTemplate) {
        this.postgreSqlRepository = postgreSqlRepository;
        this.mongoTemplate = mongoTemplate;
        this.restTemplate = restTemplate;
    }

    @After
    public void finish() {
        postgreSqlRepository.clearTables("training", "trainee", "trainer", "user", "training_type");
        mongoTemplate.getDb().drop();
    }

    @Given("the PostgreSQL database contains a trainer with username {string}")
    public void thePostgreSQLDatabaseContainsATrainerWithUsername(String trainerUsername) {
        insertUserTrainee();
        insertUserTrainer(trainerUsername);
        insertTrainee();
        insertSpecialization();
        insertTrainer();
    }

    @When("a request is sent to create a training with trainer {string} and duration {int}")
    public void aRequestIsSentToCreateATrainingWithTrainerAndDuration(String trainerUsername, int duration) {
        HttpEntity<String> requestEntity = createPayloadWithHeaders(trainerUsername, duration);

        String url = "http://localhost:" + gymCrmTestingPort + ADD_TRAINING_URL;

        response = restTemplate.postForEntity(url, requestEntity, String.class);
    }

    @Then("the response code is {int}")
    public void theResponseCodeIs(int expectedCode) {
        assertThat(response.getStatusCode().value()).isEqualTo(expectedCode);
    }

    @And("the MongoDB database contains a record with trainer {string}")
    public void theMongoDBDatabaseContainsARecordWithTrainer(String trainerUsername) {
        isRecordExists(trainerUsername);
    }

    private void isRecordExists(String trainerUsername) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(trainerUsername));

        boolean exists = mongoTemplate.exists(query, "trainer");

        assertThat(exists).isTrue();
    }

    private HttpEntity<String> createPayloadWithHeaders(String trainerUsername, int duration) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Request-X", "Gateway");
        headers.add("Authorization", "Bearer valid-token");

        String requestBody = String.format("""
                        {
                            "traineeUsername": "Ivan.Ivanoff",
                            "trainerUsername": "%s",
                            "trainingName": "Morning Motion",
                            "trainingDate": "2025-01-20",
                            "trainingDuration":  %d
                        }
                """, trainerUsername, duration);

        return new HttpEntity<>(requestBody, headers);
    }

    private void insertTrainer() {
        long id = 5L;
        long specializationId = 3L;
        long userId = 5L;

        String insertSql = "INSERT INTO public.trainer (id, specialization, user_id) VALUES (?, ?, ?)";
        postgreSqlRepository.insert(insertSql, id, specializationId, userId);
    }

    private void insertSpecialization() {
        long id = 3L;
        String specialization = "Zumba";

        String insertSql = "INSERT INTO public.training_type (id, training_type_name) VALUES (?, ?)";
        postgreSqlRepository.insert(insertSql, id, specialization);
    }

    private void insertTrainee() {
        long id = 1L;
        LocalDate dateOfBirth = LocalDate.of(2000, 11, 6);
        String address = "Kyiv city";
        long userId = 1L;

        String insertSql = "INSERT INTO public.trainee (id, date_of_birth, address, user_id) VALUES (?, ?, ?, ?)";
        postgreSqlRepository.insert(insertSql, id, dateOfBirth, address, userId);
    }

    private void insertUserTrainer(String trainerUsername) {
        long id = 5L;
        String firstName = "Taras";
        String lastName = "Tarasoff";
        String username = trainerUsername;
        String password = "cf55d388d373bc4657c34c0b53c5c5c4:c6f8f1595aee77795f1854c65e67addbfc3060ed1587490088739ee009a9128a";
        boolean isActive = true;

        String insertSql = "INSERT INTO public.user (id, first_name, last_name, username, password, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        postgreSqlRepository.insert(insertSql, id, firstName, lastName, username, password, isActive);
    }

    private void insertUserTrainee() {
        long id = 1L;
        String firstName = "Ivan";
        String lastName = "Ivanoff";
        String username = "Ivan.Ivanoff";
        String password = "84c30643696a560adb0f47069a74c4ea:8578d17bc197b91e02fc7427f4c912ca4bf5b5fb948fcb69c812461cb2995633";
        boolean isActive = true;

        String insertSql = "INSERT INTO public.user (id, first_name, last_name, username, password, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        postgreSqlRepository.insert(insertSql, id, firstName, lastName, username, password, isActive);
    }

}
