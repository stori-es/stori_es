package org.consumersunion.stories.cucumber.steps.common;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.google.inject.Inject;

import cucumber.annotation.en.When;

public class Inputs {
    private WebDriverManager webDriverManager;

    @Inject
    public Inputs(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @When("I type '([^']*)' into field '([^']*)'")
    public void whenITypeIntoField(String value, String fieldName) {
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//input[@name='" + fieldName + "']"));
        webDriverManager.sendKeys(By.xpath("//input[@name='" + fieldName + "']"), value);
    }

    @When("I type '([^']*)' into field by Locator '([^']*)'")
    public void whenITypeIntoField(String value, By locator) {
        webDriverManager.waitUntilDomIsLoaded(locator);
        webDriverManager.sendKeys(locator, value);
    }

    @When("I click the 'Sign out' link")
    public void iClickTheSignOutLink() {
        webDriverManager.Click(By.xpath("//a[@href='j_spring_security_logout']"));
    }

    @When("I hit the submit button")
    public void whenIHitTheSubmitButton() {
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//button[@name='submitButton']"));
        webDriverManager.Click(By.xpath("//button[@name='submitButton']"));
    }

    @When("I press the enter key on '([^']*)'")
    public void whenIPressTheEnterKey(By locator) {
        webDriverManager.sendKeys(locator, Keys.ENTER);
    }
}
