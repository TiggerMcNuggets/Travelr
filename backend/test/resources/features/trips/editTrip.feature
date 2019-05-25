Feature: EditTrip
  Description: The purpose of this feature is to test the api endpoint related to editing trips

  Background: The database is populated and I am signed in
    Given I populate the database
    And I provide the token "123"

  Scenario: Edit trip with valid name and 2 valid destinations
    Given I provide the token "123"
    And I provide the trip id "1"
    And The traveller id is 1
    And A trip with id "1" exists
    And provide complete trip information
    When I edit a trip
    Then I will receive a 200 response

  Scenario: Edit trip with missing name and 2 valid destinations
    Given I provide the token "123"
    And The traveller id is 1
    And I provide the trip id "1"
    And A trip with id "1" exists
    And I provide trip information missing name
    When I edit a trip
    Then I will receive a 400 response

  Scenario: Edit trip with valid name and 1 valid destination
    Given I provide the token "123"
    And I provide the trip id "1"
    And The traveller id is 1
    And A trip with id "1" exists
    And I provide trip information with one destination
    When I edit a trip
    Then I will receive a 400 response

  Scenario: Edit trip with valid name and 2 consecutive valid identical destination
    Given I provide the token "123"
    And I provide the trip id "1"
    And The traveller id is 1
    And A trip with id "1" exists
    And I provide trip information with two consecutive identical destinations
    When I edit a trip
    Then I will receive a 400 response

  Scenario: Edit trip that does not exist
    Given I provide the token "123"
    And I provide the trip id "100"
    And The traveller id is 1
    And A trip with id "100" does not exist
    Then I will receive a 404 response

  Scenario: Edit trip with valid name and 2 valid destinations but user is not logged in
    Given I do not provide a token
    And I provide the trip id "1"
    And The traveller id is 1
    And A trip with id "1" exists
    And provide complete trip information
    When I edit a trip
    Then I will receive a 401 response

  Scenario: Non-Admin user edits trip made by another user
    Given I am logged in as a non-admin user
    And I provide the trip id "1"
    And The traveller id is 1
    And provide complete trip information
    When I edit a trip
    Then I will receive a 403 response

  Scenario: Admin user edits trip made by another user
    Given I am logged in as an admin user
    And I provide the trip id "2"
    And The traveller id is 2
    And provide complete trip information
    When I edit a trip
    Then I will receive a 200 response

  Scenario: Edit trip with valid name and 2 valid destinations but invalid auth token
    Given I provide the token "kjkj123"
    And The traveller id is 1
    And I provide the trip id "2"
    And A trip with id "2" exists
    Then I will receive a 401 response