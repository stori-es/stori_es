package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.server.business_logic.IndexerService;
import org.consumersunion.stories.server.solr.collection.UpdatedCollectionTagsIndexer;
import org.consumersunion.stories.server.solr.story.UpdatedStoryTagsIndexer;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class TagsPersistenceHelper {
    private final IndexerService indexerService;

    @Inject
    TagsPersistenceHelper(IndexerService indexerService) {
        this.indexerService = indexerService;
    }

    public void deleteAutoTags(SystemEntity sytemEntity, Connection conn) throws SQLException {
        deleteTags(sytemEntity, conn, "autotag");
    }

    public void deleteTags(SystemEntity sytemEntity, Connection conn) throws SQLException {
        deleteTags(sytemEntity, conn, "tag");
    }

    protected void updateTags(SystemEntity entity, Collection<String> tags, Connection connection)
            throws SQLException {
        updateIndex(entity, tags);
        updateTags(entity, tags, connection, "tag");
    }

    protected void updateAutoTags(SystemEntity entity, Collection<String> tags, Connection connection)
            throws SQLException {
        updateTags(entity, tags, connection, "autotag");
    }

    private void updateIndex(SystemEntity entity, Collection<String> tags) {
        if (entity instanceof Story) {
            indexerService.process(new UpdatedStoryTagsIndexer((Story) entity, tags));
        } else if (entity instanceof org.consumersunion.stories.common.shared.model.Collection) {
            indexerService.process(
                    new UpdatedCollectionTagsIndexer((org.consumersunion.stories.common.shared.model.Collection) entity,
                            tags));
        }
    }

    private void updateTags(SystemEntity entity,
            Collection<String> tags,
            Connection connection,
            String table) throws SQLException {
        deleteTags(entity, connection, table);
        if (!tags.isEmpty()) {
            final List<String> cleaned = trim(tags);
            persist(entity, cleaned, connection, table);
        }
    }

    private void deleteTags(SystemEntity entity, Connection connection, String table)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM " + table + " WHERE systemEntity=?");
        ps.setInt(1, entity.getId());

        ps.executeUpdate();
    }

    private List<String> trim(Collection<String> tags) {
        List<String> result = new ArrayList<String>();
        for (String tag : tags) {
            if (!Strings.isNullOrEmpty(tag)) {
                result.add(tag.toLowerCase().trim());
            }
        }

        return result;
    }

    protected Set<String> getTags(SystemEntity entity, Connection connection) throws SQLException {
        return getTags(entity.getId(), connection);
    }

    public Set<String> getAutoTags(SystemEntity entity, Connection connection)
            throws SQLException {
        return getAutoTags(entity.getId(), connection);
    }

    public Set<String> getTags(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT value FROM " +
                "(SELECT value FROM tag t UNION SELECT value FROM autotag at) tags");

        ResultSet rs = ps.executeQuery();
        Set<String> tags = new LinkedHashSet<String>();

        for (; rs.next(); ) {
            tags.add(rs.getString(1));
        }

        return tags;
    }

    protected Set<String> getTags(int entityId, final Connection connection) throws SQLException {
        return getTags(entityId, connection, "tag");
    }

    protected Set<String> getAutoTags(int entityId, Connection connection) throws SQLException {
        return getTags(entityId, connection, "autotag");
    }

    private Set<String> getTags(int entityId,
            Connection connection,
            String table) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "SELECT value FROM " + table + " WHERE systemEntity=?");
        ps.setInt(1, entityId);

        ResultSet rs = ps.executeQuery();
        Set<String> tags = new LinkedHashSet<String>();

        for (; rs.next(); ) {
            tags.add(rs.getString(1));
        }

        return tags;
    }

    private void persist(SystemEntity entity,
            List<String> tags,
            Connection connection,
            String table) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO " + table + " (systemEntity, value) VALUES (?,?)");

        for (String tag : tags) {
            ps.setInt(1, entity.getId());
            ps.setString(2, tag);
            ps.executeUpdate();
        }
    }
}
