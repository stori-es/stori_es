package org.consumersunion.stories.server.persistence.document;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.server.persistence.AnswerSetPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;

public class AnswerSetPersisterTest extends SpringTestCase {
    @Inject
    private AnswerSetPersister answerSetPersister;

    public void testCreate() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM answerSet");
            if (results.next() == false) {
                fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update
            final AnswerSet template = createAnswerSetTemplate();
            final AnswerSet answerSet = answerSetPersister.create(template);
            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM answerSet ");
            if (!results.next()) {
                fail("No results");
            }
            final long postCount = results.getLong(1);
            conn.commit();
            assertNotNull(answerSet);
            assertEquals("New user count didn't match.", initialCount + 1, postCount);
            PersistenceUtil.process(new AnswerSetPersister.AnswerSetDeleteFunc(answerSet));
        } finally {
            conn.close();
        }
    }

    public void testDelete() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            final AnswerSet template = createAnswerSetTemplate();
            final AnswerSet answerSet = answerSetPersister.create(template);

            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM answerSet ");
            if (!results.next()) {
                fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update
            PersistenceUtil.process(new AnswerSetPersister.AnswerSetDeleteFunc(answerSet));

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM answerSet");
            if (!results.next()) {
                fail("No results");
            }
            final long postCount = results.getLong(1);

            assertEquals("New user count didn't match.", initialCount - 1, postCount);
        } finally {
            conn.close();
        }
    }

    public void testRetrieve() {
        AnswerSet template = createAnswerSetTemplate();
        template = answerSetPersister.create(template);

        final AnswerSet answerSet = answerSetPersister.get(template.getId());

        assertEquals("Unexpected id.", template.getId(), answerSet.getId());
        // assertEquals("Unexpected User.", template.getUser(),
        // answerSet.getUser()); Came 0 when its null
        assertEquals("Unexpected Questionnaire.", template.getQuestionnaire(), answerSet.getQuestionnaire());
        PersistenceUtil.process(new AnswerSetPersister.AnswerSetDeleteFunc(answerSet));
    }

    private AnswerSet createAnswerSetTemplate() {
        final AnswerSet template = new AnswerSet();
        template.setEntity(1);
        template.setPermalink("answer-set-1");
        template.setSystemEntityRelation(SystemEntityRelation.BODY);
        template.setQuestionnaire(33);
        template.setPrimaryAuthor(1001);
        template.setLocale(Locale.ENGLISH);
        return template;
    }
}
