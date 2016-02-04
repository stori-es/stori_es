package org.consumersunion.stories.server.solr.story;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;

import com.google.common.collect.Sets;

public class UpdatedStoryTagsIndexer implements Indexer {
    private final Story story;
    private final Set<String> tags;

    public UpdatedStoryTagsIndexer(Story story, Collection<String> tags) {
        this.story = story;
        this.tags = Sets.newLinkedHashSet(tags);
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("id:" + story.getId());

        QueryResponse result = solrStoryServer.query(query);
        if (result.getResults().size() > 0) {
            IndexedStoryDocument storyDocument = new IndexedStoryDocument(result.getResults().get(0));
            storyDocument.setTags(tags);
            storyDocument.setLastModified(new Date());

            solrStoryServer.add(storyDocument.toDocument());
            solrStoryServer.commit();
        }
    }

    public String toString() {
        return "-- Indexing new tags for " + story.getId();
    }
}
