package org.consumersunion.stories.dashboard.client.application.administration.account;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcOrganizationServiceAsync;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;
import org.consumersunion.stories.dashboard.client.application.administration.AdminManagerUiHandlers;
import org.consumersunion.stories.dashboard.client.util.OrganizationClientUtil;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class UpdateAccountPresenter extends PresenterWidget<UpdateAccountPresenter.MyView>
        implements HasUiHandlers<AdminManagerUiHandlers> {
    interface MyView extends View, HasUiHandlers<AdminManagerUiHandlers> {
        void setUsernameDisponibility(boolean available);

        void setData(List<CredentialedUser> data, int start, int totalCount);

        void displayUserOrganizations(List<String> selectedOrganizations);

        void displayOrganizations(List<OrganizationSummary> organizations);

        void reloadData();

        void resetForm();

        void setCurrentUser(CredentialedUser user);
    }

    public static final int PAGE_SIZE = 15;

    private final RpcOrganizationServiceAsync organizationService;

    @Inject
    UpdateAccountPresenter(
            EventBus eventBus,
            MyView view,
            RpcOrganizationServiceAsync organizationService) {
        super(eventBus, view);

        this.organizationService = organizationService;
    }

    public void reset() {
        onReveal();
    }

    public void setData(List<CredentialedUser> data, int start, int totalCount) {
        getView().setData(data, start, totalCount);
    }

    @Override
    public void setUiHandlers(AdminManagerUiHandlers uiHandlers) {
        getView().setUiHandlers(uiHandlers);
    }

    public void setUsernameDisponibility(boolean available) {
        getView().setUsernameDisponibility(available);
    }

    public void setCurrentUser(CredentialedUser user) {
        getView().setCurrentUser(user);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        getView().resetForm();
        loadOrganizations();
        getView().reloadData();
    }

    private void loadOrganizations() {
        OrganizationClientUtil.getAllAdminOrganizations(organizationService, new OrganizationClientUtil() {
            @Override
            public void processAllOrganizations(List<OrganizationSummary> organizations) {
                getView().displayOrganizations(organizations);
            }
        });
    }
}
