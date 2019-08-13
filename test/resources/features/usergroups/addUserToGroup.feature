Feature: Add User to Group
  Description: The purpose of this feature is to test the api endpoint related to adding a user to a group

  Scenario: Add a user to a group who will be an owner successfully
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And The future member exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
    {
      "isOwner": true
    }
    """
    And I send the request
    Then I will receive the response code 201
    And The user now exists in the group
    And The user is now an owner of the group


  Scenario: Add a user to a group who will be a normal member successfully
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And The future member exists
      | first | last  | email                | dob |
      | James | Smith | jamessmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
    {
      "isOwner": false
    }
    """
    And I send the request
    Then I will receive the response code 201
    And The user now exists in the group
    And The user is now a normal member of the group


  Scenario: Add a user to a group successfully as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And The future member exists
      | first | last  | email                | dob |
      | James | Smith | jamessmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
    {
      "isOwner": false
    }
    """
    And I send the request
    Then I will receive the response code 201
    And The user now exists in the group
    And The user is now a normal member of the group


  Scenario: Add a user to a group with incorrect request body
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And The future member exists
      | first | last  | email                | dob |
      | James | Smith | jamessmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
    """
    And I send the request
    Then I will receive the response code 400
    And The user still does not exist in the group


  Scenario: Add a user to a group when I am not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And The future member exists
      | first | last  | email                | dob |
      | James | Smith | jamessmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
    {
      "isOwner": false
    }
    """
    And I send the request
    Then I will receive the response code 401
    And The user still does not exist in the group


  Scenario: Add a user to a group that does not belong to me
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And The future member exists
      | first | last  | email                | dob |
      | James | Smith | jamessmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
    {
      "isOwner": false
    }
    """
    And I send the request
    Then I will receive the response code 403
    And The user still does not exist in the group


  Scenario: Add a user to a group who already belongs to that group
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
    {
      "isOwner": false
    }
    """
    And I send the request
    Then I will receive the response code 403


  Scenario: Add a user to a group which does not exist
    Given I am authenticated
    And The group does not exist
      | name         | description         |
      | Team 300     | The best team eva   |
    And The future member exists
      | first | last  | email                | dob |
      | James | Smith | jamessmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
    {
      "isOwner": false
    }
    """
    And I send the request
    Then I will receive the response code 404
    And The user still does not exist in the group


  Scenario: Add a user who does not exist to a group
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And The future member does not exist
      | first | last  | email                | dob |
      | James | Smith | jamessmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
    {
      "isOwner": false
    }
    """
    And I send the request
    Then I will receive the response code 404
    And The user still does not exist in the group



