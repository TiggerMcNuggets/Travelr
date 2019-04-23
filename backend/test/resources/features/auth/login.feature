Feature: Login
  Description: The purpose of this feature is to test api endpoints related to logging in

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Log in successfully
    Given I provide the email "adam@test.com" and the password "123"
    When I login
    Then I will receive a 200 response
    And I will receive the id 1

  Scenario: Log in with missing fields
    Given I provide an incomplete login json
    When I login
    Then I will receive a 400 response

  Scenario: Log in with wrong password
    Given I provide the email "adam@test.com" and the password "12345"
    When I login
    Then I will receive a 401 response

  Scenario: Logs in with wrong email
    Given I provide the email "adamadam@test.com" and the password "123"
    When I login
    Then I will receive a 401 response