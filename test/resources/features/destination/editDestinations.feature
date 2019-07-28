Feature: EditDestinations
  Description: The purpose of this feature is to test the api endpoint related to editing a destination

  Scenario: Edit a destination successfully
    Given I am authenticated
    And I own the destination
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to edit the destination
    And The body is
      """
      {
        "name": "Eiffel",
        "latitude": 10.0,
        "longitude": 10.0,
        "type": "Land",
        "district": "Par",
        "country": "Fra"
      }
      """
    And I send the request
    Then I will receive the response code 200
    And The destination is
      | name   | latitude  | longitude  | type | district | country |
      | Eiffel | 10.0      | 10.0       | Land | Par      | Fra     |

  Scenario: Edit a destination successfully for a user as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to edit the destination
    And The body is
      """
      {
        "name": "Eiffel",
        "latitude": 10.0,
        "longitude": 10.0,
        "type": "Land",
        "district": "Par",
        "country": "Fra"
      }
      """
    And I send the request
    Then I will receive the response code 200
    And The destination is
      | name   | latitude  | longitude  | type | district | country |
      | Eiffel | 10.0      | 10.0       | Land | Par      | Fra     |

  Scenario: Edit a destination with incomplete destination information
    Given I am authenticated
    And I own the destination
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to edit the destination
    And The body is
      """
      {
        "name": "Eiffel",
        "latitude": 10.0,
        "longitude": 10.0,
        "type": "Land",
        "district": "Par"
      }
      """
    And I send the request
    Then I will receive the response code 400
    And The destination is
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |

  Scenario: Edit a destination when I am not logged in
    Given I am not authenticated
    And I own the destination
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to edit the destination
    And The body is
      """
      {
        "name": "Eiffel",
        "latitude": 10.0,
        "longitude": 10.0,
        "type": "Land",
        "district": "Par",
        "country": "Fra"
      }
      """
    And I send the request
    Then I will receive the response code 401
    And The destination is
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |

  Scenario: Edit a destination that is not mine
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the destination
    | name         | latitude | longitude | type     | district   | country    |
    | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to edit the destination
    And The body is
      """
      {
        "name": "Eiffel",
        "latitude": 10.0,
        "longitude": 10.0,
        "type": "Land",
        "district": "Par",
        "country": "Fra"
      }
      """
    And I send the request
    Then I will receive the response code 403
    And The destination is
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |

  Scenario: Edit a destination that does not exist
    Given I am authenticated
    And I do not own a destination
    When I want to edit the destination
    And The body is
      """
      {
        "name": "Eiffel",
        "latitude": 10.0,
        "longitude": 10.0,
        "type": "Land",
        "district": "Par",
        "country": "Fra"
      }
      """
    And I send the request
    Then I will receive the response code 404