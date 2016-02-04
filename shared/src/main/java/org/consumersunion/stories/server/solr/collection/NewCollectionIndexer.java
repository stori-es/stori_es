package org.consumersunion.stories.server.solr.collection;

import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.server.solr.Indexer;

public class NewCollectionIndexer implements Indexer {
    private final Collection collection;
    private final Set<Integer> readAuths;
    private final Set<Integer> writeAuths;
    private final Set<Integer> adminAuths;
    private final Set<String> tags;
    private final Set<String> admins;

    public NewCollectionIndexer(
            Collection collection,
            Set<Integer> readAuths,
            Set<Integer> writeAuths,
            Set<Integer> adminAuths,
            Set<String> tags,
            Set<String> admins) {
        this.collection = collection;
        this.readAuths = readAuths;
        this.writeAuths = writeAuths;
        this.adminAuths = adminAuths;
        this.tags = tags;
        this.admins = admins;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        CollectionDocument collectionDocument =
                new CollectionDocument(collection.getId(), collection.getTitle(), collection.getCreated(),
                        collection.getUpdated(), collection.isPublic(), collection.getDeleted(),
                        collection.getOwner(), tags, readAuths, writeAuths, adminAuths, admins);
        solrCollectionServer.add(collectionDocument.toDocument());
        solrCollectionServer.commit();
    }
}
