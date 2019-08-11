Feature: Delete User Group
  Description: The purpose of this feature is to test the api endpoint related to adding a user to a group

  Scenario: Add a user to a group successfully
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to add the user to the group
    And The body is
    """
      "isOwner": true
    """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body text
      """
      User added
      """
    And The user now exists in the group
    And The user is now an owner of the group
