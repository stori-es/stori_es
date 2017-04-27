package org.consumersunion.stories.server.index;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.index.elasticsearch.query.Query;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.consumersunion.stories.server.index.elasticsearch.search.Search;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchBuilder;
import org.consumersunion.stories.server.persistence.SupportDataUtils;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;

public abstract class AuthIndexer<T extends Document> {
    private static final int BATCH_SIZE = 250;

    protected final List<Integer> ids;

    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final Connection conn;

    public AuthIndexer(
            SupportDataUtilsFactory supportDataUtilsFactory,
            List<Integer> ids,
            Connection conn) {
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.ids = ids;
        this.conn = conn;
    }

    public final void index() {
        if (ids.isEmpty()) {
            return;
        }

        Indexer<T> indexer = getIndexer();
        SupportDataUtils supportDataUtils = supportDataUtilsFactory.create(conn);
        int start = 0;
        do {
            Search search = createSearch(start);
            List<T> results = indexer.search(search);

            try {
                for (T document : results) {
                    updateAuths(document, supportDataUtils);
                }
            } catch (SQLException e) {
                throw new GeneralException(e);
            }

            if (!results.isEmpty()) {
                indexer.index(results);
            }

            start += BATCH_SIZE;
        } while (start < getMaxIndex(start));
    }

    protected abstract Indexer<T> getIndexer();

    protected abstract void updateAuths(T document, SupportDataUtils supportDataUtils) throws SQLException;

    private Search createSearch(int start) {
        Query query = QueryBuilder.ofIds(ids.subList(start, getMaxIndex(start)));

        return SearchBuilder.newBuilder()
                .withQuery(query)
                .withSize(BATCH_SIZE)
                .build();
    }

    private int getMaxIndex(int start) {
        if (start + BATCH_SIZE > ids.size()) {
            return ids.size();
        } else {
            return start + BATCH_SIZE;
        }
    }
}
