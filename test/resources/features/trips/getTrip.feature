Feature: GetTrip
  Description: The purpose of this feature is to test the api endpoint related to getting a trip

Scenario: Get a trip successfully
  Given I am authenticated
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
  When I want to get the trip
  And I send the request
  Then I will receive the response code 200
  And I will receive the response body
      """
      {
        "id": 1,
        "name": "Eiffel Tower",
        "latitude": 5.0,
        "longitude": 5.0,
        "type": "Landmark",
        "district": "Paris",
        "country": "France",
        "isPublic": false,
        "ownerId": 2,
        "travellerTypes": []
      }
      """

Scenario: Get another user's trip as an admin
  # TODO: Implement, (expect 200)

Scenario: Get a trip when not logged in
  # TODO: Implement, (expect 401)

Scenario:Get another user's trip as a normal user
  # TODO: Implement, (expect 403)

Scenario: Get a trip that does not exist
  # TODO: Implement, (expect 404)

# TODO: Add scenarios for creating trips related to trips within trips story