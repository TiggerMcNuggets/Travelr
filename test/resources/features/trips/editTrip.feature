#Feature: EditTrip
#  Description: The purpose of this feature is to test the api endpoint related to editing trips
#
#  Background: The database is populated and I am signed in
#    Given I populate the database
#    And I provide the token "123"
#
#  Scenario: Edit trip with valid name and 2 valid destinations
#    Given I provide the token "123"
#    And I provide the trip id "1"
#    And The traveller id is 2
#    And A trip with id "1" exists
#    And provide complete trip information
#    When I edit a trip
#    Then I will receive a 200 response
#
#  Scenario: Edit trip with missing name and 2 valid destinations
#    Given I provide the token "123"
#    And The traveller id is 2
#    And I provide the trip id "1"
#    And A trip with id "1" exists
#    And I provide trip information missing name
#    When I edit a trip
#    Then I will receive a 400 response
#
#  Scenario: Edit trip with valid name and 1 valid destination
#    Given I provide the token "123"
#    And I provide the trip id "1"
#    And The traveller id is 2
#    And A trip with id "1" exists
#    And I provide trip information with one destination
#    When I edit a trip
#    Then I will receive a 400 response
#
#  Scenario: Edit trip with valid name and 2 consecutive valid identical destination
#    Given I provide the token "123"
#    And I provide the trip id "1"
#    And The traveller id is 2
#    And A trip with id "1" exists
#    And I provide trip information with two consecutive identical destinations
#    When I edit a trip
#    Then I will receive a 400 response
#
#  Scenario: Edit trip that does not exist
#    Given I provide the token "123"
#    And I provide the trip id "100"
#    And The traveller id is 2
#    And A trip with id "100" does not exist
#    Then I will receive a 404 response
#
#  Scenario: Edit trip with valid name and 2 valid destinations but user is not logged in
#    Given I do not provide a token
#    And I provide the trip id "1"
#    And The traveller id is 2
#    And A trip with id "1" exists
#    And provide complete trip information
#    When I edit a trip
#    Then I will receive a 401 response
#
#  Scenario: Non-Admin user edits trip made by another user
#    Given I am logged in as a non-admin user
#    And I provide the trip id "1"
#    And The traveller id is 2
#    And provide complete trip information
#    When I edit a trip
#    Then I will receive a 403 response
#
#  Scenario: Admin user edits trip made by another user
#    Given I am logged in as an admin user
#    And I provide the trip id "2"
#    And The traveller id is 2
#    And provide complete trip information
#    When I edit a trip
#    Then I will receive a 200 response
#
#  Scenario: Edit trip with valid name and 2 valid destinations but invalid auth token
#    Given I provide the token "kjkj123"
#    And The traveller id is 2
#    And I provide the trip id "2"
#    And A trip with id "2" exists
#    Then I will receive a 401 response
#
#  Scenario: Edit trip with valid name and 2 valid destinations correctly updates the trip name
#    Given I am logged in as an admin user
#    And I provide the trip id "1"
#    And The traveller id is 2
#    And A trip with id "1" exists
#    And I get a trip
#    And the trip has name of the "Backpacking 2018"
#    And provide complete trip information with name "The best place eva"
#    When I edit a trip
#    And I get a trip
#    Then the trip has name "The best place eva"
#    And I will receive a 200 response

  Scenario: Edit a trip successfully
  # TODO: Implement, (expect 201)

  Scenario: Edit another user's trip as an admin
  # TODO: Implement, (expect 201)

  Scenario: Edit a trip to only have one destination
  # TODO: Implement, (expect 400)

  Scenario: Edit a trip with missing name field
  # TODO: Implement, (expect 400)

  Scenario: Edit a trip when not logged in
  # TODO: Implement, (expect 401)

  Scenario: Edit another user's trip as a normal user
  # TODO: Implement, (expect 403)

  Scenario: Edit a trip that does not exist
  # TODO: Implement, (expect 404)

# TODO: Add scenarios for creating trips related to trips within trips story

