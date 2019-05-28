Feature: AdminViewUserProfile
  Description: The purpose of the feature is to check if when viewing a user profile as an admin I receive the user full information as the user itself would

  Background: The database is populated and I am signed in
    Given I populate the database
    And I provide the token "123"

  Scenario: View another user profile using an admin account
    Given: I provide the token "123"
    And The traveller id is 3
    When I get traveller information
    Then I will receive a 200 response
    And I will receive the id 3
    And I will receive the email "john@test.com"
