package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.Organization;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;

public class OrganizationPersisterTest extends SpringTestCase {
    @Inject
    private OrganizationPersister persister;

    public void testCreate() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM organization");
            if (!results.next()) {
                fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update

            final Organization template = new Organization();
            template.setPermalink("foo");
            template.setName("foo");
            template.setPublic(false);
            template.setDefaultTheme(90);
            PersistenceUtil.process(new OrganizationPersister.CreateOrganizationFunc(template));
            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM organization");
            if (!results.next()) {
                fail("No results");
            }
            final long postCount = results.getLong(1);
            assertEquals("New user count didn't match.", initialCount + 1, postCount);
            assertTrue("Bad ID", template.getId() > 0);
            assertEquals("Unexpected version", 1, template.getVersion());
        } finally {
            conn.close();
        }
    }

    public void testRetrieve() {
        final Organization template = new Organization();
        template.setPermalink("foo-2");
        template.setName("foo");
        template.setShortName("shortName");
        template.setDefaultTheme(90);
        PersistenceUtil.process(new OrganizationPersister.CreateOrganizationFunc(template));
        final Organization retrieved = (Organization)
                PersistenceUtil.process(new OrganizationPersister.RetrieveOrganizationFunc(template.getId()));
        assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
        assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
        assertEquals("Unexpected handle.", template.getPermalink(), retrieved.getPermalink());
        assertEquals("Unexpected active.", template.getName(), retrieved.getName());
        assertEquals("Unexpected shortName", template.getShortName(), retrieved.getShortName());
    }

    public void testUpdate() {
        final Organization template = new Organization();
        template.setPermalink("permalink");
        template.setName("name");
        template.setPublic(false);
        template.setShortName("shortName");
        template.setDefaultTheme(90);
        PersistenceUtil.process(new OrganizationPersister.CreateOrganizationFunc(template));

        template.setPermalink("foo-3");
        template.setName("Foo");
        template.setShortName("f");
        PersistenceUtil.process(new OrganizationPersister.UpdateOrganizationFunc(template));
        assertEquals("Unexpected version.", 2, template.getVersion());
        final Organization retrieved = (Organization)
                PersistenceUtil.process(new OrganizationPersister.RetrieveOrganizationFunc(template.getId()));

        assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
        assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
        assertEquals("Unexpected name.", template.getName(), retrieved.getName());
        assertEquals("Unexpected shotName.", template.getShortName(), retrieved.getShortName());
        assertEquals("Unexpected Permalink.", template.getPermalink(), retrieved.getPermalink());
    }

    public void testDelete() throws SQLException {
        final Organization template = new Organization();
        template.setPermalink("permalink2");
        template.setName("name");
        template.setPublic(false);
        template.setDefaultTheme(90);
        PersistenceUtil.process(new OrganizationPersister.CreateOrganizationFunc(template));

        final Connection conn = PersistenceUtil.getConnection();
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM organization");
            if (!results.next()) {
                fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update
            PersistenceUtil.process(new OrganizationPersister.DeleteOrganizationFunc(template));
            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM organization");
            if (!results.next()) {
                fail("No results");
            }
            final long postCount = results.getLong(1);
            assertEquals("New user count didn't match.", initialCount - 1, postCount);
            assertEquals("Bad ID", -1, template.getId());
            assertEquals("Bad ID", -1, template.getVersion());
        } finally {
            conn.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void testRetrieveAdminOrganizationsFunc() {
        try {
            final List<Organization> organizations = PersistenceUtil.process(persister.retrieveAdminOrganizationsFunc(
                    new OrganizationPersister.RetrievePagedOrganizationsParams(0, 20,
                            null, true, ACCESS_MODE_EXPLICIT, 2)));
            assertNotNull("Organizations Not null", organizations);
            assertTrue("Mayor than 0", (organizations.size() > 0));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void testRetrieveOrganizationFunc() {
        try {
            final Organization organization = PersistenceUtil.process(
                    new OrganizationPersister.RetrieveOrganizationFunc(2));
            assertNotNull("Organizations Not null", organization);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void testCheckOrganizationNameAvailability() {
        final Organization template = new Organization();
        template.setPermalink("foo-123");
        template.setName("foo123");
        template.setPublic(false);
        template.setShortName("shortName");
        template.setDefaultTheme(90);
        PersistenceUtil.process(new OrganizationPersister.CreateOrganizationFunc(template));

        assertTrue(
                "Organization name must be available",
                ((Boolean) PersistenceUtil.process(persister
                        .checkOrganizationNameAvailability(
                                "Hey Jude, don't make it bad. Take a sad song and make it better.")))
                        .booleanValue());
        assertFalse("Organization name must not be available", ((Boolean) PersistenceUtil.process(persister
                .checkOrganizationNameAvailability("foo123"))).booleanValue());
    }
}
