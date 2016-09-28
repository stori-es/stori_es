package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.persistence.funcs.CreateFunc;
import org.consumersunion.stories.server.persistence.funcs.DeleteFunc;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.consumersunion.stories.server.persistence.funcs.UpdateFunc;
import org.consumersunion.stories.server.security.BaseAuthParam;
import org.consumersunion.stories.server.security.RelationalAuthorizationQueryUtil;
import org.springframework.stereotype.Component;

@Component
public class OrganizationPersister implements Persister<Organization>, MineCallbackProvider {
    private final PersistenceService persistenceService;

    @Inject
    OrganizationPersister(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public Class<Organization> getHandles() {
        return Organization.class;
    }

    @Override
    public Organization get(int id) {
        return persistenceService.process(new RetrieveOrganizationFunc(id));
    }

    @Override
    public Organization get(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveOrganizationFunc(id));
    }

    public Organization create(Organization organization) {
        return persistenceService.process(new CreateOrganizationFunc(organization));
    }

    public void delete(Organization organization) {
        persistenceService.process(new DeleteOrganizationFunc(organization));
    }

    public Organization update(Organization organization) {
        return persistenceService.process(new UpdateOrganizationFunc(organization));
    }

    public static class CreateOrganizationFunc extends CreateFunc<Organization> {
        CreateOrganizationFunc(Organization input) {
            super(input);
        }

        @Override
        protected Organization createConcrete() {
            try {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO organization (id, name, shortName, defaultTheme) VALUES (?,?,?,?)");
                insert.setInt(1, input.getId());
                insert.setString(2, input.getName());
                insert.setString(3, input.getShortName());
                if (input.getDefaultTheme() == null) {
                    insert.setNull(4, Types.INTEGER);
                } else {
                    insert.setInt(4, input.getDefaultTheme());
                }

                int insertCount = insert.executeUpdate();
                if (insertCount != 1) {
                    throw new GeneralException("Unexpected insert count: " + insertCount);
                }

                PreparedStatement updatePublic = conn.prepareStatement(
                        "UPDATE systemEntity SET public = ?, owner = ? WHERE id = ?");
                updatePublic.setBoolean(1, input.isPublic());
                updatePublic.setInt(2, input.getId());
                updatePublic.setInt(3, input.getId());

                int updateCountPublic = updatePublic.executeUpdate();
                if (updateCountPublic != 1) {
                    throw new GeneralException("Unexpected updating systementity count: " + updateCountPublic);
                }

                return input;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrieveOrganizationFunc extends RetrieveFunc<Organization> {
        public RetrieveOrganizationFunc(Integer id) {
            super(id);
        }

        @Override
        protected Organization retrieveConcrete() throws SQLException {
            PreparedStatement insert = conn.prepareStatement(
                    "SELECT " + getOrganizationColumnsForRetrieval("o", "se", "e") + " FROM systemEntity se " +
                            "JOIN organization o ON se.id=o.id JOIN entity e ON se.id=e.id WHERE se.id=?");
            insert.setInt(1, input);

            ResultSet results = insert.executeQuery();
            if (!results.next()) {
                throw new NotFoundException("No Organization with ID " + input + " found.");
            }

            return processRetrieval(results);
        }
    }

    public static class RetrieveByPermalinkFunc extends ProcessFunc<String, Organization> {
        public RetrieveByPermalinkFunc(String input) {
            super(input);
        }

        @Override
        public Organization process() {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT " + getOrganizationColumnsForRetrieval("o", "e", "n") + " " +
                                "FROM systemEntity e JOIN organization o ON e.id=o.id JOIN entity n ON e.id=n.id " +
                                "WHERE n.permalink=?");
                ps.setString(1, input);

                ResultSet results = ps.executeQuery();
                if (!results.next()) {
                    throw new GeneralException("No Organization with Permalink " + input + " found.");
                }
                return processRetrieval(results);
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class UpdateOrganizationFunc extends UpdateFunc<Organization> {
        UpdateOrganizationFunc(final Organization input) {
            super(input);
        }

        @Override
        protected Organization updateConcrete() {
            checkVersion();
            try {
                PreparedStatement update =
                        conn.prepareStatement(
                                "UPDATE organization SET name=?, shortName=?, defaultTheme = ? WHERE id=?");
                update.setString(1, input.getName());
                update.setString(2, input.getShortName());
                if (input.getDefaultTheme() == null) {
                    update.setNull(3, Types.INTEGER);
                } else {
                    update.setInt(3, input.getDefaultTheme());
                }
                update.setInt(4, input.getId());

                int updateCount = update.executeUpdate();
                if (updateCount != 1) {
                    throw new GeneralException("Unexpected insert count: " + updateCount);
                }

                PreparedStatement updatePublic = conn
                        .prepareStatement("UPDATE systemEntity SET public = ?, owner = ? WHERE id = ?");
                updatePublic.setBoolean(1, input.isPublic());
                updatePublic.setInt(2, input.getId());
                updatePublic.setInt(3, input.getId());

                int updateCountPublic = updatePublic.executeUpdate();
                if (updateCountPublic != 1) {
                    throw new GeneralException("Unexpected updating systementity count: " + updateCountPublic);
                }

                PreparedStatement updateEntity = conn
                        .prepareStatement("UPDATE entity SET permalink = ?, profile = ? WHERE id = ?");
                updateEntity.setString(1, input.getPermalink());
                updateEntity.setNull(2, Types.INTEGER);
                updateEntity.setInt(3, input.getId());

                int updateCountEntity = updateEntity.executeUpdate();
                if (updateCountEntity != 1) {
                    throw new GeneralException("Unexpected updating entity count: " + updateCountEntity);
                }

                return input;
            } catch (final SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }

    public static class DeleteOrganizationFunc extends DeleteFunc<Organization> {
        DeleteOrganizationFunc(Organization input) {
            super(input);
        }

        @Override
        public Organization process() {
            checkVersion();
            try {
                conn.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
                PreparedStatement delete = conn.prepareStatement("DELETE FROM organization WHERE id=?");
                delete.setInt(1, input.getId());

                int updateCount = delete.executeUpdate();
                if (updateCount != 1) {
                    throw new GeneralException("Unexpected insert count: " + updateCount);
                }

                deleteEntityRecordAndUpdateInput();
                conn.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");

                return input;
            } catch (final SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }

    public CheckOrganizationNameAvailability checkOrganizationNameAvailability(final String name) {
        return new CheckOrganizationNameAvailability(name);
    }

    class CheckOrganizationNameAvailability extends ProcessFunc<String, Boolean> {
        public CheckOrganizationNameAvailability(String input) {
            super(input);
        }

        @Override
        public Boolean process() {
            PreparedStatement select;
            try {
                select = conn.prepareStatement("SELECT COUNT(*) FROM organization WHERE name = ?");
                select.setString(1, input);

                ResultSet rs = select.executeQuery();
                if (!rs.next()) {
                    throw new GeneralException(
                            "Error on executing CheckOrganizationNameAvailability, non results retrieved");
                }

                return rs.getInt(1) == 0;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public ProcessFunc<BaseAuthParam, Integer> getOrganizationCount(
            BaseAuthParam params,
            final int authMask) {
        return new ProcessFunc<BaseAuthParam, Integer>(params) {

            @Override
            public Integer process() {
                try {
                    String select = "SELECT COUNT(*)";
                    String from = "FROM organization o JOIN systemEntity e ON e.id = o.id";

                    RelationalAuthorizationQueryUtil util = new RelationalAuthorizationQueryUtil(
                            OrganizationPersister.this);

                    PreparedStatement ps = util.prepareAuthorizationSelect(select, from, null, new Object[0],
                            input.noLimit(), conn, true, authMask);

                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                } catch (Exception e) {
                    throw new GeneralException(e);
                }
                return 0;
            }
        };
    }

    public RetrieveAdminOrganizationsFunc retrieveAdminOrganizationsFunc(final RetrievePagedOrganizationsParams param) {
        return new RetrieveAdminOrganizationsFunc(param);
    }

    class RetrieveAdminOrganizationsFunc extends ProcessFunc<RetrievePagedOrganizationsParams, List<Organization>> {
        RetrieveAdminOrganizationsFunc(RetrievePagedOrganizationsParams params) {
            super(params);
        }

        @Override
        public List<Organization> process() {
            try {
                RelationalAuthorizationQueryUtil queryUtil = new RelationalAuthorizationQueryUtil(
                        OrganizationPersister.this);
                PreparedStatement select = queryUtil.prepareAuthorizationSelect(
                        "SELECT " + getOrganizationColumnsForRetrieval("o", "e", "n") + " ",
                        "FROM systemEntity e JOIN organization o ON e.id=o.id JOIN entity n ON e.id=n.id",
                        "permalink IS NOT NULL", new Object[]{}, input, conn, true,
                        AuthConstants.ROLE_ADMIN);
                ResultSet results = select.executeQuery();
                if (!results.next()) {
                    return null;
                }

                List<Organization> organizations = new ArrayList<Organization>();
                do {
                    Organization organization = processRetrieval(results);
                    organizations.add(organization);
                } while (results.next());

                return organizations;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }

    public static String getOrganizationColumnsForRetrieval(String organization, String systemEntity, String entity) {
        return systemEntity + ".id, " +
                systemEntity + ".created, " +
                systemEntity + ".lastModified, " +
                systemEntity + ".version, " +
                systemEntity + ".public, " +
                systemEntity + ".owner, " +
                entity + ".permalink, " +
                organization + ".name, " +
                organization + ".shortName, " +
                organization + ".defaultTheme, " +
                organization + ".crm_api_key, " +
                organization + ".crm_api_login, " +
                organization + ".crm_endpoint";
    }

    public static Organization processRetrieval(ResultSet results) throws SQLException {
        Organization organization = new Organization(
                results.getInt(1), // ID
                results.getInt(4) // version
        );

        organization.setCreated(results.getDate(2));
        organization.setUpdated(results.getDate(3));
        organization.setPublic(results.getBoolean(5));
        organization.setOwner(results.getInt(6));
        organization.setPermalink(results.getString(7));
        organization.setName(results.getString(8));
        organization.setShortName(results.getString(9));
        Integer defaultTheme = results.getInt(10);
        organization.setDefaultTheme(results.wasNull() ? null : defaultTheme);
        organization.setCrmApiKey(results.getString(11));
        organization.setCrmApiLogin(results.getString(12));
        organization.setCrmEndpoint(results.getString(13));

        return organization;
    }

    @Override
    public String getMineWhereClause(AuthParam<?> authParam) {
        if (authParam.getSubject() != null) {
            return "o.id=" + authParam.getSubject();
        } else if (authParam.getEffectiveId() != null) {
            return "o.id=" + authParam.getEffectiveId();
        } else {
            return "false";
        }
    }

    @Override
    public String getMineJoinClause(AuthParam<?> authParam) {
        return DEFAULT_JOIN_CLAUSE;
    }

    @Override
    public boolean hasMineClause() {
        return true;
    }

    @Override
    public String getSortClause(SortField sortField, boolean ascending) {
        return DEFAULT_SORT_CLAUSE;
    }

    public static class RetrievePagedOrganizationsParams extends
            AuthParam<RetrievePagedOrganizationsParams> {
        public RetrievePagedOrganizationsParams(
                int start,
                int length,
                SortField sortField,
                boolean ascending,
                int relation,
                Integer effectiveId) {
            super(start, length, sortField, ascending, relation, effectiveId);
        }

        public RetrievePagedOrganizationsParams(
                int start,
                int length,
                SortField sortField,
                boolean ascending,
                int relation,
                Integer effectiveId,
                Integer subject) {
            super(start, length, sortField, ascending, relation, effectiveId, subject);
        }

        @Override
        public RetrievePagedOrganizationsParams noLimit() {
            return new RetrievePagedOrganizationsParams(0, 0, getSortField(), isAscending(), getAuthRelation(),
                    getEffectiveId());
        }
    }
}
