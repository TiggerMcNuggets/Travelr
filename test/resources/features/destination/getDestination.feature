Feature: GetDestinations
  Description: The purpose of this feature is to test the api endpoint related to getting a single destination

  Background: The database is populated
    Given I populate the database
    Then I will receive a 200 response

  Scenario: Get own destination as admin
    Given I provide the token "123"
    And The destination id is 1
    And The traveller id is 1
    When I get destination information
    Then I will receive a 200 response


  Scenario: Get other users destination as admin
    Given I provide the token "123"
    # This destination does not belong to admin.
    And The destination id is 4
    And The traveller id is 1
    When I get destination information
    Then I will receive a 200 response

#  Scenario: Get own destination as normal user
#    # User 2
#    Given I provide the token "abc"
#    # Destination 4 belongs to user 2
#    And The destination id is 4
#    And The traveller id is 2
#    When I get destination information
#    Then I will receive a 200 response

  Scenario: Get destination information with no token
    Given I provide the token ""
    And The destination id is 1
    And The traveller id is 2
    When I get destination information
    Then I will receive a 401 response


#    this test will need to be updated when we have created public
#    and private destinations as any user can retrieve a public destination
  Scenario: Get destination information with user that is not
        the creator of the destination
    Given I provide the token "abc"
    And The destination id is 1
    And The traveller id is 1
    When I get destination information
    Then I will receive a 403 response

  Scenario: Get a destination that does not exist
    Given I provide the token "123"
    And The destination id is 200
    And The traveller id is 1
    When I get destination information
    Then I will receive a 404 response

