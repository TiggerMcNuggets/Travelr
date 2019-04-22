Feature: GetTrip
  Description: The purpose of this feature is to test the api endpoint related to getting a trip

  Background: The database is populated and I am signed in
    Given I populate the database
    And I provide the token "123"

  Scenario: get my own trip with a valid tripId and it exists
    And I provide a tripId of "1"
    When I get a trip
    Then I will receive a 200 response

  Scenario: get someone else's trip with a valid tripId and it exists
    And I provide a tripId of "2"
    When I get a trip
    Then I will receive a 200 response

  Scenario: get a trip with a invalid tripId and it does not exist
    And I provide a tripId of "100"
    When I get a trip
    Then I will receive a 404 response

  Scenario: get a trip with a invalid tripId and it does not exist
    And I provide a tripId of "1"
    When I get a trip without an auth token
    Then I will receive a 401 response