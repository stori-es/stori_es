package org.consumersunion.stories.dashboard.client.application.widget;

import java.util.List;

import org.consumersunion.stories.common.client.event.UserChangedEvent;
import org.consumersunion.stories.common.client.service.RpcAuthorizationServiceAsync;
import org.consumersunion.stories.common.client.service.RpcProfileServiceAsync;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.DisplayAuthResponse;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.util.CurrentUser;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.dashboard.client.event.AccountChangedEvent;
import org.consumersunion.stories.dashboard.shared.MenuItem;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.NavigationEvent;
import com.gwtplatform.mvp.client.proxy.NavigationHandler;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static org.consumersunion.stories.common.client.event.UserChangedEvent.UserChangedHandler;
import static org.consumersunion.stories.dashboard.client.event.AccountChangedEvent.AccountChangedHandler;

public class HeaderPresenter extends PresenterWidget<HeaderPresenter.MyView>
        implements HeaderUiHandlers, AccountChangedHandler, NavigationHandler, UserChangedHandler {
    interface MyView extends View, HasUiHandlers<HeaderUiHandlers> {
        void reloadInfo(String login, boolean isAdmin);

        void displayOrganizationTitle(String title);

        void loadProfiles(List<ProfileSummary> profiles);

        void showAdminMenu(MenuItem item);

        void selectStoriesMenu();

        void selectCollectionsMenu();

        void setLogin(String login);
    }

    private final MessageDispatcher messageDispatcher;
    private final CurrentUser currentUser;
    private final CachingService cachingService;
    private final RpcAuthorizationServiceAsync rpcAuthorizationService;
    private final RpcProfileServiceAsync profileService;
    private final PlaceManager placeManager;

    @Inject
    HeaderPresenter(
            EventBus eventBus,
            MyView view,
            MessageDispatcher messageDispatcher,
            CachingService cachingService,
            RpcAuthorizationServiceAsync rpcAuthorizationService,
            RpcProfileServiceAsync profileService,
            PlaceManager placeManager,
            CurrentUser currentUser) {
        super(eventBus, view);

        this.messageDispatcher = messageDispatcher;
        this.currentUser = currentUser;
        this.cachingService = cachingService;
        this.rpcAuthorizationService = rpcAuthorizationService;
        this.profileService = profileService;
        this.placeManager = placeManager;

        getView().setUiHandlers(this);
    }

    @Override
    public void onNavigation(NavigationEvent navigationEvent) {
        String place = navigationEvent.getRequest().getNameToken();
        onPlaceChanged(place);
    }

    @Override
    public void userAccount() {
        PlaceRequest place = new PlaceRequest.Builder().nameToken(NameTokens.account).build();
        placeManager.revealPlace(place);
    }

    @Override
    public void onUserChanged(UserChangedEvent event) {
        getView().setLogin(event.getUser().getHandle());
    }

    @Override
    public void specificProfile(ProfileSummary profileSummary) {
        profileService.setActiveProfile(profileSummary.getProfile().getId(), false,
                new ResponseHandlerLoader<DatumResponse<ProfileSummary>>() {
                    @Override
                    public void handleSuccess(final DatumResponse<ProfileSummary> result) {
                        ProfileSummary profileSummary = result.getDatum();
                        updateCurrentProfile(profileSummary);

                        gotoOrganizationDetail();

                        getView().reloadInfo(currentUser.getUser().getHandle(), profileSummary.isAdminOverOrg());
                    }
                }
        );
    }

    @Override
    public void gotoOrganizationDetail() {
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.organization)
                .with(ParameterTokens.id,
                        String.valueOf(currentUser.getCurrentProfile().getOrganizationId()))
                .build();

        placeManager.revealPlace(place);
    }

    @Override
    public void allStories() {
        PlaceRequest place = new PlaceRequest.Builder().nameToken(NameTokens.stories).build();
        placeManager.revealPlace(place);
    }

    @Override
    public void allCollections() {
        PlaceRequest place = new PlaceRequest.Builder().nameToken(NameTokens.collections).build();
        placeManager.revealPlace(place);
    }

    @Override
    public void administration(MenuItem menuItem) {
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.administration)
                .with(ParameterTokens.menuItem, String.valueOf(menuItem.getItemType().getIndex()))
                .build();
        placeManager.revealPlace(place);
    }

    @Override
    public void authorizeMenu(final MenuItem item) {
        rpcAuthorizationService.itemAuthorized(item, new ResponseHandler<DisplayAuthResponse>() {
            @Override
            public void handleSuccess(DisplayAuthResponse response) {
                item.setVisible(response.isAuthorized());
                getView().showAdminMenu(item);
            }
        });
    }

    @Override
    public void logout() {
        cachingService.clear();
    }

    @Override
    public void onAccountChanged(AccountChangedEvent event) {
        if (currentUser.getUser() != null && event.getUser().getId() == currentUser.getUser().getId()) {
            getView().reloadInfo(event.getUser().getHandle(), true);
        }
    }

    public void reloadProfileData(ProfileSummary profileSummary) {
        profileService.setActiveProfile(profileSummary.getProfile().getId(), false,
                new ResponseHandler<DatumResponse<ProfileSummary>>() {
                    @Override
                    public void handleSuccess(DatumResponse<ProfileSummary> result) {
                        ProfileSummary profileSummary = result.getDatum();
                        updateCurrentProfile(profileSummary);
                        getView().displayOrganizationTitle(profileSummary.getOrganizationName());
                        getView().reloadInfo(currentUser.getUser().getHandle(), profileSummary.isAdminOverOrg());
                        setProfileData();
                    }
                }
        );
    }

    @Override
    protected void onBind() {
        super.onBind();

        addRegisteredHandler(AccountChangedEvent.TYPE, this);
        addRegisteredHandler(NavigationEvent.getType(), this);
        addRegisteredHandler(UserChangedEvent.TYPE, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        loadCurrentUser();
        setProfileData();
    }

    @Override
    protected void onReset() {
        super.onReset();

        onPlaceChanged(placeManager.getCurrentPlaceRequest().getNameToken());
    }

    private void updateCurrentProfile(ProfileSummary profileSummary) {
        cachingService.putCachedData("profileId", String.valueOf(profileSummary.getProfile().getId()));
        cachingService.putCachedData("actAs", String.valueOf(profileSummary.getOrganizationName()));
        currentUser.setCurrentProfile(profileSummary.getProfile());
    }

    private void onPlaceChanged(final String place) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (place.equals(NameTokens.stories) || place.equals((NameTokens.story))) {
                    getView().selectStoriesMenu();
                } else if (place.equals(NameTokens.collections) || place.equals(NameTokens.collection)) {
                    getView().selectCollectionsMenu();
                }
            }
        });
    }

    private void loadCurrentUser() {
        User user = currentUser.getUser();
        if (user != null) {
            displayWelcomeMessage();
            getView().reloadInfo(user.getHandle(), true);
            getView().displayOrganizationTitle(user.getHandle());
        }
    }

    private void displayWelcomeMessage() {
        Profile profile = currentUser.getCurrentProfile();
        String message = "Welcome " + profile.getGivenName() + " " + profile.getSurname() + "!";
        messageDispatcher.displayMessage(MessageStyle.INFORMATION, message);
    }

    private void setProfileData() {
        ResponseHandler<DataResponse<ProfileSummary>> profilesCallback =
                new ResponseHandler<DataResponse<ProfileSummary>>() {
                    @Override
                    public void handleSuccess(DataResponse<ProfileSummary> result) {
                        getView().loadProfiles(result.getData());
                        if (!Strings.isNullOrEmpty(cachingService.getCachedData("profileId"))) {
                            getView().displayOrganizationTitle(cachingService.getCachedData("actAs"));
                        }
                    }
                };
        profileService.getProfileSummaries(profilesCallback);
    }
}
