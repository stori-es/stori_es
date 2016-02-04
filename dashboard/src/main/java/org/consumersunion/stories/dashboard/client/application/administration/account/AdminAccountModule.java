package org.consumersunion.stories.dashboard.client.application.administration.account;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AdminAccountModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindSingletonPresenterWidget(CreateAccountPresenter.class, CreateAccountPresenter.MyView.class,
                CreateAccountView.class);
        bindSingletonPresenterWidget(UpdateAccountPresenter.class, UpdateAccountPresenter.MyView.class,
                UpdateAccountView.class);
    }
}
