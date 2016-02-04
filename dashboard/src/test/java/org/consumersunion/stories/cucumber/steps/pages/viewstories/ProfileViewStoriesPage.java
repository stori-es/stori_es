package org.consumersunion.stories.cucumber.steps.pages.viewstories;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;

import com.google.inject.Inject;

public class ProfileViewStoriesPage {

    private final WebDriverManager webDriverManager;
    static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";

    @Inject
    public ProfileViewStoriesPage(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }
}
