package com.example.api;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        // point to the active feature file relocated under ../../tests
        features = "../../../tests/api/src/test/resources/features",
        glue = "com.example.api.steps",
        plugin = {"pretty", "json:target/cucumber-report.json"},
        publish = false
)
public class ApiTestRunner { }
