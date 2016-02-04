package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.business_logic.UserService;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.springframework.stereotype.Component;

@Component
public class ThemePersister implements Persister<Theme> {
    private final PersistenceService persistenceService;
    private final UserService userService;

    @Inject
    ThemePersister(
            PersistenceService persistenceService,
            UserService userService) {
        this.persistenceService = persistenceService;
        this.userService = userService;
    }

    @Override
    public Class<Theme> getHandles() {
        return Theme.class;
    }

    @Override
    public Theme get(int id) {
        return persistenceService.process(new RetrieveThemeFunc(id));
    }

    @Override
    public Theme get(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveThemeFunc(id));
    }

    public void associateOrganization(Organization organization, List<Integer> themeIds) {
        persistenceService.process(new AssociateOrganization(organization, themeIds));
    }

    public List<Theme> retrieveAll() {
        return persistenceService.process(new RetrieveFunc<List<Theme>>(0) {
            @Override
            protected List<Theme> retrieveConcrete() throws SQLException {
                String query = "SELECT se.id, se.created, se.lastModified, se.version, se.public, t.name, t.themePage"
                        + " FROM theme t" + " JOIN systemEntity se ON t.id = se.id";

                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet rs = statement.executeQuery();

                List<Theme> themes = new ArrayList<Theme>();
                for (; rs.next(); ) {
                    Theme t = insantiate(rs);
                    themes.add(t);
                }

                return themes;
            }
        });
    }

    public List<Theme> retrieveForOrganization(int organizationId) {
        return persistenceService.process(new RetrieveFunc<List<Theme>>(organizationId) {
            @Override
            protected List<Theme> retrieveConcrete() throws SQLException {
                String query = "SELECT se.id, se.created, se.lastModified, se.version, se.public, t.name, t.themePage"
                        + " FROM theme t"
                        + " JOIN systemEntity se ON t.id = se.id"
                        + " JOIN organization_theme ot ON t.id = ot.theme" + " WHERE ot.organization = ?";

                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, input);
                ResultSet rs = statement.executeQuery();

                List<Theme> themes = new ArrayList<Theme>();
                for (; rs.next(); ) {
                    Theme t = insantiate(rs);
                    themes.add(t);
                }

                return themes;
            }
        });
    }

    public List<Integer> getOrganizationIds(int themeId) {
        return persistenceService.process(new ProcessFunc<Integer, List<Integer>>(themeId) {
            @Override
            public List<Integer> process() {
                try {
                    String query = "SELECT organization FROM organization_theme WHERE theme=?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, input);

                    ResultSet rs = statement.executeQuery();

                    List<Integer> organizations = new ArrayList<Integer>();
                    while (rs.next()) {
                        organizations.add(rs.getInt(1));
                    }

                    return organizations;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public Map<Integer, Boolean> getEntities(int themeId) {
        final Integer organizationId = userService.getContextOrganizationId();
        return persistenceService.process(new ProcessFunc<Integer, Map<Integer, Boolean>>(themeId) {
            @Override
            public Map<Integer, Boolean> process() {
                try {
                    String query = "SELECT c.id, EXISTS(SELECT 1 FROM questionnaire q WHERE q.id=c.id) " +
                            "FROM collection c " +
                            "JOIN systemEntity se ON se.id = c.id AND se.owner=? " +
                            "WHERE c.theme=?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, organizationId);
                    statement.setInt(2, input);

                    ResultSet rs = statement.executeQuery();

                    Map<Integer, Boolean> entities = new HashMap<Integer, Boolean>();
                    while (rs.next()) {
                        entities.put(rs.getInt(1), rs.getBoolean(2));
                    }

                    return entities;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public static class RetrieveThemeFunc extends RetrieveFunc<Theme> {
        public RetrieveThemeFunc(Integer id) {
            super(id);
        }

        @Override
        protected Theme retrieveConcrete() throws SQLException {
            String query = "SELECT se.id, se.created, se.lastModified, se.version, se.public, t.name, t.themePage"
                    + " FROM theme t" + " JOIN systemEntity se ON t.id = se.id" + " WHERE se.id=?";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, input);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                throw new GeneralException("No theme found with id=" + input);
            }

            return insantiate(rs);
        }
    }

    public static class AssociateOrganization extends ProcessFunc<Organization, Integer> {
        private final List<Integer> themeIds;

        public AssociateOrganization(Organization input, List<Integer> themeIds) {
            super(input);

            this.themeIds = themeIds;
        }

        @Override
        public Integer process() {
            try {
                PreparedStatement reset = conn.prepareStatement("DELETE FROM organization_theme " +
                        "WHERE organization = ?");
                reset.setInt(1, input.getId());
                reset.executeUpdate();
                conn.commit();

                String query = "INSERT INTO organization_theme(organization, theme) VALUES (?,?)";
                PreparedStatement statement = conn.prepareStatement(query);

                Set<Integer> idsSet = new HashSet<Integer>(themeIds);
                idsSet.add(input.getDefaultTheme());
                for (Integer tid : idsSet) {
                    statement.setInt(1, input.getId());
                    statement.setInt(2, tid);
                    statement.addBatch();
                }
                statement.executeBatch();

                return 1;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    protected static Theme insantiate(ResultSet rs) throws SQLException {
        Theme t = new Theme();
        t.setId(rs.getInt(1));
        t.setCreated(rs.getTimestamp(2) == null ? null : new Date(rs.getTimestamp(2).getTime()));
        t.setUpdated(rs.getTimestamp(3) == null ? null : new Date(rs.getTimestamp(3).getTime()));
        t.setVersion(rs.getInt(4));
        t.setPublic(rs.getBoolean(5));

        t.setName(rs.getString(6));
        t.setThemePage(rs.getString(7));

        return t;
    }
}
