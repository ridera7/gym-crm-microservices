Feature: Trainee Controller tests

  Scenario: Successfully register a new trainee
    Given there is no trainee with username "Ivan.Ivanoff" in database
    When I send a POST request to "/api/v1/trainee/register" with the following data:
      | firstName | lastName  | dateOfBirth | address |
      | Ivan      | Ivanoff   | 2000-11-06  | Kyiv    |
    Then the response status should be 200
    And the response body should contain:
      | username | Ivan.Ivanoff |
      | password | (not empty)  |

  Scenario: Successfully register a new trainee
    Given there is a trainee with username "Ivan.Ivanoff" in database already
    When I send a POST request to "/api/v1/trainee/register" with the following data:
      | firstName | lastName  | dateOfBirth | address |
      | Ivan      | Ivanoff   | 2000-11-06  | Kyiv    |
    Then the response status should be 200
    And the response body should contain:
      | username | Ivan.Ivanoff1 |
      | password | (not empty)   |