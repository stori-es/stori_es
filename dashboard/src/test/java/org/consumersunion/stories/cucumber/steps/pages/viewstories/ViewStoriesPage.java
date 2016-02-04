package org.consumersunion.stories.cucumber.steps.pages.viewstories;

import java.util.List;

import org.consumersunion.stories.common.client.util.WidgetIds;
import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.inject.Inject;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewStoriesPage {

    private final WebDriverManager webDriverManager;
    static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";

    @Inject
    public ViewStoriesPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @When("^I see the ViewStories page.$")
    public void whenISeeTheViewStoriesPage() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("i.icon-plus-sign"));
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//div[2]/div[2]/table/tbody/tr/td/div"));
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//body[@id='storiesBodyId']"));
    }

    @When("^I click the Plus Icon Bulk Options View Stories$")
    public void clickPlusIconBulkOptions() throws InterruptedException {
        //Search for the Plus icon
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("i[class='icon-plus-sign']"));
        webDriverManager.FindElement(By.cssSelector("i[class='icon-plus-sign']"));
        webDriverManager.Click(By.cssSelector("i[class='icon-plus-sign']"));
    }

    @When("^I click the sort Dropdown$")
    public void clickSortDropdown() {
        //Search for the Sort dropdown
        webDriverManager.waitUntilDomIsLoaded(By.id("stories-storiesview-sort-dropdown"));
        webDriverManager.FindElement(By.id("stories-storiesview-sort-dropdown")).click();
    }

    @Then("^the dropdown options are displayed$")
    public void theDropdownOptionsAreDisplayed() {
        By selector = getAddToElementSelector();
        webDriverManager.waitUntilDomIsLoaded(selector);
        assertEquals(3, webDriverManager.FindElements(selector).size());
    }

    @Then("^the Sort dropdown options are displayed$")
    public void theSortDropdownOptionsAreDisplayed() {
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[1]"));
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[2]"));
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[3]"));
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[4]"));
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[5]"));

        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[1]")).getText().equals("Most Recent"));
        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[2]")).getText().equals("Title"));
        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[3]")).getText().equals("Author"));
        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[4]")).getText().equals("City"));
        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[4]/div/div/div[5]")).getText().equals("State"));
    }

    @When("^I click the Add To Collection Option$")
    public void clickAddToCollectionOption() {
        List<WebElement> elementList = webDriverManager.FindElements(getAddToElementSelector());
        webDriverManager.Click(elementList.get(0));
    }

    @When("^I click the Add Tags Option$")
    public void clickAddTagsOption() {
        List<WebElement> elementList = webDriverManager.FindElements(getAddToElementSelector());
        webDriverManager.Click(elementList.get(1));
    }

    @When("^I click the Add Notes Option$")
    public void clickAddNotesOption() {
        List<WebElement> elementList = webDriverManager.FindElements(getAddToElementSelector());
        webDriverManager.Click(elementList.get(2));
    }

    @Then("^the Add Collection widget is displayed$")
    public void theAddCollectionWidgetDisplayed() {
        //Add Text displayed in the widget
        webDriverManager.waitUntilElementIsDisplayed(By.id(WidgetIds.ADD_STORIES_TO_COLLECTION));
        webDriverManager.waitUntilElementIsDisplayed(By.id(WidgetIds.ADD_STORIES_TO_COLLECTION_DROPDOWN));

        //Collection Text
        assertTrue(webDriverManager.isElementDisplayed(By.linkText("Collection(s)")));

        //Get the Go buttons
        List<WebElement> elementList = webDriverManager.FindElements(
                By.id(WidgetIds.ADD_STORIES_TO_COLLECTION_GO_BUTTON));
        Assert.assertFalse(elementList.get(0).isEnabled());
        assertTrue(elementList.get(0).isDisplayed());
    }

    @Then("^the Add Tags widget is displayed$")
    public void theAddTagsWidgetDisplayed() {
        webDriverManager.waitUntilElementIsDisplayed(By.id(WidgetIds.ADD_TAGS_TO_STORIES));
        webDriverManager.waitUntilElementIsDisplayed(By.id(WidgetIds.ADD_TAGS_TO_STORIES_DROPDOWN));

        //pages dropdown
        assertTrue(webDriverManager.FindElement(By.id(WidgetIds.ADD_TAGS_TO_STORIES_DROPDOWN)).isDisplayed());

        //Get the Go buttons
        WebElement button = webDriverManager.FindElement(By.id(WidgetIds.ADD_TAGS_TO_STORIES_GO_BUTTON));
        Assert.assertFalse(button.isEnabled());
        assertTrue(button.isDisplayed());
    }

    @Then("^the Add Notes widget is displayed$")
    public void theAddNotesWidgetDisplayed() {
        //main widget
        webDriverManager.waitUntilElementIsDisplayed(By.id(WidgetIds.ADD_NOTE_TO_STORIES));
        webDriverManager.waitUntilElementIsDisplayed(By.id(WidgetIds.ADD_NOTE_TO_STORIES_DROPDOWN));

        //pages dropdown
        assertTrue(webDriverManager.FindElement(By.id(WidgetIds.ADD_NOTE_TO_STORIES_DROPDOWN)).isDisplayed());

        //Get the Go buttons
        WebElement button = webDriverManager.FindElement(By.id(WidgetIds.ADD_NOTE_TO_STORIES_GO_BUTTON));
        Assert.assertFalse(button.isEnabled());
        assertTrue(button.isDisplayed());
    }

    /********************************************************************
     * Following section is to add the info related to new collections
     *********************************************************************/
    @When("^I click the Collections link$")
    public void clickCollectionLink() {
        webDriverManager.waitUntilDomIsLoaded(By.linkText("Collection(s)"));
        webDriverManager.Click(By.linkText("Collection(s)"));
        //Wait for the New Collection Button
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-Button"));
        //Wait for the Done Button
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-done"));
        //Wait for the Cancel link
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-cancel"));
    }

    @When("^the Add Collection Text is displayed Without Selection$")
    public void verifyTheAddCollectionText() {
        webDriverManager.waitUntilDomIsLoaded(By.linkText("Collection(s)"));
        assertTrue(webDriverManager.FindElement(By.linkText("Collection(s)")).getText().equals("Collection(s)"));
    }

    @Then("^the Add Collection Modal is displayed$")
    public void theAddCollectionModalisDisplayed() {
        //Verify if the Modal is displayed
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-PopupPanel"));
        assertTrue(webDriverManager.isElementDisplayed(By.cssSelector(".gwt-PopupPanel")));
        //Verify the text in elements / Modal
        assertTrue(webDriverManager.FindElement(
                By.xpath("//body[@id='storiesBodyId']/div[5]/div/div/div/div/div/div/span")).getText().equals(
                "Add to:"));
        assertTrue(
                webDriverManager.FindElement(By.cssSelector(".gwt-Button")).getText().equals("New Collection"));
        assertTrue(
                webDriverManager.FindElement(By.cssSelector("#collection-select-done")).getText().equals("Done"));
        assertTrue(
                webDriverManager.FindElement(By.cssSelector("#collection-select-cancel")).getText().equals("Cancel"));
    }

    @When("^I Select a Collection from the list with Index (\\d+)$")
    public void selectCollectionIndex(int index) {
        List<WebElement> wCollections = webDriverManager.FindElements(By.cssSelector(".gwt-ListBox>option"));
        webDriverManager.Click(wCollections.get(index));
    }

    @When("^I Click the Add to Collection Cancel Link$")
    public void clickAddCollectionCancelLink() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-cancel"));
        webDriverManager.Click(By.cssSelector("#collection-select-cancel"));
    }

    @When("^I Click the Add to Collection Done button$")
    public void clickAddCollectionDoneButton() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector("#collection-select-done"));
        webDriverManager.Click(By.cssSelector("#collection-select-done"));
    }

    @When("^I Click the Add to Collection New button$")
    public void clickAddCollectionNewButton() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".gwt-Button"));
        webDriverManager.Click(By.cssSelector(".gwt-Button"));
    }

    @When("^I Select a Story from the list with Index (\\d+) on the View Stories Page$")
    public StoryDetailsPage selectStoryFromList(int index) {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".title>a"));
        webDriverManager.waitUntilInstancesAreMoreThan(By.cssSelector(".title>a"), 0);

        webDriverManager.Click(webDriverManager.FindElements(By.cssSelector(".title>a")).get(index));

        return new StoryDetailsPage(webDriverManager);
    }

    private By getAddToElementSelector() {
        return By.xpath("//div[@id='" + WidgetIds.ADD_TO_BUTTON + "']//div[@class='gwt-Label']");
    }
}
