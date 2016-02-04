package org.consumersunion.stories.dashboard.client.application.administration.account;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcOrganizationServiceAsync;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;
import org.consumersunion.stories.dashboard.client.application.administration.AdminManagerUiHandlers;
import org.consumersunion.stories.dashboard.client.util.OrganizationClientUtil;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class CreateAccountPresenter
        extends PresenterWidget<CreateAccountPresenter.MyView>
        implements HasUiHandlers<AdminManagerUiHandlers> {
    interface MyView extends View, HasUiHandlers<AdminManagerUiHandlers> {
        void setUsernameDisponibility(boolean available);

        void displayOrganizations(List<OrganizationSummary> organizationSummaries);

        void resetForm();
    }

    private final RpcOrganizationServiceAsync organizationService;

    @Inject
    CreateAccountPresenter(
            EventBus eventBus,
            MyView view,
            RpcOrganizationServiceAsync organizationService) {
        super(eventBus, view);

        this.organizationService = organizationService;
    }

    @Override
    public void setUiHandlers(AdminManagerUiHandlers uiHandlers) {
        getView().setUiHandlers(uiHandlers);
    }

    public void setUsernameDisponibility(boolean available) {
        getView().setUsernameDisponibility(available);
    }

    public void loadOrganizations() {
        OrganizationClientUtil.getAllAdminOrganizations(organizationService, new OrganizationClientUtil() {
            @Override
            public void processAllOrganizations(final List<OrganizationSummary> organizations) {
                getView().displayOrganizations(organizations);
            }
        });
    }

    public void resetForm() {
        getView().resetForm();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        getView().resetForm();
        loadOrganizations();
    }
}
