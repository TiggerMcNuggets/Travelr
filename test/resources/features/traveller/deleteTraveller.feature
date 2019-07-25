Feature: DeleteTravellers
  Description: The purpose of this feature is to test the api endpoint related to deleting a traveller

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
