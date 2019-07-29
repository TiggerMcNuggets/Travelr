Feature: EditAlbum
  Description: The purpose of this feature is to test the api endpoints related to editing an album

  Scenario: Edit an album successfully
    Given I am authenticated
    And I own the album
      | name   |
      | Album  |
    When I want to edit the album
    And The body is
    """
    {
      "name": "New Album"
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The album is
      | name      |
      | New Album |


  Scenario: Edit an album successfully for a user as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the album
      | name  |
      | Album |
    When I want to edit the album
    And The body is
    """
    {
      "name": "New Album"
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The album is
      | name      |
      | New Album |


  Scenario: Edit an album with incorrect request body
    Given I am authenticated
    And I own the album
      | name  |
      | Album |
    When I want to edit the album
    And The body is
    """
    {
      "nname": "New Album"
    }
    """
    And I send the request
    Then I will receive the response code 400
    And The album is
      | name  |
      | Album |


  Scenario: Edit an album with incorrect request body
    Given I am authenticated
    And I own the album
      | name  |
      | Album |
    And I own the album
      | name      |
      | New Album |
    When I want to edit the album
    And The body is
    """
    {
      "name": "Album"
    }
    """
    And I send the request
    Then I will receive the response code 400
    And The album is
      | name      |
      | New Album |


  Scenario: Edit an album when not logged in
    Given I am not authenticated
    And I own the album
      | name  |
      | Album |
    When I want to edit the album
    And The body is
    """
    {
      "name": "New Album"
    }
    """
    And I send the request
    Then I will receive the response code 401
    And The album is
      | name  |
      | Album |


  Scenario: Edit an album that isn't mine
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the album
      | name  |
      | Album |
    When I want to edit the album
    And The body is
    """
    {
      "name": "New Album"
    }
    """
    And I send the request
    Then I will receive the response code 403
    And The album is
      | name  |
      | Album |


  Scenario: Edit an album that doesn't exist
    Given I am authenticated
    And I do not own an album
    When I want to edit the album
    And The body is
    """
    {
      "name": "New Album"
    }
    """
    And I send the request
    Then I will receive the response code 404