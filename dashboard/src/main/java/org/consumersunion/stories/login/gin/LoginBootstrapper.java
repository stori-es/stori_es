package org.consumersunion.stories.login.gin;

import org.consumersunion.stories.common.client.service.RpcUserServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.login.place.NameTokens;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class LoginBootstrapper implements Bootstrapper {
    private final PlaceManager placeManager;
    private final RpcUserServiceAsync userService;

    @Inject
    LoginBootstrapper(
            PlaceManager placeManager,
            RpcUserServiceAsync userService) {
        this.placeManager = placeManager;
        this.userService = userService;
    }

    @Override
    public void onBootstrap() {
        userService.getLoggedInUser(new AsyncCallback<DatumResponse<User>>() {
            @Override
            public void onFailure(Throwable caught) {
                placeManager.revealCurrentPlace();
            }

            @Override
            public void onSuccess(DatumResponse<User> result) {
                User datum = result.getDatum();
                if (datum == null || NameTokens.confirmSuccess.equals(History.getToken())) {
                    placeManager.revealCurrentPlace();
                } else {
                    Window.Location.assign("/stories.jsp");
                }
            }
        });
    }
}
