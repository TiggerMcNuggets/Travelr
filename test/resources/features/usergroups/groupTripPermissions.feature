Feature: TripPermissions
  Description: The purpose of this feature is to test trip and group permissions

  Scenario: User is permitted to read and write for the group as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    Then I am permitted to read for the group
    And I am permitted to write for the group

  Scenario: User is permitted to read and write for the group as an owner
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    Then I am permitted to read for the group
    And I am permitted to write for the group

  Scenario: User is permitted to read but not write for the group as a normal member
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I am authenticated
    And I am a member of the group
    Then I am permitted to read for the group
    And I am not permitted to write for the group

  Scenario: User is not permitted to read or write for the group when not part of it
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I am authenticated
    Then I am not permitted to read for the group
    And I am not permitted to write for the group

  Scenario: User is permitted to read and write for the trip as a trip owner
    Given I am authenticated
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    Then I am permitted to read for the trip
    And I am permitted to write for the trip

  Scenario: User is permitted to read and write for the trip as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    Then I am permitted to read for the trip
    And I am permitted to write for the trip

  Scenario: User is permitted to read and write for the trip as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    Then I am permitted to read for the trip
    And I am permitted to write for the trip

  Scenario: User is permitted to read and write for the trip as the group owner
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the trip
      | name         | description |
      | My First Trip| A trip      |
    And The trip belongs to the user group
    Then I am permitted to read for the trip
    And I am permitted to write for the trip

  Scenario: User is permitted to read but not write for the trip as a normal member
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description |
      | My First Trip| A trip      |
    And They own the trip
      | name         | description |
      | My First Trip| A trip      |
    And I am authenticated
    And I am a member of the group
    And The trip belongs to the user group
    Then I am permitted to read for the trip
    And I am not permitted to write for the trip

  Scenario: User is not permitted to read or write for the trip when not part of it
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
    Then I am not permitted to read for the trip
    And I am not permitted to write for the trip
