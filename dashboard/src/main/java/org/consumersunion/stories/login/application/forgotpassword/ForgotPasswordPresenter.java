package org.consumersunion.stories.login.application.forgotpassword;

import org.apache.http.HttpStatus;
import org.consumersunion.stories.common.client.api.AccountService;
import org.consumersunion.stories.common.client.api.RestCallbackAdapter;
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

public class ForgotPasswordPresenter extends Presenter<ForgotPasswordPresenter.MyView, ForgotPasswordPresenter.MyProxy>
        implements ForgotPasswordUiHandlers {
    interface MyView extends View, HasUiHandlers<ForgotPasswordUiHandlers> {
        void setError(String error);

        void clearError();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.forgot)
    interface MyProxy extends ProxyPlace<ForgotPasswordPresenter> {
    }

    private final LoginMessages loginMessages;
    private final RestDispatch dispatcher;
    private final AccountService accountService;
    private final PlaceManager placeManager;

    @Inject
    ForgotPasswordPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            RestDispatch dispatcher,
            AccountService accountService,
            PlaceManager placeManager,
            LoginMessages loginMessages) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

        getView().setUiHandlers(this);

        this.loginMessages = loginMessages;
        this.dispatcher = dispatcher;
        this.accountService = accountService;
        this.placeManager = placeManager;
    }

    @Override
    public void resetPassword(String username) {
        dispatcher.execute(accountService.reset(username), new RestCallbackAdapter<Void>() {
            @Override
            public void setResponse(Response response) {
                switch (response.getStatusCode()) {
                    case HttpStatus.SC_NOT_FOUND:
                        getView().setError(loginMessages.incorrectUsernameOrEmail());
                        break;
                    case HttpStatus.SC_OK:
                        onResetEmailSent();
                        break;
                    default:
                        getView().setError(loginMessages.oopsTryAgain());
                }
            }
        });
    }

    private void onResetEmailSent() {
        getView().clearError();
        placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.resetSent).build(), false);
    }
}
