package org.consumersunion.stories.login.application.i18n;

import com.google.gwt.i18n.client.Messages;

public interface LoginMessages extends Messages {
    @DefaultMessage("Incorrect username or email address")
    String incorrectUsernameOrEmail();

    @DefaultMessage("Incorrect username, email address or password")
    String incorrectUsernameEmailOrPassword();

    @DefaultMessage("Your password must be at least six characters long")
    String invalidPasswordLength();

    @DefaultMessage("Your password values must match")
    String passwordsNotMatching();

    @DefaultMessage("Oops. Please try again later.")
    String oopsTryAgain();
}
