package org.consumersunion.stories.server.index.elasticsearch;

import javax.inject.Inject;

import org.consumersunion.stories.server.annotations.Indexer;
import org.consumersunion.stories.server.index.profile.ProfileDocument;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("profileIndexer")
public class ElasticsearchProfileIndexer extends ElasticsearchIndexer<ProfileDocument> {
    @Inject
    protected ElasticsearchProfileIndexer(
            @Indexer ObjectMapper indexerObjectMapper,
            @Indexer String indexName,
            ElasticsearchRestClient elasticsearchRestClient) {
        super(indexerObjectMapper, elasticsearchRestClient, ProfileDocument.class, indexName, "profiles");
    }
}
