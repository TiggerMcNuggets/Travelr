Feature: Update Trip User Attendance Status
  Description: The purpose of this feature is to test the api endpoint related to updating trip attendance status

  Scenario: Update status of a trip with no destinations to GOING
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    When I want to change my status for this trip
    And The body is
    """
    {
      "status": "GOING"
    }
    """
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Trip Status Updated
      """
    And my status for this trip is "GOING"


  Scenario: Update status of a trip with no destinations to NOT_GOING
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    When I want to change my status for this trip
    And The body is
    """
    {
      "status": "NOT_GOING"
    }
    """
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Trip Status Updated
      """
    And my status for this trip is "NOT_GOING"

  Scenario: Update status of a trip with no destinations to MAYBE
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    When I want to change my status for this trip
    And The body is
    """
    {
      "status": "MAYBE"
    }
    """
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Trip Status Updated
      """
    And my status for this trip is "MAYBE"

  Scenario: Update status of a trip with no destinations to MAYBE as an admin for another user
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the trip
      | name         |
      | My First Trip|
    When I want to change my status for this trip
    And The body is
    """
    {
      "status": "MAYBE"
    }
    """
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Trip Status Updated
      """
    And my status for this trip is "MAYBE"


  Scenario: Update status of a trip when not authenticated
    Given I am not authenticated
    And I own the trip
      | name         |
      | My First Trip|
    When I want to change my status for this trip
    And The body is
    """
    {
      "status": "MAYBE"
    }
    """
    And I send the request
    Then I will receive the response code 401

  Scenario: Update status of a trip when body is in an invalid body key
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    When I want to change my status for this trip
    And The body is
    """
    {
      "statuss": "MAYBE"
    }
    """
    And I send the request
    Then I will receive the response code 400

  Scenario: Update status of a trip when body is in an invalid status type
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    When I want to change my status for this trip
    And The body is
    """
    {
      "status": "DUNNO"
    }
    """
    And I send the request
    Then I will receive the response code 400

  Scenario: Update status of a trip as another user
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the trip
      | name         |
      | My First Trip|
    When I want to change my status for this trip
    And The body is
    """
    {
      "status": "MAYBE"
    }
    """
    And I send the request
    Then I will receive the response code 403



  Scenario: Update status of a trip with a sub destination to GOING
    Given I am authenticated
    And I own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    And I own the trip
      | name         |
      | My First Trip|
    And the trip has this destination added as a part of the trip
    When I want to change my status for this trip
    And The body is
    """
    {
      "status": "GOING"
    }
    """
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Trip Status Updated
      """
    And my status for this trip is "GOING"
    And the status for the sub destination are also "GOING"




