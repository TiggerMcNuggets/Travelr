Feature: CreateTraveller
  Description: The purpose of this feature is to test the api endpoint related to creating travellers

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Signs up successfully
    Given I provide a complete traveller json
    And I provide the email "new@email.com"
    When I signup
    Then I will receive a 201 response
    And I will receive the id 7

  Scenario: Signs up with missing fields
    Given I provide an incomplete traveller json
    When I signup
    Then I will receive a 400 response

  Scenario: I provide a complete traveller json
    Given I provide a complete traveller json
    And I provide the email "admin@test.com"
    When I signup
    Then I will receive a 400 response