package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.CreateFunc;
import org.consumersunion.stories.server.persistence.funcs.DeleteFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.springframework.stereotype.Component;

/**
 * Persister to handle the Answer info. Note, you cannot update an <code>AnswerSet</code>... or rather, you can't
 * update the <code>AnswerSet</code> specific data. Certain <code>Document</code> elements may be updated.
 *
 * @author Evelyn Juarez
 */
@Component
public class AnswerSetPersister implements Persister<AnswerSet> {
    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final PersistenceService persistenceService;
    private final DocumentPersister documentPersister;

    @Inject
    AnswerSetPersister(
            SupportDataUtilsFactory supportDataUtilsFactory,
            PersistenceService persistenceService,
            DocumentPersister documentPersister) {
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.persistenceService = persistenceService;
        this.documentPersister = documentPersister;
    }

    @Override
    public Class<AnswerSet> getHandles() {
        return AnswerSet.class;
    }

    @Override
    public AnswerSet get(int id) {
        return persistenceService.process(new RetrieveAnswerSetFunc(id, this));
    }

    @Override
    public AnswerSet get(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveAnswerSetFunc(id, this));
    }

    public AnswerSet create(AnswerSet answerSet) {
        return persistenceService.process(new AnswerSetCreateFunc(answerSet, documentPersister));
    }

    public AnswerSet create(AnswerSet answerSet, Connection conn) {
        return persistenceService.process(conn, new AnswerSetCreateFunc(answerSet, documentPersister));
    }

    public static class AnswerSetCreateFunc extends CreateFunc<AnswerSet> {
        private final DocumentPersister documentPersister;

        AnswerSetCreateFunc(AnswerSet input, DocumentPersister documentPersister) {
            super(input);
            this.documentPersister = documentPersister;
        }

        @Override
        protected AnswerSet createConcrete() {
            try {
                documentPersister.create(input, conn);
                final PreparedStatement insertAnswerSet = conn
                        .prepareStatement("INSERT INTO answerSet (id, questionnaire) VALUES (?,?)");

                insertAnswerSet.setInt(1, input.getId());
                insertAnswerSet.setInt(2, input.getQuestionnaire());
                insertAnswerSet.executeUpdate();

                AnswerPersistenceHelper.persistAnswers(input, conn);
                return input;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrieveAnswerSetFunc extends RetrieveFunc<AnswerSet> {
        private final AnswerSetPersister answerSetPersister;

        public RetrieveAnswerSetFunc(
                Integer id,
                AnswerSetPersister answerSetPersister) {
            super(id);

            this.answerSetPersister = answerSetPersister;
        }

        @Override
        protected AnswerSet retrieveConcrete() throws SQLException {
            PreparedStatement select = conn
                    .prepareStatement("SELECT se.id, se.version, se.created, se.lastModified, se.public, "
                            + "d.primaryAuthor, a.questionnaire, d.locale, p.givenName, p.surname, d.systemEntity "
                            + "FROM systemEntity se JOIN answerSet a ON a.id=se.id JOIN document d ON d.id=a.id "
                            + "LEFT JOIN profile p ON d.primaryAuthor=p.id "
                            + "WHERE se.id=? ");

            select.setInt(1, input);
            ResultSet rs = select.executeQuery();

            if (!rs.next()) {
                return null;
            }

            AnswerSet answerSet = new AnswerSet(rs.getInt(1), rs.getInt(2));
            answerSet.setCreated(getTimestamp(rs, 3));
            answerSet.setUpdated(getTimestamp(rs, 4));
            answerSet.setPublic(rs.getBoolean(5));
            answerSet.setPrimaryAuthor(rs.getInt(6));
            answerSet.setQuestionnaire(rs.getInt(7));
            answerSet.setLocale(Locale.fromCode(rs.getString(8)));
            answerSet.setPrimaryAuthorFirstName(rs.getString(9));
            answerSet.setPrimaryAuthorLastName(rs.getString(10));
            answerSet.setEntity(rs.getInt(11));
            answerSet.setSystemEntityRelation(SystemEntityRelation.ANSWER_SET);

            AnswerPersistenceHelper.loadAnswers(answerSet, conn);

            SupportDataUtils dataUtils = answerSetPersister.supportDataUtilsFactory.create(conn);
            answerSet.setAuthorAddress(dataUtils.getPrimaryAddress(answerSet.getPrimaryAuthor()));

            return answerSet;
        }

        private Timestamp getTimestamp(ResultSet rs, int i) {
            try {
                return rs.getTimestamp(i);
            } catch (SQLException ignored) {
                return null;
            }
        }
    }

    public static class AnswerSetDeleteFunc extends DeleteFunc<AnswerSet> {
        public AnswerSetDeleteFunc(AnswerSet i) {
            super(i);
        }

        @Override
        public AnswerSet process() {
            try {
                conn.setSavepoint();
                PreparedStatement delete = conn.prepareStatement("DELETE FROM answerSet WHERE id=? ");
                PreparedStatement deleteD = conn.prepareStatement("DELETE FROM document WHERE id=?");
                PreparedStatement deleteSe = conn.prepareStatement("DELETE FROM systemEntity WHERE id=?");
                delete.setInt(1, input.getId());
                deleteD.setInt(1, input.getId());
                deleteSe.setInt(1, input.getId());

                AnswerPersistenceHelper.deleteAnswers(input, conn);
                int updateCount = delete.executeUpdate();
                deleteD.executeUpdate();
                deleteSe.executeUpdate();

                if (updateCount != 1) {
                    throw new GeneralException("Unexpected insert count: " + updateCount);
                }
                conn.commit();

                return input;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
                throw new GeneralException(e);
            }
        }
    }
}
