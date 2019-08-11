Feature: GetDestinations
  Description: The purpose of this feature is to test the api endpoint related to getting destinations

  Scenario: Get destinations successfully
    Given I am authenticated
    And I own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to get destinations
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      [
        {
          "id": 1,
          "name": "Eiffel Tower",
          "latitude": 5.0,
          "longitude": 5.0,
          "type": "Landmark",
          "district": "Paris",
          "country": "France",
          "isPublic": false,
          "ownerId": 2,
          "travellerTypes": [],
          "defaultAlbumId":4
        }
      ]
      """

  Scenario: Get destinations that includes a private destination that is not mine
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    And I am authenticated
    When I want to get destinations
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      []
      """

  Scenario: Get destinations that includes a public destination successfully
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    And I am authenticated
    And The destination is public
    When I want to get destinations
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      [
        {
          "name":"Eiffel Tower",
          "latitude":5.0,
          "longitude":5.0,
          "type":"Landmark",
          "district":"Paris",
          "country":"France",
          "ownerId":2,
          "isPublic":true,
          "id":1,
          "travellerTypes":[],
          "defaultAlbumId":4
        }
      ]
      """

  Scenario: Get destinations when I am not logged in
    Given I am not authenticated
    And I own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to get destinations
    And I send the request
    Then I will receive the response code 401

  Scenario: Get someone elses destinations
    Given I am authenticated
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to get destinations
    And I send the request
    Then I will receive the response code 403