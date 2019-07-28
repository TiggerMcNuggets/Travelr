Feature: Download Trip As iCal
  Description: The purpose of this feature is to test the api endpoint related to downloading a trip and checking the integrity of the file

  Background: The database is populated and I am signed in
    Given I populate the database

  Scenario: Download a trip when not logged in
    Given I provide a tripId of "1"
    And The traveller id is 1
    When I download a trip
    Then I will receive a 401 response

  Scenario: Download my own trip with a valid tripId
    Given I am logged in as an admin user
    And I provide a tripId of "1"
    And The traveller id is 1
    When I download a trip
    Then I will receive a 200 response
    And The content type will be "Optional[text/calendar]"

  Scenario: Download someone elses trip with a valid tripId
    Given I am logged in as a non-admin user
    And I provide a tripId of "1"
    And The traveller id is 2
    When I download a trip
    Then I will receive a 403 response

  Scenario: Download a trip that doesn't exist
    Given I am logged in as an admin user
    And I provide a tripId of "0"
    And The traveller id is 1
    When I download a trip
    Then I will receive a 404 response