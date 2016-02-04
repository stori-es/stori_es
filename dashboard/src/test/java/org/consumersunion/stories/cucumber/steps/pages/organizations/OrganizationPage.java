package org.consumersunion.stories.cucumber.steps.pages.organizations;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.openqa.selenium.By;

import com.google.inject.Inject;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import static org.junit.Assert.assertTrue;

public class OrganizationPage {
    private final WebDriverManager webDriverManager;

    static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";

    @Inject
    public OrganizationPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @Then("^the Organization Page loads$")
    public void theOrganizationPageLoads() {
        webDriverManager.waitUntilInstancesAreEqualsTo(By.cssSelector(".gwt-PopupPanel"), 0);
        //Verify Displayed Text in the Page
        assertTrue(
                webDriverManager.FindElement(By.cssSelector(".GNRSWN1CE2.GNRSWN1CFN")).getText().equals("Attachments"));
        assertTrue(webDriverManager.FindElement(By.cssSelector(".GNRSWN1CL-")).getText().equals("Notes"));
        //Compare the Text in the dropdown against the text in the title
        assertTrue(webDriverManager.FindElement(By.cssSelector(".GNRSWN1CNN.GNRSWN1CBR")).getText().equals(
                webDriverManager.FindElement(By.cssSelector(".GNRSWN1CPM.GNRSWN1CAM")).getText()));
    }

    @When("^I click the Info Tab$")
    public void clickInfoTab() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CDQ"));
        webDriverManager.Click(By.cssSelector(".GNRSWN1CDQ"));
    }

    @When("^I click the Permissions Tab$")
    public void clickPermissionsTab() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CFQ"));
        webDriverManager.Click(By.cssSelector(".GNRSWN1CFQ"));
    }

    @When("^I click the Plus Icon Attachments$")
    public void clickPlusIconAttachments() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CFQ"));
        webDriverManager.Click(By.cssSelector(".GNRSWN1CFQ"));
    }

    @When("^I click the Post Button Organization$")
    public void clickPostButton() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CJJ.GNRSWN1CGN"));
        webDriverManager.Click(By.cssSelector(".GNRSWN1CJJ.GNRSWN1CGN"));
    }
}
