package org.consumersunion.stories.server.index.elasticsearch;

import javax.inject.Inject;

import org.consumersunion.stories.server.annotations.Indexer;
import org.consumersunion.stories.server.index.story.StoryDocument;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("storyIndexer")
public class ElasticsearchStoryIndexer extends ElasticsearchIndexer<StoryDocument> {
    @Inject
    protected ElasticsearchStoryIndexer(
            @Indexer ObjectMapper indexerObjectMapper,
            @Indexer String indexName,
            ElasticsearchRestClient elasticsearchRestClient) {
        super(indexerObjectMapper, elasticsearchRestClient, StoryDocument.class, indexName, "stories");
    }
}
