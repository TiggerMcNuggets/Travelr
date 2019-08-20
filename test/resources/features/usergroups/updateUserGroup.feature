Feature: Update User Group
  Description: The purpose of this feature is to test the api endpoints related to editing a user group

  Scenario: Edit an user group name and description successfully
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to edit the user group
    And The body is
    """
    {
      "name": "Team 10",
      "description": "A really cool team"
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The user group is
      | name         | description          |
      | Team 10      | A really cool team   |


  Scenario: Edit an user group successfully for a user as an admin
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I am authenticated
    And I am an admin
    When I want to edit the user group
    And The body is
    """
    {
      "name": "Team 10",
      "description": "A really cool team"
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The user group is
      | name         | description          |
      | Team 10      | A really cool team   |


  Scenario: Edit an user group with incorrect request body
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to edit the user group
    And The body is
    """
    {
      "nname": "Team 10",
      "description": "A really cool team"
    }
    """
    And I send the request
    Then I will receive the response code 400
    And The user group is
      | name         | description         |
      | Team 300     | The best team eva   |


  Scenario: Edit an user group with a duplicate name
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the user group
      | name         | description          |
      | Team 10      | A really cool team   |
    When I want to edit the user group
    And The body is
    """
    {
      "nname": "Team 300",
      "description": "A really cool team"
    }
    """
    And I send the request
    Then I will receive the response code 400
    And The user group is
      | name         | description          |
      | Team 10      | A really cool team   |


  Scenario: Edit a user group when not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to edit the user group
    And The body is
    """
    {
      "name": "Team 10",
      "description": "A really cool team"
    }
    """
    And I send the request
    Then I will receive the response code 401
    And The user group is
      | name         | description         |
      | Team 300     | The best team eva   |


  Scenario: Edit an user group when I am not an owner
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to edit the user group
    And The body is
    """
    {
      "name": "Team 10",
      "description": "A really cool team"
    }
    """
    And I send the request
    Then I will receive the response code 403
    And The user group is
      | name         | description         |
      | Team 300     | The best team eva   |


  Scenario: Edit an album that doesn't exist
    Given I am authenticated
    And I do not own an user group
    When I want to edit the user group
    And The body is
     """
    {
      "name": "Team 10",
      "description": "A really cool team"
    }
    """
    And I send the request
    Then I will receive the response code 404