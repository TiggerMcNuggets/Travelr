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

  Scenario: Use a public destination in a trip
    Given I provide the token "abc"
    And The private destination id is 1
    And I make the destination public
    And The private destination id is 2
    And I make the destination public
    When I provide the token "123"
    And I provide complete trip information
    And I create a trip
    Then the owner of the destination with id 1 is 1

  Scenario: Deletion after merging destinations
    Given I provide the token "123"
    And I create a destination for user id 2
    And I provide the token "abc"
    And I create a destination for user id 3
    When I make the destination public
    Then My first destination should be deleted


