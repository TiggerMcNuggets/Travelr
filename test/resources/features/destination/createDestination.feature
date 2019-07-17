Feature: CreateDestination
  Description: The purpose of this feature is to test the api endpoint related to creating a destination

  Scenario: Create a destination successfully
    Given I am an authenticated user who is logged in
    When I create the destination
    | name         | latitude | longitude | type     | district | country |
    | Eiffel Tower | 5        | 5         | Landmark | Paris    | France  |
    Then I will receive a 201 response
    // we test the response where possible instead of just the response code
    And I will receive the id 1


  Scenario: Create a destination with incomplete destination information
  Given I am an authenticated user who is logged in
  When I create the destination
    | name         | latitude | longitude | type     | district | country |
    |              | 5        | 5         | Landmark | Paris    | France  |
  Then I will receive a 400 response


  Scenario: Create a destination when I am not logged in
    Given I am an authenticated user who is NOT logged in
    When I create the destination
      | name         | latitude | longitude | type     | district | country |
      | Eiffel Tower | 5        | 5         | Landmark | Paris    | France  |
    Then I will receive a 401 response
