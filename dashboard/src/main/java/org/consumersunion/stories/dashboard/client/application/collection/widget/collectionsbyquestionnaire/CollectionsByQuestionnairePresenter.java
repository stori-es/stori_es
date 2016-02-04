package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbyquestionnaire;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.CollectionChangedEvent;
import org.consumersunion.stories.common.client.event.RedrawEvent;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionIdsUtil;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class CollectionsByQuestionnairePresenter extends PresenterWidget<CollectionsByQuestionnairePresenter.MyView>
        implements ReloadCollectionsEvent.ReloadCollectionsHandler, CollectionsByQuestionnaireUiHandlers,
        CollectionChangedEvent.CollectionChangedHandler {
    interface MyView extends View, HasUiHandlers<CollectionsByQuestionnaireUiHandlers> {
        void setData(List<CollectionSummary> data);

        void clear();

        void onTargetCollectionRemoved(CollectionSummary collection);

        void updateCollection(Collection collection);
    }

    private final RpcCollectionServiceAsync collectionService;
    private final PlaceManager placeManager;
    private final CollectionIdsUtil collectionIdsUtil;

    private Collection currentCollection;

    @Inject
    CollectionsByQuestionnairePresenter(
            EventBus eventBus,
            MyView myView,
            RpcCollectionServiceAsync collectionService,
            PlaceManager placeManager,
            CollectionIdsUtil collectionIdsUtil) {
        super(eventBus, myView);

        this.collectionService = collectionService;
        this.placeManager = placeManager;
        this.collectionIdsUtil = collectionIdsUtil;

        getView().setUiHandlers(this);
    }

    public void init(Collection collection, List<CollectionSummary> targetCollections) {
        this.currentCollection = collection;
        setData(targetCollections);
    }

    @Override
    public void onCollectionChanged(CollectionChangedEvent event) {
        if (collectionIdsUtil.isRelatedCollection(currentCollection, event.getCollection())) {
            getView().updateCollection(event.getCollection());
        }
    }

    @Override
    public void onReloadCollections(ReloadCollectionsEvent event) {
        if (currentCollection != null && collectionIdsUtil.isSameCollection(currentCollection, event)) {
            collectionService.getCollection(currentCollection.getId(), AuthConstants.ROLE_READER,
                    new ResponseHandlerLoader<DatumResponse<CollectionData>>() {
                        @Override
                        public void handleSuccess(DatumResponse<CollectionData> result) {
                            currentCollection = result.getDatum().getCollection();
                            loadLinkedCollections();
                        }
                    });
        }
    }

    @Override
    public void removeTargetCollection(final CollectionSummary collection) {
        collectionService.removeFromCollectionSources(collection.getId(), currentCollection.getId(),
                new ResponseHandlerLoader<ActionResponse>() {
                    @Override
                    public void handleSuccess(ActionResponse result) {
                        currentCollection.getTargetCollections().remove(collection.getId());
                        getView().onTargetCollectionRemoved(collection);
                        ReloadCollectionsEvent.fire(CollectionsByQuestionnairePresenter.this, currentCollection,
                                Lists.newArrayList(collection.getId()));
                        loadLinkedCollections();
                    }
                }
        );
    }

    @Override
    public void collectionDetails(Integer collectionId) {
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.collection)
                .with(ParameterTokens.id, String.valueOf(collectionId))
                .build();
        placeManager.revealPlace(place);
    }

    @Override
    public boolean canRemoveCollection(CollectionSummary collection) {
        return !collection.isQuestionnaire();
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(ReloadCollectionsEvent.TYPE, this);
        addVisibleHandler(CollectionChangedEvent.TYPE, this);
    }

    private void setData(List<CollectionSummary> data) {
        getView().setData(data);
        RedrawEvent.fire(this);
    }

    private void setDataFromCollectionData(List<CollectionData> data) {
        List<CollectionSummary> collectionSummaries = FluentIterable.from(data).transform(
                new Function<CollectionData, CollectionSummary>() {
                    @Override
                    public CollectionSummary apply(CollectionData input) {
                        Collection collection = input.getCollection();

                        return new CollectionSummary(collection);
                    }
                }).toList();
        setData(collectionSummaries);
    }

    private void loadLinkedCollections() {
        ResponseHandler<DataResponse<CollectionData>> callback = new ResponseHandler<DataResponse<CollectionData>>() {
            @Override
            public void handleSuccess(DataResponse<CollectionData> result) {
                if (result.getGlobalErrorMessages().size() == 0) {
                    setDataFromCollectionData(result.getData());
                }
            }
        };

        collectionService.getCollectionsForQuestionnaire(currentCollection.getId(), callback);
    }
}
