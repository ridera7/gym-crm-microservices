Feature: Verify microservices interaction for creating a training session

  Scenario: Successfully create a training session with a trainer
    Given the PostgreSQL database contains a trainer with username "Taras.Tarasoff"
    When a request is sent to create a training with trainer "Taras.Tarasoff" and duration 45
    Then the response code is 200
    And the MongoDB database contains a record with trainer "Taras.Tarasoff"
