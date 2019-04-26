Feature: AdminViewUserDestinations
  Description: The purpose of the feature is to check whether an admin would receive all destination of the user whose destinations are requested.

  Background: The database is populated and I am signed in
    Given I populate the database
    And I provide the token "123"

  Scenario: View destinations of a user using an admin account
    Given: I provide the token "123"
    And The traveller id is 2
    When I get destination information
    Then I will receive a 200 response
    And I will receive the id 2
    And I will receive the email "john@test.com"
