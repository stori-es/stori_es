package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.consumersunion.stories.common.shared.service.GeneralException;

public class CollectionStoryLinkPersistenceHelper {

    protected static void updateStoryLinks(final Collection collection, final Connection conn) throws SQLException {
        @SuppressWarnings("unchecked")
        final Set<StoryLink> linkSet = (Set<StoryLink>) (collection.getStories() == null ? Collections.emptySet()
                : collection.getStories());
        final List<Integer> dbLinks = getLinkedStoryIds(collection, conn);

        for (final StoryLink link : linkSet) {
            persistStoryLink(collection, link, dbLinks, conn);
            dbLinks.remove(new Integer(link.getStory()));
        }

        for (final int unlinkedStoryId : dbLinks) {
            deleteStoryLink(collection.getId(), unlinkedStoryId, conn);
        }
    }

    private static List<Integer> getLinkedStoryIds(final Collection collection, final Connection conn)
            throws SQLException {
        final PreparedStatement ps = conn.prepareStatement("SELECT story FROM collection_story cs " +
                "JOIN collection c on c.id = cs.collection WHERE cs.collection = ? and c.deleted = 0");
        ps.setInt(1, collection.getId());

        final ResultSet rs = ps.executeQuery();
        final List<Integer> result = new ArrayList<Integer>();

        for (; rs.next(); ) {
            final int sId = rs.getInt(1);
            result.add(sId);
        }

        return result;
    }

    private static void persistStoryLink(final Collection collection, final StoryLink link, final List<Integer> dbLinks,
            final Connection conn) throws SQLException {
        final boolean isInDb = dbLinks.contains(link.getStory());

        if (isInDb) {
            updateStoryLink(collection, link, conn);
        } else {
            insertStoryLink(collection, link, conn);
        }
    }

    private static void updateStoryLink(final Collection collection, final StoryLink link, final Connection conn)
            throws SQLException {
        final PreparedStatement update = conn
                .prepareStatement(
                        "UPDATE collection_story SET clearedForPublicInclusion=? WHERE collection=? AND story=?");
        update.setBoolean(1, link.getIsClearedForPublicInclusion());
        update.setInt(2, collection.getId());
        update.setInt(3, link.getStory());

        final int updateCount = update.executeUpdate();
        if (updateCount != 1) {
            throw new GeneralException("Unexpected update count: " + updateCount);
        }
    }

    private static void insertStoryLink(final Collection collection, final StoryLink link, final Connection conn)
            throws SQLException {
        final PreparedStatement insert = conn
                .prepareStatement(
                        "INSERT INTO collection_story (collection, story, clearedForPublicInclusion) VALUES (?,?,?)");
        insert.setInt(1, collection.getId());
        insert.setInt(2, link.getStory());
        insert.setBoolean(3, link.getIsClearedForPublicInclusion());

        final int insertCount = insert.executeUpdate();
        if (insertCount != 1) {
            throw new GeneralException("Unexpected insert count: " + insertCount);
        }
    }

    protected static void deleteStoryLink(final int collectionId, final int unlinkedStoryId, final Connection conn)
            throws SQLException {
        final PreparedStatement delete = conn
                .prepareStatement("DELETE FROM collection_story WHERE collection=? AND story=?");
        delete.setInt(1, collectionId);
        delete.setInt(2, unlinkedStoryId);

        final int updateCount = delete.executeUpdate();
        if (updateCount != 1) {
            throw new GeneralException("Unexpected insert count: " + updateCount);
        }
    }

    public static void loadAllStoryLinks(final Collection collection, final Connection conn) throws SQLException {
        final PreparedStatement ps = conn
                .prepareStatement("SELECT cs2.story, cs2.clearedForPublicInclusion "
				  + "FROM collection_story cs2 "
				  + "JOIN (SELECT DISTINCT(cs1.story) AS id "
				  + "FROM collection_story cs1 JOIN document d ON d.systemEntity=cs1.story "
				  + "JOIN systemEntity de ON de.id=d.id AND de.version=d.version "
				  + "WHERE d.systemEntityRelation IN ('BODY', 'ANSWER_SET') AND cs1.collection=? "
				  + "ORDER BY de.created DESC) AS j ON j.id=cs2.story AND cs2.collection=?");
        ps.setInt(1, collection.getId());
        ps.setInt(2, collection.getId());

        final ResultSet rs = ps.executeQuery();
        final Set<StoryLink> links = instantiateLinks(rs);

        collection.setStories(links);
    }

    private static Set<StoryLink> instantiateLinks(final ResultSet rs) throws SQLException {
        final Set<StoryLink> links = new HashSet<StoryLink>();

        for (; rs.next(); ) {
            final StoryLink link = instantiateLink(rs);
            links.add(link);
        }

        return links;
    }

    private static StoryLink instantiateLink(final ResultSet rs) throws SQLException {
        final StoryLink link = new StoryLink();

        link.setStory(rs.getInt(1));
        link.setIsClearedForPublicInclusion(rs.getBoolean(2));

        return link;
    }

    protected static void deleteStoryLinks(final Collection collection, final Connection connection)
            throws SQLException {
        final PreparedStatement delete = connection.prepareStatement("DELETE FROM collection_story WHERE collection=?");
        delete.setInt(1, collection.getId());
        delete.executeUpdate();
    }
}
