package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionstoken;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.RedrawEvent;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.common.collect.Lists;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class SourceQuestionnaireTokenPresenter
        extends PresenterWidget<CollectionsTokenView<QuestionnaireI15d, QuestionnaireI15d, CollectionData>>
        implements ReloadCollectionsEvent.ReloadCollectionsHandler,
        CollectionsTokenUiHandlers<QuestionnaireI15d, QuestionnaireI15d, CollectionData> {
    private final RpcCollectionServiceAsync collectionService;
    private final RpcQuestionnaireServiceAsync questionnaireService;
    private final PlaceManager placeManager;

    private Collection currentCollection;

    @Inject
    SourceQuestionnaireTokenPresenter(
            EventBus eventBus,
            CollectionsTokenView<QuestionnaireI15d, QuestionnaireI15d, CollectionData> myView,
            RpcCollectionServiceAsync collectionService,
            RpcQuestionnaireServiceAsync questionnaireService,
            PlaceManager placeManager) {
        super(eventBus, myView);

        this.collectionService = collectionService;
        this.questionnaireService = questionnaireService;
        this.placeManager = placeManager;

        getView().setUiHandlers(this);
    }

    public void init(Collection collection, List<QuestionnaireI15d> targetCollections) {
        this.currentCollection = collection;
        setData(targetCollections);
    }

    @Override
    public boolean apply(CollectionData input) {
        return !currentCollection.getCollectionSources().contains(input.getId());
    }

    @Override
    public void removeCollection(final QuestionnaireI15d questionnaire) {
        collectionService.removeFromCollectionSources(currentCollection.getId(), questionnaire.getId(),
                new ResponseHandlerLoader<ActionResponse>() {
                    @Override
                    public void handleSuccess(ActionResponse result) {
                        currentCollection.getTargetCollections().remove(questionnaire.getId());
                        ReloadCollectionsEvent.fire(SourceQuestionnaireTokenPresenter.this, currentCollection,
                                Lists.newArrayList(questionnaire.getId()));
                        loadLinkedCollections();
                    }
                }
        );
    }

    @Override
    public void onCollectionSelected(QuestionnaireI15d questionnaire) {
        int questionnaireId = questionnaire.getId();
        currentCollection.getCollectionSources().add(questionnaireId);

        collectionService.updateCollection(currentCollection,
                new ResponseHandlerLoader<DatumResponse<Collection>>() {
                    @Override
                    public void handleSuccess(DatumResponse<Collection> result) {
                        currentCollection = result.getDatum();
                        loadLinkedCollections();
                    }
                });
    }

    @Override
    public void redraw() {
    }

    @Override
    public void onReloadCollections(ReloadCollectionsEvent event) {
        Collection sourceCollection = event.getSourceCollection();
        if (sourceCollection != null && sourceCollection.getId() == currentCollection.getId()
                || event.getRelatedCollectionIds().contains(currentCollection.getId())) {
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
    public void collectionDetails(QuestionnaireI15d questionnaire) {
        int questionnaireId = questionnaire.getId();
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.collection)
                .with(ParameterTokens.id, String.valueOf(questionnaireId))
                .build();
        placeManager.revealPlace(place);
    }

    @Override
    public boolean canRemoveCollection(QuestionnaireI15d questionnaire) {
        return currentCollection.getId() != questionnaire.getId();
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(ReloadCollectionsEvent.TYPE, this);
    }

    private void setData(List<QuestionnaireI15d> data) {
        getView().setData(data);
        RedrawEvent.fire(this);
    }

    private void loadLinkedCollections() {
        questionnaireService.getQuestionnaireSummaries(currentCollection.getId(), 0, 0,
                new ResponseHandler<PagedDataResponse<QuestionnaireI15d>>() {
                    @Override
                    public void handleSuccess(PagedDataResponse<QuestionnaireI15d> result) {
                        RedrawEvent.fire(SourceQuestionnaireTokenPresenter.this);
                        setData(result.getData());
                    }
                });
    }
}
