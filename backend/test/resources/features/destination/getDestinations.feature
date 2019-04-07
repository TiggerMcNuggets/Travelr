Feature: GetDestinations
  Description: The purpose of this feature is to test the api endpoint related to getting destinations

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Get destinations successfully
    Given I provide the token "123"
    When I get destinations
    Then I will receive a 200 response

  Scenario: Get destinations without token
    Given I provide the token "64646"
    When I get destinations
    Then I will receive a 401 response
