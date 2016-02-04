package org.consumersunion.stories.server.solr.story;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;

public class UpdateStoryAddressIndexer implements Indexer {
    private final int idx;
    private final Address address;

    public UpdateStoryAddressIndexer(int idx, Address address) {
        this.idx = idx;
        this.address = address;
    }

    public UpdateStoryAddressIndexer(Address address) {
        this(0, address);
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        if (idx == 0) { // then it's the primary address
            SolrQuery query = new SolrQuery("ownerId:" + address.getEntity());

            QueryResponse result = solrStoryServer.query(query);
            if (result.getResults().size() > 0) {
                IndexedStoryDocument storyDocument = new IndexedStoryDocument(result.getResults().get(0));
                storyDocument.setAddress(address);

                solrStoryServer.add(storyDocument.toDocument());
                solrStoryServer.commit();
            }
        }
    }
}
