Feature: CreateDestination
  Description: The purpose of this feature is to test the api endpoint related to creating a destination

  Scenario: Create a destination successfully
    Given I am authenticated
    When I want to create a destination
    And The body is
      """
      {
        "name": "Eiffel Tower",
        "latitude": 5.0,
        "longitude": 5.0,
        "type": "Landmark",
        "district": "Paris",
        "country": "France"
      }
      """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
      """
      {
        "id": 1
      }
      """
    When I check the destination
    Then The destination is
    | name         | latitude | longitude | type     | district | country |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris    | France  |

  Scenario: Create a destination successfully for a user as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to create a destination
    And The body is
      """
      {
        "name": "Eiffel Tower",
        "latitude": 5.0,
        "longitude": 5.0,
        "type": "Landmark",
        "district": "Paris",
        "country": "France"
      }
      """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
      """
      {
        "id": 1
      }
      """
    When I check the destination
    Then The destination is
      | name         | latitude | longitude | type     | district | country |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris    | France  |
    And The destination belongs to the user

  Scenario: Create a destination with incomplete destination information
    Given I am authenticated
    When I want to create a destination
    And The body is
      """
      {
        "name": "Eiffel Tower",
        "latitude": 5.0,
        "longitude": 5.0,
        "type": "Landmark",
        "district": "Paris"
      }
      """
    And I send the request
    Then I will receive the response code 400

  Scenario: Create a destination when I am not logged in
    Given I am not authenticated
    When I want to create a destination
    And The body is
      """
      {
        "name": "Eiffel Tower",
        "latitude": 5.0,
        "longitude": 5.0,
        "type": "Landmark",
        "district": "Paris",
        "country": "France"
      }
      """
    And I send the request
    Then I will receive the response code 401
