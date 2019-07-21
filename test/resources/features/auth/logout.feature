Feature: Logout
  Description: The purpose of this feature is to test api endpoints related to logging out

  Scenario: Log out successfully
    Given I am authenticated
    When I want to logout
    And I send the request
    Then I will receive the response code 200

  Scenario: Log out when not authenticated
    Given I am not authenticated
    When I want to logout
    And I send the request
    Then I will receive the response code 401