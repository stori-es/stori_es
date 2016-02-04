package org.consumersunion.stories.cucumber.steps.pages.viewcollections;

import java.util.List;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.google.inject.Inject;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import static org.junit.Assert.assertTrue;

public class QuestionnaireDetailsPage {
    private final WebDriverManager webDriverManager;
    static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";

    @Inject
    public QuestionnaireDetailsPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @Then("^the Questionnaire Page is displayed$")
    public void theCollectionPageisDisplayed() {
        //TODO
        //Need to add the ids
    }

    @When("^I click the Edit Questionnaire Button$")
    public void clickEditButton() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @When("^I click the Map Icon in the Questionnaire Details Page$")
    public void clickListIcon() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @When("^I click the List Icon in the Questionnaire Details Page$")
    public void clickMapIcon() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @When("^I click the QuestionContent Tab$")
    public void clickQuestionContentTab() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @When("^I click the OnSubmit Tab$")
    public void clickOnSubmitTab() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @When("^I click the Confirmation Tab$")
    public void clickConfirmationTab() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @When("^I click the Publication Tab$")
    public void clickPublicationTab() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @When("^I click the Edit Permalink Icon$")
    public void clickEditPermalinkIcon() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".icon-2x.icon-pencil"));
        webDriverManager.Click(By.cssSelector(".icon-2x.icon-pencil"));

        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("div[class$='icon-ok']"));
    }

    @When("^I click the Confirm Edit Permalink Icon$")
    public void clickConfirmEditPermalinkIcon() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("div[class$='icon-ok']"));
        webDriverManager.Click(By.cssSelector("div[class$='icon-ok']"));
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".icon-2x.icon-pencil"));

        //TODO
        //Verify the URL after the edit
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals(""));
    }

    @When("^I insert a new Permalink '([^']*)' value$")
    public void typeNewPermalinkValue(String newPermalink) {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("div[class$='icon-ok']"));
        webDriverManager.sendKeys(By.cssSelector("div[class$='icon-ok']"), newPermalink);
    }

    @When("^I click the Preview Button on the Publication Tab$")
    public void clickPreviewButtonOnPublicationTab(String newPermalink) {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @Then("^the Questionnaire Preview is displayed$")
    public void theConsoleDashbeardLoads() {
        //Switch to the New Window
        webDriverManager.switchToNewWindow();

        //wait for the submit button
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-Button.cu-clickable.cu-text-align-center"));

        //verify the url
        String URL = webDriverManager.getUrlWindow();

        assertTrue(URL.equals(""));
    }

    @When("^I execute a Story Search in the Questionnaire Page with Text '([^']*)'$")
    public void executeStoriesSearchInQuestionnaire(String searchText) {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));

        webDriverManager.sendKeys(By.cssSelector(""), "");

        webDriverManager.sendKeys(By.cssSelector(""), Keys.ENTER);
    }

    @When("^I cancel the Story Search in the Questionnaire Page$")
    public void cancelStoriesSearchInQuestionnaire(String searchText) {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @Then("^the Map icon is still displayed on the Page$")
    public void verifyMapIcon() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Map"));
    }

    @Then("^the List icon is still displayed on the Page$")
    public void verifyListIcon() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("List"));
    }

    @When("^I click the CustomQuestions Question Option in the Builder$")
    public void clickCustomQuestions() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @When("^I click the Standard Question Option in the Builder$")
    public void clickStandardQuestions() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.Click(By.cssSelector(""));
    }

    @Then("^the List of Custom questions are being displayed on the Page$")
    public void verifyCustomQuestions() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Text"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Paragraph Text"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Rich Text"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Radio"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Multiple Choice"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Checkboxes"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Date"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Attachments"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("----------"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Content"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Document"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Image"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Audio"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Video"));
    }

    @Then("^the List of Standard questions are being displayed on the Page$")
    public void verifyStandardQuestions() {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Story Title"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Story Ask | Plain Text"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Story Ask | Rich Text"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Email"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Phone"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("First Name"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Last Name"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Street Address"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("City"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("State"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Zip Code"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Updates Opt-in"));
        assertTrue(webDriverManager.FindElement(By.cssSelector("")).getText().equals("Preferred Email Format"));
    }

    public void clickPencilIconQuestion(int index) {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        List<WebElement> listEditIcon = webDriverManager.FindElements(By.cssSelector(""));
    }
}
