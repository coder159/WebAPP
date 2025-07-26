Feature: Sample API CRUD and auth

  Background:
    * def base = systemProperty('baseUrl') ? systemProperty('baseUrl') : 'http://localhost:4000'

  Scenario: Successful login
    Given I have a payload from "data/login-valid.json"
    When I POST to "/login"
    Then the response status should be 200
    And save the token

  Scenario: Login fails with wrong creds
    Given I have a payload from "data/login-invalid.json"
    When I POST to "/login"
    Then the response status should be 401

  Scenario: GET /items requires auth
    When I GET "/items" without auth
    Then the response status should be 401

  Scenario: CRUD item happy path
    Given I have a valid token
    And I have a payload from "data/item.json"
    When I POST to "/items"
    Then the response status should be 201
    And I remember the created id
    When I GET "/items"
    Then the response status should be 200
    And the response should contain the text "Buy milk"
    When I PUT "/items/{id}" with body {"text":"Buy oat milk"}
    Then the response status should be 200
    And the response should contain the text "Buy oat milk"
    When I DELETE "/items/{id}"
    Then the response status should be 204

  Scenario: Negative POST /items without text
    Given I have a valid token
    When I POST to "/items" with body {"bad":"payload"}
    Then the response status should be 400

  Scenario: Negative update non-existent id
    Given I have a valid token
    When I PUT "/items/9999" with body {"text":"nope"}
    Then the response status should be 404

  Scenario: Negative delete non-existent id
    Given I have a valid token
    When I DELETE "/items/9999"
    Then the response status should be 404
