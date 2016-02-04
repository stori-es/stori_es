package org.consumersunion.stories.server.solr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.annotation.NotThreadSafe;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.business_logic.SystemEntityService;

import com.google.common.collect.Maps;

@NotThreadSafe
public class SupportDataUtils {
    private static final int BATCH_SIZE = 200;

    private final SystemEntityService entityService;
    private final Connection conn;
    private final Map<PreparedStatement, Integer> statements;

    private PreparedStatement emailsStmt = null;
    private PreparedStatement phonesStmt = null;
    private PreparedStatement preferredEmailStmtDeprecated = null;
    private PreparedStatement preferredEmailStmt = null;
    private PreparedStatement optInStmtDeprecated = null;
    private PreparedStatement optInStmt = null;
    private PreparedStatement primaryAddressStmt = null;
    private PreparedStatement firstLastStoryStmt = null;
    private PreparedStatement storyCountStmt = null;
    private PreparedStatement storyAuthsStmt = null;
    private PreparedStatement nonStoryAuthsStmt = null;
    private PreparedStatement adminsStmt = null;

    SupportDataUtils(
            SystemEntityService systemEntityService,
            Connection conn) {
        this.conn = conn;
        statements = Maps.newHashMap();

        this.entityService = systemEntityService;
    }

    public List<String> getEmailsFor(int id) throws SQLException {
        if (emailsStmt == null) {
            // the value ordering has no semantic meaning, it's just there to promote consistent results
            emailsStmt = conn.prepareStatement("SELECT c.value, c.type " +
                    "FROM contact c WHERE c.entityId=? AND medium='EMAIL' ORDER BY value");
            addStatement(emailsStmt);
        }

        try {
            emailsStmt.setInt(1, id);
            return getStringList(emailsStmt.executeQuery());
        } finally {
            emailsStmt = handleStatement(emailsStmt);
        }
    }

    public List<String> getPhonesFor(int id) throws SQLException {
        if (phonesStmt == null) {
            // the value ordering has no semantic meaning, it's just there to promote consistent results
            phonesStmt = conn.prepareStatement("SELECT c.value, c.type " +
                    "FROM contact c WHERE c.entityId=? AND medium='PHONE' ORDER BY value");
            addStatement(phonesStmt);
        }

        try {
            phonesStmt.setInt(1, id);
            return getStringList(phonesStmt.executeQuery());
        } finally {
            phonesStmt = handleStatement(phonesStmt);
        }
    }

    public String getPreferredEmailFormat(int id) throws SQLException {
        if (preferredEmailStmtDeprecated == null) {
            preferredEmailStmtDeprecated = conn.prepareStatement("SELECT reportValue, MAX(id) " +
                    "FROM answer a JOIN document d ON a.answerSet=d.id " +
                    "WHERE d.primaryAuthor=? AND a.label LIKE 'Preferred Email%';");
            addStatement(preferredEmailStmtDeprecated);
        }

        try {
            preferredEmailStmtDeprecated.setInt(1, id);
            return getString(preferredEmailStmtDeprecated.executeQuery());
        } finally {
            preferredEmailStmtDeprecated = handleStatement(preferredEmailStmtDeprecated);
        }
    }

    public String getPreferredEmailFormat(int id, int orgId) throws SQLException {
        if (preferredEmailStmt == null) {
            // SYSFOUR-547: the JOIN to the collection table is a workaround
            preferredEmailStmt = conn.prepareStatement("SELECT reportValue, MAX(d.id) " +
                    "FROM answer a JOIN answerSet ans ON a.answerSet=ans.id JOIN document d ON a.answerSet=d.id " +
                    "JOIN questionnaire q ON ans.questionnaire=q.id " +
                    "JOIN collection_sources cs ON cs.sourceQuestionnaire=q.id " +
                    "JOIN collection c ON cs.targetCollection=c.id " +
                    "JOIN systemEntity ce ON ce.id=c.id " +
                    "WHERE ce.owner=? AND d.primaryAuthor=? AND a.label LIKE 'Preferred Email%';");
            addStatement(preferredEmailStmt);
        }

        try {
            preferredEmailStmt.setInt(1, orgId);
            preferredEmailStmt.setInt(2, id);
            return getString(preferredEmailStmt.executeQuery());
        } finally {
            preferredEmailStmt = handleStatement(preferredEmailStmt);
        }
    }

    public Address getPrimaryAddress(int id) throws SQLException {
        if (primaryAddressStmt == null) {
            primaryAddressStmt = conn.prepareStatement("SELECT idx, relation, address1, address2, city, state, " +
                    "country, postalCode, latitude, longitude FROM address WHERE entity=? " +
                    "ORDER BY (IF(address1 IS NULL, 0, 1) +IF(city IS NULL, 0, 1) +IF(state IS NULL, 0, 1) " +
                    "+IF(country IS NULL, 0, 1)) DESC");
            addStatement(primaryAddressStmt);
        }

        primaryAddressStmt.setInt(1, id);
        final ResultSet rs = primaryAddressStmt.executeQuery();
        try {
            if (rs.next()) {
                return new Address(id, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6),
                        rs.getString(7), rs.getString(8), rs.getBigDecimal(9), rs.getBigDecimal(10));
            } else {
                return null;
            }
        } finally {
            rs.close();
            primaryAddressStmt = handleStatement(primaryAddressStmt);
        }
    }

    @Deprecated
    public Boolean getUpdatesOptIn(int id) throws SQLException {
        if (optInStmtDeprecated == null) {
            optInStmtDeprecated = conn.prepareStatement("SELECT reportValue, MAX(id) " +
                    "FROM answer a JOIN document d ON a.answerSet=d.id " +
                    "WHERE d.primaryAuthor=? AND a.label LIKE 'Updates Opt%';");
            addStatement(optInStmtDeprecated);
        }

        try {
            optInStmtDeprecated.setInt(1, id);
            final String answer = getString(optInStmtDeprecated.executeQuery());
            if (answer == null) {
                return null;
            } else {
                // TODO: SYSFOUR-443
                return answer.toLowerCase().startsWith("yes");
            }
        } finally {
            optInStmtDeprecated = handleStatement(optInStmtDeprecated);
        }
    }

    public Boolean getUpdatesOptIn(int id, int orgId) throws SQLException {
        if (optInStmt == null) {
            // SYSFOUR-547: the JOIN to the collection table is a workaround
            optInStmt = conn.prepareStatement("SELECT reportValue, MAX(d.id) " +
                    "FROM answer a JOIN answerSet ans ON a.answerSet=ans.id JOIN document d ON a.answerSet=d.id " +
                    "JOIN questionnaire q ON ans.questionnaire=q.id " +
                    "JOIN collection_sources cs ON cs.sourceQuestionnaire=q.id " +
                    "JOIN collection c ON c.id=cs.targetCollection " +
                    "JOIN systemEntity ce ON ce.id=c.id " +
                    "WHERE ce.owner=? AND d.primaryAuthor=? AND a.label LIKE 'Updates Opt%';");
            addStatement(optInStmt);
        }

        try {
            optInStmt.setInt(1, orgId);
            optInStmt.setInt(2, id);
            String answer = getString(optInStmt.executeQuery());
            if (answer == null) {
                return null;
            } else {
                // TODO: SYSFOUR-443
                return answer.toLowerCase().startsWith("yes");
            }
        } finally {
            optInStmt = handleStatement(optInStmt);
        }
    }

    public Date[] getFirstLastStoryDate(int id) throws SQLException {
        if (firstLastStoryStmt == null) {
            firstLastStoryStmt = conn.prepareStatement("SELECT MIN(created), MAX(created) " +
                    "FROM systemEntity se JOIN story s ON s.id=se.id WHERE se.owner=?");
            addStatement(firstLastStoryStmt);
        }

        firstLastStoryStmt.setInt(1, id);
        ResultSet rs = firstLastStoryStmt.executeQuery();
        try {
            if (rs.next()) {
                return new Date[]{rs.getTimestamp(1), rs.getTimestamp(2)};
            } else {
                return new Date[]{null, null};
            }
        } finally {
            rs.close();
            firstLastStoryStmt = handleStatement(firstLastStoryStmt);
        }
    }

    public int getStoryCount(int id) throws SQLException {
        if (storyCountStmt == null) {
            storyCountStmt = conn.prepareStatement("SELECT COUNT(*) FROM story s JOIN systemEntity se ON s.id=se.id " +
                    "WHERE se.owner=?");
            addStatement(storyCountStmt);
        }

        storyCountStmt.setInt(1, id);
        ResultSet rs = storyCountStmt.executeQuery();

        try {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } finally {
            rs.close();
            storyCountStmt = handleStatement(storyCountStmt);
        }
    }

    public Set<Integer> getStoryAuths(int id, int minRole) throws SQLException {
        if (storyAuthsStmt == null) {
            storyAuthsStmt = conn.prepareStatement("SELECT sid FROM (" +
                    "(SELECT DISTINCT(sid) AS sid FROM acl_entry  WHERE acl_object_identity=? AND mask>=?) " +
                    "UNION " +
                    "(SELECT DISTINCT(sid) AS sid FROM acl_entry acl JOIN collection c ON acl.acl_object_identity=c" +
                    ".id " +

                    "JOIN collection_story cs ON c.id=cs.collection WHERE cs.story=? AND mask>=?)" +
                    ") AS tmp");
            addStatement(storyAuthsStmt);
        }

        storyAuthsStmt.setInt(1, id);
        storyAuthsStmt.setInt(2, minRole);
        storyAuthsStmt.setInt(3, id);
        storyAuthsStmt.setInt(4, minRole);

        ResultSet rs = storyAuthsStmt.executeQuery();
        try {
            Set<Integer> data = new LinkedHashSet<Integer>();
            while (rs.next()) {
                data.add(rs.getInt(1));
            }

            return data;
        } finally {
            rs.close();
            storyAuthsStmt = handleStatement(storyAuthsStmt);
        }
    }

    public Set<Integer> getNonStoryAuths(int id, int minRole) throws SQLException {
        if (nonStoryAuthsStmt == null) {
            nonStoryAuthsStmt = conn.prepareStatement(
                    "SELECT DISTINCT(sid) AS sid FROM acl_entry  WHERE acl_object_identity=? AND mask>=?");
            addStatement(nonStoryAuthsStmt);
        }

        nonStoryAuthsStmt.setInt(1, id);
        nonStoryAuthsStmt.setInt(2, minRole);

        ResultSet rs = nonStoryAuthsStmt.executeQuery();
        try {
            Set<Integer> data = new LinkedHashSet<Integer>();
            while (rs.next()) {
                data.add(rs.getInt(1));
            }

            return data;
        } finally {
            rs.close();
            nonStoryAuthsStmt = handleStatement(nonStoryAuthsStmt);
        }
    }

    private String getString(ResultSet results) throws SQLException {
        try {
            if (results.next()) {
                return results.getString(1);
            } else {
                return null;
            }
        } finally {
            results.close();
        }
    }

    private List<String> getStringList(ResultSet results) throws SQLException {
        try {
            final List<String> strings = new ArrayList<String>();
            while (results.next()) {
                strings.add(results.getString(1));
            }

            return strings;
        } finally {
            results.close();
        }
    }

    public Set<String> getAdminNames(Integer id) throws SQLException {
        if (adminsStmt == null) {
            adminsStmt = conn.prepareStatement("SELECT id FROM entity e " +
                    "JOIN acl_entry acl ON acl.sid=e.id " +
                    "WHERE acl.mask=16 AND acl.acl_object_identity=?");
            addStatement(adminsStmt);
        }

        adminsStmt.setInt(1, id);

        ResultSet rs = adminsStmt.executeQuery();
        try {
            Set<String> data = new LinkedHashSet<String>();
            while (rs.next()) {
                SystemEntity entity = entityService.getSystemEntity(id, conn);

                String name = null;
                if (entity instanceof Collection) {
                    name = ((Collection) entity).getTitle();
                } else if (entity instanceof Organization) {
                    name = ((Organization) entity).getName();
                } else if (entity instanceof User) {
                    name = ((User) entity).getHandle();
                } else if (entity instanceof Profile) {
                    name = ((Profile) entity).getFullName();
                }

                if (name != null) {
                    data.add(name);
                }
            }

            return data;
        } finally {
            rs.close();
            adminsStmt = handleStatement(adminsStmt);
        }
    }

    private void addStatement(PreparedStatement stmt) {
        statements.put(stmt, 0);
    }

    private PreparedStatement handleStatement(PreparedStatement stmt) throws SQLException {
        Integer count = statements.get(stmt);
        if (count < BATCH_SIZE) {
            statements.put(stmt, count + 1);
            return stmt;
        } else {
            statements.remove(stmt);
            stmt.close();
            return null;
        }
    }
}
