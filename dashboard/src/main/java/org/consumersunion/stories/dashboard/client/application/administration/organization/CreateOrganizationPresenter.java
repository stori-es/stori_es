package org.consumersunion.stories.dashboard.client.application.administration.organization;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcThemeServiceAsync;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.dashboard.client.application.administration.AdminManagerUiHandlers;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class CreateOrganizationPresenter extends PresenterWidget<CreateOrganizationPresenter.MyView>
        implements HasUiHandlers<AdminManagerUiHandlers> {
    interface MyView extends View, HasUiHandlers<AdminManagerUiHandlers> {
        void displayThemes(List<Theme> themes);

        void setUsernameDisponibility(boolean available);

        void setOrganizationNameDisponibility(boolean available);
    }

    private final RpcThemeServiceAsync themeService;

    @Inject
    CreateOrganizationPresenter(
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

    public void setOrganizationNameDisponibility(boolean available) {
        getView().setOrganizationNameDisponibility(available);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        loadThemeOptions();
    }

    private void loadThemeOptions() {
        themeService.getAll(new ResponseHandler<DataResponse<Theme>>() {
            @Override
            public void handleSuccess(final DataResponse<Theme> result) {
                getView().displayThemes(result.getData());
            }
        });
    }
}
