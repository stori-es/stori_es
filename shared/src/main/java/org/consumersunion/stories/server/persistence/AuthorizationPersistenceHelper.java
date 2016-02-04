package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.solr.CoreIndexer;
import org.consumersunion.stories.server.solr.IndexerFactory;
import org.consumersunion.stories.server.solr.collection.UpdateCollectionsAuthsIndexer;
import org.consumersunion.stories.server.solr.story.UpdateStoriesAuthsIndexer;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationPersistenceHelper {
    private static final Logger logger = Logger.getLogger(AuthorizationPersistenceHelper.class.getName());

    private final IndexerFactory indexerFactory;
    private final CoreIndexer coreIndexer;
    private final PersistenceService persistenceService;

    @Inject
    AuthorizationPersistenceHelper(
            IndexerFactory indexerFactory,
            CoreIndexer coreIndexer,
            PersistenceService persistenceService) {
        this.indexerFactory = indexerFactory;
        this.coreIndexer = coreIndexer;
        this.persistenceService = persistenceService;
    }

    public void grantToAllProfilesInOrganization(Connection conn, int role, int organization, int target)
            throws SQLException {
        List<Integer> profiles = persistenceService.process(conn,
                new ProfilePersister.RetrieveProfilesIdByOrganizationFunc(organization));

        doGrantInOrganization(conn, role, target, profiles);
    }

    public void grantToCollectionsInOrganization(Connection conn, int role, int organization, int subject)
            throws SQLException {
        List<Integer> collectionIds = persistenceService.process(conn,
                new CollectionPersister.RetrieveCollectionIdsFunc(organization));

        doGrantInOrganization(conn, role, collectionIds, subject);

        List<Integer> storyIds = PersistenceUtil.process(conn,
                new StoryPersister.RetrieveStoryIds(organization));

        UpdateCollectionsAuthsIndexer updateCollectionsAuthsIndexer =
                indexerFactory.createCollectionsAuth(collectionIds, conn);
        UpdateStoriesAuthsIndexer updateStoriesAuthsIndexer = indexerFactory.createStoriesAuth(storyIds, conn);

        coreIndexer.process(updateCollectionsAuthsIndexer);
        coreIndexer.process(updateStoriesAuthsIndexer);
    }

    private void doGrantInOrganization(Connection conn, int role, int target,
            List<Integer> subjects) {
        for (Integer subject : subjects) {
            persistenceService.process(conn, new GrantFunc(new AuthOperation(subject, role, target)));
        }
    }

    private void doGrantInOrganization(Connection conn, int role, List<Integer> targets, Integer subject) {
        for (Integer target : targets) {
            persistenceService.process(conn, new GrantFunc(new AuthOperation(subject, role, target)));
        }
    }

    public static class AuthOperation {
        final int subject;
        final int role;

        int target;
        String joinTable;

        public AuthOperation(int subject, int role, int target) {
            this.subject = subject;
            this.role = role;
            this.target = target;
        }

        public AuthOperation(int subject, int role, String joinTable) {
            this.subject = subject;
            this.role = role;
            this.joinTable = joinTable;
        }
    }

    public static class GrantFunc extends ProcessFunc<AuthOperation, Void> {
        public GrantFunc(AuthOperation authOperation) {
            super(authOperation);
        }

        @Override
        public Void process() {
            try {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO acl_entry (acl_object_identity, sid, mask) VALUES (?,?,?)"
                                + "ON DUPLICATE KEY UPDATE mask=VALUES(mask)");

                stmt.setInt(1, input.target);
                stmt.setInt(2, input.subject);
                stmt.setInt(3, input.role);
                stmt.execute();
            } catch (SQLException e) {
                throw new GeneralException(e);
            }

            return null;
        }
    }

    public static class GrantAtLeastFunc extends ProcessFunc<AuthOperation, Void> {
        public GrantAtLeastFunc(AuthOperation authOperation) {
            super(authOperation);
        }

        @Override
        public Void process() {
            try {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO acl_entry (acl_object_identity, sid, mask) VALUES (?, ?, ?) "
                                + "ON DUPLICATE KEY UPDATE mask=IF(mask > VALUES(mask), mask, VALUES(mask))");

                stmt.setInt(1, input.target);
                stmt.setInt(2, input.subject);
                stmt.setInt(3, input.role);
                stmt.execute();
            } catch (SQLException e) {
                throw new GeneralException(e);
            }

            return null;
        }
    }

    public static class CheckGrant extends ProcessFunc<AuthOperation, Boolean> {
        public CheckGrant(AuthOperation authOperation) {
            super(authOperation);
        }

        @Override
        public Boolean process() {
            try {
                String searchString = "SELECT COUNT(*) FROM acl_entry WHERE sid=? AND mask>=? AND acl_object_identity=?";
                PreparedStatement retrive = conn.prepareStatement(searchString);
                retrive.setInt(1, input.subject);
                retrive.setInt(2, input.role);
                retrive.setInt(3, input.target);

                ResultSet results = retrive.executeQuery();
                return results.next() && results.getInt(1) > 0;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class DenyFunc extends ProcessFunc<AuthOperation, Void> {
        public DenyFunc(AuthOperation authOperation) {
            super(authOperation);
        }

        @Override
        public Void process() {
            try {
                PreparedStatement statement =
                        conn.prepareStatement("DELETE FROM acl_entry WHERE sid=? AND acl_object_identity=?");
                statement.setInt(1, input.subject);
                statement.setInt(2, input.target);
                statement.executeUpdate();

                return null;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class GetValidTargets extends ProcessFunc<AuthOperation, List<SystemEntity>> {
        public GetValidTargets(AuthOperation authOperation) {
            super(authOperation);
        }

        @Override
        public List<SystemEntity> process() {
            List<SystemEntity> list = new ArrayList<SystemEntity>();

            int subject = input.subject;
            int minRole = input.role;
            String joinTable = input.joinTable;

            try {
                boolean isRootRequest = subject == AuthConstants.ROOT_ID;

                String searchString = "SELECT DISTINCT(systemEntity.id), systemEntity.version FROM systemEntity "
                        + (isRootRequest ? "" : "JOIN acl_entry ON systemEntity.id  = acl_entry.acl_object_identity ")
                        + (joinTable != null && !"organization".equals(joinTable)
                        ? "JOIN " + joinTable + " joinTable ON joinTable.id=systemEntity.id" : "")
                        + (isRootRequest ? "" : " WHERE acl_entry.sid = ?  AND acl_entry.mask>= ? "
                        + ("organization".equals(joinTable)
                        ? "AND acl_entry.acl_object_identity IN (SELECT id FROM " + "organization)" : ""));

                PreparedStatement retrive = conn.prepareStatement(searchString);
                if (!isRootRequest) {
                    retrive.setString(1, String.valueOf(subject));
                    retrive.setInt(2, minRole);
                }

                ResultSet results = retrive.executeQuery();

                while (results.next()) {
                    list.add(new SystemEntity(results.getInt(1), results.getInt(2)));
                }

                return list;
            } catch (SQLException e) {
                logger.severe("Could not retrive information from " + subject + " with min role  " + minRole
                        + " , this error was display: " + e.getMessage());
                e.printStackTrace();
            }

            return list;
        }
    }

    public static class GetValidPrincipals extends ProcessFunc<AuthOperation, List<SystemEntity>> {
        public GetValidPrincipals(AuthOperation authOperation) {
            super(authOperation);
        }

        @Override
        public List<SystemEntity> process() {
            List<SystemEntity> list = new ArrayList<SystemEntity>();

            int target = input.subject;
            int minRole = input.role;
            String joinTable = input.joinTable;
            try {
                String searchString = "SELECT distinct(systemEntity.id), systemEntity.version FROM systemEntity "
                        + "JOIN acl_entry ON systemEntity.id  = acl_entry.sid "
                        + (joinTable != null ? "JOIN " + joinTable + " joinTable ON joinTable.id=systemEntity.id" : "")
                        + " WHERE acl_entry.acl_object_identity = ? AND acl_entry.mask>= ?";

                PreparedStatement retrive = conn.prepareStatement(searchString);
                retrive.setString(1, String.valueOf(target));
                retrive.setInt(2, minRole);

                ResultSet results = retrive.executeQuery();

                while (results.next()) {
                    list.add(new SystemEntity(results.getInt(1), results.getInt(2)));
                }

                return list;
            } catch (SQLException e) {
                logger.severe("Could not retrive valid principals with min role " + minRole + " for " + target
                        + " , this error was display: " + e.getMessage());
                e.printStackTrace();
            }

            return list;
        }
    }
}
