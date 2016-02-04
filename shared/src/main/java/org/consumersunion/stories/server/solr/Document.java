package org.consumersunion.stories.server.solr;

import org.apache.solr.common.SolrInputDocument;

public interface Document {
    SolrInputDocument toDocument();
}
