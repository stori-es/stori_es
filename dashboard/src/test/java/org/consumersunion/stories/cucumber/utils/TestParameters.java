package org.consumersunion.stories.cucumber.utils;

public class TestParameters {
    public static final int TIME_OUT_IN_SECONDS = 20;
    public static final String signInUrl = WebDriverManager.getTestUrlBase() + "/signin.jsp";
    public static final String signOutUrl = WebDriverManager.getTestUrlBase() + "/j_spring_security_logout";
    public static final String storiesInUrl = WebDriverManager.getTestUrlBase() + "/stories.jsp";
    public static final long IMPLICIT_WAIT = 2000l;
}
