package org.consumersunion.stories.server.index.story;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.consumersunion.stories.server.index.AuthIndexer;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.persistence.SupportDataUtils;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class UpdateStoriesAuthsIndexer extends AuthIndexer<StoryDocument> {
    private final Indexer<StoryDocument> indexer;

    public UpdateStoriesAuthsIndexer(
            Indexer<StoryDocument> storyIndexer,
            SupportDataUtilsFactory supportDataUtilsFactory,
            List<Integer> stories,
            Connection conn) {
        super(supportDataUtilsFactory, stories, conn);

        this.indexer = storyIndexer;
    }

    @Override
    protected Indexer<StoryDocument> getIndexer() {
        return indexer;
    }

    @Override
    protected void updateAuths(StoryDocument storyDocument, SupportDataUtils supportDataUtils) throws SQLException {
        storyDocument.setReadAuths(supportDataUtils.getStoryAuths(storyDocument.getId(), ROLE_READER));
    }
}
