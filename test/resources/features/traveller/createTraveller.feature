Feature: CreateTraveller
  Description: The purpose of this feature is to test the api endpoint related to creating travellers

  Scenario: Signs up successfully
    When I want to sign up
    And The body is
    """
    {
      "firstName": "string",
      "middleName": "string",
      "lastName": "string",
      "dateOfBirth": 0,
      "gender": "male",
      "email": "string@string.string",
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
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
    """
    {
      "id": 2
    }
    """

  Scenario: Signs up with missing fields
    When I want to sign up
    And The body is
    """
    {
      "middleName": "string",
      "lastName": "string",
      "dateOfBirth": 0,
      "gender": "male",
      "email": "string@string.string",
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
    And I send the request
    Then I will receive the response code 400

  Scenario: The email exists
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to sign up
    And The body is
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
    And I send the request
    Then I will receive the response code 400
