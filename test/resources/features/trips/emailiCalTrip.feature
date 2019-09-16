Feature: Email iCal Trip
  Description: The purpose of this feature is to test the api endpoint related emailing iCal file to the whole group of a trip

  Scenario: Email the whole group as an owner
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    # Change email to yours to check received email. REMEMBER TO CHANGE IT BACK BEFORE PUSHING!
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The user is a normal member of the group
    When I want to send everyone an iCal email
    Then I will receive the response code 201


  Scenario: Email the whole group as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And The user exists
      | first | last  | email               | dob |
      | Jane  | Smith | janesmith@email.com | 1   |
    And The user is a normal member of the group
    When I want to send everyone an iCal email
    Then I will receive the response code 201


  Scenario: Email the whole group when not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The user is a normal member of the group
    When I want to send everyone an iCal email
    Then I will receive the response code 401


  Scenario: Email the whole group as a normal member
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    And I am a member of the group
    When I want to send everyone an iCal email
    Then I will receive the response code 403


  Scenario: Email the whole group for a trip I am not a part of
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    When I want to send everyone an iCal email
    Then I will receive the response code 403


  Scenario: Email the whole group when the trip does not exist
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I do not own the trip
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The user is a normal member of the group
    When I want to send everyone an iCal email
    Then I will receive the response code 201