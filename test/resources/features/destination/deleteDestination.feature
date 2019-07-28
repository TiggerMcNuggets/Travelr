Feature: DeleteDestination
  Description: The purpose of this feature is to test the api endpoint related to deleting destinations

  Scenario: Delete destination successfully
    Given I am authenticated
    And I own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to soft delete the destination
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
        "id": 1,
        "deleted": true
      }
      """
    And The destination does not exist

  Scenario: Delete destination successfully for a user as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to soft delete the destination
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
        "id": 1,
        "deleted": true
      }
      """
    And The destination does not exist

  Scenario: Delete destination when unauthenticated
    Given I am not authenticated
    And I own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to soft delete the destination
    And I send the request
    Then I will receive the response code 401
    And The destination does exist

  Scenario: Delete destination that is not mine
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to soft delete the destination
    And I send the request
    Then I will receive the response code 403
    And The destination does exist

  Scenario: Delete destination that does not exist
    Given I am authenticated
    And I do not own a destination
    When I want to soft delete the destination
    And I send the request
    Then I will receive the response code 404

