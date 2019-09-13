Feature: Delete Media
  Description: The purpose of this feature is to test the api endpoint related to deleting media in albums

  Scenario: Delete an media successfully from a single album
    Given I am authenticated
    And I own the album
      | name         |
      | Test Album   |
    And the album contains the media
      | uriString    |
      | test.jpg     |
    When I want to delete the media in the album
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Media deleted
      """
    And The media does not exist in the album

  Scenario: Delete media from multiple albums
    Given I am authenticated
    And I own the album
      | name        |
      | First Album |
    And the album contains the media
      | uriString    |
      | test.jpg     |
    And I own the album
      | name         |
      | Second Album |
    And the album contains the media
      | uriString    |
      | test.jpg     |
    When I want to delete the media in all albums
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Media deleted
      """
    And The media does not exist

  Scenario: Delete media from a trip album
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
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the trip is associated with my user group
    And the group has the members, ownership and statuses
      | first | last   | email               | dob | status     | owner |
      | Joe   | Bloggs | joebloggs@email.com | 1   | GOING      | false |
      | John  | Smith  | johnsmith@email.com | 1   | MAYBE      | true  |
      | Mary  | Smith  | marysmith@email.com | 1   | NOT GOING  | false |
    And The trip album contains the media
      | uriString    |
      | test.jpg     |
    When I want to delete the media in the album
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Media deleted
      """
    And The media does not exist

  Scenario: Delete media from a trip album as non group owner
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
    And The trip album contains the media
      | uriString    |
      | test.jpg     |
    When I want to delete the media in the album
    And I send the request
    Then I will receive the response code 400

