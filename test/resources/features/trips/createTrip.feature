Feature: Create Trip
  Description: The purpose of this feature is to test the api endpoint related to creating trips


  Scenario: Create a trip successfully
    Given I am authenticated
    When I want to create a trip
    And The body is
    """
    {
      "name": "My Trip"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
    """
    {
      "id": 1
    }
    """

  Scenario: Create a trip for another user as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to create a trip
    And The body is
    """
    {
      "name": "My Trip"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
    """
    {
      "id": 1
    }
    """


  Scenario: Create a trip with two destinations
    Given I am authenticated
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    When I want to create a trip
    And The body is
    """
    {
      "name": "My Trip"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
    """
    {
      "id": 1
    }
    """
    When I want to add destinations to my trip which has id 1
    And The body is
    """
    {
      "name": "My Trip",
      "nodes": [
          {
              "type": "destination",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1,
              "destination": {
                  "id": 1
              }
          },
          {
              "type": "destination",
              "name": "Place Two",
              "ordinal": 2,
              "arrivalDate": 2,
              "departureDate": 2,
              "destination": {
                  "id": 2
              }
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The trip is now
      | name    |
      | My Trip |
    And The trip's destinations are
      | ordinal | customName | arrivalDate | departureDate | destinationId|
      |   1     | Place One  | 1           | 1             | 1            |
      |   2     | Place Two  | 2           | 2             | 2            |





  Scenario: Create a trip with missing name field
    Given I am authenticated
    When I want to create a trip
    And The body is
    """
    {

    }
    """
    And I send the request
    Then I will receive the response code 400

  Scenario: Create a trip when not logged in
    Given I am not authenticated
    When I want to create a trip
    And The body is
    """
    {
      "name": "My Trip"
    }
    """
    And I send the request
    Then I will receive the response code 401


  Scenario: Create a trip for another user as a normal user
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to create a trip
    And The body is
    """
    {
      "name": "My Trip"
    }
    """
    And I send the request
    Then I will receive the response code 403



  Scenario: Create a trip for a user that does not exist as an admin
    Given I am authenticated
    And I am an admin
    And The user does not exist
    When I want to create a trip
    And The body is
    """
    {
      "name": "My Trip"
    }
    """
    And I send the request
    Then I will receive the response code 403

  Scenario: Create a trip with invalid destinations
    Given I am authenticated
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    When I want to create a trip
    And The body is
    """
    {
      "name": "My Trip"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
    """
    {
      "id": 1
    }
    """
    When I want to add destinations to my trip which has id 1
    And The body is
    """
    {
      "name": "My Trip",
      "nodes": [
          {
              "type": "destination",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1,
              "destination": {
                  "id": 3
              }
          },
          {
              "type": "destination",
              "name": "Place Two",
              "ordinal": 2,
              "arrivalDate": 2,
              "departureDate": 2,
              "destination": {
                  "id": 4
              }
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 404

