Feature: Get User Groups
  Description: The purpose of this feature is to test the api endpoint related to getting all groups that a user belongs to

  Scenario: Get all groups that a user belongs to successfully
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to get all of the groups that belongs to the user
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    [
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
    ]
    """


  Scenario: Get multiple groups that a user belongs to successfully
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I own the user group
      | name         | description           |
      | Team 400     | Not the best team eva |
    When I want to get all of the groups that belongs to the user
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    [
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
       },
       {
          "id":2,
          "name":"Team 400",
          "description":"Not the best team eva",
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
    ]
    """


  Scenario: Get all groups that a user belongs to which includes another member
    Given I am authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And the user group has the group member
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    When I want to get all of the groups that belongs to the user
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    [  
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
             },
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
    ]
    """


  Scenario: Get all groups that a user belongs to as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to get all of the groups that belongs to the user
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    [
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
    ]
    """

  Scenario: Get all groups on the website when logged in as an admin
    Given I am authenticated
    And I am an admin
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to get all user groups
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    [
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
    ]
    """


  Scenario: Do not get all groups when I am not an admin
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to get all user groups
    And I send the request
    Then I will receive the response code 403


  Scenario: Do not get group that I am not a part of
    Given The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    And I am authenticated
    When I want to get all of the groups that belongs to the user
    And I send the request
    Then I will receive the response code 200
    And I will receive the response body
    """
    []
    """


  Scenario: Get all groups that a user belongs to when not logged in
    Given I am not authenticated
    And I own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to get all of the groups that belongs to the user
    And I send the request
    Then I will receive the response code 401


  Scenario: Get all groups that a user belongs to that isn't me
    Given I am authenticated
    And The user exists
      | first | last  | email               | dob |
      | John  | Smith | johnsmith@email.com | 1   |
    And They own the user group
      | name         | description         |
      | Team 300     | The best team eva   |
    When I want to get all of the groups that belongs to the user
    And I send the request
    Then I will receive the response code 403


  Scenario: Get all groups that a user belongs to that doesn't exist
    Given I am authenticated
    And The user does not exist
    When I want to get all of the groups that belongs to the user
    And I send the request
    Then I will receive the response code 404