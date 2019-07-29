Feature: Create Trip
  Description: The purpose of this feature is to test the api endpoint related to creating trips

  Scenario: Create a trip successfully
    Given I am authenticated
    And I own the following destinations
      | name         | latitude | longitude | type     | district   | country    |
      | Eiffel Tower | 5.0      | 5.0       | Landmark | Paris      | France     |
      | Big River    | 3.0      | 3.0       | River    | Canterbury | New Zealand|
      | Small River  | 3.0      | 3.0       | River    | Otago      | New Zealand|
    When I create a trip called "My Trip" from my destinations
    Then I have a trip 







  Scenario: Create a trip for a user as an admin
  # TODO: Implement, (expect 201)

  Scenario: Create a trip with only one destination
  # TODO: Implement, (expect 400)

  Scenario: Create a trip with missing name field
  # TODO: Implement, (expect 400)

  Scenario: Create a trip when not logged in
  # TODO: Implement, (expect 401)

  Scenario: Create a trip for another user as a normal user
  # TODO: Implement, (expect 403)

  Scenario: Create a trip for a user that does not exist as an admin
  # TODO: Implement, (expect 404)

# TODO: Add scenarios for creating trips related to trips within trips story
