package org.consumersunion.stories.dashboard.client.application.collection;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.dashboard.client.application.AbstractStoriesPresenter;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.collection.widget.StoriesWidgetPresenter;
import org.consumersunion.stories.dashboard.client.application.collection.widget.SummaryWidgetPresenter;
import org.consumersunion.stories.dashboard.client.application.collection.widget.navbar.NavigationBarPresenter;
import org.consumersunion.stories.dashboard.client.application.collections.widget.CollectionItemPresenter;
import org.consumersunion.stories.dashboard.client.application.collections.widget.CollectionItemPresenterFactory;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.BuilderPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.DocumentsListPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.content.DocumentCardFactory;
import org.consumersunion.stories.dashboard.client.event.AddNewDocumentEvent;
import org.consumersunion.stories.dashboard.client.event.CreateContentEvent;
import org.consumersunion.stories.dashboard.client.event.NavigationBarSelectionEvent;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;
import org.consumersunion.stories.dashboard.client.place.InvalidPlaceHelper;
import org.consumersunion.stories.dashboard.client.util.NavigationBarElement;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static org.consumersunion.stories.common.client.util.CachedObjectKeys.OPENED_COLLECTION;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ATTACHMENT;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.CONTENT;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.NOTE;

/**
 * Presenter for the Collection detail page.
 */
public class CollectionPresenter
        extends AbstractStoriesPresenter<CollectionPresenter.MyView, CollectionPresenter.MyProxy>
        implements ReloadCollectionsEvent.ReloadCollectionsHandler,
        NavigationBarSelectionEvent.NavigationBarSelectionHandler, AddNewDocumentEvent.AddNewDocumentHandler,
        CreateContentEvent.CreateNewContentHandler {
    interface MyView extends View {
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.collection)
    interface MyProxy extends ProxyPlace<CollectionPresenter> {
    }

    static final Object SLOT_CARD = new Object();
    static final Object SLOT_NAV_BAR = new Object();
    static final Object SLOT_NAV_CONTENT = new Object();
    static final Object SLOT_NEW_CONTENT = new Object();

    private final CachingService cachingService;
    private final CollectionObserver collectionObserver;
    private final DocumentCardFactory documentCardFactory;
    private final DocumentsListPresenter documentsListPresenter;
    private final BuilderPresenter builderPresenter;

    private final RpcCollectionServiceAsync collectionService;
    private final RpcQuestionnaireServiceAsync questionnaireService;
    private final PlaceManager placeManager;
    private final InvalidPlaceHelper invalidPlaceHelper;
    private final CollectionItemPresenterFactory collectionItemFactory;
    private final NavigationBarPresenter navigationBarPresenter;
    private final SummaryWidgetPresenter summaryWidgetPresenter;
    private final StoriesWidgetPresenter storiesWidgetPresenter;

    private Integer collectionId;
    private CollectionItemPresenter collectionItemPresenter;

    @Inject
    CollectionPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            CachingService cachingService,
            CollectionObserver collectionObserver,
            DocumentCardFactory documentCardFactory,
            DocumentsListPresenter documentsListPresenter,
            BuilderPresenter builderPresenter,
            RpcCollectionServiceAsync collectionService,
            RpcQuestionnaireServiceAsync questionnaireService,
            PlaceManager placeManager,
            InvalidPlaceHelper invalidPlaceHelper,
            CollectionItemPresenterFactory collectionItemFactory,
            NavigationBarPresenter navigationBarPresenter,
            SummaryWidgetPresenter summaryWidgetPresenter,
            StoriesWidgetPresenter storiesWidgetPresenter) {
        super(eventBus, view, proxy, StoriesDashboardPresenter.SLOT_MAIN_CONTENT);

        this.cachingService = cachingService;
        this.collectionObserver = collectionObserver;
        this.documentCardFactory = documentCardFactory;
        this.documentsListPresenter = documentsListPresenter;
        this.builderPresenter = builderPresenter;
        this.collectionService = collectionService;
        this.questionnaireService = questionnaireService;
        this.placeManager = placeManager;
        this.invalidPlaceHelper = invalidPlaceHelper;
        this.collectionItemFactory = collectionItemFactory;
        this.navigationBarPresenter = navigationBarPresenter;
        this.summaryWidgetPresenter = summaryWidgetPresenter;
        this.storiesWidgetPresenter = storiesWidgetPresenter;
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        Integer newCollectionId = Integer.valueOf(request.getParameter(ParameterTokens.id, "-1"));

        if (newCollectionId == -1) {
            invalidPlaceHelper.handle(NameTokens.collections);
        } else if (!newCollectionId.equals(collectionId)) {
            documentsListPresenter.clear();
            collectionId = newCollectionId;

            storiesWidgetPresenter.prepareFromRequest(request);

            CollectionData cachedCollection = getCachedCollection(collectionId);
            if (cachedCollection == null) {
                loadCollection(true);
            } else {
                onCollectionLoaded(true, cachedCollection);
            }

            switchTo(NavigationBarElement.SUMMARY);
        } else {
            doManualReveal();
        }
    }

    @Override
    public void onAddNewDocument(AddNewDocumentEvent event) {
        createNewDocument(event.getEntityRelation());
    }

    @Override
    public void onCreateNewContent(CreateContentEvent event) {
        loadCollection(false);
        clearSlot(SLOT_NEW_CONTENT);
    }

    @Override
    public void onNavigationBarSelection(NavigationBarSelectionEvent event) {
        if (collectionId != null) {
            loadCollection(false);
            switchTo(event.getElement());
        }
    }

    @Override // from ReloadCollectionsEvent.ReloadCollectionsHandler
    public void onReloadCollections(ReloadCollectionsEvent event) {
        if (event.getSourceCollection() != null) {
            loadCollection(false);
        }
    }

    @Override
    public boolean useManualReveal() {
        return true;
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        PlaceRequest currentPlaceRequest = placeManager.getCurrentPlaceRequest();
        String idParameter = currentPlaceRequest.getParameter(ParameterTokens.id, String.valueOf(collectionId));
        collectionId = Integer.valueOf(idParameter);

        HideLoadingEvent.fire(this);
    }

    @Override
    protected void onHide() {
        super.onHide();

        collectionId = null;

        storiesWidgetPresenter.onHide();
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(ReloadCollectionsEvent.TYPE, this);
        addVisibleHandler(NavigationBarSelectionEvent.TYPE, this);
        addVisibleHandler(AddNewDocumentEvent.TYPE, this);
        addVisibleHandler(CreateContentEvent.TYPE, this);

        setInSlot(SLOT_NAV_BAR, navigationBarPresenter);
    }

    private void switchTo(NavigationBarElement element) {
        navigationBarPresenter.select(element);
        switch (element) {
            case SUMMARY:
                setInSlot(SLOT_NAV_CONTENT, summaryWidgetPresenter);
                break;
            case STORIES:
                setInSlot(SLOT_NAV_CONTENT, storiesWidgetPresenter);
                break;
            case BUILDER:
                setInSlot(SLOT_NAV_CONTENT, builderPresenter);
                break;
            case DOCUMENTS:
                setInSlot(SLOT_NAV_CONTENT, documentsListPresenter);
                break;
        }
    }

    private void createNewDocument(SystemEntityRelation entityRelation) {
        setInSlot(SLOT_NEW_CONTENT, documentCardFactory.createAddContent(collectionObserver.getCollection(),
                entityRelation));
    }

    private CollectionData getCachedCollection(Integer collectionId) {
        CollectionData collectionData = cachingService.getCachedObject(CollectionData.class, OPENED_COLLECTION);
        if (collectionData == null || collectionData.getId() == collectionId) {
            return collectionData;
        } else {
            return null;
        }
    }

    private void loadCollection(final boolean reveal) {
        final AsyncCallback<DatumResponse<CollectionData>> loadCallback = new
                AsyncCallback<DatumResponse<CollectionData>>() {
                    @Override
                    public void onSuccess(DatumResponse<CollectionData> result) {
                        onCollectionLoaded(reveal, result.getDatum());
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        getProxy().manualRevealFailed();
                    }
                };

        AsyncCallback<DatumResponse<CollectionData>> callback = createResponseCallback(reveal, loadCallback);

        collectionService.getCollection(collectionId, ROLE_READER, callback);
    }

    private void onCollectionLoaded(final boolean reveal, final CollectionData collectionData) {
        cachingService.putCachedObject(CollectionData.class, OPENED_COLLECTION, collectionData);

        Collection collection = collectionData.getCollection();

        summaryWidgetPresenter.init(collectionData);
        builderPresenter.initPresenter(collection.isQuestionnaire());
        documentsListPresenter.setDocuments(collectionData.getDocuments(), CONTENT, NOTE, ATTACHMENT);

        if (collection.isQuestionnaire()) {
            questionnaireService.getQuestionnaireI15d(collectionId,
                    new ResponseHandler<DatumResponse<QuestionnaireI15d>>() {
                        @Override
                        public void handleSuccess(DatumResponse<QuestionnaireI15d> result) {
                            collectionObserver.display(result.getDatum());
                            handleReveal(reveal, collectionData);
                        }
                    });
        } else {
            collectionObserver.display(collection);
            handleReveal(reveal, collectionData);
        }
    }

    private void handleReveal(boolean reveal, CollectionData collectionData) {
        if (reveal) {
            setMainSlot(collectionData);
            doManualReveal();
        } else {
            collectionItemPresenter.init(collectionData);
        }
    }

    private void setMainSlot(CollectionData collectionData) {
        collectionItemPresenter = collectionItemFactory.create(this, collectionData);
        collectionItemPresenter.setDetails(true);

        String editMode = placeManager.getCurrentPlaceRequest().getParameter(ParameterTokens.editMode, "false");
        collectionItemPresenter.setEditMode(Boolean.valueOf(editMode));

        collectionObserver.register(collectionItemPresenter);

        setInSlot(SLOT_CARD, collectionItemPresenter);
    }

    private void doManualReveal() {
        getProxy().manualReveal(this);
        revealInParent();
    }

    private AsyncCallback<DatumResponse<CollectionData>> createResponseCallback(
            boolean reveal,
            final AsyncCallback<DatumResponse<CollectionData>> loadCallback) {

        AsyncCallback<DatumResponse<CollectionData>> callback;
        if (reveal) {
            callback = new ResponseHandlerLoader<DatumResponse<CollectionData>>() {
                @Override
                public void handleSuccess(DatumResponse<CollectionData> result) {
                    loadCallback.onSuccess(result);
                }

                @Override
                public void onFailure(Throwable e) {
                    super.onFailure(e);

                    loadCallback.onFailure(e);
                }
            };
        } else {
            callback = new ResponseHandler<DatumResponse<CollectionData>>() {
                @Override
                public void handleSuccess(DatumResponse<CollectionData> result) {
                    loadCallback.onSuccess(result);
                }

                @Override
                public void onFailure(Throwable e) {
                    super.onFailure(e);

                    loadCallback.onFailure(e);
                }
            };
        }

        return callback;
    }
}
