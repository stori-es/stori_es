package org.consumersunion.stories.dashboard.client.application.administration.organization;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AdminOrganizationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindSingletonPresenterWidget(CreateOrganizationPresenter.class, CreateOrganizationPresenter.MyView.class,
                CreateOrganizationView.class);
        bindSingletonPresenterWidget(UpdateOrganizationPresenter.class, UpdateOrganizationPresenter.MyView.class,
                UpdateOrganizationView.class);
    }
}
