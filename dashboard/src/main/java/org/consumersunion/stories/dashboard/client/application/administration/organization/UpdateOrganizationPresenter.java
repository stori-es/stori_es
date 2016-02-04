package org.consumersunion.stories.dashboard.client.application.administration.organization;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcThemeServiceAsync;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;
import org.consumersunion.stories.dashboard.client.application.administration.AdminManagerUiHandlers;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class UpdateOrganizationPresenter extends PresenterWidget<UpdateOrganizationPresenter.MyView>
        implements HasUiHandlers<AdminManagerUiHandlers> {
    interface MyView extends View, HasUiHandlers<AdminManagerUiHandlers> {
        void setUsernameDisponibility(boolean available);

        void setOrganizationNameDisponibility(boolean available);

        void reloadData();

        void setData(List<OrganizationSummary> data, int start, int totalCount);

        void setThemeList(List<Theme> themes);

        void resetForm();

        void setCurrentOrganization(OrganizationSummary organizationSummary);

        void displayOrganizationThemes(List<String> selectedThemes);
    }

    public static final int PAGE_SIZE = 15;

    private final RpcThemeServiceAsync themeService;

    @Inject
    UpdateOrganizationPresenter(
            EventBus eventBus,
            MyView view,
            RpcThemeServiceAsync themeService) {
        super(eventBus, view);

        this.themeService = themeService;
    }

    @Override
    public void setUiHandlers(AdminManagerUiHandlers uiHandlers) {
        getView().setUiHandlers(uiHandlers);
    }

    public void setUsernameDisponibility(boolean available) {
        getView().setUsernameDisponibility(available);
    }

    public void setData(List<OrganizationSummary> data, int start, int totalCount) {
        getView().setData(data, start, totalCount);
    }

    public void displayOrganizationThemes(List<String> themes) {
        getView().displayOrganizationThemes(themes);
    }

    public void setOrganizationNameDisponibility(boolean available) {
        getView().setOrganizationNameDisponibility(available);
    }

    public void setCurrentOrganization(OrganizationSummary organizationSummary) {
        getView().setCurrentOrganization(organizationSummary);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        getView().resetForm();
        loadThemes();
        getView().reloadData();
    }

    public void loadThemes() {
        themeService.getAll(new ResponseHandler<DataResponse<Theme>>() {
            @Override
            public void handleSuccess(DataResponse<Theme> result) {
                getView().setThemeList(result.getData());
            }
        });
    }
}
