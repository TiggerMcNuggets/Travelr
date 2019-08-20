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
    | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
    | Destination 3 | destination | 1           | 2             | 0            | 1             |
  When I want to get the trip
  And I send the request
  Then I will receive the response code 200
  And I will receive the response body
  """
  {"navigation":[
      {"id":1,"name":"My First Trip"}
    ],
    "trip": {
      "id":1,
      "name":"My First Trip",
      "nodes":[
        {
        "id":2,
        "name":"Destination 3",
        "arrivalDate":1,
        "departureDate":2,
        "ordinal":0,
        "destination":{
          "id":1,
          "name":"Eiffel Tower",
          "latitude":5.0,
          "longitude":5.0,
          "type":"Landmark",
          "district":"Paris",
          "country":"France",
          "googleId":null
         },
        "type":"destination"
        }
       ]
      },
      "root":{
        "id":1,
        "name":"My First Trip",
        "user":{
          "firstName":"Test",
          "lastName":"User",
          "id":2
          }
        }
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
    | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
    | Eiffel Tower  | destination | 1           | 2             | 0            | 1             |
    | Big River     | destination | 4           | 6             | 1            | 2             |
    | Small River   | destination | 10          | 22            | 2            | 3             |
  When I want to get the trip
  And I send the request
  Then I will receive the response code 200
  And I will receive the response body
  """
    {
    "navigation":[
      {
        "id":1,
        "name":"My First Trip"
      }],
    "trip": {
      "id":1,
      "name":"My First Trip",
      "nodes":[
        {
        "id":2,
        "name":"Eiffel Tower",
        "arrivalDate":1,
        "departureDate":2,
        "ordinal":0,
        "destination":{
          "id":1,
          "name":"Eiffel Tower",
          "latitude":5.0,
          "longitude":5.0,
          "type":"Landmark",
          "district":"Paris",
          "country":"France",
          "googleId":null
        },
        "type":"destination"
       },
       {
        "id":3,
        "name":"Big River",
        "arrivalDate":4,
        "departureDate":6,
        "ordinal":1,
        "destination": {
          "id":2,
          "name":"Big River",
          "latitude":3.0,
          "longitude":3.0,
          "type":"River",
          "district":"Canterbury",
          "country":"New Zealand",
          "googleId":null
        },
        "type":"destination"
      },
      {
        "id":4,
        "name":"Small River",
        "arrivalDate":10,
        "departureDate":22,
        "ordinal":2,
        "destination": {
          "id":3,
          "name":"Small River",
          "latitude":3.0,
          "longitude":3.0,
          "type":"River",
          "district":"Otago",
          "country":"New Zealand",
          "googleId":null
         },
         "type":"destination"
        }]},
      "root": {
        "id":1,
        "name":"My First Trip",
        "user": {
          "firstName":"John",
          "lastName":"Smith",
          "id":3
        }
      }
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
    | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
    | Eiffel Tower  | destination | 1           | 2             | 0            | 1             |
    | Big River     | destination | 4           | 6             | 1            | 2             |
    | Small River   | destination | 10          | 22            | 2            | 3             |
  When I want to get the trip
  And I send the request
  Then I will receive the response code 401



Scenario:Get another user's trip as a normal user
  Given I am authenticated
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
    | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
    | Eiffel Tower  | destination | 1           | 2             | 0            | 1             |
    | Big River     | destination | 4           | 6             | 1            | 2             |
    | Small River   | destination | 10          | 22            | 2            | 3             |
  When I want to get the trip
  And I send the request
  Then I will receive the response code 403

Scenario: Get a trip that does not exist
  Given I am authenticated
  And I do not own the trip
  When I want to get the trip
  And I send the request
  Then I will receive the response code 404

# TODO: Add scenarios for creating trips related to trips within trips story