package org.consumersunion.stories.dashboard.client.application.story;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.ShowLoadingEvent;
import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.ui.stories.StoryItemFactory;
import org.consumersunion.stories.common.client.util.CachedObjectKeys;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.DocumentsContainer;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.stories.widget.StoryCard;
import org.consumersunion.stories.dashboard.client.application.stories.widget.StoryItemPresenter;
import org.consumersunion.stories.dashboard.client.application.story.widget.navbar.NavigationBarPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.DocumentsListPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.content.DocumentCardFactory;
import org.consumersunion.stories.dashboard.client.event.AddNewDocumentEvent;
import org.consumersunion.stories.dashboard.client.event.CreateContentEvent;
import org.consumersunion.stories.dashboard.client.event.StorySummaryLoadedEvent;
import org.consumersunion.stories.dashboard.client.place.InvalidPlaceHelper;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

/**
 * Presents a {@link Story} detail. Generally reached by selecting a {@link Story} in the dashboard, usually by clicking
 * the {@link Story} title.
 */
public class StoryPresenter extends Presenter<StoryPresenter.MyView, StoryPresenter.MyProxy>
        implements AddNewDocumentEvent.AddNewDocumentHandler, CreateContentEvent.CreateNewContentHandler {
    interface MyView extends View {
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.story)
    interface MyProxy extends ProxyPlace<StoryPresenter> {
    }

    static final Object SLOT_STORY = new Object();
    static final Object SLOT_NAV_BAR = new Object();
    static final Object SLOT_NEW_CONTENT = new Object();
    static final Object SLOT_DOCUMENTS = new Object();

    private final CachingService cachingService;
    private final RpcStoryServiceAsync storyService;
    private final InvalidPlaceHelper invalidPlaceHelper;
    private final DocumentsListPresenter documentsListPresenter;
    private final DocumentCardFactory documentCardFactory;
    private final NavigationBarPresenter navigationBarPresenter;
    private final StoryItemFactory storyItemFactory;
    private final PlaceManager placeManager;

    private Story currentStory;

    @Inject
    StoryPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            InvalidPlaceHelper invalidPlaceHelper,
            DocumentsListPresenter documentsListPresenter,
            DocumentCardFactory documentCardFactory,
            @StoryCard StoryItemFactory storyItemFactory,
            CachingService cachingService,
            RpcStoryServiceAsync storyService,
            NavigationBarPresenter navigationBarPresenter) {
        super(eventBus, view, proxy, StoriesDashboardPresenter.SLOT_MAIN_CONTENT);

        this.placeManager = placeManager;
        this.invalidPlaceHelper = invalidPlaceHelper;
        this.documentsListPresenter = documentsListPresenter;
        this.documentCardFactory = documentCardFactory;
        this.storyItemFactory = storyItemFactory;
        this.cachingService = cachingService;
        this.storyService = storyService;
        this.navigationBarPresenter = navigationBarPresenter;
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        documentsListPresenter.clear();

        Integer storyId = Integer.valueOf(request.getParameter(ParameterTokens.id, "-1"));

        if (storyId == -1) {
            invalidPlaceHelper.handle(NameTokens.stories);
        } else {
            ShowLoadingEvent.fire(this);

            StorySummary cachedStory = getCachedStory(storyId);
            if (cachedStory == null) {
                storyService.getStorySummary(storyId, new ResponseHandler<DatumResponse<StorySummary>>() {
                    @Override
                    public void handleSuccess(DatumResponse<StorySummary> result) {
                        onStoryLoaded(result.getDatum());
                    }
                });
            } else {
                onStoryLoaded(cachedStory);
            }
        }
    }

    @Override
    public void onAddNewDocument(AddNewDocumentEvent event) {
        createNewContent(event.getEntityRelation());
    }

    @Override
    public void onCreateNewContent(CreateContentEvent event) {
        clearSlot(SLOT_NEW_CONTENT);
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

        addVisibleHandler(AddNewDocumentEvent.TYPE, this);
        addVisibleHandler(CreateContentEvent.TYPE, this);

        setInSlot(SLOT_NAV_BAR, navigationBarPresenter);
        setInSlot(SLOT_DOCUMENTS, documentsListPresenter);
    }

    private StorySummary getCachedStory(Integer storyId) {
        StorySummary storySummary = cachingService.getCachedObject(StorySummary.class, CachedObjectKeys.OPENED_STORY);
        if (storySummary == null || storySummary.getStoryId() == storyId) {
            return storySummary;
        } else {
            return null;
        }
    }

    private void onStoryLoaded(StorySummary storySummary) {
        cachingService.putCachedObject(StorySummary.class, CachedObjectKeys.OPENED_STORY, storySummary);
        StorySummaryLoadedEvent.fire(this, storySummary);

        currentStory = storySummary.getStory();

        setupPresenters();

        setStorySlot(storySummary);

        DocumentsContainer documentsContainer = storySummary.getDocuments();
        documentsListPresenter.setDocuments(documentsContainer, SystemEntityRelation.BODY,
                SystemEntityRelation.ANSWER_SET, SystemEntityRelation.NOTE, SystemEntityRelation.ATTACHMENT);

        PlaceRequest placeRequest = placeManager.getCurrentPlaceRequest();
        boolean addContent = Boolean.parseBoolean(placeRequest.getParameter(ParameterTokens.addContent, "false"));
        if (addContent && !documentsContainer.getDocuments(SystemEntityRelation.BODY).hasDocuments()) {
            createNewContent(SystemEntityRelation.BODY);
        }
    }

    private void createNewContent(SystemEntityRelation entityRelation) {
        setInSlot(SLOT_NEW_CONTENT, documentCardFactory.createAddContent(currentStory, entityRelation));
    }

    private void setupPresenters() {
        if (currentStory != null) {
            getProxy().manualReveal(this);
            revealInParent();
        } else {
            getProxy().manualRevealFailed();
        }
        HideLoadingEvent.fire(StoryPresenter.this);
    }

    private void setStorySlot(StorySummary storySummary) {
        StoryItemPresenter content = (StoryItemPresenter) storyItemFactory.create(this, storySummary);
        content.setStoryDetails(true);

        String editMode = placeManager.getCurrentPlaceRequest().getParameter(ParameterTokens.editMode, "false");
        content.setEditMode(Boolean.valueOf(editMode));

        setInSlot(SLOT_STORY, content);
    }
}
