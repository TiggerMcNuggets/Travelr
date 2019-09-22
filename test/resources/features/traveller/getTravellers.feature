Feature: GetTravellers
  Description: The purpose of this feature is to test the api endpoint related to getting travellers

  Scenario: Get profiles successfully
    Given All previous users are removed
    And I am authenticated
    And The profile details are
      | first | middle | last  | dob | gender | email | accountType |
      | john  | james  | smith | 1   | male   | j@j.s | 0           |
    And The nationalities are
      | id | hasPassport |
      | 1  | true        |
    And The traveller types are
      | travellerType |
      | 1             |
    When I want to get profiles
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      [
        {
          "id":2,
          "firstName":"john",
          "middleName":"james",
          "lastName":"smith",
          "dateOfBirth":1,
          "gender":"male",
          "nationalities":[
            {
              "id":1,
              "name":"Afghan",
              "hasPassport":true,
              "old":false
            }
          ],
          "travellerTypes":[
            {
              "id":1,
              "deleted":false,
              "name":"Thrill-Seeker"
            }
          ],
          "userProfilePhoto":"defaultPic.png",
          "defaultAlbumId": 2
        }
      ]
      """

  Scenario: Get profiles successfully when I'm not logged in
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
    When I want to get profiles
    And I send the request
    Then I will receive the response code 401

