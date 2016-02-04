package org.consumersunion.stories.server.solr;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public abstract class AuthIndexer implements Indexer {
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

    @Override
    public final void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        if (ids.isEmpty()) {
            return;
        }

        SupportDataUtils supportDataUtils = supportDataUtilsFactory.create(conn);
        int start = 0;
        SolrServer solrServer = getEffectiveServer(solrStoryServer, solrCollectionServer, solrPersonServer);
        SolrQuery query = createQuery(start);
        QueryResponse result = solrServer.query(query, SolrRequest.METHOD.POST);
        while (start < getMaxIndex(start)) {
            List<SolrInputDocument> documents = Lists.newArrayList();
            for (SolrDocument solrDocument : result.getResults()) {
                SolrInputDocument document = getDocument(solrDocument, supportDataUtils);
                documents.add(document);
            }

            if (!documents.isEmpty()) {
                solrServer.add(documents);
                solrServer.commit();
            }

            start += BATCH_SIZE;
            if (start < getMaxIndex(start)) {
                query = createQuery(start);
                result = solrServer.query(query, SolrRequest.METHOD.POST);
            }
        }
    }

    private SolrQuery createQuery(int start) {
        SolrQuery query = new SolrQuery(
                "id:(" + Joiner.on(" OR ").join(ids.subList(start, getMaxIndex(start))) + ")");
        query.setRows(BATCH_SIZE);

        return query;
    }

    protected abstract org.apache.solr.common.SolrInputDocument getDocument(SolrDocument solrDocument,
            SupportDataUtils supportDataUtils)
            throws SQLException;

    protected abstract SolrServer getEffectiveServer(
            SolrServer solrStoryServer,
            SolrServer solrCollectionServer,
            SolrServer solrPersonServer);

    private int getMaxIndex(int start) {
        if (start + BATCH_SIZE > ids.size()) {
            return ids.size();
        } else {
            return start + BATCH_SIZE;
        }
    }
}
