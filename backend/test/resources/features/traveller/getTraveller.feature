Feature: GetTraveller
  Description: The purpose of this feature is to test the api endpoint related to getting one travellers information

  Background: User is authenticated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Get traveller information with valid ID
    Given I provide the token "123"
    And The traveller id is 1
    When I get traveller information
    Then I will receive a 200 response
    And I will receive the id 1

  Scenario: Get traveller information with invalid ID
    Given I provide the token "123"
    And The traveller id is 2233
    When I get traveller information
    Then I will receive a 404 response

  Scenario: Get traveller information with no token
    Given I provide the token ""
    And The traveller id is 1
    When I get travellers
    Then I will receive a 401 response