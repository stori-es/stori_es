package org.consumersunion.stories.dashboard.client.application.profile;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.ReloadStoriesEvent;
import org.consumersunion.stories.common.client.event.SearchEvent;
import org.consumersunion.stories.common.client.event.SortChangedEvent;
import org.consumersunion.stories.common.client.model.StorySortFieldDropDownItem;
import org.consumersunion.stories.common.client.service.RpcProfileServiceAsync;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.ui.stories.ListStoriesPresenter;
import org.consumersunion.stories.common.client.ui.stories.StoriesListHandler;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.SortDropDownItem;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.profile.widget.ContactsPresenter;
import org.consumersunion.stories.dashboard.client.application.profile.widget.NotesManagerPresenter;
import org.consumersunion.stories.dashboard.client.application.stories.widget.StoryCard;
import org.consumersunion.stories.dashboard.client.application.story.widget.AttachmentsPresenter;
import org.consumersunion.stories.dashboard.client.place.InvalidPlaceHelper;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.gwt.view.client.RowCountChangeEvent;
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

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;

public class ProfilePresenter extends Presenter<ProfilePresenter.MyView, ProfilePresenter.MyProxy>
        implements ProfileUiHandlers, StoriesListHandler {
    interface MyView extends View, HasUiHandlers<ProfileUiHandlers> {
        void displayProfile(Profile profile);

        StorySortFieldDropDownItem getSort();

        void setStoriesCount(Integer count);

        void setSearchToken(String searchToken);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.profile)
    interface MyProxy extends ProxyPlace<ProfilePresenter> {
    }

    static final Object SLOT_PROFILE = new Object();
    static final Object SLOT_NOTES = new Object();
    static final Object SLOT_STORIES = new Object();
    static final Object SLOT_ATTACHMENTS = new Object();

    private final PlaceManager placeManager;
    private final InvalidPlaceHelper invalidPlaceHelper;
    private final RpcProfileServiceAsync profileService;
    private final ContactsPresenter contactsPresenter;
    private final ListStoriesPresenter listStoriesPresenter;
    private final AttachmentsPresenter attachmentsPresenter;
    private final NotesManagerPresenter notesManagerPresenter;

    private Profile currentProfile;
    private String searchToken;
    private Integer personId;

    @Inject
    ProfilePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            InvalidPlaceHelper invalidPlaceHelper,
            RpcProfileServiceAsync profileService,
            ContactsPresenter contactsPresenter,
            @StoryCard ListStoriesPresenter listStoriesPresenter,
            AttachmentsPresenter attachmentsPresenter,
            NotesManagerPresenter notesManagerPresenter) {
        super(eventBus, view, proxy, StoriesDashboardPresenter.SLOT_MAIN_CONTENT);

        this.placeManager = placeManager;
        this.invalidPlaceHelper = invalidPlaceHelper;
        this.profileService = profileService;
        this.contactsPresenter = contactsPresenter;
        this.listStoriesPresenter = listStoriesPresenter;
        this.attachmentsPresenter = attachmentsPresenter;
        this.notesManagerPresenter = notesManagerPresenter;

        getView().setUiHandlers(this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        Integer newPersonId = Integer.valueOf(request.getParameter(ParameterTokens.id, null));

        if (newPersonId == null) {
            invalidPlaceHelper.handle(NameTokens.stories);
        } else if (!newPersonId.equals(personId)) {
            personId = newPersonId;

            searchToken = request.getParameter(ParameterTokens.search, "");
            getView().setSearchToken(searchToken);

            profileService.retrieveProfile(personId, new ResponseHandler<DatumResponse<ProfileSummary>>() {
                @Override
                public void handleSuccess(DatumResponse<ProfileSummary> result) {
                    currentProfile = result.getDatum().getProfile();

                    setupPresenters();
                    getView().displayProfile(currentProfile);
                }

                @Override
                public void onFailure(Throwable e) {
                    super.onFailure(e);

                    getProxy().manualRevealFailed();
                }
            });
        } else {
            getProxy().manualReveal(this);
        }
    }

    @Override
    public void onSortChanged(StorySortField item) {
        SortChangedEvent.fire(this, item);
    }

    @Override
    public void search(String searchText) {
        PlaceRequest.Builder placeRequestBuilder = new PlaceRequest.Builder()
                .nameToken(NameTokens.profile)
                .with(ParameterTokens.id, String.valueOf(currentProfile.getId()))
                .with(ParameterTokens.search, searchText);
        placeManager.revealPlace(placeRequestBuilder.build());

        this.searchToken = searchText;

        SearchEvent.fire(this, searchText, (SortDropDownItem) getView().getSort());
    }

    @Override
    public StorySearchParameters getStorySearchParameters(int start, int length) {
        StorySearchParameters storySearchParameters =
                StorySearchParameters.byAuthor(start, length, currentProfile.getId(), ACCESS_MODE_EXPLICIT);
        storySearchParameters.setSearchToken(searchToken);
        SortField sort = getView().getSort().getSortField();
        storySearchParameters.setSortField(sort);

        return storySearchParameters;
    }

    @Override
    public void savePerson(Profile profile) {
        profileService.update(profile, new ResponseHandlerLoader<DatumResponse<ProfileSummary>>() {
            @Override
            public void handleSuccess(DatumResponse<ProfileSummary> result) {
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Personal information was saved successfully.");
                getView().displayProfile(result.getDatum().getProfile());
                ReloadStoriesEvent.fire(ProfilePresenter.this);
            }
        });
    }

    @Override
    public void onRowCountChange(RowCountChangeEvent event) {
        getView().setStoriesCount(event.getNewRowCount());
    }

    @Override
    public boolean useManualReveal() {
        return true;
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        HideLoadingEvent.fire(this);
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(SLOT_PROFILE, contactsPresenter);
        setInSlot(SLOT_NOTES, notesManagerPresenter);
        setInSlot(SLOT_STORIES, listStoriesPresenter);
        setInSlot(SLOT_ATTACHMENTS, attachmentsPresenter);
    }

    private void setupPresenters() {
        contactsPresenter.initPresenter(currentProfile.getId());
        notesManagerPresenter.initPresenter(currentProfile);
        listStoriesPresenter.initPresenter(this);
        attachmentsPresenter.initPresenter(currentProfile);

        getProxy().manualReveal(this);
    }
}
