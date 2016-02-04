package org.consumersunion.stories.dashboard.client.gin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Provider;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.service.RpcAuthorizationServiceAsync;
import org.consumersunion.stories.common.client.service.RpcProfileServiceAsync;
import org.consumersunion.stories.common.client.service.RpcUserServiceAsync;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.DisplayAuthResponse;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.util.CurrentUser;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.dashboard.client.resource.JsClientBundle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

public class StoriesBootstrapper implements Bootstrapper, HasHandlers {
    private final static Logger logger = Logger.getLogger(StoriesBootstrapper.class.getName());
    private final static Integer TIMEOUT_LOAD = 12 * 1000;

    private final PlaceManager placeManager;
    private final RpcUserServiceAsync userService;
    private final TokenFormatter tokenFormatter;
    private final CachingService cachingService;
    private final RpcAuthorizationServiceAsync rpcAuthorizationService;
    private final RpcProfileServiceAsync profileService;
    private final EventBus eventBus;
    private final CurrentUser currentUser;
    private final ResponseHandler<DatumResponse<User>> getCurrentUserCallback;
    private final Provider<JsClientBundle> jsClientBundleProvider;

    private boolean mapLoaded;

    @Inject
    StoriesBootstrapper(
            RpcUserServiceAsync userService,
            PlaceManager placeManager,
            TokenFormatter tokenFormatter,
            CachingService cachingService,
            RpcAuthorizationServiceAsync rpcAuthorizationService,
            RpcProfileServiceAsync profileService,
            Resources resources,
            EventBus eventBus,
            CurrentUser currentUser,
            Provider<JsClientBundle> jsClientBundleProvider) {
        this.userService = userService;
        this.placeManager = placeManager;
        this.tokenFormatter = tokenFormatter;
        this.cachingService = cachingService;
        this.rpcAuthorizationService = rpcAuthorizationService;
        this.profileService = profileService;
        this.eventBus = eventBus;
        this.currentUser = currentUser;
        this.jsClientBundleProvider = jsClientBundleProvider;

        resources.generalStyleCss().ensureInjected();
        resources.buttonStyleCss().ensureInjected();
        resources.builderStyleCss().ensureInjected();
        resources.blockConfigurators().ensureInjected();
        resources.cards().ensureInjected();
        resources.collectionsStyle().ensureInjected();
        resources.gwtUploadStyle().ensureInjected();

        getCurrentUserCallback = new ResponseHandler<DatumResponse<User>>() {
            @Override
            public void handleSuccess(DatumResponse<User> result) {
                onUserLoaded(result);
            }
        };

        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                HideLoadingEvent.fire(StoriesBootstrapper.this);
            }
        });
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }

    @Override
    public void onBootstrap() {
        Cookies.setCookie("OPENSESSIONID", "SID" + new Date().getTime());
        mapLoaded = false;
        userService.getLoggedInUser(getCurrentUserCallback);
        cachingService.clearObjects();
    }

    private void retrieveUserRole() {
        rpcAuthorizationService.isSuperUser(new ResponseHandler<DisplayAuthResponse>() {
            @Override
            public void handleSuccess(DisplayAuthResponse response) {
                onGetCurrentUser();
            }
        });
    }

    private void onGetCurrentUser() {
        if (currentUser == null) {
            logger.info("User is not authentified -- access denied...");
            Window.open(GWT.getHostPageBaseURL() + "j_spring_security_logout", "_self", null);
        } else {
            User user = currentUser.getUser();
            if (user != null) {
                setActiveProfile();
                cachingService.putCachedData("userId", String.valueOf(user.getId()));
            } else {
                logger.info("Session timed out");
                Window.open(GWT.getHostPageBaseURL() + "signin.jsp", "_self", null);
            }
        }
    }

    private void setActiveProfile() {
        final String expiredToken = cachingService.getCachedData("expiredToken");
        final List<PlaceRequest> expiredReq = tokenFormatter.toPlaceRequestHierarchy(Strings.nullToEmpty(expiredToken));

        String cachedProfileId = cachingService.getCachedData("profileId");
        if (isSameUser() && !Strings.isNullOrEmpty(cachedProfileId)) {
            profileService.setActiveProfile(Integer.valueOf(cachedProfileId), false,
                    new ResponseHandler<DatumResponse<ProfileSummary>>() {
                        @Override
                        public void handleSuccess(DatumResponse<ProfileSummary> result) {
                            if (result.isError()) {
                                setDefaultProfile();
                            } else {
                                setCurrentProfile(result.getDatum());
                                revealCurrentPlaceOrExpire(expiredToken, expiredReq);
                            }
                        }
                    }
            );
        } else {
            setDefaultProfile();
        }
    }

    private void setDefaultProfile() {
        cachingService.clear();
        // the default profile is set on login
        profileService.setActiveProfile(currentUser.getUser().getDefaultProfile(), false,
                new ResponseHandler<DatumResponse<ProfileSummary>>() {
                    @Override
                    public void handleSuccess(DatumResponse<ProfileSummary> result) {
                        setCurrentProfile(result.getDatum());
                        placeManager.revealCurrentPlace();
                    }
                }
        );
    }

    private void setCurrentProfile(ProfileSummary profileSummary) {
        Profile profile = profileSummary.getProfile();
        if (profile != null) {
            cachingService.putCachedData("profileId", String.valueOf(profile.getId()));
            cachingService.putCachedData("actAs", profileSummary.getOrganizationName());
            currentUser.setCurrentProfile(profile);
        }
    }

    private boolean isSameUser() {
        String userIdFromCache = cachingService.getCachedData("userId");

        return currentUser.getUser() != null && String.valueOf(currentUser.getUser().getId()).equals(userIdFromCache);
    }

    private void revealCurrentPlaceOrExpire(String expiredToken, List<PlaceRequest> expiredReq) {
        if (Strings.isNullOrEmpty(expiredToken)) {
            placeManager.revealCurrentPlace();
        } else {
            cachingService.putCachedData("expiredToken", "");
            placeManager.revealPlaceHierarchy(expiredReq);
        }
    }

    private void onUserLoaded(DatumResponse<User> result) {
        currentUser.setUser(result.getDatum());
        loadMapApi();
    }

    private void loadMapApi() {
        ArrayList<LoadApi.LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();

        loadLibraries.add(LoadApi.LoadLibrary.PLACES);
        Runnable onLoad = new Runnable() {
            @Override
            public void run() {
                mapLoaded = true;
                retrieveUserRole();
            }
        };

        Timer apiLoadTimer = new Timer() {
            @Override
            public void run() {
                if (!mapLoaded) {
                    retrieveUserRole();
                }
            }
        };

        boolean sensor = true;
        LoadApi.go(onLoad, loadLibraries, sensor, "client=gme-consumersunionof1");
        apiLoadTimer.schedule(TIMEOUT_LOAD);

        ScriptInjector.fromString(jsClientBundleProvider.get().markerClusterer().getText())
                .setWindow(ScriptInjector.TOP_WINDOW)
                .inject();
    }
}
