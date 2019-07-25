Feature: CreateAlbum
  Description: The purpose of this feature is to test the api endpoint related to creating an album

  Scenario: Create an album successfully
    Given I am authenticated
    When I want to create an album
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
      """
      {
        "id": 1
      }
      """
    When I check the album
    Then The album id is 1

  Scenario: Create an album successfully for a user as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to create an album
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
      """
      {
        "id": 1
      }
      """
    When I check the album
    Then The album id is 1
#    And The album belongs to the user TODO: implement this step when users are connected to albums
#
  Scenario: Create an album when I am not logged in
    Given I am not authenticated
    When I want to create an album
    And I send the request
    Then I will receive the response code 401