Feature: Promote/Demote users to group owners

Scenario: Promote a group member to trip owner
Given I am authenticated
  And I own the user group
  | name         | description         |
  | Team 300     | The best team eva   |
  And The future member exists
  | first | last  | email               | dob |
  | John  | Smith | johnsmith@email.com | 1   |
  And I want to add the user to the group
  And The body is
  """
      {
        "isOwner": false
      }
  """
  And I send the request
  And The user now exists in the group
When I want to promote the user to group owner
  And I send the request
Then I will receive the response code 200
  And The user is now an owner of the group


Scenario: Demote a group member to normal trip member
  Given I am authenticated
  And I own the user group
    | name         | description         |
    | Team 300     | The best team eva   |
  And The future member exists
    | first | last  | email               | dob |
    | John  | Smith | johnsmith@email.com | 1   |
  And I want to add the user to the group
  And The body is
  """
      {
        "isOwner": true
      }
  """
  And I send the request
  And The user now exists in the group
  And The user is now an owner of the group
  When I want to promote the user to group owner
  And I send the request
  Then I will receive the response code 200
  And The user is now an not a owner of the group


Scenario: Trying to promote a user that is not in the group
  Given I am authenticated
  And I own the user group
    | name         | description         |
    | Team 300     | The best team eva   |
  And The future member exists
    | first | last  | email               | dob |
    | John  | Smith | johnsmith@email.com | 1   |
  When I want to promote the user to group owner
  And I send the request
  Then I will receive the response code 404


Scenario: Promote a group member when I don't own the group
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

Scenario: Promote a group member when I am not logged in
  Given I am not authenticated
  And The user exists
    | first | last  | email               | dob |
    | John  | Smith | johnsmith@email.com | 1   |
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
