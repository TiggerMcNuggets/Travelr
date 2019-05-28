Feature: GetTravellers
  Description: The purpose of this feature is to test the api endpoint related to getting travellers

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Get travellers successfully
    Given I provide the token "123"
    When I get travellers
    Then I will receive a 200 response

  Scenario: Get travellers without token
    Given I provide the token "64646"
    When I get travellers
    Then I will receive a 401 response

