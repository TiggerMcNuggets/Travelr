Feature: DeleteDestination
  Description: The purpose of this feature is to test the api endpoint related to deleting destinations

  Background: The database is populated and I am signed in and a trip exists
    Given I populate the database
    And I provide the token "123"

  Scenario: Soft delete destination with 200 response
    Given The destination id is 1
    And The traveller id is 2
    When I soft delete the destination with id 1
    Then I will receive a 200 response


  Scenario: Soft delete destination and destination no longer present
    Given The destination id is 1
    And The traveller id is 2
    When I soft delete the destination with id 1
    And I get destinations
    Then I will receive a 200 response
    And in the list of destinations the destination with id 1 will not be present.


  Scenario: Undo soft delete destination and destination is present again
    Given The destination id is 1
    And The traveller id is 2
    When I soft delete the destination with id 1
    And I undo the deletion status on destination with id 1
    And I get destinations
    Then I will receive a 200 response
    And in the list of destinations the destination with id 1 will be present.