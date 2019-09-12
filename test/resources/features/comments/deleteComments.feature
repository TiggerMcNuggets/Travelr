Feature: Create Comment
  Description: The purpose of this feature is to test the api endpoint related to creating comments


  Scenario: Delete one of my comments
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And I want to add a comment
    And The body is
    """
    {
      "message": "Comment"
    }
    """
    And I send the request
    And I check the comment
    And The comment is
      | message |
      | Comment |
    When I want to delete the comment
    And I send the request
    Then I will receive the response code 200
    And the comment has been removed









#  Scenario: Create a comment successfully as an admin
#    Given The user exists
#      | first | last  | email               | dob |
#      | John  | Smith | johnsmith@email.com | 1   |
#    And They own the user group
#      | name         | description |
#      | My First Trip| A trip      |
#    And They own the trip
#      | name         | description |
#      | My First Trip| A trip      |
#    And The trip belongs to the user group
#    And I am authenticated
#    And I am an admin
#    When I want to add a comment
#    And The body is
#    """
#    {
#      "message": "Comment"
#    }
#    """
#    And I send the request
#    Then I will receive the response code 201
#    And I will receive the response body
#    """
#    {
#      "id": 1
#    }
#    """
#    When I check the comment
#    Then The comment is
#      | message |
#      | Comment |
#
#
#  Scenario: Create a comment with a bad JSON body
#    Given I am authenticated
#    And I own the user group
#      | name         | description         |
#      | Team 300     | The best team eva   |
#    And I own the trip
#      | name         | description |
#      | My First Trip| A trip      |
#    And The trip belongs to the user group
#    When I want to add a comment
#    And The body is
#    """
#    {
#      "mesage": "Comment"
#    }
#    """
#    And I send the request
#    Then I will receive the response code 400
#
#
#  Scenario: Create a comment when not logged in
#    Given I am not authenticated
#    And I own the user group
#      | name         | description         |
#      | Team 300     | The best team eva   |
#    And I own the trip
#      | name         | description |
#      | My First Trip| A trip      |
#    And The trip belongs to the user group
#    When I want to add a comment
#    And The body is
#    """
#    {
#      "message": "Comment"
#    }
#    """
#    And I send the request
#    Then I will receive the response code 401
#
#
#  Scenario: Create a comment as a normal member
#    Given The user exists
#      | first | last  | email               | dob |
#      | John  | Smith | johnsmith@email.com | 1   |
#    And They own the user group
#      | name         | description |
#      | My First Trip| A trip      |
#    And They own the trip
#      | name         | description |
#      | My First Trip| A trip      |
#    And The trip belongs to the user group
#    And I am authenticated
#    And I am a member of the group
#    When I want to add a comment
#    And The body is
#    """
#    {
#      "message": "Comment"
#    }
#    """
#    And I send the request
#    Then I will receive the response code 403
#
#
#  Scenario: Create a comment when not part of the group
#    Given The user exists
#      | first | last  | email               | dob |
#      | John  | Smith | johnsmith@email.com | 1   |
#    And They own the user group
#      | name         | description |
#      | My First Trip| A trip      |
#    And They own the trip
#      | name         | description |
#      | My First Trip| A trip      |
#    And The trip belongs to the user group
#    And I am authenticated
#    When I want to add a comment
#    And The body is
#    """
#    {
#      "message": "Comment"
#    }
#    """
#    And I send the request
#    Then I will receive the response code 403
#
#
#  Scenario: Create a comment when the trip does not exist
#    Given I am authenticated
#    And I own the user group
#      | name         | description |
#      | My First Trip| A trip      |
#    And I do not own the trip
#    And The trip belongs to the user group
#    When I want to add a comment
#    And The body is
#    """
#    {
#      "message": "Comment"
#    }
#    """
#    And I send the request
#    Then I will receive the response code 404
#
#
#
