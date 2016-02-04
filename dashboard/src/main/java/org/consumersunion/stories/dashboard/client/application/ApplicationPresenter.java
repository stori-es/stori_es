package org.consumersunion.stories.dashboard.client.application;

import java.util.List;

import org.consumersunion.stories.common.client.api.AccountService;
import org.consumersunion.stories.common.client.api.RestCallbackAdapter;
import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.InteractiveMessageEvent;
import org.consumersunion.stories.common.client.event.SessionTimerInitEvent;
import org.consumersunion.stories.common.client.event.ShowLoadingEvent;
import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.widget.LoadingPresenter;
import org.consumersunion.stories.common.client.widget.MessagesPresenter;
import org.consumersunion.stories.common.client.widget.messages.MessageFactory;
import org.consumersunion.stories.common.shared.model.entity.Contact;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.client.RestDispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy>
        implements ApplicationUiHandlers, ShowLoadingEvent.ShowLoadingHandler, HideLoadingEvent.HideLoadingHandler,
        SessionTimerInitEvent.SessionTimerInitHandler {

    interface MyView extends View, HasUiHandlers<ApplicationUiHandlers> {
        void showErrorPopup(String message);
    }

    @ProxyStandard
    interface MyProxy extends Proxy<ApplicationPresenter> {
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_MAIN_CONTENT = new Type<RevealContentHandler<?>>();
    public static final Object TYPE_SetMessagesContent = new Object();

    // In theory and with some testing, our timeout here should end up actually logging out the user. However, there
    // were reports that this wouldn't always happen so we want to keep them matched just in case.
    private static final int TIMEOUT = 2700; // 45min timeout; matched to Tomcat session timeout.
    private static final int COUNTDOWN = 900; // 15 min countdown

    private final MessagesPresenter messagesPresenter;
    private final LoadingPresenter loadingPresenter;
    private final CommonI18nMessages commonI18nMessages;
    private final CachingService cachingService;
    private final PlaceManager placeManager;
    private final RestDispatchAsync restDispatch;
    private final AccountService accountService;
    private final MessageFactory messageFactory;

    private int loadingCounts = 0;
    private Timer timeoutTimer = null;
    private Timer countdownTimer = null;

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            MessagesPresenter messagesPresenter,
            LoadingPresenter loadingPresenter,
            CommonI18nMessages commonI18nMessages,
            CachingService cachingService,
            PlaceManager placeManager,
            RestDispatchAsync restDispatch,
            AccountService accountService,
            MessageFactory messageFactory) {
        super(eventBus, view, proxy, RevealType.Root);

        this.messagesPresenter = messagesPresenter;
        this.loadingPresenter = loadingPresenter;
        this.commonI18nMessages = commonI18nMessages;
        this.cachingService = cachingService;
        this.placeManager = placeManager;
        this.restDispatch = restDispatch;
        this.accountService = accountService;
        this.messageFactory = messageFactory;

        getView().setUiHandlers(this);
    }

    @ProxyEvent
    @Override
    public void onShowLoading(ShowLoadingEvent event) {
        loadingCounts++;
        loadingPresenter.setFullScreen(event.isFullScreen());
        if (!loadingPresenter.isVisible()) {
            addToPopupSlot(loadingPresenter, true);
        }
    }

    @ProxyEvent
    @Override
    public void onHideLoading(HideLoadingEvent event) {
        if (loadingCounts > 0) {
            loadingCounts--;
        }
        if (loadingPresenter.isVisible() && loadingCounts == 0) {
            removeFromPopupSlot(loadingPresenter);
        }
    }

    @Override
    public void onSessionTimerInit(SessionTimerInitEvent event) {
        initSessionTimeout();
    }

    @Override
    public void renewSession() {
        cancelTimeout();
        timeoutTimer.schedule(TIMEOUT * 1000);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        restDispatch.execute(accountService.getPrimaryEmail(), new RestCallbackAdapter<String>() {
            @Override
            public void setResponse(Response response) {
                switch (response.getStatusCode()) {
                    case 404:
                        getEmails();
                        break;
                }
            }
        });
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(TYPE_SetMessagesContent, messagesPresenter);

        addRegisteredHandler(SessionTimerInitEvent.TYPE, this);
        addRegisteredHandler(ShowLoadingEvent.TYPE, this);
        addRegisteredHandler(HideLoadingEvent.TYPE, this);
    }

    private void getEmails() {
        restDispatch.execute(accountService.getEmails(), new RestCallbackAdapter<List<Contact>>() {
            @Override
            public void onSuccess(List<Contact> result) {
                InteractiveMessageEvent.fire(ApplicationPresenter.this, messageFactory.createPrimaryEmail(result));
            }
        });
    }

    private void initSessionTimeout() {
        cancelTimeout();
        timeoutTimer = new Timer() {
            public void run() {
                startCountdown();
            }
        };
        timeoutTimer.schedule(TIMEOUT * 1000);
    }

    private void startCountdown() {
        cancelTimeout();
        countdownTimer = new Timer() {
            public void run() {
                cancelCountdown();
            }
        };
        countdownTimer.schedule(COUNTDOWN * 1000);
        String counter = Integer.valueOf(COUNTDOWN / 60).toString();
        getView().showErrorPopup(commonI18nMessages.timeoutWarning(counter));
    }

    private void cancelTimeout() {
        if (timeoutTimer != null) {
            timeoutTimer.cancel();
        }
    }

    private void cancelCountdown() {
        if (countdownTimer != null) {
            countdownTimer.cancel();
            cachingService.putCachedData("expiredToken", placeManager.buildRelativeHistoryToken(0));
            Window.open(GWT.getHostPageBaseURL() + "j_spring_security_logout", "_self", null);
        }
    }
}
