package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang3.ArrayUtils;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.ConvioSyncStatus;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryAndStorytellerData;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchBuilder;
import org.consumersunion.stories.server.index.profile.ProfileDocument;
import org.consumersunion.stories.server.persistence.funcs.CreateFunc;
import org.consumersunion.stories.server.persistence.funcs.DeleteFunc;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.consumersunion.stories.server.persistence.funcs.UpdateFunc;
import org.consumersunion.stories.server.persistence.params.PagedRetrieveParams;
import org.consumersunion.stories.server.security.RelationalAuthorizationQueryUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.common.shared.AuthConstants.ROOT_ID;

@Component
public class ProfilePersister implements Persister<Profile>, MineCallbackProvider {
    private final PersistenceService persistenceService;
    private final AuthorizationPersistenceHelper authorizationPersistenceHelper;
    private final Provider<Indexer<ProfileDocument>> profileIndexerProvider;

    @Inject
    ProfilePersister(
            PersistenceService persistenceService,
            AuthorizationPersistenceHelper authorizationPersistenceHelper,
            @Qualifier("profileIndexer") Provider<Indexer<ProfileDocument>> profileIndexerProvider) {
        this.persistenceService = persistenceService;
        this.authorizationPersistenceHelper = authorizationPersistenceHelper;
        this.profileIndexerProvider = profileIndexerProvider;
    }

    @Override
    public Class<Profile> getHandles() {
        return Profile.class;
    }

    @Override
    public Profile get(int id) {
        return persistenceService.process(new RetrieveProfileFunc(id));
    }

    @Override
    public Profile get(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveProfileFunc(id));
    }

    public ProfileSummary getProfileSummary(Integer profileId) {
        return persistenceService.process(new RetrieveProfileSummaryFunc(profileId));
    }

    public ProfileSummary getProfileSummary(Integer profileId, Connection conn) {
        return persistenceService.process(conn, new RetrieveProfileSummaryFunc(profileId));
    }

    public ProfileSummary getProfileByOrganization(int organizationId, int userId) {
        return persistenceService.process(new RetrieveProfileForOrganizationFunc(organizationId, userId));
    }

    public ProfileSummary create(Profile profile) {
        Profile savedProfile = persistenceService.process(new CreateProfileFunc(profile, this));

        return getProfileSummary(savedProfile.getId());
    }

    public Profile createProfile(Profile profile) {
        return persistenceService.process(new CreateProfileFunc(profile, this));
    }

    public Profile createProfile(Profile profile, Connection conn) {
        return persistenceService.process(conn, new CreateProfileFunc(profile, this));
    }

    public void delete(int id) {
        persistenceService.process(new DeleteProfileByIdFunc(id));
    }

    public Profile update(Profile profile) {
        return persistenceService.process(new UpdateProfileFunc(profile));
    }

    public List<Integer> getProfileIdsForOrganization(int organizationId) {
        return persistenceService.process(new RetrieveFunc<List<Integer>>(organizationId) {
            @Override
            protected List<Integer> retrieveConcrete() throws SQLException {
                PreparedStatement select = conn.prepareStatement(
                        "SELECT id FROM profile WHERE organization=? ORDER BY id DESC");
                select.setInt(1, input);

                ResultSet result = select.executeQuery();

                List<Integer> ids = new ArrayList<Integer>();
                while (result.next()) {
                    ids.add(result.getInt(1));
                }

                return ids;
            }
        });
    }

    public static class CreateProfileFunc extends CreateFunc<Profile> {
        private final AuthorizationPersistenceHelper authorizationPersistenceHelper;

        protected CreateProfileFunc(
                Profile input,
                ProfilePersister profilePersister) {
            super(input);

            authorizationPersistenceHelper = profilePersister.authorizationPersistenceHelper;
        }

        @Override
        protected Profile createConcrete() throws SQLException {
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO profile (id, surname,givenName, user, organization) VALUES (?,?,?,?,?)");
            insert.setInt(1, input.getId());
            insert.setString(2, input.getSurname());
            insert.setString(3, input.getGivenName());
            if (input.getUserId() == null) {
                insert.setNull(4, Types.INTEGER);
            } else {
                insert.setInt(4, input.getUserId());
            }
            insert.setInt(5, input.getOrganizationId());

            int insertCount = insert.executeUpdate();
            if (insertCount != 1) {
                throw new GeneralException("Unexpected insert count: " + insertCount);
            }

            if (input.getUserId() != null) {
                authorizationPersistenceHelper.grantToCollectionsInOrganization(conn, ROLE_ADMIN,
                        input.getOrganizationId(), input.getId());
            }

            return input;
        }
    }

    public static class RetrieveProfileForOrganizationFunc extends RetrieveFunc<ProfileSummary> {
        private final Integer userId;

        public RetrieveProfileForOrganizationFunc(Integer orgId, Integer userId) {
            super(orgId);
            this.userId = userId;
        }

        @Override
        protected ProfileSummary retrieveConcrete() throws SQLException {
            PreparedStatement select = conn
                    .prepareStatement("SELECT p.id, p.organization, e.version, p.surname, p.givenName, p.user, " +
                            "o.name, IF(acl.acl_object_identity IS NOT NULL, TRUE, FALSE) AS isAdmin " +
                            "FROM profile p " +
                            "JOIN organization o ON p.organization=o.id " +
                            "JOIN systemEntity e ON p.id=e.id " +
                            "LEFT OUTER JOIN acl_entry acl ON p.id=acl.sid AND p.organization=acl.acl_object_identity" +
                            " AND acl.mask=16 " +
                            "WHERE p.user=? AND p.organization=?");
            select.setInt(1, userId);
            select.setInt(2, input);

            ResultSet results = select.executeQuery();
            if (!results.next()) {
                return null;
            }

            Profile profile = new Profile(results.getInt(1), results.getInt(2), results.getInt(3));
            profile.setSurname(results.getString(4));
            profile.setGivenName(results.getString(5));
            profile.setUserId(results.getInt(6));
            ProfileSummary profileSummary = new ProfileSummary(profile, results.getString(7), results.getBoolean(8));

            results.close();
            select.close();

            return profileSummary;
        }
    }

    public static class UserOrgTuple {
        private final int userId;
        private final int orgId;

        public UserOrgTuple(int userId, int orgId) {
            this.userId = userId;
            this.orgId = orgId;
        }
    }

    public static class ProfileIdByUserAndOrgFunc extends ProcessFunc<UserOrgTuple, Integer> {
        public ProfileIdByUserAndOrgFunc(UserOrgTuple input) {
            super(input);
        }

        @Override
        public Integer process() {
            try {
                PreparedStatement statement =
                        conn.prepareStatement("SELECT id FROM profile WHERE user=? AND organization=?");
                statement.setInt(1, input.userId);
                statement.setInt(2, input.orgId);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    return result.getInt(1);
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrieveProfileFunc extends RetrieveFunc<Profile> {
        RetrieveProfileFunc(Integer id) {
            super(id);
        }

        @Override
        protected Profile retrieveConcrete() throws SQLException {
            ProfileSummary profileSummary = getProfileSummary(conn, input);

            return profileSummary == null ? null : profileSummary.getProfile();
        }
    }

    private static ProfileSummary getProfileSummary(Connection conn, int profileId)
            throws SQLException {
        PreparedStatement select =
                conn.prepareStatement(
                        "SELECT p.id, p.organization, e.version, p.surname, p.givenName, p.user, o.name, " +
                                "IF(acl.acl_object_identity IS NOT NULL, TRUE, FALSE) AS isAdmin FROM profile p " +
                                "JOIN organization o ON p.organization=o.id " +
                                "JOIN systemEntity e ON p.id=e.id " +
                                "LEFT OUTER JOIN acl_entry acl ON p.id=acl.sid " +
                                "AND p.organization=acl.acl_object_identity AND acl.mask>=" + ROLE_ADMIN +
                                " WHERE p.id=?");
        select.setInt(1, profileId);

        ResultSet results = select.executeQuery();
        if (!results.next()) {
            return null;
        }

        Profile profile = new Profile(results.getInt(1), results.getInt(2), results.getInt(3));
        profile.setSurname(results.getString(4));
        profile.setGivenName(results.getString(5));

        int userId = results.getInt(6);
        if (!results.wasNull()) {
            profile.setUserId(userId);
        }

        PreparedStatement convioSelect =
                conn.prepareStatement("SELECT orgId, cons_id, syncStatus FROM personConvioSyncStatus WHERE personId=?");
        convioSelect.setInt(1, profileId);

        ResultSet convioResults = convioSelect.executeQuery();
        while (convioResults.next()) {
            profile.setConvioSyncStatus(convioResults.getInt(1), convioResults.getInt(2), convioResults.getString(3));
        }

        return new ProfileSummary(profile, results.getString(7), results.getBoolean(8));
    }

    public static class RetrieveProfileSummaryFunc extends RetrieveFunc<ProfileSummary> {
        public RetrieveProfileSummaryFunc(Integer id) {
            super(id);
        }

        @Override
        protected ProfileSummary retrieveConcrete() throws SQLException {
            return getProfileSummary(conn, input);
        }
    }

    public static class UpdateProfileFunc extends UpdateFunc<Profile> {

        public UpdateProfileFunc(Profile input) {
            super(input);
        }

        @Override
        protected Profile updateConcrete() throws SQLException {
            PreparedStatement update = conn
                    .prepareStatement("UPDATE profile p SET p.surname=?, p.givenName=? WHERE p.id=?");
            update.setString(1, input.getSurname());
            update.setString(2, input.getGivenName());
            update.setInt(3, input.getId());

            int updateCount = update.executeUpdate();
            if (updateCount != 1) {
                throw new GeneralException("Unexpected update count: " + updateCount);
            }

            boolean originalAutoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);
                conn.createStatement().execute("DELETE FROM personConvioSyncStatus WHERE personId=" + input.getId());
                PreparedStatement convioUpdate =
                        conn.prepareStatement(
                                "INSERT INTO personConvioSyncStatus (personId, orgId, cons_id, syncStatus) VALUES (?," +
                                        "?,?,?)");
                for (Entry<Integer, ConvioSyncStatus> syncStatus : input.getConvioSyncStatti().entrySet()) {
                    convioUpdate.setInt(1, input.getId());
                    convioUpdate.setInt(2, syncStatus.getKey());
                    convioUpdate.setInt(3, syncStatus.getValue().getCons_id());
                    convioUpdate.setString(4, syncStatus.getValue().getConvioSyncStatus());
                    convioUpdate.addBatch();
                }
                convioUpdate.executeBatch();
                conn.commit();
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }

            return input;
        }
    }

    public static class DeleteProfileFunc extends DeleteFunc<Profile> {

        public DeleteProfileFunc(final Profile input) {
            super(input);
        }

        @Override
        public Profile process() {
            try {
                PreparedStatement deleteProfile = conn.prepareStatement("DELETE FROM profile WHERE id = ?");
                deleteProfile.setInt(1, input.getId());
                PreparedStatement deleteAddress = conn.prepareStatement("DELETE FROM address WHERE entity = ?");
                deleteAddress.setInt(1, input.getId());
                PreparedStatement deleteContact = conn.prepareStatement("DELETE FROM contact WHERE entityId = ?");
                deleteContact.setInt(1, input.getId());

                deleteProfile.executeUpdate();
                deleteAddress.executeUpdate();
                deleteContact.executeUpdate();
                deleteEntityRecordAndUpdateInput();

                return input;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private static class DeleteProfileByIdFunc extends ProcessFunc<Integer, Void> {
        DeleteProfileByIdFunc(int id) {
            super(id);
        }

        @Override
        public Void process() {
            try {
                PreparedStatement deleteProfile = conn.prepareStatement("DELETE FROM profile WHERE id = ?");
                deleteProfile.setInt(1, input);
                deleteProfile.executeUpdate();

                PreparedStatement deleteEntity = conn.prepareStatement("DELETE FROM entity WHERE id=?");
                deleteEntity.setInt(1, input);
                int deleteCount = deleteEntity.executeUpdate();
                if (deleteCount != 1) {
                    throw new GeneralException("Unexpected insert count: " + deleteCount);
                }

                PreparedStatement deleteSystemEntity = conn.prepareStatement("DELETE FROM systemEntity WHERE id=?");
                deleteSystemEntity.setInt(1, input);
                deleteCount = deleteSystemEntity.executeUpdate();
                if (deleteCount != 1) {
                    throw new GeneralException("Unexpected insert count: " + deleteCount);
                }
            } catch (SQLException e) {
                throw new GeneralException(e);
            }

            return null;
        }
    }

    public static class RetrieveProfilesIdByOrganizationFunc extends ProcessFunc<Integer, List<Integer>> {
        public RetrieveProfilesIdByOrganizationFunc(Integer input) {
            super(input);
        }

        @Override
        public List<Integer> process() {
            try {
                List<Integer> profiles = Lists.newArrayList();

                PreparedStatement update = conn.prepareStatement(
                        "SELECT p.id FROM profile p JOIN user u ON p.user=u.id WHERE p.organization=?");
                update.setInt(1, input);

                ResultSet results = update.executeQuery();
                while (results.next()) {
                    profiles.add(results.getInt(1));
                }

                return profiles;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrieveProfilesFunc extends ProcessFunc<Integer, List<ProfileSummary>> {
        public RetrieveProfilesFunc(Integer input) {
            super(input);
        }

        @Override
        public List<ProfileSummary> process() {
            try {
                List<ProfileSummary> profileSummaries = new ArrayList<ProfileSummary>();

                PreparedStatement update = conn
                        .prepareStatement("SELECT p.id, p.organization, e.version, p.surname, p.givenName, p.user, " +
                                "o.name, IF(acl.acl_object_identity IS NOT NULL OR p.user=" + ROOT_ID + ", TRUE, " +
                                "FALSE) AS isAdmin " +
                                "FROM profile p " +
                                "JOIN organization o ON p.organization=o.id " +
                                "JOIN systemEntity e ON p.id=e.id " +
                                "LEFT OUTER JOIN acl_entry acl ON p.id=acl.sid " +
                                "AND p.organization=acl.acl_object_identity AND acl.mask>=" + ROLE_ADMIN +
                                " WHERE p.user=?");
                update.setInt(1, input);

                ResultSet results = update.executeQuery();
                while (results.next()) {
                    Profile profile = new Profile(results.getInt(1), results.getInt(2), results.getInt(3));
                    profile.setSurname(results.getString(4));
                    profile.setGivenName(results.getString(5));
                    profile.setUserId(results.getInt(6)); // User cannot be null in this case
                    profileSummaries.add(new ProfileSummary(profile, results.getString(7), results.getBoolean(8)));
                }

                return profileSummaries;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    @Override
    public String getMineWhereClause(AuthParam<?> authParam) {
        return MineCallbackProvider.DEFAULT_MINE_CLAUSE;
    }

    @Override
    public String getMineJoinClause(AuthParam<?> authParam) {
        return MineCallbackProvider.DEFAULT_JOIN_CLAUSE;
    }

    @Override
    public boolean hasMineClause() {
        return false;
    }

    @Override
    public String getSortClause(SortField sortField, boolean ascending) {
        return MineCallbackProvider.DEFAULT_SORT_CLAUSE;
    }

    public static class RetrieveProfileByEmailsFunc extends ProcessFunc<Collection<String>, Profile> {
        final int orgId;

        public RetrieveProfileByEmailsFunc(int orgId, Collection<String> email) {
            super(email);

            this.orgId = orgId;
        }

        @Override
        public Profile process() {
            try {
                if (input.isEmpty()) {
                    return null;
                }

                List<Object> queryElements = FluentIterable.from(input)
                        .transform(new Function<String, Object>() {
                            @Override
                            public String apply(String input) {
                                return "LOWER(TRIM(value))=LOWER(TRIM(?))";
                            }
                        }).toList();
                String query = " AND " + Joiner.on(" OR ").join(queryElements);

                PreparedStatement retrieve = conn
                        .prepareStatement("SELECT p.id, p.surname, p.givenName, se.version, p.user, p.organization " +
                                "FROM profile p JOIN systemEntity se ON p.id=se.id JOIN contact c ON p.id = c" +
                                ".entityId " +
                                "WHERE c.medium='EMAIL' AND p.organization=?" + query +
                                " ORDER BY user DESC");
                retrieve.setInt(1, orgId);
                int i = 2;
                for (String email : input) {
                    retrieve.setString(i++, email);
                }

                ResultSet results = retrieve.executeQuery();

                if (!results.next()) {
                    return null;
                }

                Profile profile = new Profile(results.getInt(1), results.getInt(6), results.getInt(4));
                profile.setSurname(results.getString(2));
                profile.setGivenName(results.getString(3));
                profile.setUserId(results.getInt(5));

                return profile;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public ProcessFunc<? extends PagedRetrieveParams, List<StoryAndStorytellerData>> retrieveStorytellersPagedFunc(
            StoryTellersParams params) {
        return new ProcessFunc<StoryTellersParams, List<StoryAndStorytellerData>>(params) {
            @Override
            public List<StoryAndStorytellerData> process() {
                try {
                    RelationalAuthorizationQueryUtil util = new RelationalAuthorizationQueryUtil(ProfilePersister.this);
                    PreparedStatement select = util
                            .prepareAuthorizationSelect(
                                    "SELECT DISTINCT(e.id), e.version, u.handle, "
                                            + "p.givenName, p.surname, a.city, a.state, a.address1, a.postalCode ",
                                    buildFromClause(input),
                                    buildSearchClause(input),
                                    buildSearchParameters(input),
                                    input, conn, true, ROLE_READER);

                    ResultSet results = select.executeQuery();

                    List<StoryAndStorytellerData> stories = new ArrayList<StoryAndStorytellerData>();
                    for (; results.next(); ) {
                        User user = new User(results.getInt(1), results.getInt(2));
                        if (results.getString(3) == null || results.getString(3).isEmpty()) {
                            user.setHandle("Undefined");
                        } else {
                            user.setHandle(results.getString(3));
                        }

                        Profile profile = new Profile();
                        profile.setGivenName(results.getString(4));
                        profile.setSurname(results.getString(5));

                        Address address = new Address();
                        address.setCity(results.getString(6));
                        address.setState(results.getString(7));
                        address.setAddress1(results.getString(8));
                        address.setPostalCode(results.getString(9));
                        stories.add(new StoryAndStorytellerData(null, null, user, address, profile));
                    }

                    return stories;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        };
    }

    public ProcessFunc<? extends PagedRetrieveParams, Integer> countStorytellersFunc(StoryTellersParams params) {
        return new ProcessFunc<StoryTellersParams, Integer>(params) {
            @Override
            public Integer process() {
                try {
                    QueryBuilder queryBuilder = QueryBuilder.newBuilder();
                    if (input.getCollectionId() != null) {
                        queryBuilder.withTerm("collections", input.getCollectionId());
                    }
                    if (input.getQuestionnaireId() != null) {
                        queryBuilder.withTerm("questionnaires", input.getQuestionnaireId());
                    }

                    return (int) profileIndexerProvider.get().count(SearchBuilder.ofQuery(queryBuilder.build()));
                } catch (Exception e) {
                    throw new GeneralException(e);
                }
            }
        };
    }

    private String buildFromClause(StoryTellersParams params) {
        return " FROM systemEntity e " + // that's the profile
                "JOIN profile p on p.id=e.id " +
                "LEFT JOIN user u ON u.id=p.id " + // not all persons are users
                "LEFT JOIN address a ON p.id=a.entity " + // not all persons have address; see address bit in where
                // clause
                (params.getCollectionId() == null ? "" :
                        "JOIN systemEntity es ON e.id=es.owner " +
                                "JOIN collection_story cs ON es.id=cs.story");
    }

    private String buildSearchClause(StoryTellersParams params) {
        // a profile may have multiple addresses, here we just want the one
        final String limitToOne = "(a.idx=(SELECT MIN(a2.idx) FROM address a2 WHERE a2.entity=a.entity) OR a.idx IS " +
                "NULL)" +
                (params.getCollectionId() == null ? "" : " AND " +
                        // and a profile may have multiple stories
                        "cs.story=(SELECT MIN(cs2.story) FROM collection_story cs2 JOIN systemEntity es2 ON es2" +
                        ".id=cs2.story " +
                        "WHERE cs2.collection=cs.collection AND es2.owner=p.id)");
        if (params.getSearchText() == null && params.getCollectionId() == null) {
            return limitToOne;
        } else if (params.getCollectionId() == null) { // then search text must be null
            return limitToOne + " AND LOWER(u.handle) LIKE LOWER(?) OR LOWER(p.givenName) LIKE LOWER(?) OR LOWER(p" +
                    ".surname) LIKE LOWER(?)";
        } else if (params.getSearchText() == null || "".equals(
                params.getSearchText())) { // then collection ID must be null
            return limitToOne + " AND cs.collection=?";
        } else { // has both search text and collection
            return limitToOne + " AND " +
                    "LOWER(u.handle) LIKE LOWER(?) OR LOWER(p.givenName) LIKE LOWER(?) OR LOWER(p.surname) LIKE LOWER" +
                    "(?) " +
                    " AND cs.collection=?";
        }
    }

    private Object[] buildSearchParameters(StoryTellersParams params) {
        Object[] searchParameters = params.getSearchText() == null ? new Object[0] :
                new Object[]{"%" + params.getSearchText() + "%",
                        "%" + params.getSearchText() + "%", "%" + params.getSearchText() + "%"};
        if (params.getCollectionId() != null) {
            searchParameters =
                    ArrayUtils.addAll(searchParameters, params.getCollectionId());
        }

        return searchParameters;
    }
}
