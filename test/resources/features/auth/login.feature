Feature: Login
  Description: The purpose of this feature is to test api endpoints related to logging in

  Scenario: Log in successfully
    Given The user exists
    | first | last  | email               | dob |
    | John  | Smith | johnsmith@email.com | 1   |
    And The password is "password"
    When I want to login
    And The body is
      """
      {
        "email": "johnsmith@email.com",
        "password": "password"
      }
      """
    And I send the request
    Then I will receive the response code 200

  Scenario: Log in without a password
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The password is "password"
    When I want to login
    And The body is
      """
      {
        "email": "johnsmith@email.com"
      }
      """
    And I send the request
    Then I will receive the response code 400

  Scenario: Log in without a password
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The password is "password"
    When I want to login
    And The body is
      """
      {
        "email": "johnsmith@email.com"
      }
      """
    And I send the request
    Then I will receive the response code 400

  Scenario: Log in with incorrect password
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The password is "password"
    When I want to login
    And The body is
      """
      {
        "email": "johnsmith@email.com",
        "password": "pass"
      }
      """
    And I send the request
    Then I will receive the response code 401

  Scenario: Log in with incorrect email
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And The password is "password"
    When I want to login
    And The body is
      """
      {
        "email": "john@email.com",
        "password": "password"
      }
      """
    And I send the request
    Then I will receive the response code 401