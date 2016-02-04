package org.consumersunion.stories.login.application.confirmemail;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ConfirmEmailModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ConfirmEmailPresenter.class, ConfirmEmailPresenter.MyView.class, ConfirmEmailView.class,
                ConfirmEmailPresenter.MyProxy.class);
    }
}
