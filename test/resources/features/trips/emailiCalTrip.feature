Feature: Email iCal Trip
  Description: The purpose of this feature is to test the api endpoint related emailing iCal file to the whole group of a trip

  Scenario: Email the whole group as an owner
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And The trip contains the trip destinations
      | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
      | Eiffel Tower  | destination | 1           | 2             | 0            | 1             |
      | Big River     | destination | 4           | 6             | 1            | 2             |
    And The trip belongs to the user group
    # Change email to yours to check received email. REMEMBER TO CHANGE IT BACK BEFORE PUSHING!
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to send everyone an iCal email
    And I send the request
    Then I will receive the response code 201


  Scenario: Email the whole group as an admin
    And I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And The trip contains the trip destinations
      | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
      | Eiffel Tower  | destination | 1           | 2             | 0            | 1             |
      | Big River     | destination | 4           | 6             | 1            | 2             |
    And The trip belongs to the user group
    And the user group has the group member
      | first | last  | email               | dob |
      | Jane  | Smith | janesmith@email.com | 1   |
    When I want to send everyone an iCal email
    And I send the request
    Then I will receive the response code 201


  Scenario: Email the whole group when not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And The trip contains the trip destinations
      | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
      | Eiffel Tower  | destination | 1           | 2             | 0            | 1             |
      | Big River     | destination | 4           | 6             | 1            | 2             |
    And The trip belongs to the user group
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to send everyone an iCal email
    And I send the request
    Then I will receive the response code 401


  Scenario: Email the whole group as a normal member
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And The trip contains the trip destinations
      | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
      | Eiffel Tower  | destination | 1           | 2             | 0            | 1             |
      | Big River     | destination | 4           | 6             | 1            | 2             |
    And The trip belongs to the user group
    And I am authenticated
    And I am a member of the group
    When I want to send everyone an iCal email
    And I send the request
    Then I will receive the response code 403


  Scenario: Email the whole group for a trip I am not a part of
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The destinations are
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
    And The trip contains the trip destinations
      | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
      | Eiffel Tower  | destination | 1           | 2             | 0            | 1             |
      | Big River     | destination | 4           | 6             | 1            | 2             |
    And The trip belongs to the user group
    And I am authenticated
    When I want to send everyone an iCal email
    And I send the request
    Then I will receive the response code 403


  Scenario: Email the whole group when the trip does not exist
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I do not own the trip
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to send everyone an iCal email
    And I send the request
    Then I will receive the response code 404