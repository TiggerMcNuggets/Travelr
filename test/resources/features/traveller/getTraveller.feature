Feature: GetTraveller
  Description: The purpose of this feature is to test the api endpoint related to getting one travellers information

  """
    {
      "firstName": "string",
      "middleName": "string",
      "lastName": "string",
      "dateOfBirth": 0,
      "gender": "male",
      "email": "johnsmith@email.com",
      "password": "string",
      "nationalities": [
        {
          "id": 1,
          "hasPassport": true
        }
      ],
      "travellerTypes": [1],
      "accountType": 0
    }
    """


  Scenario: Get my profile successfully
    Given I am authenticated
    And My details are
      | first | middle | last  | dob | gender | email | password | accountType |
      | john  | james  | smith | 1   | male   | j@j.s | password | 0           |
    And My nationalities are
      | id | hasPassport |
      | 1  | true        |
    And My traveller types are
      | travellerType |
      | 1             |
    When I want to get my profile
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
        "id": 1,
        "name": "Eiffel Tower",
        "latitude": 5.0,
        "longitude": 5.0,
        "type": "Landmark",
        "district": "Paris",
        "country": "France",
        "isPublic": false,
        "ownerId": 2,
        "travellerTypes": []
      }
      """

#  Scenario: Get traveller information with my valid ID as admin
#    Given I provide the token "123"
#    And The traveller id is 2
#    When I get traveller information
#    Then I will receive a 200 response
#    And I will receive the id 1
#    And I will receive the email "admin@test.com"
#
#  Scenario: Get traveller information with my valid ID as not an admin
#    Given I provide the token "abc"
#    And The traveller id is 3
#    When I get traveller information
#    Then I will receive a 200 response
#    And I will receive the id 2
#    And I will receive the email "john@test.com"
#
#  Scenario: Get traveller information with my valid ID as not an admin
#    Given I provide the token "abc"
#    And The traveller id is 4
#    When I get traveller information
#    Then I will receive a 200 response
#    And I will receive the id 3
#    And I will receive not receive the email back
#
#  Scenario: Get traveller information with other user valid ID as an admin
#    Given I provide the token "123"
#    And The traveller id is 2
#    When I get traveller information
#    Then I will receive a 200 response
#    And I will receive the id 2
#    And I will receive the email "john@test.com"

#  Scenario: Get traveller information with invalid ID
#    Given I provide the token "123"
#    And The traveller id is 2233
#    When I get traveller information
#    Then I will receive a 404 response
#
#  Scenario: Get traveller information with no token
#    Given I provide the token ""
#    And The traveller id is 1
#    When I get travellers
#    Then I will receive a 401 response