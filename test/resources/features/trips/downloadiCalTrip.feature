Feature: Download Trip As iCal
  Description: The purpose of this feature is to test the api endpoint related to downloading a trip and checking the integrity of the file

  Background: The database is populated and I am signed in
    Given I populate the database
    And I provide the token "123"
    And The traveller id is 1

  Scenario: Download my own trip with a valid tripId
    Given I provide a tripId of "1"
    When I download a trip
    Then I will receive a 200 response