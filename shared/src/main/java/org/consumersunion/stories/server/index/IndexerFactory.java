package org.consumersunion.stories.server.index;

import java.sql.Connection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.server.index.collection.CollectionDocument;
import org.consumersunion.stories.server.index.collection.UpdateCollectionsAuthsIndexer;
import org.consumersunion.stories.server.index.story.StoryDocument;
import org.consumersunion.stories.server.index.story.UpdateStoriesAuthsIndexer;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class IndexerFactory {
    private final Provider<Indexer<StoryDocument>> storyIndexerProvider;
    private final Provider<Indexer<CollectionDocument>> collectionIndexerProvider;
    private final SupportDataUtilsFactory dataUtilsFactory;

    @Inject
    IndexerFactory(
            @Qualifier("storyIndexer") Provider<Indexer<StoryDocument>> storyIndexerProvider,
            @Qualifier("collectionIndexer") Provider<Indexer<CollectionDocument>> collectionIndexerProvider,
            SupportDataUtilsFactory dataUtilsFactory) {
        this.storyIndexerProvider = storyIndexerProvider;
        this.collectionIndexerProvider = collectionIndexerProvider;
        this.dataUtilsFactory = dataUtilsFactory;
    }

    public UpdateCollectionsAuthsIndexer createCollectionsAuth(List<Integer> collections, Connection conn) {
        return new UpdateCollectionsAuthsIndexer(collectionIndexerProvider.get(), dataUtilsFactory, collections, conn);
    }

    public UpdateStoriesAuthsIndexer createStoriesAuth(List<Integer> stories, Connection conn) {
        return new UpdateStoriesAuthsIndexer(storyIndexerProvider.get(), dataUtilsFactory, stories, conn);
    }
}
