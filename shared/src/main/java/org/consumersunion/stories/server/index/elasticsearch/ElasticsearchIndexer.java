package org.consumersunion.stories.server.index.elasticsearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.concurrent.FutureCallback;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.index.Document;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.elasticsearch.search.Search;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchHit;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;

public abstract class ElasticsearchIndexer<T extends Document> implements Indexer<T> {
    private static final Logger LOGGER = Logger.getLogger(ElasticsearchIndexer.class.getName());
    private static final String EMPTY_OBJECT = "{}";

    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final String GET = "GET";

    private final ObjectMapper objectMapper;
    private final ElasticsearchRestClient restClient;
    private final Class<T> documentClass;
    private final String indexName;
    private final String type;

    protected ElasticsearchIndexer(
            ObjectMapper objectMapper,
            ElasticsearchRestClient restClient,
            Class<T> documentClass,
            String indexName,
            String type) {
        this.objectMapper = objectMapper;
        this.restClient = restClient;
        this.documentClass = documentClass;
        this.indexName = indexName;
        this.type = type;
    }

    @Override
    public void index(Document document) {
        assert document.getType().equals(type);

        try {
            request(objectMapper.writeValueAsString(document), PUT,
                    String.format("/%s/%s/%d?refresh=wait_for", indexName, type, document.getId()));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    @Override
    public void indexAsync(T document) {
        assert document.getType().equals(type);

        try {
            requestAsync(objectMapper.writeValueAsString(document), PUT,
                    String.format("/%s/%s/%d", indexName, type, document.getId()));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    @Override
    public void index(List<T> documents) {
        String content = getBulkContent(documents);
        try {
            request(content, POST, String.format("/%s/%s/_bulk", indexName, type));
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    @Override
    public void indexAsync(List<T> documents) {
        String content = getBulkContent(documents);
        try {
            requestAsync(content, POST, String.format("/%s/%s/_bulk", indexName, type));
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    @Override
    public T get(int id) {
        try {
            SearchHit result = get(String.format("/%s/%s/%d", indexName, type, id), SearchHit.class);

            return convertToDocument(result, documentClass);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    @Override
    public List<T> search(Search search) {
        List<T> documents = new ArrayList<T>();
        try {
            String content = objectMapper.writeValueAsString(search);
            SearchResult result = post(String.format("/%s/%s/_search", indexName, type), content, SearchResult.class);

            List<SearchHit> hits = result.getHits().getHits();
            if (hits != null) {
                FluentIterable.from(hits)
                        .transform(new Function<SearchHit, T>() {
                            @Override
                            public T apply(SearchHit input) {
                                try {
                                    return convertToDocument(input, documentClass);
                                } catch (IOException e) {
                                }
                                return null;
                            }
                        })
                        .filter(Predicates.<T>notNull())
                        .copyInto(documents);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }

        return documents;
    }

    @Override
    public void updateFromQuery(UpdateByQuery updateByQuery) {
        try {
            String content = objectMapper.writeValueAsString(updateByQuery);
            HttpResponse response = restClient.performRequest(POST,
                    String.format("/%s/%s/_update_by_query?refresh=true&wait_for_completion=true&conflicts=proceed",
                            indexName, type), content);

            LOGGER.info(IOUtils.toString(response.getEntity().getContent()));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            delete(String.format("/%s/%s/%d", indexName, type, id));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    @Override
    public void deleteByQuery(Search search) {
        try {
            String content = objectMapper.writeValueAsString(search);
            request(content, POST, String.format("/%s/%s/_delete_by_query", indexName, type));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    @Override
    public void deleteIndex() {
        try {
            delete(String.format("/%s", indexName));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    @Override
    public long count(Search search) {
        try {
            String content = objectMapper.writeValueAsString(search);
            CountResult countResult = post(String.format("/%s/%s/_count", indexName, type), content, CountResult.class);

            return countResult.getCount();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new GeneralException(e);
        }
    }

    private T convertToDocument(SearchHit input, Class<T> documentClass) throws IOException {
        JsonNode source = input.getSource();
        String content = source == null ? EMPTY_OBJECT : source.toString();

        T document = objectMapper.readValue(content, documentClass);
        document.setId(input.getId());
        return document;
    }

    private <R> R get(String endpoint, Class<R> resultClass) throws IOException {
        HttpResponse response = restClient.performRequest(GET, endpoint);

        return objectMapper.readValue(response.getEntity().getContent(), resultClass);
    }

    private <R> R post(String endpoint, String content, Class<R> resultClass) throws IOException {
        HttpResponse response = restClient.performRequest(POST, endpoint, content);

        return objectMapper.readValue(response.getEntity().getContent(), resultClass);
    }

    private void delete(String endpoint) throws IOException {
        HttpResponse response = restClient.performRequest(DELETE, endpoint);

        LOGGER.info(IOUtils.toString(response.getEntity().getContent()));
    }

    private void request(String content, String method, String endpoint) throws IOException {
        HttpResponse response = restClient.performRequest(method, endpoint, content);

        LOGGER.info(IOUtils.toString(response.getEntity().getContent()));
    }

    private void requestAsync(String content, String method, String endpoint) throws IOException {
        FutureCallback<Content> responseListener = new FutureCallback<Content>() {
            @Override
            public void completed(Content content) {
                LOGGER.info(content.asString());
            }

            @Override
            public void failed(Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }

            @Override
            public void cancelled() {
                LOGGER.info("Request cancelled");
            }
        };

        restClient.performRequestAsync(method, endpoint, content, responseListener);
    }

    private String getBulkContent(List<T> documents) {
        StringBuilder stringBuilder = new StringBuilder();
        for (T document : documents) {
            try {
                stringBuilder.append("{")
                        .append("\"index\":")
                        .append(String.format("{\"_index\": \"%s\", \"_type\": \"%s\", \"_id\": \"%d\"}",
                                indexName, document.getType(), document.getId()))
                        .append("\n")
                        .append(objectMapper.writeValueAsString(document))
                        .append("\n");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new GeneralException(e);
            }
        }

        return stringBuilder.append('\n').toString();
    }
}
