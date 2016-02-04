package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;

import junit.framework.TestCase;

public class AnswerPersistenceHelperTest extends TestCase {
    @Override
    public void setUp() throws Exception {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            // should be safe and possible to clear all answers (at time of
            // writing, no constraints on answers)
            conn.createStatement().executeUpdate("DELETE FROM answer");
            conn.commit();
        } finally {
            conn.close();
        }
    }

    public void testCreate() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM answer");
            if (results.next() == false) {
                TestCase.fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update

            final List<String> values = new ArrayList<String>();
            values.add("testReportValue");
            final Answer template = new Answer("0", "testDisplayValue", values, 101);
            final AnswerSet as = new AnswerSet();
            as.setId(101);
            as.addAnswer(template);

            final AnswerPersistenceHelper persister = new AnswerPersistenceHelper();
            persister.persistAnswers(as, conn);

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM answer ");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long postCount = results.getLong(1);
            conn.commit();
            TestCase.assertEquals("New user count didn't match.", initialCount + 1, postCount);

            persister.deleteAnswers(as, conn);
        } finally {
            conn.close();
        }
    }

    public void testRetrieve() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            final List<String> values = new ArrayList<String>();
            values.add("testReportValue");
            final Answer template = new Answer("0", "testDisplayValue", values, 101);
            final AnswerSet as = new AnswerSet();
            as.setId(101);
            as.addAnswer(template);

            final AnswerPersistenceHelper persister = new AnswerPersistenceHelper();
            persister.persistAnswers(as, conn);

            as.setAnswers(new ArrayList<Answer>());
            persister.loadAnswers(as, conn);

            final Answer answer = as.getAnswers().get(0);
            assertEquals("Unexpected label.", template.getLabel(), answer.getLabel());
            assertEquals("Unexpected Displayvalue.", template.getDisplayValue(), answer.getDisplayValue());
            assertEquals("Unexpected Reportvalue.", template.getReportValues(), answer.getReportValues());

            persister.deleteAnswers(as, conn);
        } finally {
            conn.close();
        }
    }

    public void testDelete() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            final List<String> values = new ArrayList<String>();
            values.add("testReportValue");
            final Answer template = new Answer("3", "testDisplayValue", values, 101);
            final AnswerSet as = new AnswerSet();
            as.setId(101);
            as.addAnswer(template);

            final AnswerPersistenceHelper persister = new AnswerPersistenceHelper();
            persister.persistAnswers(as, conn);

            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM answer");
            if (!results.next()) {
                TestCase.fail("No results");
            }

            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the udpate
            persister.deleteAnswers(as, conn);

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM answer");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long postCount = results.getLong(1);

            TestCase.assertEquals("New user count didn't match.", initialCount - 1, postCount);
        } finally {
            conn.close();
        }
    }
}
