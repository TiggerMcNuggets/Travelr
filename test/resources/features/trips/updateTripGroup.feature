Feature: UpdateTripGroup
  Description: The purpose of this feature is to test the api endpoint related to updating groups for trips

  Scenario: Add group to a trip
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to add the group to the trip
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Trip group updated successfully
      """
    And the group will be added to the trip

  Scenario: Remove a group from a trip and remove all status
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    And The trip contains the trip destinations
      | name          | type        | arrivalDate | departureDate | ordinal      | destinationId |
      | Eiffel Tower  | destination | 1           | 2             | 0            | 1             |
      | Big River     | destination | 4           | 6             | 1            | 2             |
      | Small River   | destination | 10          | 22            | 2            | 3             |
    And the trip is associated with the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the group has the members, ownership and statuses
      | first | last   | email               | dob | status     | owner |
      | Joe   | Bloggs | joebloggs@email.com | 1   | GOING      | false |
      | John  | Smith  | johnsmith@email.com | 1   | MAYBE      | true  |
      | Mary  | Smith  | marysmith@email.com | 1   | NOT GOING  | false |
    When I want to remove the group from the trip
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Trip group updated successfully
      """
    And all trip status values associated with the trip are removed
    And the group will not be associated with the trip


  Scenario: Add group to a trip as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the trip
      | name         |
      | My First Trip|
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to add the group to the trip
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Trip group updated successfully
      """
    And the group will be added to the trip

  Scenario: Add a group to a trip when not authenticated
    Given I am not authenticated
    And I own the trip
      | name         |
      | My First Trip|
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to add the group to the trip
    And I send the request
    Then I will receive the response code 401

  Scenario: Add a group to a trip when I do not own any trips
    Given I am authenticated
    And I do not own the trip
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to add the group to the trip
    And I send the request
    Then I will receive the response code 404

  Scenario: Add a group to a trip when I do not own any groups
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    And I do not own an user group
    When I want to add the group to the trip
    And I send the request
    Then I will receive the response code 404

  Scenario: Add a group which I do not own to a trip
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the trip
      | name         |
      | My First Trip|
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to add the group to the trip
    And I send the request
    Then I will receive the response code 403

