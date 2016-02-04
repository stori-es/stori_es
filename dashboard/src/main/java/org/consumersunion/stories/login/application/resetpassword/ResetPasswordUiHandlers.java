package org.consumersunion.stories.login.application.resetpassword;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ResetPasswordUiHandlers extends UiHandlers {
    void resetPassword(String password, String confirmPassword);
}
