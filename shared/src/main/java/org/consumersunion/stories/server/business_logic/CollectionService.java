package org.consumersunion.stories.server.business_logic;

import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.dto.tasks.AddStoriesToCollectionTask;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.business_logic.interceptor.RequiresLoggedUser;
import org.consumersunion.stories.server.exception.PermalinkAlreadyExistsException;

public interface CollectionService {
    /**
     * Retrieves a {@link Collection based on the ID. This requires the
     * Collection be public, owned by the effective role, associated to the
     * current Organization.
     */
    Collection getCollection(int id);

    CollectionData getCollectionData(int id);

    CollectionData getCollectionForRole(int id, int minRole);

    /**
     * Create a new {@link Collection}. This only requires the user be logged
     * in. If acting on behalf of an Organization, the Organization will own the
     * newly created Collection. Otherwise the user will own the Collection if
     * acting on his or her own behalf. No special privileges are required to
     * create a Colleciton. Users may always create Collections on their own
     * behalf, and general OBO privileges are sufficient to create a Collection
     * on behalf of an Organization.
     */
    @RequiresLoggedUser
    Collection createCollection(Collection collection) throws PermalinkAlreadyExistsException;

    Collection getCollectionByPermalink(String permalink);

    void deleteCollection(int collectionId);

    void setTheme(Collection collection);

    /**
     * Searches collections based on the search text (which may be null) and the
     * access mode. The access mode is the method by which the effective role is
     * attempting to gain access to the collections and is documented in the
     * Developer Reference.
     */
    List<CollectionData> searchCollections(RetrievePagedCollectionsParams params);

    /**
     * Retrieves all the Collections which include the indicated
     * {@link org.consumersunion.stories.common.shared.model.Story}.
     * Effective role must have READ access to the Story (by any access mode)
     * and READ access to each Collection as well.
     */
    List<CollectionData> searchCollectionsByStory(RetrievePagedCollectionsParams params);

    /**
     * Retrieves non-{@link org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire},
     * non-deleted {@link Collection Collections} writable by the current effective role,
     * excluding those collections already associated with the indicated story.
     * The effective role must have access to the story, and the we retrieve the
     * owned collections.
     */
    List<CollectionData> searchCollectionsByUserWithoutStoryAssociated(RetrievePagedCollectionsParams params);

    /**
     * Updates the {@link Collection}. Requires effective role has write
     * privileges over or owns the Collection in question.
     */
    Collection updateCollection(Collection collection);

    Collection linkStoriesToCollection(Collection collection, java.util.Collection<Integer> storyIds);

    void linkStoryToCollections(Set<Integer> collectionIds, Story savedStory);

    Collection linkStorySummariesToCollection(Collection collection, List<StorySummary> stories);

    void linkStoriesToCollection(AddStoriesToCollectionTask task);
}
