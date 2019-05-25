Feature: DeleteTrip
  Description: The purpose of this feature is to test the api endpoint related to deleting trips

  Background: The database is populated and I am signed in and a trip exists
    Given I populate the database
    And I provide the token "123"

  Scenario: Soft delete trip with 200 response
    Given I provide the trip id "1"
    And The traveller id is 1
    And A trip with id "1" exists
    When I soft delete trip with id 1
    Then I will receive a 200 response


  Scenario: Soft delete trip and trip no longer present
    Given I provide the trip id "1"
    And The traveller id is 1
    And A trip with id "1" exists
    When I soft delete trip with id 1
    And I get all trips for traveller
    Then I will receive a 200 response
    And in the list of trips the trip with id 1 will not be present.


  Scenario: Undo soft delete trip and trip is present again
    Given I provide the trip id "1"
    And The traveller id is 2
    And A trip with id "1" exists
    When I soft delete trip with id 1
    And I undo the deletion status on trip with id 1
    And I get all trips for traveller
    Then I will receive a 200 response
    And in the list of trips the trip with id 1 will be present

    

