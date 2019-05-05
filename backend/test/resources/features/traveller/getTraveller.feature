Feature: GetTraveller
  Description: The purpose of this feature is to test the api endpoint related to getting one travellers information

  Background: User is authenticated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Get traveller information with my valid ID as admin
    Given I provide the token "123"
    And The traveller id is 1
    When I get traveller information
    Then I will receive a 200 response
    And I will receive the id 1
    And I will receive the email "admin@test.com"

  Scenario: Get traveller information with my valid ID as not an admin
    Given I provide the token "abc"
    And The traveller id is 2
    When I get traveller information
    Then I will receive a 200 response
    And I will receive the id 2
    And I will receive the email "john@test.com"

  Scenario: Get traveller information with my valid ID as not an admin
    Given I provide the token "abc"
    And The traveller id is 3
    When I get traveller information
    Then I will receive a 200 response
    And I will receive the id 3
    And I will receive not receive the email back

  Scenario: Get traveller information with other user valid ID as an admin
    Given I provide the token "123"
    And The traveller id is 2
    When I get traveller information
    Then I will receive a 200 response
    And I will receive the id 2
    And I will receive the email "john@test.com"

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