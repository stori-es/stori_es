package org.consumersunion.stories.dashboard.client.application.account;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.event.MapBoundariesChangeEvent;
import org.consumersunion.stories.common.client.event.SortChangedEvent;
import org.consumersunion.stories.common.client.event.UserChangedEvent;
import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.service.RpcProfileServiceAsync;
import org.consumersunion.stories.common.client.service.RpcUserServiceAsync;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.ui.stories.ListStoriesPresenter;
import org.consumersunion.stories.common.client.ui.stories.StoriesListHandler;
import org.consumersunion.stories.common.client.util.CurrentUser;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.model.type.LanguageType;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.dashboard.client.application.AbstractStoriesPresenter;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.profile.widget.ContactsPresenter;
import org.consumersunion.stories.dashboard.client.application.profile.widget.NotesManagerPresenter;
import org.consumersunion.stories.dashboard.client.application.stories.widget.StoryCard;
import org.consumersunion.stories.dashboard.client.application.util.location.DefaultLocationProvider;
import org.consumersunion.stories.dashboard.client.application.widget.StoriesMapPresenter;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class AccountPresenter extends AbstractStoriesPresenter<AccountPresenter.MyView, AccountPresenter.MyProxy>
        implements AccountUiHandlers, StoriesListHandler, MapBoundariesChangeEvent.MapBoundariesChangeHandler {
    interface MyView extends View, HasUiHandlers<AccountUiHandlers> {
        void setupAccountForm(User user, Profile profile);

        void setupLanguages(List<LanguageType> languageTypes);

        void setupOrganizationLanguages(List<LanguageType> languageTypes);

        void setStoriesCount(int count);

        SortField getSort();

        void setSearchToken(String searchToken);

        void setPasswordUpdateButtonEnabled(boolean enabled);

        void clearPassword();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.account)
    interface MyProxy extends ProxyPlace<AccountPresenter> {
    }

    static final Object TYPE_SetListStories = new Object();
    static final Object TYPE_SetPersonProfil = new Object();
    static final Object TYPE_SetNoteManager = new Object();
    static final Object SLOT_MAP = new Object();

    private final RpcUserServiceAsync userService;
    private final RpcProfileServiceAsync profileService;
    private final ListStoriesPresenter listStoriesPresenter;
    private final ContactsPresenter contactsPresenter;
    private final NotesManagerPresenter notesManagerPresenter;
    private final CommonI18nMessages messages;
    private final StoriesMapPresenter storiesMapPresenter;
    private final PlaceManager placeManager;
    private final DefaultLocationProvider defaultLocationProvider;
    private final CurrentUser currentUser;

    private String searchToken;
    private LatLng northEast;
    private LatLng southWest;

    @Inject
    AccountPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            CommonI18nMessages messages,
            RpcUserServiceAsync userService,
            RpcProfileServiceAsync profileService,
            @StoryCard ListStoriesPresenter listStoriesPresenter,
            ContactsPresenter contactsPresenter,
            NotesManagerPresenter notesManagerPresenter,
            StoriesMapPresenter storiesMapPresenter,
            PlaceManager placeManager,
            DefaultLocationProvider defaultLocationProvider,
            CurrentUser currentUser) {
        super(eventBus, view, proxy, StoriesDashboardPresenter.SLOT_MAIN_CONTENT);

        this.userService = userService;
        this.messages = messages;
        this.profileService = profileService;
        this.listStoriesPresenter = listStoriesPresenter;
        this.contactsPresenter = contactsPresenter;
        this.notesManagerPresenter = notesManagerPresenter;
        this.storiesMapPresenter = storiesMapPresenter;
        this.placeManager = placeManager;
        this.defaultLocationProvider = defaultLocationProvider;
        this.currentUser = currentUser;

        getView().setUiHandlers(this);
    }

    @Override
    public StorySearchParameters getStorySearchParameters(int start, int length) {
        StorySearchParameters storySearchParameters =
                StorySearchParameters.byAuthor(start, length, currentUser.getCurrentProfileId(),
                        AuthConstants.ACCESS_MODE_EXPLICIT);
        storySearchParameters.setSearchToken(searchToken);
        SortField sort = getView().getSort();
        storySearchParameters.setSortField(sort);

        return storySearchParameters;
    }

    @Override
    public void onRowCountChange(RowCountChangeEvent event) {
        getView().setStoriesCount(event.getNewRowCount());
    }

    @Override
    public void changeAccountPassword(String currentPassword, String newPassword) {
        userService.updatePassword(currentPassword, newPassword, new ResponseHandlerLoader<ActionResponse>() {
            @Override
            public void handleSuccess(ActionResponse result) {
                if (result.getGlobalErrorMessages().size() == 0) {
                    messageDispatcher.displayMessage(MessageStyle.SUCCESS, messages.passwordSaved());
                    getView().clearPassword();
                } else {
                    messageDispatcher.displayMessage(MessageStyle.ERROR, result.getGlobalErrorMessages().get(0));
                }

                getView().setPasswordUpdateButtonEnabled(true);
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);

                getView().setPasswordUpdateButtonEnabled(true);
            }
        });
    }

    @Override
    public void saveAccountSettings(final User editedUser) {
        User user = currentUser.getUser();
        user.setHandle(editedUser.getHandle());
        user.setActive(true);
        updateUserAccount(user);
    }

    @Override
    public void saveProfile(Profile profile) {
        profileService.update(profile, new ResponseHandlerLoader<DatumResponse<ProfileSummary>>() {
            @Override
            public void handleSuccess(DatumResponse<ProfileSummary> result) {
                ProfileSummary updatedProfile = result.getDatum();

                currentUser.getUser().updateProfile(updatedProfile);
                currentUser.setCurrentProfile(updatedProfile.getProfile());
                getView().setupAccountForm(currentUser.getUser(), updatedProfile.getProfile());
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, messages.personSaved());
            }
        });
    }

    @Override
    public void onSortChanged(StorySortField item) {
        SortChangedEvent.fire(this, item);
    }

    @Override
    public void search(String searchText) {
        PlaceRequest.Builder placeRequestBuilder = new PlaceRequest.Builder()
                .nameToken(NameTokens.account)
                .with(ParameterTokens.search, searchText)
                .without(ParameterTokens.page);
        placeManager.revealPlace(placeRequestBuilder.build());
    }

    @Override
    public void switchToList() {
        northEast = null;
        southWest = null;

        clearSlot(SLOT_MAP);
        MapBoundariesChangeEvent.fire(this, northEast, southWest);
    }

    @Override
    public void switchToMap() {
        northEast = defaultLocationProvider.northEast();
        southWest = defaultLocationProvider.southWest();

        setInSlot(SLOT_MAP, storiesMapPresenter);
        MapBoundariesChangeEvent.fire(this, northEast, southWest);
    }

    @Override
    public void onBoundariesChanged(MapBoundariesChangeEvent event) {
        northEast = event.getNorthEast();
        southWest = event.getSouthWest();
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(MapBoundariesChangeEvent.TYPE, this);

        setInSlot(TYPE_SetListStories, listStoriesPresenter);
        setInSlot(TYPE_SetPersonProfil, contactsPresenter);
        setInSlot(TYPE_SetNoteManager, notesManagerPresenter);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        Profile currentProfile = currentUser.getCurrentProfile();
        contactsPresenter.initPresenter(currentProfile.getId());
        notesManagerPresenter.initPresenter(currentProfile);

        getView().setupAccountForm(currentUser.getUser(), currentProfile);
        loadLanguages();
        loadOrganizationLanguages();

        listStoriesPresenter.initPresenter(this);
    }

    @Override
    protected void onReset(PlaceRequest placeRequest) {
        searchToken = placeRequest.getParameter(ParameterTokens.search, "");
        getView().setSearchToken(searchToken);
    }

    private void updateUserAccount(final User user) {
        userService.updateAccount(user, new ResponseHandlerLoader<DatumResponse<User>>() {
            @Override
            public void handleSuccess(DatumResponse<User> result) {
                User user = result.getDatum();
                UserChangedEvent.fire(this, user);
                getView().setupAccountForm(user, currentUser.getCurrentProfile());
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Account updated successfully");
            }
        });
    }

    private void loadLanguages() {
        // TODO : Need to add server side languages infrastructure...
        List<LanguageType> languageTypes = new ArrayList<LanguageType>();
        languageTypes.add(new LanguageType("English", "en", true));
        languageTypes.add(new LanguageType("Spanish", "es", false));
        getView().setupLanguages(languageTypes);
    }

    private void loadOrganizationLanguages() {
        // TODO : Need to add server side languages infrastructure...
        List<LanguageType> languageTypes = new ArrayList<LanguageType>();
        languageTypes.add(new LanguageType("English", "en", true));
        languageTypes.add(new LanguageType("Spanish", "es", false));
        getView().setupOrganizationLanguages(languageTypes);
    }
}
