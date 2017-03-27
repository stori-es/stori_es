package org.consumersunion.stories.server.solr;

import java.util.Collection;
import java.util.HashSet;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;

public class UpdateEntityReadIndex implements Indexer {
    public final static int STORY_CORE = 0;
    public final static int PERSON_CORE = 1;

    private final int id;
    private final int core;
    private final int[] subjects;

    public UpdateEntityReadIndex(int id, int core, int... subjects) {
        this.id = id;
        this.core = core;
        this.subjects = subjects;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("id:" + id);

        QueryResponse result;
        if (core == STORY_CORE) {
            result = solrStoryServer.query(query);
        } else {
            result = solrPersonServer.query(query);
        }

        if (!result.getResults().isEmpty()) {
            SolrDocument document = result.getResults().get(0);

            Collection<Integer> readAuths = (Collection<Integer>) (Collection<?>) document.getFieldValues("readAuths");
            if (readAuths == null) {
                readAuths = new HashSet<Integer>();
            }

            for (int subject : subjects) {
                if (!readAuths.contains(subject)) {
                    readAuths.add(subject);
                }
            }
            // If it's a new user, we must add here, adding right after instantiation doesn't work. If the document
            // already has a collection, then adding it back here causes a ConcurrentModificationException. This all
            // seems clunky, but this seems to be the only way it works.
            if (document.getFieldValues("readAuths") == null) {
                document.addField("readAuths", readAuths);
            }

            if (core == STORY_CORE) {
                solrStoryServer.add(ClientUtils.toSolrInputDocument(document));
                solrStoryServer.commit();
            } else if (core == PERSON_CORE) {
                solrPersonServer.add(ClientUtils.toSolrInputDocument(document));
                solrPersonServer.commit();
            }
        }
    }
}
