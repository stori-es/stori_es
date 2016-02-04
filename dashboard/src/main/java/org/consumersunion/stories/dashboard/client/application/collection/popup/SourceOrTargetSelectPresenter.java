package org.consumersunion.stories.dashboard.client.application.collection.popup;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcOrganizationServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.CollectionDataPagedResponse;
import org.consumersunion.stories.common.client.service.response.CollectionSurveyI15dResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.CollectionSortField;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.QuestionnaireMask;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;
import org.consumersunion.stories.dashboard.client.util.AbstractAsyncCallback;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

/**
 * A child of {@link CollectionSelectPresenter} specialized to associate target {@link Collection}s to
 * {@link Questionnaire}s and source {@link Questionnaire}s to {@link Collection}s.
 */
public class SourceOrTargetSelectPresenter extends CollectionSelectPresenter {
    private Collection pivotCollection;

    @Inject
    SourceOrTargetSelectPresenter(
            EventBus eventBus,
            MyView view,
            RpcCollectionServiceAsync collectionService,
            RpcOrganizationServiceAsync organizationService,
            RpcQuestionnaireServiceAsync questionnaireService) {
        super(eventBus, view, collectionService, organizationService, questionnaireService);
    }

    public void initialize(Collection pivotCollection) {
        this.pivotCollection = pivotCollection;
        if (hasPivotCollection() && pivotCollection.isQuestionnaire()) {
            getView().linkActionTitle(false);
        } else {
            getView().linkActionTitle(true);
        }
    }

    /**
     * Associates the current (pivot) {@link Collection} with the indicated {@link Collection}s. If the current
     * {@link Collection} is a {@link Questionnaire}, then the indicate {@link Collection} IDs are understood as
     * target {@link Collection}s. If the current {@link Collection} is a vanilla, non-{@link Questionnaire}, then the
     * {@link Collection} IDs are understood as source {@link Questionnaire}s.
     */
    @Override
    protected void associateCollections(List<String> collectionIds) {
        if (hasPivotCollection()) {
            associateWithPivotCollection(collectionIds);
        }
    }

    private boolean hasPivotCollection() {
        return pivotCollection != null;
    }

    private void associateWithPivotCollection(final List<String> collectionIds) {
        if (pivotCollection.isQuestionnaire()) { // Then each of the selected Collections to this Questionnaire
            final List<String> failedUpdates = new ArrayList<String>();
            final Integer[] counter = new Integer[]{0};

            for (String collectionIdAsString : collectionIds) {
                final int collectionId = Integer.parseInt(collectionIdAsString);
                collectionService.getCollection(collectionId, ROLE_CURATOR,
                        new ResponseHandlerLoader<DatumResponse<CollectionData>>() {
                            @Override
                            public void onFailure(Throwable e) {
                                super.onFailure(e);

                                failedUpdates.add("ID: " + collectionId);
                                processFinal(counter, failedUpdates, messageDispatcher);
                            }

                            @Override
                            public void handleSuccess(DatumResponse<CollectionData> result) {
                                Collection targetCollection = result.getDatum().getCollection();
                                targetCollection.getCollectionSources().add(pivotCollection.getId());
                                updateTargetCollection(collectionId, targetCollection, counter, failedUpdates);
                            }
                        });
            }
        } else { // Then we associate all the selected Questionnaires to this Collection.
            for (String collectionIdAsString : collectionIds) {
                int questionnaireId = Integer.parseInt(collectionIdAsString);
                pivotCollection.getCollectionSources().add(questionnaireId);
            }

            collectionService.updateCollection(pivotCollection,
                    new ResponseHandlerLoader<DatumResponse<Collection>>() {
                        @Override
                        public void handleSuccess(DatumResponse<Collection> result) {
                            if (!collectionIds.isEmpty()) {
                                pivotCollection = result.getDatum();
                                updateReport(messageDispatcher,
                                        MessageStyle.SUCCESS,
                                        "Successfully added " + collectionIds.size() + " source questionnaire" +
                                                (collectionIds.size() == 1 ? "" : "s") + ".");
                            }
                        }
                    });
        }
    }

    @Override
    protected void createAndAssociate(String title) {
        if (hasPivotCollection()) {
            if (pivotCollection.isQuestionnaire()) {
                Collection newCollection = new Collection();
                newCollection.getBodyDocument().setTitle(title);
                newCollection.setPublic(true);

                collectionService.createCollection(newCollection,
                        new ResponseHandlerLoader<DatumResponse<Collection>>() {
                            @Override
                            public void handleSuccess(DatumResponse<Collection> result) {
                                List<String> collectionIds = new ArrayList<String>();
                                collectionIds.add(String.valueOf(result.getDatum().getId()));

                                associateNotification(collectionIds);
                                getView().hide();
                            }
                        });
            } else {
                QuestionnaireI15d newQuestionnaire = new QuestionnaireI15d();
                newQuestionnaire.getBodyDocument().setTitle(title);
                newQuestionnaire.setPublic(true);

                saveAndAssociate(newQuestionnaire);
            }
        }
    }

    private void updateTargetCollection(
            final int collectionId,
            final Collection targetCollection,
            final Integer[] counter,
            final List<String> failedUpdates) {
        collectionService.updateCollection(targetCollection,
                new ResponseHandlerLoader<DatumResponse<Collection>>() {
                    @Override
                    public void onFailure(Throwable e) {
                        super.onFailure(e);

                        failedUpdates.add(targetCollection.getTitle() + " (" + collectionId + ")");
                        processFinal(counter, failedUpdates, messageDispatcher);
                    }

                    @Override
                    public void handleSuccess(DatumResponse<Collection> result) {
                        processFinal(counter, failedUpdates, messageDispatcher);
                        getView().hide();
                    }
                });
    }

    private void saveAndAssociate(QuestionnaireI15d newQuestionnaire) {
        questionnaireService.saveQuestionnaire(pivotCollection, newQuestionnaire,
                new ResponseHandlerLoader<CollectionSurveyI15dResponse>() {
                    @Override
                    public void handleSuccess(CollectionSurveyI15dResponse result) {
                        if (result.getGlobalErrorMessages().isEmpty()) {
                            pivotCollection = result.getCollection();

                            List<String> questionnaireIds = new ArrayList<String>();
                            questionnaireIds.add(String.valueOf(result.getQuestionnaire().getId()));
                            associateNotification(questionnaireIds);
                        }
                    }
                });
    }

    private void processFinal(
            Integer[] counter,
            List<String> failedUpdates,
            MessageDispatcher messageDispatcher) {
        counter[0] = counter[0] + 1;

        if (counter[0] == collectionIds.size()) {
            if (failedUpdates.size() == 0) {
                updateReport(messageDispatcher,
                        MessageStyle.SUCCESS,
                        "Successfully added " + collectionIds.size() + " target collection" +
                                (collectionIds.size() == 1 ? "" : "s") + ".");
            } else { // At least one failure.
                String message;
                if (failedUpdates.size() == collectionIds.size()) {
                    message = "All updates failed.";
                } else {
                    String joinedFailures = Joiner.on(", ").join(failedUpdates);
                    message = "Successfully added " + (collectionIds.size() - failedUpdates.size()) + " source " +
                            "questionnaire" + (collectionIds.size() == 1 ? "" : "s") + ", but failed to add " +
                            joinedFailures + ".";
                }

                updateReport(messageDispatcher, MessageStyle.ERROR, message);
            }
        }
    }

    private void updateReport(MessageDispatcher messageDispatcher, MessageStyle messageStyle, String message) {
        messageDispatcher.displayMessage(messageStyle, message);
        ReloadCollectionsEvent.fire(this, pivotCollection, collectionIds);
        pivotCollection = null;
    }

    @Override
    protected void loadCollections(final ResponseHandler<CollectionDataPagedResponse> callback) {
        Preconditions.checkNotNull(pivotCollection, "Pivot collection not set as required.");

        AsyncCallback<CollectionDataPagedResponse> callbackWrapper = new AsyncCallback<CollectionDataPagedResponse>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(CollectionDataPagedResponse result) {
                Iterables.removeIf(result.getData(), new Predicate<CollectionData>() {
                    @Override
                    public boolean apply(CollectionData input) {
                        return pivotCollection.getTargetCollections().contains(input.getId());
                    }
                });

                callback.onSuccess(result);
            }
        };

        if (pivotCollection.isQuestionnaire()) { // then we need to load non-Questionnaire Collections
            // TODO: TASK-130: The '500' here is a total hack. We need to support a mini-pager with search box.
            collectionService.getCollections(new RetrievePagedCollectionsParams.Builder()
                    .withStart(0)
                    .withLength(500)
                    .withSortField(CollectionSortField.TITLE_A_Z)
                    .withAscending(true)
                    .withQuestionnaireMask(QuestionnaireMask.QUESTIONNAIRE_MASK_NON_QUESTIONNAIRES)
                    .withPermissionMask(ROLE_READER)
                    .withAccessMode(ACCESS_MODE_EXPLICIT)
                    .build(""), callbackWrapper);
        } else { // we need to load Questionnaire-Collections
            AsyncCallback<PagedDataResponse<QuestionnaireI15d>> questionnaireCallback =
                    createQuestionnairesCallback(callbackWrapper);
            questionnaireService.getQuestionnairesExcludeAssociated(pivotCollection.getId(), 0, 500,
                    questionnaireCallback);
        }
    }

    private void onQuestionnairesLoaded(PagedDataResponse<QuestionnaireI15d> result,
            AsyncCallback<CollectionDataPagedResponse> callback) {
        List<CollectionData> collectionDatas = Lists.transform(result.getData(),
                new Function<QuestionnaireI15d, CollectionData>() {
                    @Override
                    public CollectionData apply(QuestionnaireI15d questionnaire) {
                        return new CollectionData(questionnaire, null);
                    }
                });

        CollectionDataPagedResponse collectionPagedDataResponse = new CollectionDataPagedResponse();
        collectionPagedDataResponse.setStart(result.getStart());
        collectionPagedDataResponse.setTotalCount(result.getTotalCount());
        collectionPagedDataResponse.setData(Lists.newArrayList(collectionDatas));

        callback.onSuccess(collectionPagedDataResponse);
    }

    private AsyncCallback<PagedDataResponse<QuestionnaireI15d>> createQuestionnairesCallback(
            final AsyncCallback<CollectionDataPagedResponse> callback) {
        return new AbstractAsyncCallback<PagedDataResponse<QuestionnaireI15d>>() {
            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);

                callback.onFailure(e);
            }

            @Override
            public void onSuccess(PagedDataResponse<QuestionnaireI15d> result) {
                onQuestionnairesLoaded(result, callback);
            }
        };
    }
}
