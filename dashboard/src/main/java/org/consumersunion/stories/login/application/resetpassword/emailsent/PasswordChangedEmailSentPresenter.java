package org.consumersunion.stories.login.application.resetpassword.emailsent;

import org.consumersunion.stories.login.application.ApplicationPresenter;
import org.consumersunion.stories.login.place.NameTokens;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class PasswordChangedEmailSentPresenter
        extends Presenter<PasswordChangedEmailSentPresenter.MyView, PasswordChangedEmailSentPresenter.MyProxy>
        implements PasswordChangedEmailSentUiHandler {
    interface MyView extends View, HasUiHandlers<PasswordChangedEmailSentUiHandler> {
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.resetSuccess)
    interface MyProxy extends ProxyPlace<PasswordChangedEmailSentPresenter> {
    }

    private final PlaceManager placeManager;

    @Inject
    PasswordChangedEmailSentPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

        this.placeManager = placeManager;

        getView().setUiHandlers(this);
    }

    @Override
    public void signIn() {
        placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.login).build());
    }
}
