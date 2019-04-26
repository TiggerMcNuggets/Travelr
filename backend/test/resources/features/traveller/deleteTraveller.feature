Feature: DeleteTravellers
  Description: The purpose of this feature is to test the api endpoint related to deleting a traveller

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Non-admin user deletes their own traveller account
    Given The traveller id is 2
    And I provide the token "abc"
    When I delete the traveller
    Then I will receive a 200 response

  Scenario: Admin user deletes a traveller account that is not their own
    Given The traveller id is 2
    And I provide the token "123"
    When I delete the traveller
    Then I will receive a 200 response

  Scenario: Non-admin user tries to delete another user
    Given The traveller id is 3
    And I provide the token "abc"
    When I delete the traveller
    Then I will receive a 403 response

  Scenario: Admin user tries to delete a master admin user
    Given The traveller id is 5
    And I provide the token "123"
    When I delete the traveller
    Then I will receive a 403 response
  

  Scenario: Admin user tries to delete a user that does not exist
    Given The traveller id is 100
    And I provide the token "123"
    When I delete the traveller
    Then I will receive a 404 response