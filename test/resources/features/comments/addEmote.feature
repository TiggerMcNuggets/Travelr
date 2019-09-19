Feature: Add emoji to comment
  Description: The purpose of this feature is to test the api endpoint related to adding emojis to comments


  Scenario: Add an emoji successfully to a comment as a group owner
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And I add a comment "This is the best trip eva!!" to the trip
    When I want to add an emoji
    And The body is
    """
    {
      "emoji": "test"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And the comment has the emoji react 1 times
    And the emoji is related to the user who posted 
    And the emote is "test"


  Scenario: Add an emoji to a comment successfully as an admin
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
    And I add a comment "This is the best trip eva!!" to the trip
    When I want to add an emoji
    And The body is
    """
    {
      "emoji": "test"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And the comment has the emoji react 1 times
    And the emoji is related to the user who posted 
    And the emote is "test"


  Scenario: Add an emoji with a bad JSON body
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And I add a comment "This is the best trip eva!!" to the trip
    When I want to add an emoji
    And The body is
    """
    {
      "emojjjjji": "test"
    }
    """
    And I send the request
    Then I will receive the response code 400


  Scenario: Add an emoji when not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And I add a comment "This is the best trip eva!!" to the trip
    When I want to add an emoji
    And The body is
    """
    {
      "emoji": "test"
    }
    """
    And I send the request
    Then I will receive the response code 401


  Scenario: Add an emoji to another users comment as a normal member
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
    And I add a comment "This is the best trip eva!!" to the trip
    And I am authenticated
    And I am a member of the group
    When I want to add an emoji
    And The body is
    """
    {
      "emoji": "test"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And the comment has the emoji react 1 times
    And the emoji is related to the user who posted 
    And the emote is "test"

  Scenario: Create a comment when not part of the group
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
    And I add a comment "This is the best trip eva!!" to the trip
    And I am authenticated
    When I want to add an emoji
    And The body is
    """
    {
      "emoji": "test"
    }
    """
    And I send the request
    Then I will receive the response code 403


  Scenario: Add an emoji when the trip does not exist
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And I add a comment "This is the best trip eva!!" to the trip
    And I do not own the trip
    When I want to add an emoji
    And The body is
    """
    {
      "emoji": "test"
    }
    """
    And I send the request
    Then I will receive the response code 404

  Scenario: Add an emoji when the comment does not exist
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    When I want to add an emoji to a non existent comment
    And The body is
    """
    {
      "emoji": "test"
    }
    """
    And I send the request
    Then I will receive the response code 404

  Scenario: Create a comment when the user does not exist
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And I add a comment "This is the best trip eva!!" to the trip
    And The user does not exist
    When I want to add an emoji
    And The body is
    """
    {
      "emoji": "test"
    }
    """
    And I send the request
    Then I will receive the response code 404
