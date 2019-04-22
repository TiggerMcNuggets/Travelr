Feature: GetTravellerTrips
  Description: The purpose of this feature is to test the api endpoint related to getting all of a travellers trips

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Get all trips for traveller successfully
    Given I provide the token "123"
    When I get all trips for traveller "1"
    Then I will receive a 200 response

  Scenario: Get all trips for traveller successfully
    Given I provide the token "1234"
    When I get all trips for traveller "1"
    Then I will receive a 401 response

  Scenario: Get all trips for traveller successfully
    Given I provide the token "123"
    When I get all trips for traveller "2"
    Then I will receive a 403 response

  Scenario: Get all trips for traveller successfully
    Given I provide the token "123"
    When I get all trips for traveller "200"
    Then I will receive a 404 response