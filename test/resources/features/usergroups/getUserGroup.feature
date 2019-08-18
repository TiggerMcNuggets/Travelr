Feature: Get User Group
  Description: The purpose of this feature is to test the api endpoint getting a user group

  Scenario: Get a user group successfully
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to get the user group
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
         "id":1,
         "name":"Team 300",
         "description":"The best team eva",
         "owners":[
            2
         ],
         "members":[
            {
               "id":2,
               "firstName":"Test",
               "middleName":null,
               "lastName":"User",
               "dateOfBirth":1,
               "gender":"Male",
               "nationalities":[

               ],
               "travellerTypes":[
                  {
                     "id":1,
                     "deleted":false,
                     "name":"Thrill-Seeker"
                  }
               ],
               "userProfilePhoto":"defaultPic.png",
               "defaultAlbumId":2
            }
         ]
      }
      """

  Scenario: Get a group not part of as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to get the user group
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
      """
      {
         "id":1,
         "name":"Team 300",
         "description":"The best team eva",
         "owners":[
            3
         ],
         "members":[
            {
               "id":3,
               "firstName":"John",
               "middleName":null,
               "lastName":"Smith",
               "dateOfBirth":1,
               "gender":"Male",
               "nationalities":[

               ],
               "travellerTypes":[

               ],
               "userProfilePhoto":"defaultPic.png",
               "defaultAlbumId":3
            }
         ]
      }
      """

  Scenario: Get a user group when not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to get the user group
    And I send the request
    Then I will receive the response code 401

  Scenario: Get a user group that does not exist
    Given I am authenticated
    And I do not own an user group
    When I want to get the user group
    And I send the request
    Then I will receive the response code 404