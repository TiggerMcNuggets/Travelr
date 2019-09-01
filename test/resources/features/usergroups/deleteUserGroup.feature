Feature: Delete User Group
  Description: The purpose of this feature is to test the api endpoint related to deleting a user group

  Scenario: Delete a user group without soft deleting all its members
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to remove the user group and all its members
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Group removed
      """
    And The user group does not exist
    And All its group members are in the group

  Scenario: Delete a user group as an admin non owner of the group without soft deleting all its members
    Given I am authenticated
    And I am an admin
    And I do not own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to remove the user group and all its members
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Group removed
      """
    And The user group does not exist
    And All its group members are in the group


  Scenario: Toggle delete twice a user group and all its members successfully
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to remove the user group and all its members
    And I send the request
    When I want to remove the user group and all its members
    And I send the request
    Then The user group exist
    And All its group members are in the group



  Scenario: Delete an user group when not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to remove the user group and all its members
    And I send the request
    Then I will receive the response code 401

  Scenario: Delete an user group which does not exist
    Given I am authenticated
    And The group does not exist
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to remove the user group and all its members
    And I send the request
    Then I will receive the response code 403

  Scenario: Delete an user group when not its owner
    Given I am authenticated
    And I do not own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to remove the user group and all its members
    And I send the request
    Then I will receive the response code 403
    And I will receive the response body text
      """
      Forbidden: Access Denied
      """