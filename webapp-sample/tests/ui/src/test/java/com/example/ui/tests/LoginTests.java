package com.example.ui.tests;

import com.example.ui.pages.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTests extends BaseUiTest {

    @Test
    void invalidLoginShowsError() {
        LoginPage login = new LoginPage(driver);
        login.open(baseUrl);
        login.login("wrong", "creds");
        String err = login.getError();
        Assertions.assertNotNull(err);
        Assertions.assertTrue(err.toLowerCase().contains("invalid"));
    }

    @Test
    void validLoginNavigatesToTodos() {
        LoginPage login = new LoginPage(driver);
        login.open(baseUrl);
        login.login("admin", "secret");
        // After login, Todos view renders input with id=newItem
        boolean atTodos = driver.getPageSource().contains("Add todo");
        Assertions.assertTrue(atTodos, "Should see the Todos view after successful login");
    }
}
