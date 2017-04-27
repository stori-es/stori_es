package org.consumersunion.stories.server.index.elasticsearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.http.nio.entity.NStringEntity;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.index.Document;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.elasticsearch.search.Search;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchHit;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchResult;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.rest.RestRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;

import static org.elasticsearch.rest.RestRequest.Method.DELETE;
import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;
import static org.elasticsearch.rest.RestRequest.Method.PUT;

public abstract class ElasticsearchIndexer<T extends Document> implements Indexer<T> {
    private static final Logger LOGGER = Logger.getLogger(ElasticsearchIndexer.class.getName());
    private static final String EMPTY_OBJECT = "{}";

    private final ObjectMapper objectMapper;
    private final Provider<RestClient> restClientProvider;
    private final Class<T> documentClass;
    private final String indexName;
    private final String type;

    protected ElasticsearchIndexer(
            ObjectMapper objectMapper,
            Provider<RestClient> restClientProvider,
            Class<T> documentClass,
            String indexName,
            String type) {
        this.objectMapper = objectMapper;
        this.restClientProvider = restClientProvider;
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
        RestClient restClient = restClientProvider.get();
        try {
            String content = objectMapper.writeValueAsString(updateByQuery);
            Response response = restClient.performRequest(POST.name(),
                    String.format("/%s/%s/_update_by_query", indexName, type),
                    Collections.<String, String>emptyMap(), new NStringEntity(content));

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
        RestClient restClient = restClientProvider.get();
        Response response = restClient.performRequest(GET.name(), endpoint,
                Collections.<String, String>emptyMap());

        return objectMapper.readValue(response.getEntity().getContent(), resultClass);
    }

    private <R> R post(String endpoint, String content, Class<R> resultClass) throws IOException {
        RestClient restClient = restClientProvider.get();
        Response response = restClient.performRequest(POST.name(), endpoint,
                Collections.<String, String>emptyMap(), new NStringEntity(content));

        return objectMapper.readValue(response.getEntity().getContent(), resultClass);
    }

    private void delete(String endpoint) throws IOException {
        RestClient restClient = restClientProvider.get();

        Response response = restClient.performRequest(DELETE.name(), endpoint, Collections.<String, String>emptyMap());

        LOGGER.info(IOUtils.toString(response.getEntity().getContent()));
    }

    private void request(String content, RestRequest.Method method, String endpoint) throws IOException {
        RestClient restClient = restClientProvider.get();
        Response response = restClient.performRequest(method.name(), endpoint,
                Collections.<String, String>emptyMap(), new NStringEntity(content));

        LOGGER.info(IOUtils.toString(response.getEntity().getContent()));
    }

    private void requestAsync(String content, RestRequest.Method method, String endpoint) throws IOException {
        RestClient restClient = restClientProvider.get();

        ResponseListener responseListener = new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    LOGGER.info(IOUtils.toString(response.getEntity().getContent()));
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    throw new GeneralException(e);
                }
            }

            @Override
            public void onFailure(Exception exception) {
                LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
            }
        };

        restClient.performRequestAsync(method.name(), endpoint, Collections.<String, String>emptyMap(),
                new NStringEntity(content), responseListener);
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
