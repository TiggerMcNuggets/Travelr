#Feature: downloadiCalTrip
#  Description: The purpose of this feature is to test the api endpoint related to downloading a trip and checking the integrity of the file
#
#  Scenario: Download my own trip successfully
#    Given I am authenticated
#    And The destinations are
#      | name         | latitude | longitude | type     | district   | country    |
#      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
#      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
#      | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
#    And I own the trip
#      | name         | description |
#      | My First Trip| A trip      |
#    And The trip contains the trip destinations
#      | ordinal | customName | arrivalDate | departureDate | destinationId| depth |
#      |   1     | Place One  | 1           | 2             | 1            | 0     |
#      |   2     | Place Two  | 2           | 3             | 2            | 0     |
#      |   3     | Place Three| 3           | 4             | 3            | 0     |
#    When I want to download the trip
#    And I send the request
#    Then I will receive the response code 200
#    And The content type will be "Optional[text/calendar]"
#
#  Scenario: Download a trip when not logged in
#    Given I am not authenticated
#    And The destinations are
#      | name         | latitude | longitude | type     | district   | country    |
#      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
#      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
#      | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
#    And I own the trip
#      | name         | description |
#      | My First Trip| A trip      |
#    And The trip contains the trip destinations
#      | ordinal | customName | arrivalDate | departureDate | destinationId| depth |
#      |   1     | Place One  | 1           | 2             | 1            | 0     |
#      |   2     | Place Two  | 2           | 3             | 2            | 0     |
#      |   3     | Place Three| 3           | 4             | 3            | 0     |
#    When I want to download the trip
#    And I send the request
#    Then I will receive the response code 401
#
#  Scenario: Download someone elses trip with a valid tripId
#    Given I am authenticated
#    And The user exists
#      | first | last  | email               | dob |
#      | John  | Smith | johnsmith@email.com | 1   |
#    And The destinations are
#      | name         | latitude | longitude | type     | district   | country    |
#      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
#      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
#      | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
#    And They own the trip
#      | name         | description |
#      | My First Trip| A trip      |
#    And The trip contains the trip destinations
#      | ordinal | customName | arrivalDate | departureDate | destinationId| depth |
#      |   1     | Place One  | 1           | 2             | 1            | 0     |
#      |   2     | Place Two  | 2           | 3             | 2            | 0     |
#      |   3     | Place Three| 3           | 4             | 3            | 0     |
#    When I want to download the trip
#    And I send the request
#    Then I will receive the response code 403
#
#  Scenario: Download a trip that doesn't exist
#    Given I am authenticated
#    And I do not own the trip
#    When I want to download the trip
#    And I send the request
#    Then I will receive the response code 404