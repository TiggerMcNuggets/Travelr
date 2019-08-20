Feature: Create User Group
  Description: The purpose of this feature is to test the api endpoint related to creating a user group

  Scenario: Create a user group successfully
    Given I am authenticated
    When I want to create a group
    And The body is
    """
    {
      "name": "Good Ol' Mates",
      "description": "The fam from SENG302 2019 YEET!"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
    """
    {
      "id": 1
    }
    """

  Scenario: Create a user group without description
    Given I am authenticated
    When I want to create a group
    And The body is
    """
    {
      "name": "Good Ol' Mates"
    }
    """
    And I send the request
    Then I will receive the response code 201
    And I will receive the response body
    """
    {
      "id": 1
    }
    """

  Scenario: Create a user group without description
    Given I am authenticated
    When I want to create a group
    And The body is
    """
    {
      "description": "The fam from SENG302 2019 YEET!"
    }
    """
    And I send the request
    Then I will receive the response code 400

  Scenario: Create a user group when not logged in
    Given I am not authenticated
    When I want to create a group
    And The body is
    """
    {
      "name": "Good Ol' Mates",
      "description": "The fam from SENG302 2019 YEET!"
    }
    """
    And I send the request
    Then I will receive the response code 401