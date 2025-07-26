package com.example.api.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class ApiSteps {

    private final String baseUrl = System.getProperty("baseUrl", "http://localhost:4000");
    private String token;
    private Response response;
    private String lastId;
    private String bodyJson;

    @Given("I have a payload from {string}")
    public void i_have_a_payload_from(String resourcePath) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            assertNotNull("Resource not found: " + resourcePath, is);
            this.bodyJson = new String(is.readAllBytes());
        }
    }

    @When("I POST to {string}")
    public void i_post_to(String path) {
        response = given()
                .contentType(ContentType.JSON)
                .body(bodyJson == null ? "{}" : bodyJson)
                .post(baseUrl + path);
    }

    @When("I GET {string} without auth")
    public void i_get_without_auth(String path) {
        response = given().get(baseUrl + path);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer code) {
        assertEquals(code.intValue(), response.statusCode());
    }

    @And("save the token")
    public void save_the_token() throws Exception {
        Map<?,?> json = new ObjectMapper().readValue(response.asByteArray(), Map.class);
        token = (String) json.get("token");
        assertNotNull("Token should be present", token);
    }

    @Given("I have a valid token")
    public void i_have_a_valid_token() {
        if (token == null) {
            // fallback to known good token used by the API for demo
            token = "fake-token-123";
        }
    }

    @And("I remember the created id")
    public void i_remember_the_created_id() throws Exception {
        Map<?,?> json = new ObjectMapper().readValue(response.asByteArray(), Map.class);
        lastId = String.valueOf(json.get("id"));
        assertNotNull(lastId);
    }

    @And("the response should contain the text {string}")
    public void the_response_should_contain_text(String text) {
        assertTrue(response.asString().contains(text));
    }

    @When("I GET {string}")
    public void i_get(String path) {
        response = given()
                .header("Authorization", "Bearer " + token)
                .get(baseUrl + path);
    }

    @When("I POST to {string} with body {string}")
    public void i_post_with_body(String path, String json) {
        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(json)
                .post(baseUrl + path);
    }

    @When("I PUT {string} with body {string}")
    public void i_put_with_body(String path, String json) {
        path = path.replace("{id}", lastId == null ? "0" : lastId);
        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(json)
                .put(baseUrl + path);
    }

    @When("I DELETE {string}")
    public void i_delete(String path) {
        path = path.replace("{id}", lastId == null ? "0" : lastId);
        response = given()
                .header("Authorization", "Bearer " + token)
                .delete(baseUrl + path);
    }
}
