#Feature: GetTrip
#  Description: The purpose of this feature is to test the api endpoint related to getting a trip
#
#  Background: The database is populated and I am signed in
#    Given I populate the database
#    And I provide the token "123"
#    And The traveller id is 1
#
#  Scenario: get my own trip with a valid tripId and it exists
#    Given I provide a tripId of "1"
#    When I get a trip
#    Then I will receive a 200 response
#
##  Scenario: get someone else's trip when I am not an admin with a valid tripId and the trip exists
##    Given I provide a tripId of "2"
##    And The traveller id is 4
##    And I provide the token "nonadmin"
##    When I get a trip
##    Then I will receive a 403 response
#
#  Scenario: get someone else's trip when I am an admin with a valid tripId and the trip exists
#    Given I provide a tripId of "2"
#    And The traveller id is 1
#    And I provide the token "123"
#    When I get a trip
#    Then I will receive a 200 response
#
##  Scenario: get my trip when I am an not admin with a valid tripId and the trip exists
##    Given I provide a tripId of "2"
##    And The traveller id is 2
##    And I provide the token "abc"
##    When I get a trip
##    Then I will receive a 200 response
#
#  Scenario: get a trip with a invalid tripId and it does not exist
#    Given I provide a tripId of "100"
#    When I get a trip
#    Then I will receive a 404 response
#
#  Scenario: get a trip with a invalid tripId and it does not exist
#    Given I provide a tripId of "1"
#    When I get a trip without an auth token
#    Then I will receive a 401 response