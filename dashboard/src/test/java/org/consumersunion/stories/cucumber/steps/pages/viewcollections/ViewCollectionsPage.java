package org.consumersunion.stories.cucumber.steps.pages.viewcollections;

import org.consumersunion.stories.cucumber.steps.pages.viewstories.StoryDetailsPage;
import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.google.inject.Inject;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import static org.junit.Assert.assertTrue;

public class ViewCollectionsPage {

    private final WebDriverManager webDriverManager;
    static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";

    @Inject
    public ViewCollectionsPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @When("^I click the New Collection Button$")
    public void clickNewCollectionButton() {
        //Wait for the Collection button
        webDriverManager.waitUntilDomIsLoaded(By.xpath(
                ".//*[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div/div" +
                        "[1]/div[1]/div[1]/button[1]"));
        webDriverManager.Click(By.xpath(
                ".//*[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div/div" +
                        "[1]/div[1]/div[1]/button[1]"));
    }

    @Then("^the Modal Window New Collection is displayed$")
    public void theModalWindowNewCollectionisDisplayed() {
        //New Collection Title Element
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CCDB"));
        //Textbox Element
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-TextBox"));
        //Cancel Button
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-cancel"));
        //Done Button
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-done"));

        //Verify Displayed Text in the Page
        assertTrue(
                webDriverManager.FindElement(By.cssSelector(".GNRSWN1CCDB")).getText().equals("New Collection"));
        assertTrue(
                webDriverManager.FindElement(By.cssSelector("#collection-select-cancel")).getText().equals("Cancel"));
        assertTrue(
                webDriverManager.FindElement(By.cssSelector("#collection-select-done")).getText().equals("Done"));
    }

    @When("^I click the New Questionnaire Button$")
    public void clickNewQuestionnaireButton() {
        //Wait for the Questionnaire button
        webDriverManager.waitUntilDomIsLoaded(By.xpath(
                ".//*[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div/div" +
                        "[1]/div[1]/div[1]/button[2]"));
        webDriverManager.Click(By.xpath(
                ".//*[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div/div" +
                        "[1]/div[1]/div[1]/button[2]"));
    }

    @Then("^the Modal Window New Questionnaire is displayed$")
    public void theModalWindowNewQuestionnaireisDisplayed() {
        //New Questionnaire Title Element
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#new-collection .stories-modal-title"));
        //Textbox Element
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-TextBox"));
        //Cancel Button
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-cancel"));
        //Done Button
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-done"));

        //Verify Displayed Text in the Page
        assertTrue(webDriverManager.FindElement(
                By.cssSelector("#new-collection-popup .stories-modal-title")).getText().equals("New Questionnaire"));
        assertTrue(
                webDriverManager.FindElement(By.cssSelector("#collection-select-cancel")).getText().equals("Cancel"));
        assertTrue(
                webDriverManager.FindElement(By.cssSelector("#collection-select-done")).getText().equals("Done"));
    }

    @When("^I click the X icon on the View Collection Search$")
    public void clickCancelIconViewcollectionSearch() {
        //Wait for the Cancel icon on the search
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CJM"));
        webDriverManager.Click(By.cssSelector(".GNRSWN1CJM"));
    }

    @When("^I click the Sort Dropdown on View Collection Search$")
    public void clickSortOption() {
        //Wait for the Cancel icon on the search
        WebElement wElement = webDriverManager.FindElements(By.cssSelector(".GNRSWN1CCN")).get(1);
        webDriverManager.Click(wElement);
    }

    @Then("^The options in the dropdown are being displayed$")
    public void verifySortOptions() {
        //Wait for the First option in the Dropdown
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[2]"));

        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[2]")).getText().equals("Title (Z to A)"));
        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[3]")).getText().equals("Created (Oldest)"));
        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[4]")).getText().equals("Created (Newest)"));
        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[5]")).getText().equals("Modified (Oldest)"));
        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[6]")).getText().equals("Modified (Newest)"));
    }

    @When("^I Select a Questionnaire from the list with Index (\\d+) on the View Collections Page$")
    public StoryDetailsPage selectQuestionnaireFromList(int index) {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.waitUntilInstancesAreMoreThan(By.cssSelector(""), 0);

        webDriverManager.Click(webDriverManager.FindElements(By.cssSelector("")).get(index));

        return new StoryDetailsPage(webDriverManager);
    }

    @When("^I Select a Collection from the list with Index (\\d+) on the View Collections Page$")
    public CollectionPage selectCollectionFromList(int index) {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.waitUntilInstancesAreMoreThan(By.cssSelector(""), 0);

        webDriverManager.Click(webDriverManager.FindElements(By.cssSelector("")).get(index));

        return new CollectionPage(webDriverManager);
    }

    @When("^I execute a View Collection Search using the following text '([^']*)'$")
    public void executeViewCollectionSearch(String searchText) {
        //Replace with the correct id
        webDriverManager.waitUntilDomIsLoaded(By.xpath(
                ".//*[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div/div/div" +
                        "[1]/div[2]/div/div[2]/div[1]/input"));
        webDriverManager.sendKeys(By.xpath(
                        ".//*[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div" +
                                "/div/div" +
                                "[1]/div[2]/div/div[2]/div[1]/input"),
                searchText);

        webDriverManager.sendKeys(By.xpath(
                        ".//*[@id='storiesBodyId']/div[3]/div[2]/div/div[3]/div/div[2]/div/div[3]/div/div[2]/div/div" +
                                "/div/div" +
                                "[1]/div[2]/div/div[2]/div[1]/input"),
                Keys.ENTER);
    }

    @When("^I click the Done button in the New Questionnaire Modal$")
    public void clickDoneButtonQuestionnaireModal() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-done"));
        webDriverManager.Click(By.cssSelector("#collection-select-done"));
    }

    @When("^I click the Cancel button in the New Questionnaire Modal$")
    public void clickCancelButtonQuestionnaireModal() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-cancel"));
        webDriverManager.Click(By.cssSelector("#collection-select-cancel"));
    }

    @When("^I insert a new Questionnaire Name in the Modal Window '([^']*)'$")
    public void insertNewQuestionnaireNameModalWindow() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-TextBox"));
        webDriverManager.sendKeys(By.cssSelector(".gwt-TextBox"), "");
    }
}
