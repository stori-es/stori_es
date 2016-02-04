package org.consumersunion.stories.login.application.login;

import org.consumersunion.stories.login.application.ApplicationPresenter;
import org.consumersunion.stories.login.place.NameTokens;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class LoginPresenter extends Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy>
        implements LoginUiHandlers {
    interface MyView extends View, HasUiHandlers<LoginUiHandlers> {
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.login)
    interface MyProxy extends ProxyPlace<LoginPresenter> {
    }

    @Inject
    LoginPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

        getView().setUiHandlers(this);
    }
}
