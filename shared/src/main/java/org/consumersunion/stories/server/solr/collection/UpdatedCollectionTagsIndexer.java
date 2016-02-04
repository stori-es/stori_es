package org.consumersunion.stories.server.solr.collection;

import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.server.solr.Indexer;

import com.google.common.collect.Lists;

public class UpdatedCollectionTagsIndexer implements Indexer {
    private final Collection collection;
    private final List<String> tags;

    public UpdatedCollectionTagsIndexer(Collection collection, java.util.Collection<String> tags) {
        this.collection = collection;
        this.tags = Lists.newArrayList(tags);
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("id:" + collection.getId());

        QueryResponse result = solrCollectionServer.query(query);
        if (!result.getResults().isEmpty()) {
            CollectionDocument collectionDoc = new CollectionDocument(result.getResults().get(0));
            collectionDoc.setLastModified(new Date());
            SolrInputDocument inputDoc = collectionDoc.toDocument();

            inputDoc.setField("tags", tags);

            List<SolrInputDocument> docs = Lists.<SolrInputDocument>newArrayList(inputDoc);
            solrCollectionServer.add(docs);
            solrCollectionServer.commit();
        }
    }

    public String toString() {
        return "-- Indexing new tags for " + collection.getId();
    }
}
