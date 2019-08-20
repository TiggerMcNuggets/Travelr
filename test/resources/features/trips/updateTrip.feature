Feature: UpdateTrip
  Description: The purpose of this feature is to test the api endpoint related to updating trips

  Scenario: Add two destinations to a trip
    Given I am authenticated
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And I own the trip
      | name         |
      | My First Trip|
    When I want to edit the trip
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


  Scenario: Add two sub trips to a trip
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    When I want to edit the trip
    And The body is
    """
    {
      "name": "My Trip of Trips",
      "nodes": [
          {
              "type": "trip",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1
          },
          {
              "type": "trip",
              "name": "Place Two",
              "ordinal": 2,
              "arrivalDate": 2,
              "departureDate": 2
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The trip is now
      | name             |
      | My Trip of Trips |
    And The trip's sub trips are
      | name           |
      | Place One      |
      | Place Two      |


  Scenario: Remove a sub trip
    Given I am authenticated
    And I own the trip
      | name          |
      | My First Trip |
    When I want to edit the trip
    And The body is
    """
    {
      "name": "My Trip of Trips",
      "nodes": [
          {
              "type": "trip",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1
          },
          {
              "type": "trip",
              "name": "Place Two",
              "ordinal": 2,
              "arrivalDate": 2,
              "departureDate": 2
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The trip is now
      | name             |
      | My Trip of Trips |
    And The trip's sub trips are
      | name           |
      | Place One      |
      | Place Two      |
    When The body is
    """
    {
      "name": "My Trip of Trips",
      "nodes": [
          {
              "id": 2,
              "type": "trip",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1
          },
          {
              "type": "trip",
              "name": "New",
              "ordinal": 2,
              "arrivalDate": 2,
              "departureDate": 2
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The trip is now
      | name             |
      | My Trip of Trips |
    And The trip's sub trips are
      | name           |
      | Place One      |
      | New            |


  Scenario: Unremove a sub trip
    Given I am authenticated
    And I own the trip
      | name          |
      | My First Trip |
    When I want to edit the trip
    And The body is
    """
    {
      "name": "My Trip of Trips",
      "nodes": [
          {
              "type": "trip",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1
          },
          {
              "type": "trip",
              "name": "Place Two",
              "ordinal": 2,
              "arrivalDate": 2,
              "departureDate": 2
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The trip is now
      | name             |
      | My Trip of Trips |
    And The trip's sub trips are
      | name           |
      | Place One      |
      | Place Two      |
    When The body is
    """
    {
      "name": "My Trip of Trips",
      "nodes": [
          {
              "id": 2,
              "type": "trip",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The trip is now
      | name             |
      | My Trip of Trips |
    And The trip's sub trips are
      | name           |
      | Place One      |
    When The body is
    """
    {
      "name": "My Trip of Trips",
      "nodes": [
          {
              "id": "2",
              "type": "trip",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1
          },
          {
              "id": "3",
              "type": "trip",
              "name": "Place Two",
              "ordinal": 2,
              "arrivalDate": 2,
              "departureDate": 2
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The trip is now
      | name             |
      | My Trip of Trips |
    And The trip's sub trips are
      | name           |
      | Place One      |
      | Place Two      |



  Scenario: Add two destinations to a another users trip as admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And I own the trip
      | name         |
      | My First Trip|
    When I want to edit the trip
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


  Scenario: Add two sub trips to another users trip as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And I own the trip
      | name         |
      | My First Trip|
    When I want to edit the trip
    And The body is
    """
    {
      "name": "My Trip of Trips",
      "nodes": [
          {
              "type": "trip",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1
          },
          {
              "type": "trip",
              "name": "Place Two",
              "ordinal": 2,
              "arrivalDate": 2,
              "departureDate": 2
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The trip is now
      | name             |
      | My Trip of Trips |


  Scenario: Update trip with incorrect body
    Given I am authenticated
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And I own the trip
      | name         |
      | My First Trip|
    When I want to edit the trip
    And The body is
    """
    {
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
    Then I will receive the response code 400


  Scenario: Update trip when not logged in
    Given I am not authenticated
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And I own the trip
      | name         |
      | My First Trip|
    When I want to edit the trip
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
    Then I will receive the response code 401


  Scenario: Update destination that is not mine
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And I own the trip
      | name         |
      | My First Trip|
    When I want to edit the trip
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
    Then I will receive the response code 403


  Scenario: Update trip that does not exist
    Given I am authenticated
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And I do not own the trip
    When I want to edit the trip
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
    Then I will receive the response code 404


  Scenario: Update trip where destination does not exist
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    When I want to edit the trip
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
    Then I will receive the response code 404


  Scenario: Add two sub trips that doesn't exist to a trip
    Given I am authenticated
    And I own the trip
      | name          |
      | My First Trip |
    When I want to edit the trip
    And The body is
    """
    {
      "name": "My Trip of Trips",
      "nodes": [
          {
              "id": 2,
              "type": "trip",
              "name": "Place One",
              "ordinal": 1,
              "arrivalDate": 1,
              "departureDate": 1
          },
          {
              "id": 3,
              "type": "trip",
              "name": "Place Two",
              "ordinal": 2,
              "arrivalDate": 2,
              "departureDate": 2
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 404