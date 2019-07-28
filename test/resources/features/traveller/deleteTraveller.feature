#Feature: DeleteTravellers
#  Description: The purpose of this feature is to test the api endpoint related to deleting a traveller
#
#  Background: The database is populated
#    Given I populate the database
#    Then I will receive a 200 response
#
#  Scenario: Admin user deletes a traveller account that is not their own
#    Given The traveller id is 3
#    And I provide the token "123"
#    When I delete the traveller
#    Then I will receive a 200 response
#
#  Scenario: Non-admin user deletes their own traveller account
#    Given The traveller id is 2
#    And I provide the token "abc"
#    When I delete the traveller
#    Then I will receive a 403 response
#
#  Scenario: Non-admin user tries to delete another user
#    Given The traveller id is 4
#    And I provide the token "abc"
#    When I delete the traveller
#    Then I will receive a 403 response
#
#  Scenario: Admin user tries to delete a master admin user
#    Given The traveller id is 6
#    And I provide the token "123"
#    When I delete the traveller
#    Then I will receive a 403 response
#
#  Scenario: Admin user tries to delete a user that does not exist
#    Given The traveller id is 100
#    And I provide the token "123"
#    When I delete the traveller
#    Then I will receive a 404 response
#
#  Scenario: Admin tries to delete a user and the user is no longer available.
#    Given The traveller id is 4
#    And I provide the token "123"
#    When I delete the traveller
#    And I get traveller information
#    Then I will receive a 404 response
#
#  Scenario: Undo admin deleting a user and the user is now available.
#    Given The traveller id is 3
#    And I provide the token "123"
#    When I delete the traveller
#    And I undo the deletion status on the user with id 3
#    And I get traveller information
#    Then I will receive a 200 response
#

  Scenario: Deleting an account that is not mine as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to delete the account
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    {
      "id": 3,
      "deleted": true
    }
    """

  Scenario: Deleting an account that is not mine as an admin when I am not logged in
    Given I am not authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to delete the account
    And I send the request
    Then I will receive the response code 401

  Scenario: Deleting an account that is not mine as a normal user
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to delete the account
    And I send the request
    Then I will receive the response code 403

  Scenario: Deleting my account as an admin
    Given I am authenticated
    And I am an admin
    When I want to delete the account
    And I send the request
    Then I will receive the response code 403

  Scenario: Deleting an account that doesn't exist as an admin
    Given I am authenticated
    And I am an admin
    And The user does not exist
    When I want to delete the account
    And I send the request
    Then I will receive the response code 404
