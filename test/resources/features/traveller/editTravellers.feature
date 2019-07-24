Feature: EditTravellers
  Description: The purpose of this feature is to test the api endpoint related to editing a traveller

  Scenario: Edit my profile successfully
    Given I am authenticated
    When I want to edit the profile
    And The body is
    """
    {
      "firstName": "string",
      "middleName": "string",
      "lastName": "string",
      "dateOfBirth": 0,
      "gender": "male",
      "nationalities": [
        {
          "id": 1,
          "hasPassport": true
        }
      ],
      "travellerTypes": [1]
    }
    """
    And I send the request
    Then I will receive the response code 200
    And The traveller details are
    | first  | middle | last   | dob | gender |
    | string | string | string | 0   | male   |
    And The traveller's nationalities are
    | id | hasPassport |
    | 1  | true        |
    And The traveller's traveller types are
    | travellerType |
    | 1             |

  Scenario: Edit my profile with missing fields
    Given I am authenticated
    When I want to edit the profile
    And The body is
    """
    {
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

  Scenario: Edit traveller when I am not logged in
    Given I am not authenticated
    When I want to edit the profile
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
    Then I will receive the response code 401

  Scenario: Edit traveller that isn't me
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to edit the profile
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
    Then I will receive the response code 403

  Scenario: Edit traveller that does not exist
    Given I am authenticated
    And The user does not exist
    When I want to edit the profile
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
    Then I will receive the response code 404