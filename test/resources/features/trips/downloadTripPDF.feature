Feature: downloadTripPDF
  Description: Feature to test the trip pdf endpoint and

  Scenario: Downloading the trip pdf successfully
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
    When I want to download the trip pdf
    And I send the request
    Then I will receive the response code 200
    And The content type will be "Optional[application/pdf]"

  Scenario: Downloading another user's trip as an admin
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
      | Destination 3 | destination | 1           | 2             | 0            | 1             |
    When I want to download the trip pdf
    And I send the request
    Then I will receive the response code 200
    And The content type will be "Optional[application/pdf]"

  Scenario: Failing to download a trip pdf when I am not authenticated
    Given I am not authenticated
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
      | Destination 3 | destination | 1           | 2             | 0            | 1             |
    When I want to download the trip pdf
    And I send the request
    Then I will receive the response code 401

  Scenario: Failing to download a trip pdf when it does not exist
    Given I am authenticated
    And I do not own the trip
    When I want to download the trip pdf
    And I send the request
    Then I will receive the response code 404

#  Scenario: Failing to download a trip pdf when I am not a part of a trip
#    Given I am not authenticated
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
#      | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
#      | Destination 3 | destination | 1           | 2             | 0            | 1             |
#    When I want to download the trip pdf
#    And I send the request
#    Then I will receive the response code 403

