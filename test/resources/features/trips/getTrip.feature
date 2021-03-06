Feature: GetTrip
  Description: The purpose of this feature is to test the api endpoint related to getting a trip

Scenario: Get a trips album
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
  And the trip is associated with the user group
    | name         | description         |
    | Team 300     | The best team eva   |
  And the group has the members, ownership and statuses
    | first | last   | email               | dob | status     | owner |
    | Joe   | Bloggs | joebloggs@email.com | 1   | GOING      | false |
    | John  | Smith  | johnsmith@email.com | 1   | MAYBE      | true  |
    | Mary  | Smith  | marysmith@email.com | 1   | NOT GOING  | false |
  When I want to get the trips album
  And I send the request
  Then I will receive the response code 200
  And I will receive the response body
  """
  {"mediaItems":[]}
  """


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
  And the trip is associated with the user group
    | name         | description         |
    | Team 300     | The best team eva   |
  And the group has the members, ownership and statuses
    | first | last   | email               | dob | status     | owner |
    | Joe   | Bloggs | joebloggs@email.com | 1   | GOING      | false |
    | John  | Smith  | johnsmith@email.com | 1   | MAYBE      | true  |
    | Mary  | Smith  | marysmith@email.com | 1   | NOT_GOING  | false |
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
        }
     ],
     "trip":{
        "id":1,
        "name":"My First Trip",
        "usergroup":[
           {"userId":2,"firstName":"Test","lastName":"User","status":"NOT ANSWERED","owner":true},
           {
              "userId":3,
              "firstName":"Joe",
              "lastName":"Bloggs",
              "status":"GOING",
              "owner":false
           },
           {
              "userId":4,
              "firstName":"John",
              "lastName":"Smith",
              "status":"MAYBE",
              "owner":true
           },
           {
              "userId":5,
              "firstName":"Mary",
              "lastName":"Smith",
              "status":"NOT_GOING",
              "owner":false
           }
        ],
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
              "type":"destination",
              "usergroup": [{
                "userId":2,
                "firstName":"Test",
                "lastName":"User",
                "status":"NOT ANSWERED",
                "owner":true
                },
                {
                  "userId":3,
                  "firstName":"Joe",
                  "lastName":"Bloggs",
                  "status":"NOT ANSWERED",
                  "owner":false
                 },
                 {
                  "userId":4,
                  "firstName":"John",
                  "lastName":"Smith",
                  "status":"NOT ANSWERED",
                  "owner":true
                 },
                 {
                  "userId":5,
                  "firstName":"Mary",
                  "lastName":"Smith",
                  "status":"NOT ANSWERED",
                  "owner":false
                  }
                ]
              }
            ]
          },
       "root": {
          "id":1,
          "name":"My First Trip",
          "user": {
            "firstName":"Test",
            "lastName":"User",
            "id":2
           },
          "albumId":6,
          "groupName":"Team 300",
          "groupId":1,
          "slackWorkspaceDomain":null
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
        }
     ],
     "trip":{
        "id":1,
        "name":"My First Trip",
        "usergroup":[

        ],
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
              "type":"destination",
              "usergroup": []
           },
           {
              "id":3,
              "name":"Big River",
              "arrivalDate":4,
              "departureDate":6,
              "ordinal":1,
              "destination":{
                 "id":2,
                 "name":"Big River",
                 "latitude":3.0,
                 "longitude":3.0,
                 "type":"River",
                 "district":"Canterbury",
                 "country":"New Zealand",
                 "googleId":null
              },
              "type":"destination",
              "usergroup":[]
           },
           {
              "id":4,
              "name":"Small River",
              "arrivalDate":10,
              "departureDate":22,
              "ordinal":2,
              "destination":{
                 "id":3,
                 "name":"Small River",
                 "latitude":3.0,
                 "longitude":3.0,
                 "type":"River",
                 "district":"Otago",
                 "country":"New Zealand",
                 "googleId":null
              },
              "type":"destination",
              "usergroup":[]
           }
        ]
     },
     "root":{
        "id":1,
        "name":"My First Trip",
        "user":{
           "firstName":"John",
           "lastName":"Smith",
           "id":3
        },
        "albumId":7,
        "groupName":"",
        "groupId":null,
        "slackWorkspaceDomain": ""
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