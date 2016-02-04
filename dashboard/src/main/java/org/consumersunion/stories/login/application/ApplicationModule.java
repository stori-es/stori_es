package org.consumersunion.stories.login.application;

import org.consumersunion.stories.login.application.confirmemail.ConfirmEmailModule;
import org.consumersunion.stories.login.application.forgotpassword.ForgotPasswordModule;
import org.consumersunion.stories.login.application.login.LoginModule;
import org.consumersunion.stories.login.application.resetpassword.ResetPasswordModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new ForgotPasswordModule());
        install(new LoginModule());
        install(new ResetPasswordModule());
        install(new ConfirmEmailModule());

        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
    }
}
