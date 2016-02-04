package org.consumersunion.stories.dashboard.client.application.administration;

import org.consumersunion.stories.dashboard.client.application.administration.account.AdminAccountModule;
import org.consumersunion.stories.dashboard.client.application.administration.organization.AdminOrganizationModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AdministrationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new AdminAccountModule());
        install(new AdminOrganizationModule());

        bindPresenter(AdministrationPresenter.class, AdministrationPresenter.MyView.class, AdministrationView.class,
                AdministrationPresenter.MyProxy.class);
    }
}
