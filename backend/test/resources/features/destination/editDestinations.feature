Feature: EditDestinations
  Description: The purpose of this feature is to test the api endpoint related to editing a destination

  Background: The database is populated
    Given I populate the database
    And I provide the token "123"

  Scenario: Edit destination successfully
    Given I provide valid destination edit values
    And The destination id is 1
    When I edit the destination
    Then I will receive a 200 response

  Scenario: Edit destination with missing fields
    Given I provide the token "123"
    And I provide invalid destination edit values
    And The destination id is 1
    When I edit the destination
    Then I will receive a 400 response

  Scenario: Edit destination with invalid token
    Given I provide the token "345345"
    And I provide valid destination edit values
    And The destination id is 1
    When I edit the destination
    Then I will receive a 401 response

  Scenario: Editing a destination which does not exist
    Given I provide the token "123"
    And I provide valid destination edit values
    And The destination id is 4
    When I edit the destination
    Then I will receive a 403 response

  Scenario: Editing a destination which does not belong to the signed in user.
    Given I provide the token "123"
    And I provide valid destination edit values
    And Another user has created a destination with id 4
    And The destination id is 4
    When I edit the destination
    Then I will receive a 403 response