package org.consumersunion.stories.cucumber.steps.convio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.rest.api.convio.PollConvioOnly;
import org.consumersunion.stories.server.util.ApplicationContextProvider;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Strings;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import static org.junit.Assert.assertEquals;

public class CreateNewUserSteps {
    private WebDriverManager webDriverManager;

    private String userEmail = null;

    private String getUserEmail() {
        if (userEmail == null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            final Calendar cal = Calendar.getInstance();
            userEmail = "test-" + dateFormat.format(cal.getTime()) + "@test.org";
        }
        return userEmail;
    }

    @Given("^I access the form at ([\\w\\/-]+)$")
    public void loadApplication(String surveySlug) {
        final String url = WebDriverManager.getTestUrlBase() + surveySlug;
        webDriverManager.getWebDriver().get(url);
    }

    @When("^I fill out the form with a new email address$")
    public void whenISeeApplication() {
        webDriverManager.getWebDriver().findElement(
                By.xpath("(//*[@class='dataTable']//table[contains(@class,'form-inputBox')])[6]//input")).sendKeys(
                getUserEmail());
    }

    @When("^I tell a story$")
    public void whenITellAStory() {
        webDriverManager.getWebDriver().findElement(By.xpath("(//*[@class='dataTable']//tr)[2]//input")).sendKeys(
                "a title");
        // tried many ways to fill out inline, but switching back to main window was tough
        webDriverManager.getWebDriver().findElement(
                By.xpath("(//*[@class='dataTable']//table[contains(@class,'form-inputBox')])[4]//input")).sendKeys(
                "Cucumber");
        webDriverManager.getWebDriver().findElement(
                By.xpath("(//*[@class='dataTable']//table[contains(@class,'form-inputBox')])[5]//input")).sendKeys(
                "Test");
        webDriverManager.getWebDriver().findElement(
                By.xpath("(//*[@class='dataTable']//table[contains(@class,'form-inputBox')])[7]//input")).sendKeys(
                "100 Main");
        webDriverManager.getWebDriver().findElement(
                By.xpath("(//*[@class='dataTable']//table[contains(@class,'form-inputBox')])[8]//input")).sendKeys(
                "Cucumber");
        webDriverManager.getWebDriver().findElement(
                By.xpath("(//*[@class='dataTable']//table[contains(@class,'form-inputBox')])[9]//select")).sendKeys(
                "T");
        webDriverManager.getWebDriver().findElement(
                By.xpath("(//*[@class='dataTable']//table[contains(@class,'form-inputBox')])[10]//input")).sendKeys(
                "78755");
        webDriverManager.getWebDriver().findElement(
                By.xpath("(//*[@class='dataTable']//table[contains(@class,'form-inputBox')])[11]//input")).sendKeys(
                "512-555-5555");

        final String whandle = webDriverManager.getWebDriver().getWindowHandle();
        WebElement iframe = webDriverManager.getWebDriver().findElement(
                By.xpath("(//*[@class='dataTable']//tr)//iframe"));
        webDriverManager.getWebDriver().switchTo().frame(iframe);
        webDriverManager.getWebDriver().findElement(By.xpath("//body")).sendKeys("a story");
        webDriverManager.getWebDriver().switchTo().window(whandle);
    }

    @When("^I submit the form$")
    public void whenISubmitTheForm() {
        // In the first button is the 'Return to Questionnaire' button; the second button (1-indexed) is the 'Submit'
        // button.
        webDriverManager.getWebDriver().findElement(By.xpath("(//button[contains(@class, 'gwt-Button')])[2]")).click();
    }

    @When("^I see the submission finishes processing$")
    public void whenISeeTheSubmissionFinishesProcessing() {
        new WebDriverWait(webDriverManager.getWebDriver(), 5).until(
                ExpectedConditions.presenceOfElementLocated(By.className("cu-collection-submit-thank-you")));
    }

    @Then("^I see the Convio constituent.$")
    public void thenISeeTheNewConvioConstituent() throws SQLException {
        PollConvioOnly constituentPoll = null;
        for (int i = 0; i < 15; i += 1) {
            final Connection conn = PersistenceUtil.getConnection();
            try {
                final PreparedStatement select =
                        conn.prepareStatement("SELECT p.id, acl.sid, c.* " +
                                "FROM profile p " +
                                "JOIN contact c ON p.id=c.entityId " +
                                "JOIN acl_entry acl ON acl.acl_object_identity=p.id " +
                                "JOIN organization o ON acl.sid=o.id " +
                                "WHERE acl.mask=1 AND c.medium='EMAIL' " +
                                "ORDER BY p.id DESC LIMIT 1;");
                final ResultSet results = select.executeQuery();

                Assert.assertTrue("Could not select newly created user.", results.next());
                final int personId = results.getInt(1);
                final int orgId = results.getInt(2);

                constituentPoll = new PollConvioOnly(getUserEmail(), orgId);
                ApplicationContextProvider.getConvioSyncWorker().process(constituentPoll);
                if (constituentPoll.getCommunicationException() == null &&
                        Strings.isNullOrEmpty(constituentPoll.getConvioErrorCode()) &&
                        Strings.isNullOrEmpty(constituentPoll.getErrorMessage()) &&
                        constituentPoll.getConvioConstituent() != null &&
                        getUserEmail().equals(constituentPoll.getConvioConstituent().getEmail().getPrimary_address())) {
                    break;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                } // nothing we can do
            }
        }
        Assert.assertNull("Unexpected communication exception while verifying with Convio.",
                constituentPoll.getCommunicationException());
        Assert.assertTrue("Unexpected error code: " + constituentPoll.getConvioErrorCode(),
                Strings.isNullOrEmpty(constituentPoll.getConvioErrorCode()));
        Assert.assertTrue("Unexpected error message: " + constituentPoll.getConvioErrorCode(),
                Strings.isNullOrEmpty(constituentPoll.getErrorMessage()));
        Assert.assertNotNull("Did not find constituent.", constituentPoll.getConvioConstituent());
        Assert.assertNotNull("Did not find constituent.", constituentPoll.getConvioConstituent());
        assertEquals("Constituent had unexpected primary email address.",
                getUserEmail(), constituentPoll.getConvioConstituent().getEmail().getPrimary_address());
    }
}
