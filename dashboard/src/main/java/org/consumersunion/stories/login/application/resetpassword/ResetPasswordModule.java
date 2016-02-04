package org.consumersunion.stories.login.application.resetpassword;

import org.consumersunion.stories.login.application.resetpassword.emailsent.PasswordChangedEmailSentModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ResetPasswordModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new PasswordChangedEmailSentModule());

        bindPresenter(ResetPasswordPresenter.class, ResetPasswordPresenter.MyView.class, ResetPasswordView.class,
                ResetPasswordPresenter.MyProxy.class);
    }
}
