Feature: Promote/Demote users to group owners

  Scenario: Promote a group member to group owner
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And I want to toggle promote the user
    And I send the request
    Then I will receive the response code 200
    And The user is now an owner of the group


  Scenario: Demote a group owner to a normal member of the group
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group owner
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And I want to toggle promote the user
    And I send the request
    Then I will receive the response code 200
    And The user is now a normal member of the group


  Scenario: Promote a group member when I am not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And I want to toggle promote the user
    And I send the request
    Then I will receive the response code 401
    And The user is now a normal member of the group


  Scenario: Promote a group member when I don't own the group
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | James  | Smith | jamesmith@email.com | 1   |
    When I want to toggle promote the user
    And I send the request
    Then I will receive the response code 403


  Scenario: Promote a group member when I don't own the group but I am an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | James  | Smith | jamesmith@email.com | 1   |
    When I want to toggle promote the user
    And I send the request
    Then I will receive the response code 200
    And The user is now an owner of the group

  Scenario: Demote the last group owner when I am not an owner but am an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group owner
      | first | last  | email               | dob |
      | James | Smith | jamesmith@email.com | 1   |
    And I want to toggle promote the user
    And I send the request
    Then I will receive the response code 200
    And The user is now a normal member of the group

  Scenario: Demote the last group owner
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I am the group member
    And I want to toggle promote the user
    And I send the request
    Then I will receive the response code 403


  Scenario: Promote a group member who doesn't belong to the group
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group does not have another group member
    When I want to toggle promote the user
    And I send the request
    Then I will receive the response code 403


  Scenario: Promote a group member who doesn't exist
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And The future member does not exist
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And I want to toggle promote the user
    And I send the request
    Then I will receive the response code 403





