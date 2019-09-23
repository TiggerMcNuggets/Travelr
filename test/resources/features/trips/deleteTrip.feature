Feature: Delete Trip
  Description: The purpose of this feature is to test the api endpoint related to deleting trips

  Scenario: Delete a trip successfully as the owner of the trip
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    When I want to delete the trip
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    true
    """

  Scenario: Delete a trip unsuccessfully when unauthenticated
    Given I am not authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the trip
      | name         |
      | My First Trip|
    When I want to delete the trip
    And I send the request
    Then I will receive the response code 401

  Scenario: Delete a trip unsuccessfully when trip belongs to another user
    Given I am authenticated
    And The user exists
      | first | last  | email            | dob |
      | John  | Smith | george@email.com | 1   |
    And They own the trip
      | name         |
      | My First Trip|
    When I want to delete the trip
    And I send the request
    Then I will receive the response code 403

  Scenario: Delete another user's trip successfully as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email            | dob |
      | John  | Smith | george@email.com | 1   |
    And They own the trip
      | name         |
      | My First Trip|
    When I want to delete the trip
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    true
    """

  Scenario: Delete a trip associated with my group successfully as an owner
    Given I am authenticated
    And They own the trip
      | name         |
      | My First Trip|
    And the trip is associated with the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the group has the members, ownership and statuses
      | first | last   | email               | dob | status     | owner |
      | Joe   | Bloggs | joebloggs@email.com | 1   | GOING      | false |
      | Mary  | Smith  | marysmith@email.com | 1   | NOT GOING  | true  |
    And I am an owner of the group
    When I want to delete the trip
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    true
    """


#TODO fix this test
#  Scenario: Delete a trip associated with my group unsuccessfully as a member
#    Given I am authenticated
#    And They own the trip
#      | name         |
#      | My First Trip|
#    And the trip is associated with the user group
#      | name         | description         |
#      | Team 300     | The best team eva   |
#    And the group has the members, ownership and statuses
#      | first | last   | email               | dob | status     | owner |
#      | Joe   | Bloggs | joebloggs@email.com | 1   | GOING      | false |
#      | Mary  | Smith  | marysmith@email.com | 1   | NOT GOING  | true  |
#    And I am a member of the group
#    When I want to delete the trip
#    And I send the request
#    Then I will receive the response code 403


#
#
#  Scenario: Delete another user's trip an admin
#    Given I am authenticated
#    And I am an admin
#    And The user exists
#      | first | last  | email               | dob |
#      | John  | Smith | johnsmith@email.com | 1   |
#    And I own the trip
#      | name         |
#      | My First Trip|
#    When I want to delete the trip
#    And I send the request
#    Then I will receive the response code 200
#    And I will receive the response body
#    """
#    {
#      "tripId": 1,
#      "deleted": true
#    }
#    """
#
#  Scenario: Delete a trip when not logged in
#    Given I am not authenticated
#    And I own the trip
#      | name         |
#      | My First Trip|
#    When I want to delete the trip
#    And I send the request
#    Then I will receive the response code 401
#
#  Scenario: Delete another user's trip as a normal user
#    Given I am authenticated
#    And The user exists
#      | first | last  | email               | dob |
#      | John  | Smith | johnsmith@email.com | 1   |
#    And I own the trip
#      | name         |
#      | My First Trip|
#    When I want to delete the trip
#    And I send the request
#    Then I will receive the response code 403
#
#
#  Scenario: Delete a trip that does not exist
#    Given I am authenticated
#    And The trip does not exist
#    When I want to delete the trip
#    And I send the request
#    Then I will receive the response code 404
##
### TODO: Add scenarios for deleting trips related to trips within trips story