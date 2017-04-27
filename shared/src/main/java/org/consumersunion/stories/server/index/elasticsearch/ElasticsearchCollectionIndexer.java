package org.consumersunion.stories.server.index.elasticsearch;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.server.annotations.Indexer;
import org.consumersunion.stories.server.index.collection.CollectionDocument;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("collectionIndexer")
public class ElasticsearchCollectionIndexer extends ElasticsearchIndexer<CollectionDocument> {
    @Inject
    protected ElasticsearchCollectionIndexer(
            @Indexer ObjectMapper indexerObjectMapper,
            @Indexer String indexName,
            Provider<RestClient> restClientProvider) {
        super(indexerObjectMapper, restClientProvider, CollectionDocument.class, indexName, "collections");
    }
}
