Feature: Delete User Group Member
  Description: The purpose of this feature is to test the api endpoint related to deleting a user from a user group

  Scenario: Delete a user successfully from a single user group
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to remove the group member in the user group
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Group member removed
      """
    And The group member does not exist in the user group

  Scenario: Delete a user successfully from a single user group when admin and not group owner
    Given I am authenticated
    And I am an admin
    And I do not own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to remove the group member in the user group
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Group member removed
      """
    And The group member does not exist in the user group


  Scenario: Toggle delete twice a member from a group
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to remove the group member in the user group
    And I send the request
    When I want to remove the group member in the user group
    And I send the request
    Then The user now exists in the group

  Scenario: Deleting a user from a single user group when I am not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to remove the group member in the user group
    And I send the request
    Then I will receive the response code 401

  Scenario: Deleting a user which does not exist from a single user group
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group does not have another group member
    When I want to remove the group member in the user group
    And I send the request
    Then I will receive the response code 404
    And I will receive the response body text
      """
      Group member not found
      """