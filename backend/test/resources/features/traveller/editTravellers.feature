Feature: EditTravellers
  Description: The purpose of this feature is to test the api endpoint related to editing a traveller

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Edit my own profile as admin user
    Given I provide the token "123"
    And I provide a complete editTraveller json
    And The traveller id is 1
    When I edit the traveller
    Then I will receive a 200 response

  Scenario: Edit another user's profile as admin user
    Given I provide the token "123"
    And I provide a complete editTraveller json
    And The traveller id is 2
    When I edit the traveller
    Then I will receive a 200 response

  Scenario: Edit my own profile as normal user
    Given I provide the token "abc"
    And I provide a complete editTraveller json
    And The traveller id is 3
    When I edit the traveller
    Then I will receive a 200 response

  Scenario: Edit traveller with missing fields
    Given I provide the token "123"
    And I provide an incomplete editTraveller json
    And The traveller id is 1
    When I edit the traveller
    Then I will receive a 400 response

  Scenario: Edit traveller with invalid token
    Given I provide the token "345345"
    And I provide a complete editTraveller json
    And The traveller id is 1
    When I edit the traveller
    Then I will receive a 401 response

  Scenario: Editing another traveller as a normal user
    Given I provide the token "abc"
    And I provide a complete editTraveller json
    And The traveller id is 1
    When I edit the traveller
    Then I will receive a 403 response