Feature: GetTravellerTrips
  Description: The purpose of this feature is to test the api endpoint related to getting all of a travellers trips

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Get all trips for traveller successfully
    Given I provide the token "123"
    And The traveller id is 1
    When I get all trips for traveller
    Then I will receive a 200 response

  Scenario: Get all trips for traveller that is not logged in
    Given I provide the token "1234"
    And The traveller id is 1
    When I get all trips for traveller
    Then I will receive a 401 response

  Scenario: Get all trips for traveller that does not exist
    Given I provide the token "123"
    And The traveller id is 200
    When I get all trips for traveller
    Then I will receive a 404 response