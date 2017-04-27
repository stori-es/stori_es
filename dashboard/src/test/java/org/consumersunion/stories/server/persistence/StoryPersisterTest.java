package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.server.persistence.StoryPersister.StoryPagedRetrieveParams;

public class StoryPersisterTest extends SpringTestCase {
    @Inject
    private StoryPersister persister;
    @Inject
    private DocumentPersister documentPersister;
    @Inject
    private PersistenceService persistenceService;

    public void testCreate() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        final String byLine = "Created by someone";
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM story");
            if (!results.next()) {
                fail("No results");
            }

            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update

            final Story template = createTemplate();
            template.setByLine(byLine);

            final Story storyResult = PersistenceUtil.process(new StoryPersister.CreateStoryFunc(template));

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM story");
            if (!results.next()) {
                fail("No results");
            }
            final long postCount = results.getLong(1);

            assertEquals("New story count didn't match.", initialCount + 1, postCount);
            assertTrue("Bad ID", template.getId() > 0);
            assertEquals("Unexpected version", 1, template.getVersion());
            assertEquals("ByLine was not inserted properly", byLine, storyResult.getByLine());
            assertEquals("AnswerSetId was not persisted", (Integer) 6,
                    template.getOnlyDocument(SystemEntityRelation.ANSWER_SET));
            assertNotNull(storyResult);

            PersistenceUtil.process(new StoryPersister.DeleteStoryFunc(template, persister));
        } finally {
            conn.close();
        }
    }

    public void testRetrieve() {
        final Story template = createTemplate();
        PersistenceUtil.process(new StoryPersister.CreateStoryFunc(template));

        final Story retrieved = PersistenceUtil.process(new StoryPersister.RetrieveStoryFunc(template.getId()));

        assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
        assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
        assertEquals("Unexpected permalink.", template.getPermalink(), retrieved.getPermalink());
        assertEquals("Unexpected defaultContent", template.getDefaultContent(),
                retrieved.getDefaultContent());
        assertEquals("Unexpected owner", template.getOwner(), retrieved.getOwner());
        assertEquals("Unexpected ByLine", template.getByLine(), retrieved.getByLine());

        // MySQL ignores the millis of the timestamp.
        final long diff = Math.abs(template.getFirstPublished().getTime() - retrieved.getFirstPublished().getTime());
        assertTrue("Unexpected FirstPublished date", diff < 1000);

        PersistenceUtil.process(new StoryPersister.DeleteStoryFunc(template, persister));
    }

    public void testUpdate() {
        final Story template = createTemplate();
        PersistenceUtil.process(new StoryPersister.CreateStoryFunc(template));

        final String originalByLine = template.getByLine();
        template.setByLine("Another byline");
        template.setPermalink("perma2");

        PersistenceUtil.process(new StoryPersister.UpdateStoryFunc(template));
        assertEquals("Unexpected version.", 2, template.getVersion());

        final Story retrieved = PersistenceUtil.process(new StoryPersister.RetrieveStoryFunc(template.getId()));

        assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
        assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
        assertEquals("Unexpected permalink.", template.getPermalink(), retrieved.getPermalink());
        assertEquals("Unexpected defaultContent", template.getDefaultContent(),
                retrieved.getDefaultContent());
        assertEquals("Unexpected owner", template.getOwner(), retrieved.getOwner());
        assertEquals("ByLine should not be updated", originalByLine, retrieved.getByLine());

        PersistenceUtil.process(new StoryPersister.DeleteStoryFunc(template, persister));
    }

    public void testDelete() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            final Story template = createTemplate();
            PersistenceUtil.process(new StoryPersister.CreateStoryFunc(template));

            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM story");
            if (!results.next()) {
                fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the udpate
            PersistenceUtil.process(new StoryPersister.DeleteStoryFunc(template, persister));

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM story");
            if (!results.next()) {
                fail("No results");
            }
            final long postCount = results.getLong(1);

            assertEquals("New collection count didn't match.", initialCount - 1, postCount);
            assertEquals("Bad ID", -1, template.getId());
            assertEquals("Bad ID", -1, template.getVersion());
        } finally {
            conn.close();
        }
    }

    /* This test requires Solr index to be run */
    public void _testSearchSpecificField() {
        StoryPagedRetrieveParams params = new StoryPagedRetrieveParams(0, 0, StorySortField.CREATED_OLD, false,
                "title:title6", ACCESS_MODE_ANY, 1, null, null, null, null, null, ROLE_READER, 0);

        List<StorySummary> stories = persistenceService.process(persister.getStoriesPagedFunc(params, false));

        assertEquals("Unexpected story count.", 1, stories.size());
        assertEquals("Unexpected story title.", "Story Title6", stories.get(0).getTitle());
    }

    private Story createTemplate() {
        final Story s = new Story(-1, -1);
        s.setDefaultContent(55);
        s.setFirstPublished(new Date());
        s.setOwner(1001);
        s.setPermalink("permalink");
        s.setPublished(true);
        s.setByLine("byline");
        s.addDocument(SystemEntityRelation.ANSWER_SET, 6);

        return s;
    }

    public void testLimitCharacters() {
        final String testString = " Hey my name is <single/><em>Zane</em>. I like cheese.";

        final String result1 = persister.limitCharacters(testString, 1);
        assertEquals("Hey...", result1);

        final String result2 = persister.limitCharacters(testString, 4);
        assertEquals("Hey...", result2);

        final String result3 = persister.limitCharacters(testString, 5);
        assertEquals("Hey my...", result3);

        final String result4 = persister.limitCharacters(testString, 16);
        assertEquals("Hey my name is Zane...", result4);
    }
}
