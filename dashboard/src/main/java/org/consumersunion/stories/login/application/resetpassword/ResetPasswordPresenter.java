package org.consumersunion.stories.login.application.resetpassword;

import org.apache.http.HttpStatus;
import org.consumersunion.stories.common.client.api.AccountService;
import org.consumersunion.stories.common.client.api.RestCallbackAdapter;
import org.consumersunion.stories.common.shared.dto.ResetPasswordRequest;
import org.consumersunion.stories.login.application.ApplicationPresenter;
import org.consumersunion.stories.login.application.i18n.LoginMessages;
import org.consumersunion.stories.login.place.NameTokens;

import com.google.gwt.http.client.Response;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ResetPasswordPresenter extends Presenter<ResetPasswordPresenter.MyView, ResetPasswordPresenter.MyProxy>
        implements ResetPasswordUiHandlers {
    interface MyView extends View, HasUiHandlers<ResetPasswordUiHandlers> {
        void clearError();

        void setError(String error);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.reset)
    interface MyProxy extends ProxyPlace<ResetPasswordPresenter> {
    }

    private final PlaceManager placeManager;
    private final RestDispatch dispatcher;
    private final AccountService accountService;
    private final LoginMessages loginMessages;

    private String code;

    @Inject
    ResetPasswordPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            RestDispatch dispatcher,
            AccountService accountService,
            LoginMessages loginMessages) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.accountService = accountService;
        this.loginMessages = loginMessages;

        getView().setUiHandlers(this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        code = request.getParameter(NameTokens.code, "");
    }

    @Override
    public void resetPassword(String password, String confirmPassword) {
        dispatcher.execute(accountService.reset(new ResetPasswordRequest(code, password, confirmPassword)),
                new RestCallbackAdapter<Void>() {
                    @Override
                    public void setResponse(Response response) {
                        switch (response.getStatusCode()) {
                            case HttpStatus.SC_OK:
                                onPasswordReset();
                                break;
                            default:
                                getView().setError(loginMessages.oopsTryAgain());
                        }
                    }
                }

        );
    }

    private void onPasswordReset() {
        getView().clearError();
        placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.resetSuccess).build(), false);
    }
}
