package com.example.ui.tests;

import com.example.ui.pages.LoginPage;
import com.example.ui.pages.TodosPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TodoCrudTests extends BaseUiTest {

    private void login() {
        LoginPage login = new LoginPage(driver);
        login.open(baseUrl);
        login.login("admin", "secret");
    }

    @Test
    void createEditDeleteFlow() {
        login();
        TodosPage todos = new TodosPage(driver);

        // Create
        String original = "Buy milk";
        todos.add(original);
        Assertions.assertTrue(todos.getItems().contains(original));

        // Edit
        todos.clickEditFor(original);
        String updated = "Buy oat milk";
        todos.saveEdited(updated);
        Assertions.assertTrue(todos.getItems().contains(updated));
        Assertions.assertFalse(todos.getItems().contains(original));

        // Delete
        todos.delete(updated);
        Assertions.assertFalse(todos.getItems().contains(updated));
    }
}
