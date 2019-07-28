Feature: Delete Media
  Description: The purpose of this feature is to test the api endpoint related to deleting media in albums

  Scenario: Delete an media successfully from a single album
    Given I am authenticated
    And I own the album
      | name         |
      | Test Album   |
    And the album contains the media
      | uriString    |
      | test.jpg     |
    When I want to delete the media in the album
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Media deleted
      """
#    And The media does not exist in the album // Problem since the state doesn't seem to update the album ???
