package org.consumersunion.stories.dashboard.client.application.stories.widget;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.RedrawEvent;
import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.CachedObjectKeys;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.util.StoriesJsonEncoderDecoder;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbystory
        .CollectionsByStoryPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.TagsPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.TagsPresenterFactory;
import org.consumersunion.stories.dashboard.client.event.AddNewDocumentEvent;
import org.consumersunion.stories.dashboard.client.event.CloseCardEvent;
import org.consumersunion.stories.dashboard.client.event.CreateContentEvent;
import org.consumersunion.stories.dashboard.client.event.SavedDocumentEvent;
import org.consumersunion.stories.dashboard.client.event.StoriesSelectionEvent;
import org.consumersunion.stories.dashboard.client.event.StorySelectedEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class StoryItemPresenter extends PresenterWidget<StoryItemPresenter.MyView>
        implements StoryItemUiHandlers, org.consumersunion.stories.common.client.ui.stories.StoryCard,
        TagsPresenter.Handler, CreateContentEvent.CreateNewContentHandler, SavedDocumentEvent.SavedDocumentHandler,
        CloseCardEvent.CloseCardHandler, TagsPresenter.SearchTagHandler, StoriesSelectionEvent.StoriesSelectionHandler,
        RedrawEvent.RedrawHandler<StorySummary> {
    interface MyView extends View, HasUiHandlers<StoryItemUiHandlers> {
        void init(StorySummary storySummary);

        void updateTagsPosition();

        void setForStoryDetails();

        void setForStoryCard(String historyToken);

        void setEditMode(boolean isEditMode);

        void saveFailed();

        void saveSuccess(boolean done);

        void showCheckbox(boolean showCheckbox);
    }

    static final Object SLOT_COLLECTIONS = new Object();
    static final Object SLOT_TAGS = new Object();

    private final PlaceManager placeManager;
    private final StoriesJsonEncoderDecoder jsonEncoderDecoder;
    private final CachingService cachingService;
    private final StoryTellerDashboardI18nLabels labels;
    private final PresenterWidget handler;
    private final TagsPresenter tagsPresenter;
    private final CollectionsByStoryPresenter collectionsByStoryPresenter;
    private final RpcStoryServiceAsync storyService;

    private StorySummary storySummary;
    private StorySummary editedStorySummary;
    private boolean isStoryDetails;
    private boolean isEditMode;

    @Inject
    StoryItemPresenter(
            EventBus eventBus,
            MyView view,
            PlaceManager placeManager,
            StoriesJsonEncoderDecoder jsonEncoderDecoder,
            TagsPresenterFactory tagsPresenterFactory,
            CachingService cachingService,
            CollectionsByStoryPresenter collectionsByStoryPresenter,
            RpcStoryServiceAsync storyService,
            StoryTellerDashboardI18nLabels labels,
            @Assisted PresenterWidget handler,
            @Assisted StorySummary storySummary) {
        super(eventBus, view);

        this.placeManager = placeManager;
        this.jsonEncoderDecoder = jsonEncoderDecoder;
        this.cachingService = cachingService;
        this.labels = labels;
        this.handler = handler;
        this.tagsPresenter = tagsPresenterFactory.create(this);
        this.collectionsByStoryPresenter = collectionsByStoryPresenter;
        this.storyService = storyService;
        this.storySummary = storySummary;

        getView().setUiHandlers(this);

        this.collectionsByStoryPresenter.initPresenter(storySummary);
        onStorySummaryFetched();
    }

    @Override
    public void redraw() {
        Story story = storySummary.getStory();

        getView().init(storySummary);

        tagsPresenter.setTags(story, storySummary.getTags());
    }

    @Override
    public StorySummary getStorySummary() {
        return storySummary;
    }

    @Override
    public void onCreateNewContent(CreateContentEvent event) {
        if (isEditMode) {
            Document document = event.getDocument();
            Integer defaultContent = editedStorySummary.getStory().getDefaultContent();

            if (SystemEntityRelation.BODY.equals(document.getSystemEntityRelation()) && defaultContent == null) {
                editedStorySummary.getStory().setDefaultContent(document.getId());
                editedStorySummary.setTitle(document.getTitle());
                editedStorySummary.setSummary(document.getFirstContent());

                updateEditedView();
            }
        }
        fetchStorySummary(null);
    }

    public void setStoryDetails(boolean isStoryDetails) {
        this.isStoryDetails = isStoryDetails;
    }

    @Override
    public void onTagAdded(String token) {
        storySummary.getTags().add(token);
        editedStorySummary.getTags().add(token);
    }

    @Override
    public void onTagRemoved(String token) {
        storySummary.getTags().remove(token);
        editedStorySummary.getTags().remove(token);
    }

    @Override
    public void onSearchTag(String tag) {
        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.stories)
                .with(ParameterTokens.search, "tag:(" + tag + ")")
                .build();

        placeManager.revealPlace(placeRequest);
    }

    @Override
    public void onEditComplete() {
        getView().updateTagsPosition();
    }

    @Override
    public void onTagsReceived() {
        getView().updateTagsPosition();
    }

    @Override
    public void enableTagsEdit() {
        tagsPresenter.startEdit();
    }

    @Override
    public void onCardClosed(CloseCardEvent event) {
        Object object = event.getObject();
        if (object instanceof StorySummary) {
            StorySummary summary = (StorySummary) object;
            if (summary.getStoryId() == storySummary.getStoryId()) {
                storySummary = summary;
                redraw();
            }
        }
    }

    @Override
    public void closeCard() {
        cachingService.remove(CachedObjectKeys.OPENED_CONTENT);
        PlaceRequest place = new PlaceRequest.Builder().nameToken(NameTokens.stories).build();
        CloseCardEvent.fire(this, StoriesDashboardPresenter.SLOT_MAIN_CONTENT, handler, place, storySummary);
    }

    @Override
    public void openCard(boolean addContent) {
        cachingService.putCachedObject(StorySummary.class, CachedObjectKeys.OPENED_STORY,
                editedStorySummary == null ? storySummary : editedStorySummary);

        PlaceRequest place = getStoryDetailsPlaceRequestBuilder(addContent, true).build();
        placeManager.revealRelativePlace(place);
    }

    @Override
    public void onRedraw(RedrawEvent<StorySummary> event) {
        getView().updateTagsPosition();

        StorySummary data = event.getData();
        if (data != null && data.getStoryId() == storySummary.getStoryId()) {
            storySummary = data;
            onStorySummaryFetched();
        }
    }

    @Override
    public void save(final boolean done) {
        storyService.updateStory(editedStorySummary.getStory(), new ResponseHandlerLoader<DatumResponse<Story>>() {
            @Override
            public void handleSuccess(DatumResponse<Story> result) {
                fetchStorySummary(new AsyncCallback<DatumResponse<StorySummary>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        getView().saveFailed();
                    }

                    @Override
                    public void onSuccess(DatumResponse<StorySummary> result) {
                        getView().saveSuccess(done);
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, labels.storySaved());
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);

                getView().saveFailed();
            }
        });
    }

    @Override
    public void addContent() {
        if (isStoryDetails) {
            AddNewDocumentEvent.fire(this, SystemEntityRelation.BODY);
        } else {
            openCard(true);
        }
    }

    @Override
    public void removeDefaultContent() {
        Story story = editedStorySummary.getStory();
        story.setDefaultContent(null);
        updateEditedView();
    }

    @Override
    public void cancelEdit() {
        getView().init(storySummary);
    }

    @Override
    public void edit() {
        isEditMode = true;
        storyService.getStorySummary(storySummary.getStoryId(),
                new ResponseHandlerLoader<DatumResponse<StorySummary>>() {
                    @Override
                    public void handleSuccess(DatumResponse<StorySummary> result) {
                        doEdit(result.getDatum());
                    }
                });
    }

    @Override
    public void setDefaultContent(@Nullable Document document) {
        editedStorySummary.getStory().setDefaultContent(document == null ? null : document.getId());
    }

    @Override
    public void onStorySelected() {
        StorySelectedEvent.fire(this, storySummary.getStory());
    }

    @Override
    public void onStoriesSelected(StoriesSelectionEvent event) {
        getView().showCheckbox(event.isWithCheckboxes());
    }

    @Override
    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    @Override
    public void onDocumentSaved(SavedDocumentEvent event) {
        fetchStorySummary(null);
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(SLOT_COLLECTIONS, collectionsByStoryPresenter);
        setInSlot(SLOT_TAGS, tagsPresenter);

        addVisibleHandler(SavedDocumentEvent.TYPE, this);
        addVisibleHandler(CloseCardEvent.TYPE, this);
        addVisibleHandler(StoriesSelectionEvent.TYPE, this);
        addVisibleHandler(RedrawEvent.TYPE, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        if (isStoryDetails) {
            tagsPresenter.setSearchTagHandler(this);
            addVisibleHandler(CreateContentEvent.TYPE, this);
            getView().setForStoryDetails();
        } else {
            tagsPresenter.setSearchTagHandler(null);
            String historyToken = placeManager.buildHistoryToken(
                    getStoryDetailsPlaceRequestBuilder(false, false).build());
            getView().setForStoryCard(historyToken);
        }

        if (isEditMode) {
            StorySummary openedStoryData = cachingService.getCachedObject(StorySummary.class,
                    CachedObjectKeys.OPENED_STORY);
            doEdit(openedStoryData);
        }
    }

    private PlaceRequest.Builder getStoryDetailsPlaceRequestBuilder(boolean addContent, boolean fromCard) {
        return new PlaceRequest.Builder().nameToken(NameTokens.story)
                .with(ParameterTokens.id, String.valueOf(storySummary.getStoryId()))
                .with(ParameterTokens.addContent, String.valueOf(addContent))
                .with(ParameterTokens.editMode, String.valueOf(isEditMode));
    }

    private void doEdit(StorySummary data) {
        editedStorySummary = data;
        getView().setEditMode(isEditMode);
    }

    private void updateEditedView() {
        getView().init(editedStorySummary);
    }

    private void fetchStorySummary(final AsyncCallback<DatumResponse<StorySummary>> callback) {
        storyService.getStorySummary(storySummary.getStoryId(),
                new ResponseHandlerLoader<DatumResponse<StorySummary>>() {
                    @Override
                    public void handleSuccess(DatumResponse<StorySummary> result) {
                        storySummary = result.getDatum();
                        onStorySummaryFetched();

                        if (callback != null) {
                            callback.onSuccess(result);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        super.onFailure(e);

                        if (callback != null) {
                            callback.onFailure(e);
                        }
                    }
                }
        );
    }

    private void onStorySummaryFetched() {
        editedStorySummary = jsonEncoderDecoder.clone(StorySummary.class, storySummary);
        cachingService.putCachedObject(StorySummary.class, CachedObjectKeys.OPENED_STORY, storySummary);
        redraw();
    }
}
