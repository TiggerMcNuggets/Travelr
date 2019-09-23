Feature: Get Comment
  Description: The purpose of this feature is to test the api endpoint related to getting comments



  Scenario: Retrieve a comment successfully as the owner of the trip
    Given I am authenticated
    And I own the user group
      | name     | description       |
      | Team 300 | The best team eva |
    And I own the trip
      | name          | description |
      | My First Trip | A trip      |
    And The trip belongs to the user group
    And the trip has the comment
      | message |
      | Comment |
    When I want to retrieve comments
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
        "commentsLength": 1,
        "comments": [
          {
            "id": 1,
            "userId": 2,
            "userFirstName": "Test",
            "userLastName": "User",
            "comment": "Comment",
            "profilePhoto": "defaultPic.png",
            "timestamp": 1569191117
          }
        ]
      }
      """

  Scenario: Retrieve comments of an unassociated trip successfully as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name     | description       |
      | Team 300 | The best team eva |
    And They own the trip
      | name          | description |
      | My First Trip | A trip      |
    And The trip belongs to the user group
    And the trip has the comment
      | message |
      | Comment |
    When I want to retrieve comments
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
        "commentsLength": 1,
        "comments": [
          {
            "id": 1,
            "userId": 3,
            "userFirstName": "John",
            "userLastName": "Smith",
            "comment": "Comment",
            "profilePhoto": "defaultPic.png",
            "timestamp": 1569191117
          }
        ]
      }
      """

  Scenario: Retrieve comments of an unassociated trip unsuccessfully as a normal user
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name     | description       |
      | Team 300 | The best team eva |
    And They own the trip
      | name          | description |
      | My First Trip | A trip      |
    And The trip belongs to the user group
    And the trip has the comment
      | message |
      | Comment |
    When I want to retrieve comments
    And I send the request
    Then I will receive the response code 403

  Scenario: Retrieve comments of a trip unsuccessfully when not authenticated
    Given I am not authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name     | description       |
      | Team 300 | The best team eva |
    And They own the trip
      | name          | description |
      | My First Trip | A trip      |
    And The trip belongs to the user group
    And the trip has the comment
      | message |
      | Comment |
    When I want to retrieve comments
    And I send the request
    Then I will receive the response code 401

  Scenario: Retrieve comments of an associated trip successfully as a member of the user group
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name     | description       |
      | Team 300 | The best team eva |
    And They own the trip
      | name          | description |
      | My First Trip | A trip      |
    And The trip belongs to the user group
    And the trip has the comment
      | message |
      | Comment |
    And I am authenticated
    And I am a member of the group
    When I want to retrieve comments
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
        "commentsLength": 1,
        "comments": [
          {
            "id": 1,
            "userId": 2,
            "userFirstName": "John",
            "userLastName": "Smith",
            "comment": "Comment",
            "profilePhoto": "defaultPic.png",
            "timestamp": 1569191117
          }
        ]
      }
      """