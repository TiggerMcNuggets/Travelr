Feature: GetDestinations
  Description: The purpose of this feature is to test the api endpoint related to getting destinations

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Get my own destinations as admin
    Given I provide the token "123"
    And The traveller id is 1
    When I get destinations
    Then I will receive a 200 response

  Scenario: Get others destinations as admin
    Given I provide the token "123"
    And The traveller id is 2
    When I get destinations
    Then I will receive a 200 response

#  Scenario: Get my own destinations as normal user
#    Given I provide the token "abc"
#    And The traveller id is 4
#    When I get destinations
#    Then I will receive a 200 response

  Scenario: Get others destinations as normal user
    Given I provide the token "abc"
    And The traveller id is 2
    When I get destinations
    Then I will receive a 403 response

  Scenario: Get destinations without token
    Given I provide the token "64646"
    When I get destinations
    Then I will receive a 401 response