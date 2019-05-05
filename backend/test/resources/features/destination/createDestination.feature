Feature: CreateDestination
  Description: The purpose of this feature is to test the api endpoint related to creating a destination

  Background: The database is populated
    Given I populate the database
    And The traveller id is 1
    Then I will receive a 200 response

  Scenario: Create a destination successfully
    Given I provide the token "123"
    And I provide complete destination information
    When I create a destination for user with id 1
    Then I will receive a 201 response
    And I will receive the id 6

  Scenario: Create a destination with incomplete destination information
  Given I provide the token "123"
  And I provide incomplete destination information
  When I create a destination for user with id 1
  Then I will receive a 400 response

  Scenario: Create a destination when session is expired and destination is complete
    Given I provide the token "expired token"
    And I provide complete destination information
    When I create a destination for user with id 1
    Then I will receive a 401 response

  Scenario: Create a destination when session is expired and destination is incomplete
    Given I provide the token "expired token"
    And I provide incomplete destination information
    When I create a destination for user with id 1
    Then I will receive a 401 response

  Scenario: Create a destination when I am not logged in
    And I provide complete destination information
    When I create a destination for user with id 1
    Then I will receive a 401 response
