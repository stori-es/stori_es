package org.consumersunion.stories.server.index.story;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchBuilder;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class StoriesIndexChecker {
    private final Provider<FullStoryIndexer> fullStoryIndexerProvider;
    private final Provider<Indexer<StoryDocument>> storyIndexerProvider;

    @Inject
    public StoriesIndexChecker(
            Provider<FullStoryIndexer> fullStoryIndexerProvider,
            @Qualifier("storyIndexer") Provider<Indexer<StoryDocument>> storyIndexerProvider) {
        this.fullStoryIndexerProvider = fullStoryIndexerProvider;
        this.storyIndexerProvider = storyIndexerProvider;
    }

    public void check()
            throws Exception {
        Indexer storyIndexer = storyIndexerProvider.get();
        long indexedStories = storyIndexer.count(SearchBuilder.ofQuery(QueryBuilder.newMatchAll()));
        long existingStories = storiesCount();

        if (indexedStories != existingStories) {
            fullStoryIndexerProvider.get().index();
        }
    }

    private long storiesCount() throws Exception {
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
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
