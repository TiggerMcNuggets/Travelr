Feature: Logout
  Description: The purpose of this feature is to test api endpoints related to logging out

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Log out successfully
    Given I provide the token "123"
    When I logout
    Then I will receive a 200 response

  Scenario: Log out with an invalid token
    Given I provide the token "234234234"
    When I logout
    Then I will receive a 401 response