package org.consumersunion.stories.login.application.forgotpassword;

import org.consumersunion.stories.login.application.forgotpassword.emailsent.ResetEmailSentModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ForgotPasswordModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new ResetEmailSentModule());

        bindPresenter(ForgotPasswordPresenter.class, ForgotPasswordPresenter.MyView.class, ForgotPasswordView.class,
                ForgotPasswordPresenter.MyProxy.class);
    }
}
