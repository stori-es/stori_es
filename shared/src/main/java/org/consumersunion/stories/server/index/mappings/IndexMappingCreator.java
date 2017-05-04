package org.consumersunion.stories.server.index.mappings;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.consumersunion.stories.server.annotations.Indexer;
import org.consumersunion.stories.server.index.elasticsearch.ElasticsearchRestClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class IndexMappingCreator {
    @JsonIgnore
    private final ElasticsearchRestClient elasticsearchRestClient;
    @JsonIgnore
    private final ObjectMapper indexerObjectMapper;
    @JsonIgnore
    private final String indexName;

    private Map<String, Object> mappings = new LinkedHashMap<String, Object>();

    @Inject
    IndexMappingCreator(
            ElasticsearchRestClient elasticsearchRestClient,
            @Indexer ObjectMapper indexerObjectMapper,
            @Indexer String indexName) {
        this.elasticsearchRestClient = elasticsearchRestClient;
        this.indexerObjectMapper = indexerObjectMapper;
        this.indexName = indexName;

        mappings.put("collections", new CollectionsMapping());
        mappings.put("stories", new StoriesMapping());
//        mappings.put("profiles", new ProfilesMapping());
    }

    public void create() throws IOException {
        String content = indexerObjectMapper.writeValueAsString(this);

        HttpResponse response = elasticsearchRestClient.performRequest("PUT", "/" + indexName, content);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 400) {
            String error = IOUtils.toString(response.getEntity().getContent());
            if (error == null || !error.contains("already_exists")) {
                throw new RuntimeException(error);
            }

            update();
        } else {
            IOUtils.copy(response.getEntity().getContent(), System.out);
        }
    }

    private void update() throws IOException {
        for (Map.Entry<String, Object> entry : mappings.entrySet()) {
            String type = entry.getKey();
            String content = indexerObjectMapper.writeValueAsString(entry.getValue());

            HttpResponse response = elasticsearchRestClient.performRequest("PUT",
                    String.format("/%s/_mapping/%s", indexName, type), content);
            IOUtils.copy(response.getEntity().getContent(), System.out);
        }
    }

    public Map<String, Object> getMappings() {
        return mappings;
    }

    public void setMappings(Map<String, Object> mappings) {
        this.mappings = mappings;
    }
}
