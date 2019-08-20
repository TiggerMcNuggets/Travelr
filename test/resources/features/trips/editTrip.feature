Feature: EditTrip
Description: The purpose of this feature is to test the api endpoint related to editing trips

  Scenario: Edit a trip when not logged in
    Given I am not authenticated
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
      | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip contains the trip destinations
      | ordinal | customName | arrivalDate | departureDate | destinationId| depth |
      |   1     | Place One  | 1           | 2             | 1            | 0     |
      |   2     | Place Two  | 2           | 3             | 2            | 0     |
      |   3     | Place Three| 3           | 4             | 3            | 0     |
    When I want to edit the trip
    And The body is
    """
    {
      "name": "My Trip",
      "description": "A trip",
      "destinations": [
          {
              "customName": "Place One",
              "ordinal": 1,
              "depth": 1,
              "arrivalDate": 1,
              "departureDate": 1,
              "destination": {
                  "id": 1
              }
          },
          {
              "customName": "Place Two",
              "ordinal": 2,
              "depth": 2,
              "arrivalDate": 2,
              "departureDate": 2,
              "destination": {
                  "id": 3
              }
          },
          {
              "customName": "Place Three",
              "ordinal": 3,
              "depth": 3,
              "arrivalDate": 3,
              "departureDate": 3,
              "destination": {
                  "id": 2
              }
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 401


  Scenario: Edit another user's trip as a normal user
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
      | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip contains the trip destinations
      | ordinal | customName | arrivalDate | departureDate | destinationId| depth |
      |   1     | Place One  | 1           | 2             | 1            | 0     |
      |   2     | Place Two  | 2           | 3             | 2            | 0     |
      |   3     | Place Three| 3           | 4             | 3            | 0     |
    When I want to edit the trip
    And The body is
    """
    {
      "name": "My Trip",
      "description": "A trip",
      "destinations": [
          {
              "customName": "Place One",
              "ordinal": 1,
              "depth": 1,
              "arrivalDate": 1,
              "departureDate": 1,
              "destination": {
                  "id": 1
              }
          },
          {
              "customName": "Place Two",
              "ordinal": 2,
              "depth": 2,
              "arrivalDate": 2,
              "departureDate": 2,
              "destination": {
                  "id": 3
              }
          },
          {
              "customName": "Place Three",
              "ordinal": 3,
              "depth": 3,
              "arrivalDate": 3,
              "departureDate": 3,
              "destination": {
                  "id": 2
              }
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 403

  Scenario: Edit a trip that does not exist
    Given I am authenticated
    And I do not own the trip

    When I want to edit the trip
    And The body is
    """
    {
      "name": "My Trip",
      "description": "A trip",
      "destinations": [
          {
              "customName": "Place One",
              "ordinal": 1,
              "depth": 1,
              "arrivalDate": 1,
              "departureDate": 1,
              "destination": {
                  "id": 1
              }
          },
          {
              "customName": "Place Two",
              "ordinal": 2,
              "depth": 2,
              "arrivalDate": 2,
              "departureDate": 2,
              "destination": {
                  "id": 3
              }
          },
          {
              "customName": "Place Three",
              "ordinal": 3,
              "depth": 3,
              "arrivalDate": 3,
              "departureDate": 3,
              "destination": {
                  "id": 2
              }
          }
      ]
    }
    """
    And I send the request
    Then I will receive the response code 404
