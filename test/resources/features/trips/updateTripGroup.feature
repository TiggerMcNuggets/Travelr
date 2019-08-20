Feature: UpdateTripGroup
  Description: The purpose of this feature is to test the api endpoint related to updating trips

  Scenario: Add group to a trip
    Given I am authenticated
    And I own the trip
      | name         |
      | My First Trip|
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to add the group to the trip
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body text
      """
      Group member removed
      """
    And the group is added to the trip

