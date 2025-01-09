Feature: Update trainer's total training duration
  As a user
  I want to record training durations for trainers
  So that the total duration of their trainings is tracked

  Scenario: Create a new trainer record for a training
    Given there is no trainer with username "Semen.Semenoff" in the database
    When I add a training for trainer "Semen.Semenoff" with duration 45 minutes
    Then a new record for trainer "Semen.Semenoff" should exist
    And the total duration for trainer "Semen.Semenoff" should be 45 minutes

  # TO DO
    #  Scenario: Add training duration to an existing trainer
#    Given the following trainer exists in the database:
#      | username       | totalDuration |
#      | Ivan.Ivanoff   | 120           |
#    When I add a training for trainer "Ivan.Ivanoff" with duration 60 minutes
#    Then the total duration for trainer "Ivan.Ivanoff" should be 180 minutes
