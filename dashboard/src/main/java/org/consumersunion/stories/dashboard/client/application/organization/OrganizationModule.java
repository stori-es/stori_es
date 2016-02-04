package org.consumersunion.stories.dashboard.client.application.organization;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class OrganizationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(OrganizationUiHandlers.class).to(OrganizationPresenter.class);

        bindPresenter(OrganizationPresenter.class, OrganizationPresenter.MyView.class, OrganizationView.class,
                OrganizationPresenter.MyProxy.class);
    }
}
