package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import java.util.List;

import org.consumersunion.stories.common.client.event.CollectionChangedEvent;
import org.consumersunion.stories.common.client.event.RedrawEvent;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionIdsUtil;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

/**
 * Presenter listing the {@link QuestionnaireI15d}s which linked as source for a {@link Collection}. Provides
 * controls to
 * remove link itself for {@link Collection}s. Removal is handled locally, while
 * addition to existing or new Collection is handled through
 * {@link org.consumersunion.stories.dashboard.client.application.collection.popup.SourceOrTargetSelectPresenter}.
 */
public class ListQuestionnairePresenter extends PresenterWidget<ListQuestionnairePresenter.MyView>
        implements ListQuestionnaireUiHandlers, ReloadCollectionsEvent.ReloadCollectionsHandler,
        CollectionChangedEvent.CollectionChangedHandler {
    interface MyView extends View, HasUiHandlers<ListQuestionnaireUiHandlers> {
        void setData(List<QuestionnaireI15d> data, int start, int totalCount);

        void init();

        void clear();

        void refreshQuestionnaires();
    }

    static final int PAGE_SIZE = 20;

    private final RpcQuestionnaireServiceAsync questionnaireService;
    private final RpcCollectionServiceAsync collectionService;
    private final PlaceManager placeManager;
    private final CollectionIdsUtil collectionIdsUtil;

    private Collection currentCollection;

    @Inject
    ListQuestionnairePresenter(
            EventBus eventBus,
            MyView view,
            RpcQuestionnaireServiceAsync questionnaireService,
            RpcCollectionServiceAsync collectionService,
            PlaceManager placeManager,
            CollectionIdsUtil collectionIdsUtil) {
        super(eventBus, view);

        this.questionnaireService = questionnaireService;
        this.collectionService = collectionService;
        this.placeManager = placeManager;
        this.collectionIdsUtil = collectionIdsUtil;

        getView().setUiHandlers(this);
    }

    public void initPresenter(Collection currentCollection) {
        this.currentCollection = currentCollection;

        getView().init();
    }

    public void initPresenter(Collection collection, List<QuestionnaireI15d> sourceQuestionnaires) {
        this.currentCollection = collection;
        getView().setData(sourceQuestionnaires, 0, sourceQuestionnaires.size());
    }

    @Override
    public void onCollectionChanged(CollectionChangedEvent event) {
        if (collectionIdsUtil.isRelatedCollection(currentCollection, event.getCollection())) {
            getView().refreshQuestionnaires();
        }
    }

    @Override
    public void loadQuestionnaire(int start, int length) {
        questionnaireService.getQuestionnaireSummaries(currentCollection.getId(), start, length,
                new ResponseHandler<PagedDataResponse<QuestionnaireI15d>>() {
                    @Override
                    public void handleSuccess(PagedDataResponse<QuestionnaireI15d> result) {
                        RedrawEvent.fire(ListQuestionnairePresenter.this);
                        getView().setData(result.getData(), result.getStart(), result.getTotalCount());
                    }
                });
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
                            initPresenter(result.getDatum().getCollection());
                        }
                    });
        }
    }

    @Override
    public void questionnaireDetails(QuestionnaireI15d questionnaire) {
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.collection)
                .with(ParameterTokens.id, String.valueOf(questionnaire.getId()))
                .build();
        placeManager.revealPlace(place);
    }

    @Override
    public void removeSourceQuestionnaire(QuestionnaireI15d questionnaire) {
        currentCollection.getCollectionSources().remove(questionnaire.getId());
        updateSources(questionnaire.getId());
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(ReloadCollectionsEvent.TYPE, this);
        addVisibleHandler(CollectionChangedEvent.TYPE, this);
    }

    private void updateSources(final Integer... removedIds) {
        collectionService.updateCollection(currentCollection,
                new ResponseHandlerLoader<DatumResponse<Collection>>() {
                    @Override
                    public void handleSuccess(DatumResponse<Collection> result) {
                        currentCollection = result.getDatum();
                        initPresenter(currentCollection);
                        ReloadCollectionsEvent.fire(ListQuestionnairePresenter.this, currentCollection,
                                Lists.newArrayList(removedIds));
                    }
                });
    }
}
