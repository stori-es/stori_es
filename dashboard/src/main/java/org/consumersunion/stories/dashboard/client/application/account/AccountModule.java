package org.consumersunion.stories.dashboard.client.application.account;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AccountModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(AccountPresenter.class, AccountPresenter.MyView.class, AccountView.class,
                AccountPresenter.MyProxy.class);
    }
}
