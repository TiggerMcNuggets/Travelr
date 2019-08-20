Feature: UpdateTrip
  Description: The purpose of this feature is to test the api endpoint related to updating trips

  Scenario: Add two destinations to a trip
    Given I am authenticated
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
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
      | name    | description |
      | My Trip | My Trip      |
    And The trip's destinations are
      | ordinal | customName | arrivalDate | departureDate | destinationId|
      |   1     | Place One  | 1           | 1             | 1            |
      |   2     | Place Two  | 2           | 2             | 2            |

  Scenario: Add two sub trips to a trip
    Given I am authenticated
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
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
      | name             | description |
      | My Trip of Trips | My Trip     |