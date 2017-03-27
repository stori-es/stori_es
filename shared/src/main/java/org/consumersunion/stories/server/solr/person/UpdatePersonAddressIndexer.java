package org.consumersunion.stories.server.solr.person;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.SolrServer;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;

public class UpdatePersonAddressIndexer implements Indexer {
    private final int idx;
    private final Address address;

    public UpdatePersonAddressIndexer(int idx, Address address) {
        this.idx = idx;
        this.address = address;
    }

    public UpdatePersonAddressIndexer(Address address) {
        this(0, address);
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        if (idx == 0) { // then it's the primary address
            // first update the Person
            SolrQuery query = new SolrQuery("id:" + address.getEntity());

            QueryResponse result = solrPersonServer.query(query);
            if (result.getResults().size() > 0) {
                ProfileDocument profileDocument = new ProfileDocument(result.getResults().get(0));
                profileDocument.setPrimaryCity(address.getCity());
                profileDocument.setPrimaryState(address.getState());
                profileDocument.setPrimaryPostalCode(address.getPostalCode());
                profileDocument.setPrimaryAddress1(address.getAddress1());

                solrPersonServer.add(profileDocument.toDocument());
                solrPersonServer.commit();
            }

            // now update the Stories
            query = new SolrQuery("authorId:" + address.getEntity());

            result = solrStoryServer.query(query);
            for (int i = 0; i < result.getResults().size(); i += 1) {
                IndexedStoryDocument storyDocument = new IndexedStoryDocument(result.getResults().get(i));
                storyDocument.setAddress(address);

                solrStoryServer.add(storyDocument.toDocument());
            }
            if (result.getResults().size() > 0) {
                solrStoryServer.commit();
            }
        }
    }
}
