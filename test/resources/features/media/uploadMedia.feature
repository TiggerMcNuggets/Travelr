Feature: UploadMedia
  Description: The purpose of this feature is to test the api endpoint related to uploading media

#  Scenario: Upload media successfully
#    Given I am authenticated
#    And An album exists
#    When I want to upload media
#    And I select a valid media file
#    And I send the request
#    Then I will receive the response code 201
#    And I will receive the response body
#    """
#    {
#      "id": 1
#    }
#    """
#    And The media exists

#  Scenario: Upload media for a user as an admin
#    Given I am authenticated
#    And I am an admin
#    And An album exists
#    When I want to upload media
#    And I send the request
#    Then I will receive the response code 201
#    And I will receive the response body
#    """
#    {
#      "id": 1
#    }
#    """
#    And The media exists
#
#  Scenario: Upload media when I am not logged in
#    Given I am not authenticated
#    When I want to create an album
#    And I send the request
#    Then I will receive the response code 401
