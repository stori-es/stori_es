package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.util.List;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.service.datatransferobject.PagedData;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.export.StoryCsv;
import org.consumersunion.stories.server.export.StoryExport;
import org.consumersunion.stories.server.persistence.StoryTellersParams;

public interface StoryService {
    /**
     * Retrieves a story. Effective role must have read access by some means: public, ownership, or read privilege over
     * story.
     */
    Story getStory(int id);

    StorySummary getStorySummary(int id);

    StorySummary getStorySummary(int id, boolean includeFullText);

    Story createStory(Story story);

    Story createStory(Story story, Connection conn);

    /**
     * Effective role must own, have write privs over Story, or be root.
     */
    Story updateStory(Story story);

    void deleteStory(int id);

    PagedData<StorySummary> getStories(StorySearchParameters storySearchParameters);

    PagedData<StorySummary> getStories(StorySearchParameters storySearchParameters, Integer effectiveSubject);

    int getStoriesCount(StorySearchParameters storySearchParameters, int profileId);

    StoryExport<StoryCsv> exportStories(int userId, StorySearchParameters searchParameters);

    int getStorytellerCount(StoryTellersParams params);

    void updateAuthor(ProfileSummary profileSummary);

    List<Integer> getStoriesIds(int id);
}
