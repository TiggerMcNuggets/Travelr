Feature: GetDestination
  Description: The purpose of this feature is to test the api endpoint related to getting a single destination

  Scenario: Get a destination successfully
    Given I am authenticated
    And I own the destination
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to get a destination
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
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
        "public": false
      }
      """

  Scenario: Get a public destination which is not mine successfully
    Given I am authenticated
    And The global admin owns the destination
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    And The destination is public
    When I want to get a destination
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
        "id": 1,
        "name": "Eiffel Tower",
        "latitude": 5.0,
        "longitude": 5.0,
        "type": "Landmark",
        "district": "Paris",
        "country": "France",
        "isPublic": true,
        "ownerId": 1,
        "travellerTypes": [],
        "public": true
      }
      """

  Scenario: Get a destination when I am not logged in
    Given I am not authenticated
    And I own the destination
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to get a destination
    And I send the request
    Then I will receive the response code 401

  Scenario: Get a private destination that is not mine
    Given I am authenticated
    And The global admin owns the destination
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to get a destination
    And I send the request
    Then I will receive the response code 403

  Scenario: Get a destination that does not exist
    Given I am authenticated
    And I do not own a destination
    When I want to get a destination
    And I send the request
    Then I will receive the response code 404

