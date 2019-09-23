#Scenario: Get a trips files
#  Given I am authenticated
#  And The destinations are
#  | name         | latitude | longitude | type     | district   | country    |
#  | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
#  | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
#  | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
#  And I own the trip
#  | name         | description |
#  | My First Trip| A trip      |
#  And The trip contains the trip destinations
#  | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
#  | Destination 3 | destination | 1           | 2             | 0            | 1             |
#  And the trip is associated with the user group
#  | name         | description         |
#  | Team 300     | The best team eva   |
#  And the group has the members, ownership and statuses
#  | first | last   | email               | dob | status     | owner |
#  | Joe   | Bloggs | joebloggs@email.com | 1   | GOING      | false |
#  | John  | Smith  | johnsmith@email.com | 1   | MAYBE      | true  |
#  | Mary  | Smith  | marysmith@email.com | 1   | NOT GOING  | false |
#  When I want to get the trips files
#  And I send the request
#  Then I will receive the response code 200
#
#Scenario: Get a trips files with no auth
#  Given I am authenticated
#  And The destinations are
#  | name         | latitude | longitude | type     | district   | country    |
#  | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
#  | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
#  | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
#  And I own the trip
#  | name         | description |
#  | My First Trip| A trip      |
#  And The trip contains the trip destinations
#  | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
#  | Destination 3 | destination | 1           | 2             | 0            | 1             |
#  And the trip is associated with the user group
#  | name         | description         |
#  | Team 300     | The best team eva   |
#  And the group has the members, ownership and statuses
#  | first | last   | email               | dob | status     | owner |
#  | Joe   | Bloggs | joebloggs@email.com | 1   | GOING      | false |
#  | John  | Smith  | johnsmith@email.com | 1   | MAYBE      | true  |
#  | Mary  | Smith  | marysmith@email.com | 1   | NOT GOING  | false |
#  When I want to get the trips files
#  And I send the request
#  Then I will receive the response code 403
#
#Scenario: Get a trips files with no files
#  Given I am authenticated
#  And The destinations are
#  | name         | latitude | longitude | type     | district   | country    |
#  | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
#  | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
#  | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
#  And I own the trip
#  | name         | description |
#  | My First Trip| A trip      |
#  And The trip contains the trip destinations
#  | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
#  | Destination 3 | destination | 1           | 2             | 0            | 1             |
#  And the trip is associated with the user group
#  | name         | description         |
#  | Team 300     | The best team eva   |
#  And the group has the members, ownership and statuses
#  | first | last   | email               | dob | status     | owner |
#  | Joe   | Bloggs | joebloggs@email.com | 1   | GOING      | false |
#  | John  | Smith  | johnsmith@email.com | 1   | MAYBE      | true  |
#  | Mary  | Smith  | marysmith@email.com | 1   | NOT GOING  | false |
#  When I want to get the trips files
#  And I send the request
#  Then I will receive the response code 200
#  And I will receive the response body
#  """
#  []
#  """
#
