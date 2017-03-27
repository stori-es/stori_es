package org.consumersunion.stories.server.solr.story;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.solr.IndexChecker;
import org.consumersunion.stories.server.solr.SolrServer;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;
import org.springframework.stereotype.Component;

@Component
public class StoriesIndexChecker implements IndexChecker {
    private final FullStoryIndexer fullStoryIndexer;

    @Inject
    public StoriesIndexChecker(FullStoryIndexer fullStoryIndexer) {
        this.fullStoryIndexer = fullStoryIndexer;
    }

    @Override
    public void check(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("type:" + IndexedStoryDocument.class.getSimpleName());
        QueryResponse result = solrStoryServer.query(query);

        Long indexedStories = result.getResults().getNumFound();
        Long existingStories = storiesCount();

        if (indexedStories.longValue() != existingStories.longValue()) {
            fullStoryIndexer.index(solrStoryServer, solrCollectionServer, solrPersonServer);
        }
    }

    private Long storiesCount() throws Exception {
        Connection connection = null;
        try {
            connection = PersistenceUtil.getConnection();
            PreparedStatement countStmt = connection.prepareStatement("SELECT COUNT(*)" +
                    " FROM (SELECT DISTINCT s.id" +
                    "      FROM document d" +
                    "        JOIN profile p ON p.id = d.primaryAuthor" +
                    "        JOIN document t ON d.id = t.id" +
                    "        JOIN story s ON d.systemEntity = s.id" +
                    "        JOIN systemEntity e ON s.id = e.id" +
                    "        LEFT OUTER JOIN document dt2 ON (t.id = dt2.id AND dt2.version > t.version)" +
                    "      WHERE d.systemEntityRelation = 'BODY' AND dt2.id IS NULL) A");
            ResultSet resultSet = countStmt.executeQuery();
            resultSet.next();

            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
