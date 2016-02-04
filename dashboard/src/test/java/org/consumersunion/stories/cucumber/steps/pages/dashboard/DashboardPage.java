package org.consumersunion.stories.cucumber.steps.pages.dashboard;

import org.consumersunion.stories.cucumber.steps.pages.login.LoginPage;
import org.consumersunion.stories.cucumber.steps.pages.viewcollections.ViewCollectionsPage;
import org.consumersunion.stories.cucumber.steps.pages.viewstories.ViewStoriesPage;
import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.openqa.selenium.By;

import com.google.inject.Inject;

import cucumber.annotation.en.When;

public class DashboardPage {

    private final WebDriverManager webDriverManager;

    @Inject
    public DashboardPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    @When("^I click the Sign Out link$")
    public LoginPage clickSignOutLink() {
        //Search for the Sign Out Link
        webDriverManager.waitUntilDomIsLoaded(By.linkText("Sign out"));
        webDriverManager.Click(By.linkText("Sign out"));
        return new LoginPage(this.webDriverManager);
    }

    @When("^I click the ViewStories Tab$")
    public ViewStoriesPage clickViewStoriesTab() {
        //Search for Tabs in the app, 3 tabs
        webDriverManager.waitUntilDomIsLoaded(By.id("stories-header-view-stories"));
        webDriverManager.Click(webDriverManager.FindElements(By.id("stories-header-view-stories")).get(0));
        return new ViewStoriesPage(this.webDriverManager);
    }

    @When("^I click the ViewCollections Tab$")
    public ViewCollectionsPage clickViewCollectionsTab() {
        //Search for Tabs in the app, 3 tabs
        webDriverManager.waitUntilDomIsLoaded(By.xpath("//div[2]/table/tbody/tr/td/div"));
        webDriverManager.Click(webDriverManager.FindElement(By.xpath("//div[2]/table/tbody/tr/td/div")));
        return new ViewCollectionsPage(this.webDriverManager);
    }
}
