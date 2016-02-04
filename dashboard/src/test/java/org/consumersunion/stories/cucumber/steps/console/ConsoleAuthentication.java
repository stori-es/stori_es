package org.consumersunion.stories.cucumber.steps.console;

import org.consumersunion.stories.cucumber.utils.TestParameters;
import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;

import com.google.inject.Inject;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ConsoleAuthentication {
    private WebDriverManager webDriverManager;

    @Inject
    public ConsoleAuthentication(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @When("^I load the sign in page$")
    public void loadApplication() {
        webDriverManager.getWebDriver().get(TestParameters.signInUrl);
    }

    @When("^I see the sign in page.?$")
    public void whenISeeTheSignInPage() {
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//form[@name='login_form']"));
    }

    @Then("^the console dashboard loads.?$")
    public void theConsoleDashbeardLoads() {
        try {
            webDriverManager.Pause(2000);
        } catch (InterruptedException e) {
        }
        // There was a case where we were running into 'already logged in', which is announced via an alert
        try {
            final Alert alert = webDriverManager.getWebDriver().switchTo().alert();
            alert.accept();
            // fail("Got alert: " + alert.getText());
        } catch (NoAlertPresentException Ex) {
        } // good
        boolean wasLoaded = webDriverManager.waitUntilDomIsLoaded(By.xpath("//body[@id='storiesBodyId']"));
        // In some cases, the user could get bounced back to the lolgin page, so we give it a few seconds.
        try {
            webDriverManager.Pause(2000);
        } catch (InterruptedException e) {
        }
        try {
            webDriverManager.FindElement(By.xpath("//body[@id='storiesBodyId']"));
        } catch (NoSuchElementException e) {
            fail("Looks like we logged in, but then got bounced back to the login page.");
        }
        Assert.assertNotNull(wasLoaded);
        Assert.assertTrue(wasLoaded);
    }

    @Then("^I see the error message '([^']*)'.$")
    public void iSeeErrorMessage(String message) {
        assertEquals(message,
                webDriverManager.getWebDriver().findElement(By.xpath("//div[@class='fieldErrorMessage']")).getText());
    }
}
