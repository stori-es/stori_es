package org.consumersunion.stories.cucumber.steps.pages.accounts;

import java.util.List;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.inject.Inject;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import static org.junit.Assert.assertTrue;

public class AccountPage {

    private final WebDriverManager webDriverManager;
    static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";

    @Inject
    public AccountPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @Then("^the Account Page loads$")
    public void theAccountPageLoads() {
        webDriverManager.waitUntilInstancesAreEqualsTo(By.cssSelector(".gwt-PopupPanel"), 0);

        //Get the Title displayed at Top
        WebElement wElement = webDriverManager.FindElements(By.cssSelector(".GNRSWN1CFM.GNRSWN1CAT")).get(0);
        //Verify Displayed Text in the Page
        assertTrue(wElement.getText().equals("Update password"));
        assertTrue(webDriverManager.FindElement(By.cssSelector(".GNRSWN1CKS")).getText().equals("Languages"));
        assertTrue(webDriverManager.FindElement(By.cssSelector(".GNRSWN1CL-")).getText().equals("Notes"));
        assertTrue(webDriverManager.FindElement(By.cssSelector(".GNRSWN1CIS")).getText().equals("Stories"));

        List<WebElement> allElements = webDriverManager.FindElements(By.cssSelector(".GNRSWN1CN-.GNRSWN1CFN"));

        assertTrue(allElements.get(0).getText().equals("Email"));
        assertTrue(allElements.get(1).getText().equals("Phone Numbers"));
        assertTrue(allElements.get(2).getText().equals("Addresses"));
        assertTrue(allElements.get(3).getText().equals("Social"));
    }

    @When("^I click the Password Tab$")
    public void clickPasswordTab() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CDQ"));
        webDriverManager.Click(By.cssSelector(".GNRSWN1CDQ"));
    }

    @When("^I click the Update Password Button$")
    public void clickUpdatePaswordButton() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CJJ.GNRSWN1CMJ"));
        webDriverManager.Click(webDriverManager.FindElements(By.cssSelector(".GNRSWN1CJJ.GNRSWN1CMJ")).get(0));
    }

    @Then("^The Password required message is displayed$")
    public void verifyRequirePassworddMessage() {
        assertTrue(
                webDriverManager.FindElement(By.xpath(".//*[@id='messages']/table/tbody/tr/td/div")).getText().equals(
                        "Please fill all the required fields."));
    }

    @Then("^Current Invalid Password message is displayed$")
    public void verifyInvalidCurrentPasswordMessage() {
        assertTrue(
                webDriverManager.FindElement(By.xpath(".//*[@id='messages']/table/tbody/tr/td/div")).getText().equals(
                        "Invalid Current Password"));
    }

    @Then("^Confirmation password does not match message is displayed$")
    public void verifyConfirmationPasswordMessage() {
        assertTrue(
                webDriverManager.FindElement(By.xpath(".//*[@id='messages']/table/tbody/tr/td/div")).getText().equals(
                        "Confirmation Password does not match"));
    }

    @When("^I click the Localization Tab$")
    public void clickLocalizationTab() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CFQ"));
        webDriverManager.Click(By.cssSelector(".GNRSWN1CFQ"));
    }

    @When("^I click the Update Localization Button$")
    public void clickUpdateLocalizationButton() {
        webDriverManager.waitUntilInstancesAreEqualsTo(By.cssSelector(".GNRSWN1CJJ.GNRSWN1CMJ"), 2);
        WebElement wElement = webDriverManager.FindElements(By.cssSelector(".GNRSWN1CJJ.GNRSWN1CMJ")).get(1);
        webDriverManager.Click(wElement);
    }

    @When("^I check the language option$")
    public void checkLocalizationOption() throws Exception {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CFQ"));
        webDriverManager.check(By.cssSelector(".GNRSWN1CFQ"));
    }

    @When("^I uncheck the language option$")
    public void unchekLocalizationOption() throws Exception {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CFQ"));
        webDriverManager.uncheck(By.cssSelector(".GNRSWN1CFQ"));
    }

    @When("^I click the Stories Icon$")
    public void clickStoriesIcon() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CFT.icon-2x.icon-comment"));
        webDriverManager.Click(By.cssSelector(".GNRSWN1CFT.icon-2x.icon-comment"));
    }

    @When("^I click the Post Button Accounts$")
    public void clickPostButtonAccounts() {
        webDriverManager.waitUntilDomIsLoaded(By.cssSelector(".GNRSWN1CJJ.GNRSWN1CGN"));
        webDriverManager.Click(By.cssSelector(".GNRSWN1CJJ.GNRSWN1CGN"));
    }
}
