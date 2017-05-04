package org.consumersunion.stories.server.index.elasticsearch;

import javax.inject.Inject;

import org.consumersunion.stories.server.annotations.Indexer;
import org.consumersunion.stories.server.index.collection.CollectionDocument;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("collectionIndexer")
public class ElasticsearchCollectionIndexer extends ElasticsearchIndexer<CollectionDocument> {
    @Inject
    protected ElasticsearchCollectionIndexer(
            @Indexer ObjectMapper indexerObjectMapper,
            @Indexer String indexName,
            ElasticsearchRestClient elasticsearchRestClient) {
        super(indexerObjectMapper, elasticsearchRestClient, CollectionDocument.class, indexName, "collections");
    }
}
