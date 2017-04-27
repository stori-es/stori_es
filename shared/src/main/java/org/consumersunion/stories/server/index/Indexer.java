package org.consumersunion.stories.server.index;

import java.util.List;

import org.consumersunion.stories.server.index.elasticsearch.UpdateByQuery;
import org.consumersunion.stories.server.index.elasticsearch.search.Search;

public interface Indexer<T extends Document> {
    int BATCH_SIZE = 250;

    void index(T document);

    void index(List<T> documents);

    void indexAsync(T document);

    void indexAsync(List<T> documents);

    T get(int id);

    List<T> search(Search search);

    void updateFromQuery(UpdateByQuery updateByQuery);

    long count(Search search);

    void delete(int id);

    void deleteByQuery(Search search);

    void deleteIndex();
}
