Feature: Delete User Group
  Description: The purpose of this feature is to test the api endpoint related to deleting a user group

  Scenario: Delete an user group and all its members successfully
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
    And All its group members are removed

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
    When I want to remove the user group with an id 100
    And I send the request
    Then I will receive the response code 404
    And I will receive the response body text
      """
      Group not found
      """

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