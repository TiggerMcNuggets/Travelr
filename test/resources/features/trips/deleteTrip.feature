Feature: Delete Trip
  Description: The purpose of this feature is to test the api endpoint related to deleting trips

  Scenario: Delete a trip successfully
  Given I am authenticated
    And I own the trip
      | name         | description |
      | trip downtown| get meowt   |
    And I want to delete the trip
    When I send the request
    Then I will receive the response code 200

  Scenario: Delete another user's trip an admin
  Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the trip
      | name         | description |
      | trip downtown| get meowt   |
    When I want to delete the trip
    And I send the request
    Then I will receive the response code 200

  Scenario: Delete a trip when not logged in
    Given I am not authenticated
    And I own the trip
      | name         | description |
      | trip downtown| get meowt   |
    When I want to delete the trip
    And I send the request
    Then I will receive the response code 401

  Scenario: Delete another user's trip as a normal user
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the trip
      | name         | description |
      | trip downtown| get meowt   |
    When I want to delete the trip
    And I send the request
    Then I will receive the response code 403

  Scenario: Delete a trip that does not exist
    Given I am authenticated
    And I do not own the trip
    When I want to delete the trip
    And I send the request
    Then I will receive the response code 404


# TODO: Add scenarios for deleting trips related to trips within trips story