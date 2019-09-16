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



  Scenario: Double delete (re-toggle) one of my comments
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
    When I want to delete the comment
    And I send the request
    Then I will receive the response code 200
    And the comment exists




  Scenario: Delete a comment successfully as an admin
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description |
      | My First Trip| A trip      |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And I am authenticated
    And I am an admin
    When I want to add a comment
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



  Scenario: Delete a comment successfully as an admin
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description |
      | My First Trip| A trip      |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And I am authenticated
    And I am an admin
    When I want to add a comment
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
    When I want to delete the comment
    And I send the request
    Then I will receive the response code 200
    And the comment exists



  Scenario: Delete a comment when not logged in
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    When I want to add a comment
    And The body is
    """
    {
      "message": "Comment"
    }
    """
    And I send the request
    When I check the comment
    And I want to logout
    And I send the request
    And I want to delete the comment
    And I send the request
    Then I will receive the response code 401



  Scenario: Delete someone else's comment
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description |
      | My First Trip| A trip      |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    When I want to add a comment
    And The body is
    """
    {
      "message": "Comment"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
    """
    {
      "id": 1
    }
    """
    When I check the comment
    Then The comment is
      | message |
      | Comment |

    # this part mocks the user being the original user,
    # trying to simulate a user that does not own the comment
    When The user exists
      | first    | last  | email                    | dob |
      | Lorenzo  | Ciao  | testing_flexth@email.com | 1   |
    And I want to delete the comment
    And I send the request
    Then I will receive the response code 403



  Scenario: Delete someone else's comment as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description |
      | My First Trip| A trip      |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    When I want to add a comment
    And The body is
    """
    {
      "message": "Comment"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
    """
    {
      "id": 1
    }
    """
    When I check the comment
    Then The comment is
      | message |
      | Comment |

    And I want to delete the comment
    And I send the request
    Then I will receive the response code 200

