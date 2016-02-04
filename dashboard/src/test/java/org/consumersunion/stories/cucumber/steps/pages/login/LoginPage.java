package org.consumersunion.stories.cucumber.steps.pages.login;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;

import com.google.inject.Inject;

public class LoginPage {

    private final WebDriverManager webDriverManager;
    static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";

    @Inject
    public LoginPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }
}
