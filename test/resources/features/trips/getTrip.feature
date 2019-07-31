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
         "id":1,
         "name":"My First Trip",
         "description":"A trip",
         "user":{
            "firstName":"Test",
            "lastName":"User",
            "id":2
         },
         "destinations":[
            {
               "id":1,
               "depth":0,
               "customName":"Place One",
               "ordinal":1,
               "arrivalDate":1,
               "departureDate":2,
               "destination":{
                  "id":1,
                  "name":"Eiffel Tower",
                  "latitude":5.0,
                  "longitude":5.0,
                  "type":"Landmark",
                  "district":"Paris",
                  "country":"France",
                  "googleId":null
               }
            },
            {
               "id":2,
               "depth":0,
               "customName":"Place Two",
               "ordinal":2,
               "arrivalDate":2,
               "departureDate":3,
               "destination":{
                  "id":2,
                  "name":"Big River",
                  "latitude":3.0,
                  "longitude":3.0,
                  "type":"River",
                  "district":"Canterbury",
                  "country":"New Zealand",
                  "googleId":null
               }
            },
            {
               "id":3,
               "depth":0,
               "customName":"Place Three",
               "ordinal":3,
               "arrivalDate":3,
               "departureDate":4,
               "destination":{
                  "id":3,
                  "name":"Small River",
                  "latitude":3.0,
                  "longitude":3.0,
                  "type":"River",
                  "district":"Otago",
                  "country":"New Zealand",
                  "googleId":null
               }
            }
         ],
         "published":false
      }
      """

Scenario: Get another user's trip as an admin
  Given I am authenticated
  And I am an admin
  And The user exists
    | first | last  | email               | dob |
    | John  | Smith | johnsmith@email.com | 1   |
  And The destinations are
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
  And They own the trip
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
         "id":1,
         "name":"My First Trip",
         "description":"A trip",
         "user":{
            "firstName":"John",
            "lastName":"Smith",
            "id":3
         },
         "destinations":[
            {
               "id":1,
               "depth":0,
               "customName":"Place One",
               "ordinal":1,
               "arrivalDate":1,
               "departureDate":2,
               "destination":{
                  "id":1,
                  "name":"Eiffel Tower",
                  "latitude":5.0,
                  "longitude":5.0,
                  "type":"Landmark",
                  "district":"Paris",
                  "country":"France",
                  "googleId":null
               }
            },
            {
               "id":2,
               "depth":0,
               "customName":"Place Two",
               "ordinal":2,
               "arrivalDate":2,
               "departureDate":3,
               "destination":{
                  "id":2,
                  "name":"Big River",
                  "latitude":3.0,
                  "longitude":3.0,
                  "type":"River",
                  "district":"Canterbury",
                  "country":"New Zealand",
                  "googleId":null
               }
            },
            {
               "id":3,
               "depth":0,
               "customName":"Place Three",
               "ordinal":3,
               "arrivalDate":3,
               "departureDate":4,
               "destination":{
                  "id":3,
                  "name":"Small River",
                  "latitude":3.0,
                  "longitude":3.0,
                  "type":"River",
                  "district":"Otago",
                  "country":"New Zealand",
                  "googleId":null
               }
            }
         ],
         "published":false
      }
      """

Scenario: Get a trip when not logged in
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
  When I want to get the trip
  And I send the request
  Then I will receive the response code 401



Scenario:Get another user's trip as a normal user
  # TODO: Implement, (expect 403)

Scenario: Get a trip that does not exist
  # TODO: Implement, (expect 404)

# TODO: Add scenarios for creating trips related to trips within trips story