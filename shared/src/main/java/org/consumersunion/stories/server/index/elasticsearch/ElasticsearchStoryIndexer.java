package org.consumersunion.stories.server.index.elasticsearch;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.server.annotations.Indexer;
import org.consumersunion.stories.server.index.story.StoryDocument;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("storyIndexer")
public class ElasticsearchStoryIndexer extends ElasticsearchIndexer<StoryDocument> {
    @Inject
    protected ElasticsearchStoryIndexer(
            @Indexer ObjectMapper indexerObjectMapper,
            @Indexer String indexName,
            Provider<RestClient> restClientProvider) {
        super(indexerObjectMapper, restClientProvider, StoryDocument.class, indexName, "stories");
    }
}
