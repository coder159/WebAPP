package com.example.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class TodosPage {
    private final WebDriver driver;
    private final By newItem = By.id("newItem");
    private final By addBtn = By.id("addBtn");
    private final By list = By.id("todoList");
    private final By editText = By.id("editText");
    private final By saveBtn = By.id("saveBtn");

    public TodosPage(WebDriver driver) { this.driver = driver; }

    public void add(String text) {
        driver.findElement(newItem).clear();
        driver.findElement(newItem).sendKeys(text);
        driver.findElement(addBtn).click();
    }

    public List<String> getItems() {
        List<WebElement> items = driver.findElement(list).findElements(By.cssSelector("li .todo-text"));
        return items.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void clickEditFor(String text) {
        List<WebElement> lis = driver.findElement(list).findElements(By.cssSelector("li"));
        for (WebElement li : lis) {
            WebElement span = li.findElement(By.cssSelector(".todo-text"));
            if (span.getText().equals(text)) {
                li.findElement(By.cssSelector(".editBtn")).click();
                return;
            }
        }
        throw new RuntimeException("Item not found to edit: " + text);
    }

    public void saveEdited(String newText) {
        driver.findElement(editText).clear();
        driver.findElement(editText).sendKeys(newText);
        driver.findElement(saveBtn).click();
    }

    public void delete(String text) {
        List<WebElement> lis = driver.findElement(list).findElements(By.cssSelector("li"));
        for (WebElement li : lis) {
            WebElement span = li.findElement(By.cssSelector(".todo-text"));
            if (span.getText().equals(text)) {
                li.findElement(By.cssSelector(".deleteBtn")).click();
                return;
            }
        }
        throw new RuntimeException("Item not found to delete: " + text);
    }
}
