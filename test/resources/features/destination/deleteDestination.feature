Feature: DeleteDestination
  Description: The purpose of this feature is to test the api endpoint related to deleting destinations

  Scenario: Delete destination successfully
    Given I am authenticated
    And I own a destination
    When I want to soft delete the destination
    And I send the request
    Then I will receive the response code 200
    And The destination does not exist

  Scenario: Delete destination that is not mine
    Given I am authenticated
    And The global admin owns a destination
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
    And The destination does exist

