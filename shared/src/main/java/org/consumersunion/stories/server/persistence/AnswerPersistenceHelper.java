package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.service.GeneralException;

/**
 * Persister to handle the Answer info
 *
 * @author Machin
 */
public class AnswerPersistenceHelper {
    protected static void persistAnswers(AnswerSet answerSet, Connection connection) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(
                        "INSERT INTO answer (label, displayValue, reportValue, answerSet, idx) VALUES (?,?,?,?,?)");

        for (Answer a : answerSet.getAnswers()) {
            addAnswerToBatch(answerSet, a, ps);
        }

        int[] insertCount = ps.executeBatch();
        verifyInsertCount(insertCount, answerSet);
    }

    private static void verifyInsertCount(int[] insertCount, AnswerSet answerSet) {
        int expectedCount = 0;
        for (Answer a : answerSet.getAnswers()) {
            expectedCount += a.getReportValues().size();
        }

        if (insertCount.length != expectedCount) {
            throw new GeneralException("Unexpected insert Answer count: " + insertCount.length);
        }

        for (int c : insertCount) {
            if (c != 1) {
                throw new GeneralException("Unexpected insert Answer count: " + c);
            }
        }
    }

    private static void addAnswerToBatch(AnswerSet answerSet, Answer answer, PreparedStatement ps)
            throws SQLException {
        int i = 0;
        for (String value : answer.getReportValues()) {
            ps.setString(1, answer.getLabel());
            ps.setString(2, answer.getDisplayValue());
            ps.setString(3, value);
            ps.setInt(4, answerSet.getId());
            ps.setInt(5, i);
            ps.addBatch();
            i++;
        }
    }

    protected static void loadAnswers(AnswerSet answerSet, Connection connection) throws SQLException {
        PreparedStatement ps =
                connection.prepareStatement(
                        "SELECT a.label, a.displayValue, a.reportValue, a.answerSet " + " FROM answer a "
                                + " WHERE a.answerSet=?" + " ORDER BY label");
        ps.setInt(1, answerSet.getId());

        ResultSet rs = ps.executeQuery();

        List<Answer> answers = new ArrayList<Answer>();
        boolean hasResults = rs.next();
        for (; hasResults && !rs.isAfterLast(); ) {
            Answer a = instantiateAnswer(rs);
            answers.add(a);
        }

        answerSet.setAnswers(answers);
    }

    private static Answer instantiateAnswer(ResultSet rs) throws SQLException {
        String label = rs.getString(1);
        String displayValue = rs.getString(2);
        int answerSet = rs.getInt(4);
        List<String> reportValues = new ArrayList<String>();

        for (; (!rs.isAfterLast()) && label.equals(rs.getString(1)); ) {
            String reportValue = rs.getString(3);
            reportValues.add(reportValue);
            rs.next();
        }

        return new Answer(label, displayValue, reportValues, answerSet);
    }

    protected static void deleteAnswers(AnswerSet answerSet, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM answer" + " WHERE answerSet=?");
        ps.setInt(1, answerSet.getId());
        ps.executeUpdate();
    }
}
