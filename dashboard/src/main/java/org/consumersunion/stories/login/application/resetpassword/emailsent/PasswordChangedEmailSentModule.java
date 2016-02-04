package org.consumersunion.stories.login.application.resetpassword.emailsent;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PasswordChangedEmailSentModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(PasswordChangedEmailSentPresenter.class, PasswordChangedEmailSentPresenter.MyView.class,
                PasswordChangedEmailSentView.class,
                PasswordChangedEmailSentPresenter.MyProxy.class);
    }
}
