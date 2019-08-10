Feature: GetTraveller
  Description: The purpose of this feature is to test the api endpoint related to getting one travellers information

  Scenario: Get my profile successfully
    Given I am authenticated
    And The profile details are
      | first | middle | last  | dob | gender | email | accountType |
      | john  | james  | smith | 1   | male   | j@j.s | 0           |
    And The nationalities are
      | id | hasPassport |
      | 1  | true        |
    And The traveller types are
      | travellerType |
      | 1             |
    When I want to get the profile
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
        "id": 2,
        "firstName": "john",
        "middleName": "james",
        "lastName": "smith",
        "dateOfBirth": 1,
        "gender": "male",
        "nationalities": [
          {
            "id": 1,
            "name": "Afghan",
            "hasPassport": true,
            "old": false
          }
        ],
        "travellerTypes": [
          {
            "id": 1,
            "deleted": false,
            "name": "Thrill-Seeker"
          }
        ],
        "email": "j@j.s",
        "accountType": 0,
        "userProfilePhoto": "defaultPic.png",
        "defaultAlbumId": 2
      }
      """

  Scenario: Get my profile when I am not logged in
    Given I am not authenticated
    And The profile details are
      | first | middle | last  | dob | gender | email | accountType |
      | john  | james  | smith | 1   | male   | j@j.s | 0           |
    And The nationalities are
      | id | hasPassport |
      | 1  | true        |
    And The traveller types are
      | travellerType |
      | 1             |
    When I want to get the profile
    And I send the request
    Then I will receive the response code 401

  Scenario: Get someone else's profile
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    # Bit of repetition here in the tables but it lets us reuse steps
    And The profile details are
      | first | middle | last  | dob | gender | email | accountType |
      | john  | james  | smith | 1   | male   | j@j.s | 0           |
    And The nationalities are
      | id | hasPassport |
      | 1  | true        |
    And The traveller types are
      | travellerType |
      | 1             |
    When I want to get the profile
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
        "id": 3,
        "firstName": "john",
        "middleName": "james",
        "lastName": "smith",
        "dateOfBirth": 1,
        "gender": "male",
        "nationalities": [
          {
            "id": 1,
            "name": "Afghan",
            "hasPassport": true,
            "old": false
          }
        ],
        "travellerTypes": [
          {
            "id": 1,
            "deleted": false,
            "name": "Thrill-Seeker"
          }
        ],
        "userProfilePhoto": "defaultPic.png",
        "defaultAlbumId": 3
      }
      """

  Scenario: Get a profile which does not exist
    Given I am authenticated
    And The user does not exist
    When I want to get the profile
    And I send the request
    Then I will receive the response code 404