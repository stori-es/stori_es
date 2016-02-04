package org.consumersunion.stories.server.solr;

import java.sql.Connection;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.server.solr.collection.UpdateCollectionsAuthsIndexer;
import org.consumersunion.stories.server.solr.collection.UpdatedCollectionIndexer;
import org.consumersunion.stories.server.solr.story.UpdateStoriesAuthsIndexer;
import org.consumersunion.stories.server.solr.story.UpdatedStoryCollectionIndexer;
import org.springframework.stereotype.Component;

@Component
public class IndexerFactory {
    private final SupportDataUtilsFactory supportDataUtilsFactory;

    @Inject
    IndexerFactory(SupportDataUtilsFactory supportDataUtilsFactory) {
        this.supportDataUtilsFactory = supportDataUtilsFactory;
    }

    public UpdatedStoryCollectionIndexer createUpdatedStory(int storyId) {
        return new UpdatedStoryCollectionIndexer(supportDataUtilsFactory, storyId);
    }

    public UpdatedCollectionIndexer createUpdatedCollection(Collection collection) {
        return new UpdatedCollectionIndexer(supportDataUtilsFactory, collection);
    }

    public UpdateCollectionsAuthsIndexer createCollectionsAuth(List<Integer> collections, Connection conn) {
        return new UpdateCollectionsAuthsIndexer(supportDataUtilsFactory, collections, conn);
    }

    public UpdateStoriesAuthsIndexer createStoriesAuth(List<Integer> stories, Connection conn) {
        return new UpdateStoriesAuthsIndexer(supportDataUtilsFactory, stories, conn);
    }
}
