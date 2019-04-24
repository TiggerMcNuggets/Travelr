Feature: AdminCreateTraveller
  Description: The purpose of this feature is to test the api endpoint related to creating travellers as an admin

  Background: The database is populated and I am signed in
    Given I populate the database
    And I provide the token "123"

  Scenario: Create a destination for another user as an admin
    Given I provide complete destination information
    When I create a destination for user with id 2
    Then I will receive a 201 response