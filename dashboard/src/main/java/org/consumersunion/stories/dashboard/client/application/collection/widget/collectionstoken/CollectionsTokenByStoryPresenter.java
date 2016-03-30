package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionstoken;

import java.util.LinkedHashSet;
import java.util.Set;

import org.consumersunion.stories.common.client.event.RedrawEvent;
import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.CurrentUser;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

/**
 * Presenter listing the {@link Collection}s to which a particular {@link Story} is associated. Provides controls to
 * add, create, and remove the {@link Story} in question from {@link Collection}.
 */
public class CollectionsTokenByStoryPresenter
        extends PresenterWidget<CollectionsTokenView<CollectionSummary, Collection, CollectionData>>
        implements CollectionsTokenUiHandlers<CollectionSummary, Collection, CollectionData>,
        RedrawEvent.RedrawHandler<StorySummary> {

    private final RpcCollectionServiceAsync collectionService;
    private final CommonI18nMessages messages;
    private final CurrentUser currentUser;
    private final PlaceManager placeManager;
    private final RpcStoryServiceAsync storyService;

    private StorySummary storySummary;
    private Story currentStory;

    @Inject
    CollectionsTokenByStoryPresenter(
            EventBus eventBus,
            CollectionsTokenView<CollectionSummary, Collection, CollectionData> view,
            RpcCollectionServiceAsync collectionService,
            CurrentUser currentUser,
            PlaceManager placeManager,
            CommonI18nMessages messages,
            RpcStoryServiceAsync storyService) {
        super(eventBus, view);

        this.collectionService = collectionService;
        this.currentUser = currentUser;
        this.placeManager = placeManager;
        this.messages = messages;
        this.storyService = storyService;

        getView().setUiHandlers(this);
        getView().showQuestionnaireIcon();
    }

    public void initPresenter(StorySummary storySummary) {
        this.storySummary = storySummary;
        this.currentStory = storySummary.getStory();
        setData(storySummary);
    }

    @Override
    public boolean apply(CollectionData input) {
        for (CollectionSummary collectionSummary : storySummary.getCollections()) {
            if (collectionSummary.getId() == input.getId()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void removeCollection(CollectionSummary collection) {
        final int collectionId = collection.getId();
        int storyId = currentStory.getId();

        collectionService.removeStoryFromCollection(collectionId, storyId,
                new ResponseHandlerLoader<DatumResponse<CollectionData>>() {
                    @Override
                    public void handleSuccess(DatumResponse<CollectionData> result) {
                        reloadData();
                    }
                });
    }

    @Override
    public void onCollectionSelected(Collection collection) {
        addCollection(collection);
    }

    @Override
    public void collectionDetails(CollectionSummary collectionSummary) {
        int collectionId = collectionSummary.getId();
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.collection)
                .with(ParameterTokens.id, String.valueOf(collectionId))
                .build();

        PlaceRequest currentPlaceRequest = placeManager.getCurrentPlaceRequest();
        if (!currentPlaceRequest.hasSameNameToken(place)
                || !String.valueOf(collectionId).equals(currentPlaceRequest.getParameter(ParameterTokens.id, ""))) {
            placeManager.revealRelativePlace(place);
        }
    }

    @Override
    public boolean canRemoveCollection(CollectionSummary collection) {
        return !collection.isQuestionnaire() ||
                (currentStory != null && currentStory.getOwner() == currentUser.getCurrentProfileId());
    }

    @Override
    public void redraw() {
        RedrawEvent.fire(this);
    }

    @Override
    public void onRedraw(RedrawEvent<StorySummary> event) {
        Object data = event.getData();
        if (event.getSource() != this && data != null) {
            StorySummary storySummary = event.getData();
            if (this.storySummary.getStoryId() == storySummary.getStoryId()) {
                getView().setData(storySummary.getCollections());
            }
        }
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(RedrawEvent.TYPE, this);
    }

    private void addCollection(Collection collection) {
        Set<Integer> storyIds = Sets.newLinkedHashSet();
        storyIds.add(currentStory.getId());

        LinkedHashSet<Integer> collections = Sets.newLinkedHashSet();
        collections.add(collection.getId());
        collectionService.linkStoriesToCollections(collections, storyIds,
                new ResponseHandlerLoader<ActionResponse>() {
                    @Override
                    public void handleSuccess(ActionResponse result) {
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, messages.storiesAddedSuccessfully());
                        reloadData();
                    }
                });
    }

    private void setData(StorySummary storySummary) {
        getView().setData(storySummary.getCollections());
        RedrawEvent.fire(this, storySummary);
    }

    private void reloadData() {
        storyService.getStorySummary(currentStory.getId(),
                new ResponseHandler<DatumResponse<StorySummary>>() {
                    @Override
                    public void handleSuccess(DatumResponse<StorySummary> result) {
                        storySummary = result.getDatum();
                        setData(storySummary);
                    }
                });
    }
}
