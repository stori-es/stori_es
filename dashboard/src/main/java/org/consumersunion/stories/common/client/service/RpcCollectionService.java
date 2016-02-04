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
import org.consumersunion.stories.server.business_logic.CollectionService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/collection")
public interface RpcCollectionService extends RemoteService {
    /**
     * @see CollectionService#createCollection(Collection)
     */
    DatumResponse<Collection> createCollection(Collection collection);

    /**
     * @see CollectionService#getCollectionForOperation(int, int)
     */
    DatumResponse<CollectionData> getCollection(final int id, int operation);

    /**
     * Saves the {@link AnswerSet} (which may include a @ Story} ). There are no
     * authorization restrictions on this method. When a story is saved, a
     * {@link Person} must be associated with the Story. The Person is:
     * <p/>
     * * the logged in {@link User} * matched by email if an email (standard
     * field) is provided with the {@link AnswerSet} * created if no match is
     * found or no match is possiblel
     * <p/>
     * The {@link Organization} associated with the {@link Collection}
     * associated to the {@link Questionnaire} is granted READ, WRITE, and ADMIN
     * privileges over the newly created Person, or if an existing Person was
     * found, then the Organization is granted READ access.
     * <p/>
     * The Person is made the owner of the Story.
     * <p/>
     * The Organization is granted READ, WRITE, and ADMIN over any story
     * submitted to their Collection.
     */
    QuestionnaireSurveyResponse saveAnswersAndStory(AnswerSet answerSet);

    CollectionDataPagedResponse getCollections(RetrievePagedCollectionsParams params);

    /**
     * @see CollectionService#updateCollection(Collection)
     */
    DatumResponse<Collection> updateCollection(Collection collection);

    PagedDataResponse<CollectionData> getCollectionsReferencingStory(int start, int length, SortField sortField,
            boolean ascending, final int storyId);

    CollectionDataPagedResponse getCollectionsByEffectiveRoleExcludeByStory(int storyId);

    /**
     * Links the indicated {@link Story}s to the indicated {@link Collection}.
     * The effective role must have read access by any means (public, priv, or
     * own) over story and must own or have write privs over the the Collection.
     * The check is 'all or none', meaning that if the user lacks access to any
     * Story, the method will fail.
     */
    DatumResponse<Collection> linkStoriesToCollection(int collectionId, java.util.Collection<Integer> storyIds);

    /**
     * Removes the indicated story from the indicated collection. The effective
     * role must have WRITE privileges over the collection.
     */
    DatumResponse<CollectionData> removeStoryFromCollection(int collectionId, int storyId);

    DatumResponse<Collection> checkUnusedLink(String collectionLink);

    /**
     * Returns non-deleted Collections fed by the indicated Questionnaire.
     *
     * @param questionnaireId
     * @return
     */
    DataResponse<CollectionData> getCollectionsForQuestionnaire(int questionnaireId);

    ActionResponse linkStoriesToCollections(Set<Integer> collectionIds, Set<Integer> storyIds);

    ActionResponse removeFromCollectionSources(Integer collectionId, Integer sourceCollection);

    DatumResponse<Collection> getCollectionByPermalink(String permalink);
}
