package org.consumersunion.stories.dashboard.client.application.collections.widget;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.api.TaskService;
import org.consumersunion.stories.common.client.event.CollectionChangedEvent;
import org.consumersunion.stories.common.client.event.InteractiveMessageEvent;
import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.RpcEntityServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.CollectionSurveyI15dResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.util.CurrentUser;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.client.widget.messages.MessageFactory;
import org.consumersunion.stories.common.shared.ExportContainer;
import org.consumersunion.stories.common.shared.ExportKind;
import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;
import org.consumersunion.stories.common.shared.dto.tasks.TasksFactory;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionObserver;
import org.consumersunion.stories.dashboard.client.application.collection.popup.SourceOrTargetSelectPresenter;
import org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbyquestionnaire
        .CollectionsByQuestionnairePresenter;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.ListQuestionnairePresenter;
import org.consumersunion.stories.dashboard.client.application.widget.TagsPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.TagsPresenterFactory;
import org.consumersunion.stories.dashboard.client.event.CloseCardEvent;
import org.consumersunion.stories.dashboard.client.event.CollectionDeletedEvent;
import org.consumersunion.stories.dashboard.client.event.CreateContentEvent;
import org.consumersunion.stories.dashboard.client.event.DisplayEvent;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;
import org.consumersunion.stories.dashboard.client.event.SavedCollectionEvent;
import org.consumersunion.stories.dashboard.client.event.SavedDocumentEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static org.consumersunion.stories.common.client.util.CachedObjectKeys.OPENED_COLLECTION;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;

public class CollectionItemPresenter extends PresenterWidget<CollectionItemPresenter.MyView>
        implements CollectionItemUiHandlers, TagsPresenter.Handler, ReloadCollectionsEvent.ReloadCollectionsHandler,
        TagsPresenter.SearchTagHandler, CreateContentEvent.CreateNewContentHandler, CloseCardEvent.CloseCardHandler,
        SavedDocumentEvent.SavedDocumentHandler, CollectionObserver.CollectionHandler<Collection> {
    interface MyView extends View, HasUiHandlers<CollectionItemUiHandlers> {
        void init(CollectionData collectionData, String historyToken);

        void updateTagsPosition();

        void setWatched(boolean watched);

        void setForCard(String historyToken);

        void setForDetails();

        void switchToViewMode();

        void saveFailed();

        void saveSuccess(boolean done, boolean isDetails);

        void setEditMode(boolean isEditMode);
    }

    static final Object SLOT_TAGS = new Object();
    static final Object SLOT_SOURCE_OR_TARGET = new Object();

    private final PlaceManager placeManager;
    private final CachingService cachingService;
    private final RpcEntityServiceAsync entityService;
    private final RpcCollectionServiceAsync collectionService;
    private final RpcQuestionnaireServiceAsync questionnaireService;
    private final RpcDocumentServiceAsync documentService;
    private final CollectionsByQuestionnairePresenter collectionsByQuestionnairePresenter;
    private final ListQuestionnairePresenter listQuestionnairePresenter;
    private final SourceOrTargetSelectPresenter sourceOrTargetSelectPresenter;
    private final CommonI18nMessages messages;
    private final StoryTellerDashboardI18nLabels labels;
    private final TagsPresenter tagsPresenter;
    private final CollectionObserver collectionObserver;
    private final MessageFactory messageFactory;
    private final TasksFactory tasksFactory;
    private final CurrentUser currentUser;
    private final ResourceDelegate<TaskService> taskServiceDelegate;
    private final PresenterWidget handler;

    private Collection collection;
    private CollectionData collectionData;
    private boolean isEditMode;
    private boolean isDetails;
    private Request fetchCollectionRequest;

    @Inject
    CollectionItemPresenter(
            EventBus eventBus,
            MyView view,
            PlaceManager placeManager,
            CachingService cachingService,
            RpcEntityServiceAsync entityService,
            RpcCollectionServiceAsync collectionService,
            RpcQuestionnaireServiceAsync questionnaireService,
            RpcDocumentServiceAsync documentService,
            CollectionsByQuestionnairePresenter collectionsByQuestionnairePresenter,
            ListQuestionnairePresenter listQuestionnairePresenter,
            SourceOrTargetSelectPresenter sourceOrTargetSelectPresenter,
            TagsPresenterFactory tagsPresenterFactory,
            CommonI18nMessages messages,
            StoryTellerDashboardI18nLabels labels,
            CollectionObserver collectionObserver,
            MessageFactory messageFactory,
            TasksFactory tasksFactory,
            CurrentUser currentUser,
            ResourceDelegate<TaskService> taskServiceDelegate,
            @Assisted PresenterWidget handler,
            @Assisted CollectionData collectionData) {
        super(eventBus, view);

        this.cachingService = cachingService;
        this.entityService = entityService;
        this.collectionService = collectionService;
        this.questionnaireService = questionnaireService;
        this.documentService = documentService;
        this.collectionsByQuestionnairePresenter = collectionsByQuestionnairePresenter;
        this.listQuestionnairePresenter = listQuestionnairePresenter;
        this.sourceOrTargetSelectPresenter = sourceOrTargetSelectPresenter;
        this.messages = messages;
        this.labels = labels;
        this.collectionObserver = collectionObserver;
        this.messageFactory = messageFactory;
        this.tasksFactory = tasksFactory;
        this.currentUser = currentUser;
        this.taskServiceDelegate = taskServiceDelegate;
        this.handler = handler;
        this.collectionData = collectionData;
        this.collection = collectionData.getCollection();
        this.placeManager = placeManager;
        tagsPresenter = tagsPresenterFactory.create(this);

        getView().setUiHandlers(this);
        getView().switchToViewMode();

        init(collectionData);
    }

    @Override
    public void onCardClosed(CloseCardEvent event) {
        Object object = event.getObject();
        if (object instanceof CollectionData) {
            CollectionData data = (CollectionData) object;
            if (data.getId() == collectionData.getId()) {
                collectionData = data;
                redraw();
            }
        }
    }

    @Override
    public void closeCard() {
        cachingService.remove(OPENED_COLLECTION);
        PlaceRequest place = new PlaceRequest.Builder().nameToken(NameTokens.collections).build();
        CloseCardEvent.fire(this, StoriesDashboardPresenter.SLOT_MAIN_CONTENT, handler, place, collectionData);
    }

    @Override
    public void openCard() {
        PlaceRequest place = getCollectionDetailsPlaceRequestBuilder().build();
        placeManager.revealRelativePlace(place);
    }

    @Override
    public void onTagAdded(String token) {
    }

    @Override
    public void onTagRemoved(String token) {
        getView().updateTagsPosition();
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
    public void onCreateNewContent(CreateContentEvent event) {
        if (isEditMode) {
            Window.alert("CreateContentEvent in edit mode");
        } else {
            fetchCollectionData(null);
        }
    }

    @Override
    public void onSearchTag(String tag) {
        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.collections)
                .with(ParameterTokens.search, "tag:(" + tag + ")")
                .build();

        placeManager.revealPlace(placeRequest);
    }

    @Override
    public void deleteCollection() {
        collection.setDeleted(true);
        collectionService.updateCollection(collection, new ResponseHandlerLoader<DatumResponse<Collection>>() {
            @Override
            public void handleSuccess(DatumResponse<Collection> result) {
                String message = collection.isQuestionnaire() ?
                        messages.questionnaireDeleted() : messages.collectionDeleted();
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, message);
                onCollectionDeleted(result);
            }
        });
    }

    @Override
    public void export(ExportKind value) {
        ExportContainer container =
                collection.isQuestionnaire() ? ExportContainer.QUESTIONNAIRE : ExportContainer.COLLECTION;
        int profileId = currentUser.getCurrentProfileId();

        ExportTask task = tasksFactory.createExportTask(profileId, value, container, collection.getId());
        taskServiceDelegate.withCallback(new AsyncCallback<ExportTask>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ExportTask result) {
                CollectionSummary collectionSummary = new CollectionSummary(collection);
                InteractiveMessageEvent.fire(CollectionItemPresenter.this,
                        messageFactory.createExportMessage(result.getId(), collectionSummary));
            }
        }).createTask(task);
    }

    @Override
    public void enableTagsEdit() {
        tagsPresenter.startEdit();
    }

    @Override
    public void addSourceOrTarget() {
        sourceOrTargetSelectPresenter.initialize(collection);
        addToPopupSlot(sourceOrTargetSelectPresenter, true);
    }

    @Override
    public void watchCollection(boolean enable) {
        if (enable) {
            entityService.startWatching(collection.getId(), NotificationTrigger.STORY_ADDED,
                    new ResponseHandlerLoader<ActionResponse>() {
                        @Override
                        public void handleSuccess(ActionResponse result) {
                            messageDispatcher.displayMessage(MessageStyle.SUCCESS, messages.notificationsOn());
                        }
                    });
        } else {
            entityService.stopWatching(collection.getId(), NotificationTrigger.STORY_ADDED,
                    new ResponseHandlerLoader<ActionResponse>() {
                        @Override
                        public void handleSuccess(ActionResponse result) {
                            messageDispatcher.displayMessage(MessageStyle.SUCCESS, messages.notificationsOff());
                        }
                    });
        }
    }

    public void init(CollectionData collectionData) {
        loadSourceOrTargetCollections();
        tagsPresenter.setTags(collection, collectionData.getTags());
        getView().init(collectionData, placeManager.buildHistoryToken(getDetailsPlace()));
        getView().setWatched(!collectionData.getSubscriptionsForActiveProfile().isEmpty());
    }

    /**
     * Creates a Questionnaire from an existing Questionnaire. Although this method could be called with a Collection,
     * only a Questionnaire should be used.
     *
     * @param questionnaireData The data for a Questionnaire
     */
    @Override
    public void copyQuestionnaire(CollectionContent questionnaireData) {
        questionnaireService.copyQuestionnaire(collection.getId(), questionnaireData.getTitle(),
                questionnaireData.getSummary(), questionnaireData.getLocale(),
                new ResponseHandlerLoader<CollectionSurveyI15dResponse>() {
                    @Override
                    public void handleSuccess(CollectionSurveyI15dResponse result) {
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, messages.questionnaireCopied());
                        getView().switchToViewMode();
                        ReloadCollectionsEvent.fire(CollectionItemPresenter.this, collection);
                    }
                });
    }

    @Override
    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    @Override
    public void onDocumentSaved(SavedDocumentEvent event) {
        fetchCollectionData(null);
    }

    @Override
    public void cancelEdit() {
        initWithCurrentData();
    }

    @Override
    public void save(final boolean done) {
        documentService.saveDocument(collection.getBodyDocument(),
                new ResponseHandlerLoader<DatumResponse<Document>>() {
                    @Override
                    public void handleSuccess(DatumResponse<Document> result) {
                        fetchCollectionData(new AsyncCallback<DatumResponse<CollectionData>>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                getView().saveFailed();
                            }

                            @Override
                            public void onSuccess(DatumResponse<CollectionData> result) {
                                String message = collection.isQuestionnaire() ?
                                        labels.questionnaireSaved() : labels.collectionSaved();
                                messageDispatcher.displayMessage(MessageStyle.SUCCESS, message);
                                onCollectionSaved(result, done);
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
    public void updateBodyDocument(CollectionContent data) {
        Document bodyDocument = collection.getBodyDocument();
        bodyDocument.setTitle(data.getTitle());
        bodyDocument.setSummary(data.getSummary());
        bodyDocument.setLocale(data.getLocale());
    }

    @Override
    public void goToCollectionDetails() {
        placeManager.revealRelativePlace(getDetailsPlace());
    }

    public void setDetails(boolean isDetails) {
        this.isDetails = isDetails;
    }

    @Override
    public void onReloadCollections(ReloadCollectionsEvent event) {
        Collection sourceCollection = event.getSourceCollection();
        if (sourceCollection != null && sourceCollection.getId() == collection.getId()) {
            collection = sourceCollection;
        }
    }

    @Override
    public void onCollectionSaved(SavedCollectionEvent event) {
        collection = event.get();
    }

    @Override
    public void onDisplay(DisplayEvent<Collection> event) {
        collection = event.get();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        if (isVisible()) {
            redraw();
        }

        if (isDetails) {
            tagsPresenter.setSearchTagHandler(this);
            addVisibleHandler(CreateContentEvent.TYPE, this);
            getView().setForDetails();
        } else {
            tagsPresenter.setSearchTagHandler(null);
            String historyToken = placeManager.buildHistoryToken(
                    getCollectionDetailsPlaceRequestBuilder().build());
            getView().setForCard(historyToken);
        }

        if (isEditMode) {
            doEdit();
        }
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(SavedDocumentEvent.TYPE, this);
        addVisibleHandler(CloseCardEvent.TYPE, this);
        addVisibleHandler(ReloadCollectionsEvent.TYPE, this);

        setInSlot(SLOT_TAGS, tagsPresenter);

        if (collection.isQuestionnaire()) {
            setInSlot(SLOT_SOURCE_OR_TARGET, collectionsByQuestionnairePresenter);
        } else {
            setInSlot(SLOT_SOURCE_OR_TARGET, listQuestionnairePresenter);
        }
    }

    @Override
    protected void onUnbind() {
        super.onUnbind();

        collectionObserver.unregister(this);
    }

    private void onCollectionSaved(DatumResponse<CollectionData> result, boolean done) {
        CollectionChangedEvent.fire(this, result.getDatum().getCollection());

        getView().saveSuccess(done, isDetails);
    }

    private void redraw() {
        initWithCurrentData();

        tagsPresenter.setTags(collection, collectionData.getTags());

        if (isDetails) {
            getView().setForDetails();
        }
    }

    private void doEdit() {
        getView().setEditMode(isEditMode);
    }

    private void initWithCurrentData() {
        getView().init(collectionData, placeManager.buildHistoryToken(getDetailsPlace()));
    }

    private PlaceRequest.Builder getCollectionDetailsPlaceRequestBuilder() {
        return new PlaceRequest.Builder().nameToken(NameTokens.collection)
                .with(ParameterTokens.id, String.valueOf(collection.getId()))
                .with(ParameterTokens.editMode, String.valueOf(isEditMode));
    }

    private void loadSourceOrTargetCollections() {
        if (collection.isQuestionnaire()) {
            List<CollectionSummary> filtered = new ArrayList<CollectionSummary>();
            List<CollectionSummary> targetCollections = collectionData.getTargetCollections();
            for (CollectionSummary cs : targetCollections) {
                if (!cs.isDeleted()) {
                    filtered.add(cs);
                }
            }
            collectionsByQuestionnairePresenter.init(collection, filtered);
            fetchCollectionData(null);
        } else {
            listQuestionnairePresenter.initPresenter(collection, collectionData.getSourceQuestionnaires());
        }
    }

    private PlaceRequest getDetailsPlace() {
        return new PlaceRequest.Builder()
                .nameToken(NameTokens.collection)
                .with(ParameterTokens.id, String.valueOf(collection.getId()))
                .build();
    }

    private void onCollectionDeleted(DatumResponse<Collection> result) {
        CollectionDeletedEvent.fire(this, result.getDatum());
        if (isDetails) {
            closeCard();
        }
    }

    private void fetchCollectionData(final AsyncCallback<DatumResponse<CollectionData>> callback) {
        if (fetchCollectionRequest == null || fetchCollectionRequest.isPending()) {
            fetchCollectionRequest = collectionService.getCollection(collection.getId(), ROLE_CURATOR,
                    new ResponseHandler<DatumResponse<CollectionData>>() {
                        @Override
                        public void handleSuccess(DatumResponse<CollectionData> result) {
                            collectionData = result.getDatum();
                            collection = collectionData.getCollection();
                            if (isDetails) {
                                cachingService.putCachedObject(CollectionData.class, OPENED_COLLECTION, collectionData);
                                collectionObserver.display(collection);
                            }
                            redraw();

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
    }
}
