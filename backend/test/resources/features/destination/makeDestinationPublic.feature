Feature: Make Destination Public
  Description: Tests the api endpoint related to making a destination public

  Background: The database is populated
    Given I populate the database

  Scenario: Make a destination public successfully
    Given I provide the token "123"
    And The private destination id is 1
    When I make the destination public
    Then I will receive a 201 response

  Scenario: Make a destination public that does not exist
    Given I provide the token "123"
    And The private destination id is 100
    When I make the destination public
    Then I will receive a 404 response