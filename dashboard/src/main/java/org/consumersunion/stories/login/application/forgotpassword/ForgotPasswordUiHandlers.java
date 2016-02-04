package org.consumersunion.stories.login.application.forgotpassword;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ForgotPasswordUiHandlers extends UiHandlers {
    void resetPassword(String username);
}
