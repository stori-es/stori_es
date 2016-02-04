package org.consumersunion.stories.cucumber.steps.pages.viewcollections;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.openqa.selenium.By;

import com.google.inject.Inject;

import cucumber.annotation.en.Then;

import static org.junit.Assert.assertTrue;

public class CollectionPage {

    private final WebDriverManager webDriverManager;
    static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";

    @Inject
    public CollectionPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @Then("^the Collection Page is displayed$")
    public void theCollectionPageisDisplayed() {
        //Title element
        webDriverManager.waitUntilDomIsLoaded(By.xpath(
                "//body[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div" +
                        "/div/div[2]/div/div/div/div/div"));
        //Timestamp icon
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".dateTimeWrapper"));
        //Plus icon
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".icon-plus-sign"));
        //Element with the Text Showing 0 stories
        webDriverManager.waitUntilDomIsLoaded(By.xpath(
                "//body[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div" +
                        "/div/div[2]/div/div/div[2]/div[3]/div"));
        //Map Icon element
        webDriverManager.waitUntilDomIsLoaded(By.xpath(
                "//body[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div" +
                        "/div/div[2]/div/div/div[2]/div[3]/div[2]"));
        //Plus Icon source of Questionnaire
        webDriverManager.waitUntilDomIsLoaded(By.xpath(
                ".//*[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div/div" +
                        "[1]/div[3]/div[4]/div[1]/div[3]/div/div"));
        //Tags Element
        webDriverManager.waitUntilDomIsLoaded(By.xpath(
                "//body[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div" +
                        "/div/div[3]/div[5]/div"));
        //Notes Element
        webDriverManager.waitUntilDomIsLoaded(By.xpath(
                "//body[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div" +
                        "/div/div[3]/div[7]/div/div"));

        //Verify Displayed Text in the Page
        assertTrue(webDriverManager.FindElement(By.cssSelector(
                "//body[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div" +
                        "/div/div[2]/div/div/div[2]/div[3]/div")).getText().equals(
                "Showing 0 Stories"));
        assertTrue(webDriverManager.FindElement(By.cssSelector(
                "//body[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div" +
                        "/div/div[3]/div[5]/div")).getText().equals(
                "Tags"));
        assertTrue(webDriverManager.FindElement(By.cssSelector(
                "//body[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div" +
                        "/div/div[3]/div[7]/div/div")).getText().equals(
                "Notes"));
    }
}
