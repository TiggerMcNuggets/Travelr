Feature: Make Destination Public
  Description: Tests the api endpoint related to making a destination public

  Scenario: Make a destination public successfully
    Given I am authenticated
    And I own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to make the destination public
    And I send the request
    Then I will receive the response code 201
    And My destination is public

  Scenario: Make a destination private successfully
    Given I am authenticated
    And I own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    And The destination is public
    When I want to make the destination private
    And I send the request
    Then I will receive the response code 201
    And My destination is private

  Scenario: Make a destination public when I am not logged in
    Given I am not authenticated
    And I own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    When I want to make the destination public
    And I send the request
    Then I will receive the response code 401

  Scenario: Make a destination private when I am not logged in
    Given I am not authenticated
    And I own the destination
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
    And The destination is public
    When I want to make the destination private
    And I send the request
    Then I will receive the response code 401

  Scenario: Make a destination public that does not exist
    Given I am authenticated
    And I do not own a destination
    When I want to make the destination public
    And I send the request
    Then I will receive the response code 404

  # TODO: Scenario: Make a destination public and someone else uses in a trip
  # TODO: Scenario: Deletion after merging destinations
  # ^ Both of these are integration tests that requires trip tests to be finished