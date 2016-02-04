package org.consumersunion.stories.cucumber.steps.pages.viewstories;

import java.util.List;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.inject.Inject;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import static org.junit.Assert.assertFalse;

public class StoryDetailsPage {

    private final WebDriverManager webDriverManager;
    static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";

    @Inject
    public StoryDetailsPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @When("^I click the Add To Collection Plus Icon in Story Details Page$")
    public void clickCollectionPlusIcon() {
        webDriverManager.waitUntilDomIsLoaded(By.id("add-to-collection-control"));
        webDriverManager.Click(By.id("add-to-collection-control"));
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-PopupPanel"));
    }

    @Then("^the Add to Collection Modal is displayed in Story Details Page$")
    public void verifyAddToCollectionModal() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-PopupPanel"));
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("collection-select-done"));
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("collection-select-cancel"));
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-ListBox"));
        //TODO
        //Message shouldn't be displayed
        assertFalse(webDriverManager.FindElement(By.cssSelector("")).isDisplayed());
    }

    @When("^I click the New Collection Button in Story Details Page Add to Collection Modal$")
    public void clickNewCollection() throws Exception {
        WebElement wNewCollectionButton = webDriverManager.FindElements(By.cssSelector(".gwt-Button")).get(2);
        if (wNewCollectionButton.isEnabled()) {
            wNewCollectionButton.click();
        } else {
            throw new Exception("Click New Collection Button is not enable");
        }
    }

    @When("^I click the Cancel Button in Story Details Page Add to Collection Modal$")
    public void clickCancelButton() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("collection-select-cancel"));
    }

    @When("^I click the Done Button in Story Details Page Add to Collection Modal$")
    public void clickDoneButton() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("collection-select-done"));
    }

    @Then("^the Required Collection Name Message is displayed on the Story Details Page$")
    public void verifyRequiredCollectionNameMessage(String collectionName) {
        //TODO
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(""));
        webDriverManager.FindElement(By.cssSelector(""));
        Assert.assertTrue(webDriverManager.FindElement(By.cssSelector("")).isDisplayed());
    }

    @When("^I Select a Collection from the list in the Story Details Page$")
    public void clickCollectionFromList(int index) {
        //Wait for the elements on the list
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-ListBox>option"));

        //get the total number of collections in the listbox
        int totalCollection = webDriverManager.FindElements(By.cssSelector(".gwt-ListBox>option")).size();

        //select a specific collection from the list
        if (index <= totalCollection) {
            WebElement collectionToBeSelected = webDriverManager.FindElements(
                    By.cssSelector(".gwt-ListBox>option")).get(index);
            collectionToBeSelected.click();
        }
    }

    public List<WebElement> getAttachedCollections() {
        return webDriverManager.FindElements(By.cssSelector(".attachedCollections"));
    }

    public List<WebElement> getDeleteAttachedCollections() {
        return webDriverManager.FindElements(By.cssSelector(".deleteCollection"));
    }

    public List<WebElement> getStories() {
        //TODO
        return webDriverManager.FindElements(By.cssSelector(""));
    }

    @When("^I click the Attached Collection Story Details Page$")
    public void clickAttachedCollection(WebElement collectionLink) {
        webDriverManager.Click(collectionLink);
    }

    @When("^I click the Delete Collection Story Details Page$")
    public void clickDeleteCollectionIcon(WebElement deleteIcon, boolean deleteCollection) {
        webDriverManager.Click(deleteIcon);
        if (deleteCollection) {
            //TODO
        } else {
            //TODO
        }
    }

    @Then("^the Verify Delete Collection Message is displayed in Story Details Page$")
    public void verifyDeleteCollectionMessage(String collectionName) {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-Label"));
        webDriverManager.FindElement(By.cssSelector(".gwt-Label"));
    }

    @When("^I click the Confirmation Button in Story Details Page$")
    public void clickConfirmDeleteCollection() {
        //TODO need the ids
    }

    @When("^I click the Cancel Button Delete Collection in Story Details Page$")
    public void clickCancelDeleteCollectionModalWindow() {
        //TODO need the ids
    }

    @When("^I click the Story Link Story Details Page$")
    public void clickStoryLink(WebElement wElement) {
        webDriverManager.Click(wElement);
    }

    @When("I post a new Note '([^']*)' in the Story Details Page$")
    public void postNewNote(String newNoteText) {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-TextArea"));
        webDriverManager.sendKeys(By.cssSelector(".gwt-TextArea"), newNoteText);
        //TODO
        //Add the click action for the new post button
    }

    @When("^I click the Story Tab in the Story Details Page$")
    public void clickStoryTab() {
        //TODO
    }

    @When("^I click the QA Tab in the Story Details Page$")
    public void clickQATab() {
        //TODO
    }

    @When("^I click the Original Tab in the Story Details Page$")
    public void clickOriginalTab() {
        //TODO
    }

    @Then("^the Original Tab displayed in Story Details Page$")
    public void verifyOriginalTabDisplayed(boolean isDisplayed) {
        //TODO
        if (isDisplayed) {
            Assert.assertTrue(webDriverManager.isElementDisplayed(By.cssSelector("")));
        } else {
            //TODO
            //assertFalse(webDriverManager.isElementDisplayed(By.cssSelector("")));
        }
    }

    @When("^I click the Person Link in the Story Details Page$")
    public ProfileViewStoriesPage clickPersonLink() {
        //TODO
        return new ProfileViewStoriesPage(webDriverManager);
    }

    @Then("^verify the Original Tab content in the Story Details Page '([^']*)'$")
    public void verifyOriginalContent(String content) {
        webDriverManager.waitUntilDomIsLoaded(By.id(""));
        WebElement wContent = webDriverManager.FindElement(By.id(""));
        Assert.assertEquals(content, wContent.getText());
    }
    
    /*
    @Then("^verify the Original Tab content in the Story Details Page '([^']*)'$")
    public void editStoryTitle(String newTitle){
    	//TODO
    	webDriverManager.waitUntilDomIsLoaded(By.id(""));
    	WebElement storyTitle = webDriverManager.FindElement(By.id(""));
    	
    	//Click the story Title
    	storyTitle.click();
    	webDriverManager.waitUntilDomIsLoaded(By.id(".gwt-TextBox"));
    	webDriverManager.sendKeys(By.cssSelector(".gwt-TextBox"), newTitle);    	    
    }*/

    @When("^I click the Save Button in the Edit Story Details Page$")
    public void clickSaveButtonEditStory() {
        List<WebElement> wButtons = webDriverManager.FindElements(By.cssSelector(".gwt-Button"));
        //Click Save Button
        wButtons.get(1);
    }

    @When("^I click the Cancel Button in the Edit Story Details Page$")
    public void clickCancelButtonEditStory() {
        List<WebElement> wButtons = webDriverManager.FindElements(By.cssSelector(".gwt-Button"));
        //Click Cancel Button
        wButtons.get(0);
    }
}
