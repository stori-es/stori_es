package org.consumersunion.stories.common.client.util;

import javax.inject.Provider;

import org.consumersunion.stories.common.client.event.SessionTimerInitEvent;
import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.service.response.Response;
import org.consumersunion.stories.common.client.widget.MessageStyle;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public abstract class ResponseHandler<R extends Response> implements AsyncCallback<R>, HasHandlers {

    @Inject
    private static Provider<EventBus> eventBusProvider;
    @Inject
    private static Provider<CommonI18nErrorMessages> errorMessagesProvider;
    @Inject
    private static Provider<MessageDispatcher> messageDispatcherProvider;
    @Inject
    private static Provider<CachingService> cachingServiceProvider;
    @Inject
    private static Provider<PlaceManager> placeManagerProvider;
    @Inject
    private static Provider<CookiesHelper> cookiesHelperProvider;

    private static final String SESSION_COOKIE = "OPENSESSIONID";

    protected final EventBus eventBus;
    protected final CommonI18nErrorMessages errorMessages;
    protected final MessageDispatcher messageDispatcher;
    protected final CachingService cachingService;
    protected final PlaceManager placeManager;
    protected final CookiesHelper cookiesHelper;

    private final boolean checkLogin;

    public ResponseHandler() {
        this(true);
    }

    ResponseHandler(boolean checkLogin) {
        this.checkLogin = checkLogin;
        eventBus = eventBusProvider.get();
        errorMessages = errorMessagesProvider.get();
        messageDispatcher = messageDispatcherProvider.get();
        cachingService = cachingServiceProvider.get();
        placeManager = placeManagerProvider.get();
        cookiesHelper = cookiesHelperProvider.get();
    }

    public void onFailure(Throwable e) {
        GWT.log(e.getMessage());
        if (e instanceof InvocationException) {
            InvocationException ie = (InvocationException) e;
            if (ie.getMessage().contains("j_spring_security_check")) {
                throw new RuntimeException(e);
//                bounceToLogin();
//                return;
            }
            // Oddly, the InvocationException does not carry the underlying
            // cause, so at this point, all we can do is
            // give a generic error message. We say "network error" which is a
            // possibility, though it could be a
            // runtime exception on the server side
            else {
                messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessages.serviceError(
                        errorMessages.resourceNotFound()));
            }
        } else if (errorMessages.notLoggedIn().equals(e.getMessage())) {
            throw new RuntimeException(e);
//            bounceToLogin();
//            return;
        } else {
            throw new RuntimeException(e);
//            messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessages.serviceError(
//                    errorMessages.resourceNotFound()));
//            bounceToLogin();
        }
    }

    @Override
    public void onSuccess(final R result) {
        if (checkLogin) {
            if (!result.isLoggedIn()) {
                bounceToLogin();
                return;
            } else {
                SessionTimerInitEvent.fire(this);
            }

            if (checkUserCookieExpired()) {
                return;
            }
        }

        if (!result.getGlobalErrorMessages().isEmpty()) {
            if (result.getShowErrorMessages()) {
                messageDispatcher.displayMessage(MessageStyle.ERROR, result.getGlobalErrorMessages().get(0));
            } else {
                messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessages.serviceError(
                        errorMessages.resourceNotFound()));
            }
        }

        handleSuccess(result);
    }

    private Boolean checkUserCookieExpired() {
        String OSessionId = cookiesHelper.getCookie(SESSION_COOKIE);
        if (Strings.isNullOrEmpty(OSessionId)) {
            bounceToLogin();
            return true;
        } else {
            return false;
        }
    }

    public abstract void handleSuccess(R result);

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }

    public void bounceToLogin() {
        registerLastToken();
        cookiesHelper.removeCookie(SESSION_COOKIE);
        Window.alert("Session is timed out. Please login again");
        Window.open(GWT.getHostPageBaseURL() + "j_spring_security_logout", "_self", null);
    }

    private void registerLastToken() {
        cachingService.putCachedData("expiredToken", placeManager.buildRelativeHistoryToken(0));
    }
}
