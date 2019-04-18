Feature: GetDestinations
  Description: The purpose of this feature is to test the api endpoint related to getting a single destination

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Get destination successfully
    Given I provide the token "123"
    And The destination id is 1
    When I get destination information
    Then I will receive a 200 response
    And I will receive the id 1

  Scenario: Get destination information with no token
    Given I provide the token ""
    And The destination id is 1
    When I get destination information
    Then I will receive a 401 response


#    this test will need to be updated when we have created public
#    and private destinations as any user can retrieve a public destination
  Scenario: Get destination information with user that is not
        the creator of the destination
    Given I provide the token "123"
    And The destination id is 4
    When I get destination information
    Then I will receive a 403 response

  Scenario: Get a destination that does not exist
    Given I provide the token "123"
    And The destination id is 200
    When I get destination information
    Then I will receive a 404 response

