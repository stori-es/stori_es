package org.consumersunion.stories.login.application.forgotpassword.emailsent;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ResetEmailSentModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ResetEmailSentPresenter.class, ResetEmailSentPresenter.MyView.class, ResetEmailSentView.class,
                ResetEmailSentPresenter.MyProxy.class);
    }
}
