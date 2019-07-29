Feature: Delete Album
  Description: The purpose of this feature is to test the api endpoint related to deleting an album

  Scenario: Delete an album successfully
    Given I am authenticated
    And I own the album
      | name         |
      | January      |
    When I want to delete the album
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Album deleted
      """
    And The album does not exist

  Scenario: Delete album successfully for a user as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the album
      | name         |
      | January      |
    When I want to delete the album
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Album deleted
      """
    And The album does not exist

  Scenario: Delete album when unauthenticated
    Given I am not authenticated
    And I own the album
      | name         |
      | January      |
    When I want to delete the album
    And I send the request
    Then I will receive the response code 401
    And The album does exist

  Scenario: Delete album that is not mine
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the album
      | name         |
      | January      |
    When I want to delete the album
    And I send the request
    Then I will receive the response code 403
    And The album does exist

  # CAN'T DO THIS UNTIL THE GET USER ALBUMS COMPLETED
#  Scenario: Delete album that does not exist
#    Given I am authenticated
#    And I do not own a album
#    When I want to delete the album
#    And I send the request
#    Then I will receive the response code 404

