package org.consumersunion.stories.common.client.service;

import java.util.Set;

import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.CollectionDataPagedResponse;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcCollectionServiceAsync {

    void createCollection(Collection collection, AsyncCallback<DatumResponse<Collection>> callback);

    Request getCollection(int id, int operation, AsyncCallback<DatumResponse<CollectionData>> callback);

    void saveAnswersAndStory(AnswerSet answerSet, AsyncCallback<QuestionnaireSurveyResponse> callback);

    Request getCollections(RetrievePagedCollectionsParams params, AsyncCallback<CollectionDataPagedResponse> callback);

    void updateCollection(Collection collection, AsyncCallback<DatumResponse<Collection>> callback);

    void getCollectionsReferencingStory(int start, int length, SortField sortField, boolean ascending,
            int storyId, AsyncCallback<PagedDataResponse<CollectionData>> callback);

    void getCollectionsByEffectiveRoleExcludeByStory(int storyId, AsyncCallback<CollectionDataPagedResponse> callback);

    void linkStoriesToCollection(int collectionId, java.util.Collection<Integer> storyIds,
            AsyncCallback<DatumResponse<Collection>> callback);

    void linkStoriesToCollections(Set<Integer> collectionIds, Set<Integer> storyIds,
            AsyncCallback<ActionResponse> callback);

    void removeStoryFromCollection(int collectionData, int storyId,
            AsyncCallback<DatumResponse<CollectionData>> callback);

    Request checkUnusedLink(String collectionLink, AsyncCallback<DatumResponse<Collection>> callback);

    void getCollectionsForQuestionnaire(int questionnaireId, AsyncCallback<DataResponse<CollectionData>> callback);

    void removeFromCollectionSources(Integer collectionId, Integer sourceCollection,
            AsyncCallback<ActionResponse> callback);

    void getCollectionByPermalink(String permalink, AsyncCallback<DatumResponse<Collection>> publicResponseHandler);
}
