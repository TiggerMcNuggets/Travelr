Feature: AdminCreateTraveller
  Description: The purpose of this feature is to test the api endpoint related to creating travellers as an admin

  Background: The database is populated and I am signed in
    Given I populate the database
    And I provide the token "123"

  Scenario: Create a normal traveller with valid traveller json
    Given I provide a complete normal traveller json
    And I provide an email "user@email.com"
    When I create traveller
    Then I will receive a 201 response

  Scenario: Create an admin traveller with valid traveller json
    Given I provide a complete admin traveller json
    And I provide an email "admin@email.com"
    When I create traveller
    Then I will receive a 201 response

  Scenario: Create an admin traveller with invalid traveller json missing fields
    Given I provide an incomplete traveller json
    When I create traveller
    Then I will receive a 400 response

  Scenario: Create a traveller with complete traveller json with existing email of a current user
    Given I provide a complete traveller json
    And I provide an email "adam@test.com"
    When I create traveller
    Then I will receive a 400 response