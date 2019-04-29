Feature: CreateTrip
  Description: The purpose of this feature is to test the api endpoint related to creating trips

  Background: The database is populated and I am signed in
    Given I populate the database
    And I provide the token "123"

  Scenario: Create trip with 2 valid destinations and valid name
    And I provide complete trip information
    When I create a trip
    Then I will receive a 201 response
    And I will receive the id 3

  Scenario: Create trip with no name and no destinations
    Given I provide incomplete trip information
    When I create a trip
    Then I will receive a 400 response

  Scenario: Create trip with no name but valid destinations
    Given I provide trip information where I have no trip name
    When I create a trip
    Then I will receive a 400 response

  Scenario: Create trip with valid name valid destinations but one destination appears consecutively in the list
    Given I provide trip information where the same destination appears consecutively
    When I create a trip
    Then I will receive a 400 response

  Scenario: Create trip with valid name valid destination but only one destination given
    Given I provide trip information where only one destination is given
    When I create a trip
    Then I will receive a 400 response

  Scenario: Create trip with no auth token header
    Given I provide complete trip information
    When I create a trip omitting auth token as header
    Then I will receive a 401 response

  Scenario: Create trip with invalid auth token
    Given I provide the token ""
    And I provide complete trip information
    When I create a trip
    Then I will receive a 401 response

