package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister.UserProfileStruct;

import junit.framework.TestCase;

public class UserPersisterTest extends SpringTestCase {
    @Inject
    private CredentialedUserPersister credentialedUserPersister;

    public void testCreate() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM user");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the udpate

            final User template = createTemplate(conn, "foo").credentialedUser.getUser();

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM user");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long postCount = results.getLong(1);

            TestCase.assertEquals("New user count didn't match.", initialCount + 1, postCount);
            TestCase.assertTrue("Bad ID", template.getId() > 0);
            // The create process actually involves an update, so version is 2.
            assertEquals("Unexpected version", 2, template.getVersion());
        } finally {
            conn.close();
        }
    }

    public void testRetrieve() throws SQLException {
        Connection conn = PersistenceUtil.getConnection();
        try {
            final User template = createTemplate(conn, "roger").credentialedUser.getUser();

            final User retrieved = PersistenceUtil.process(conn, new UserPersister.RetrieveUserFunc(template.getId()));

            assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
            assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
            assertEquals("Unexpected handle.", template.getHandle(), retrieved.getHandle());
            assertEquals("Unexpected active.", template.isActive(), retrieved.isActive());
        } finally {
            conn.close();
        }
    }

    public void testUpdate() throws SQLException {
        Connection conn = PersistenceUtil.getConnection();
        try {
            final User template = createTemplate(conn, "roger2").credentialedUser.getUser();

            template.setActive(false);
            PersistenceUtil.process(conn, new UserPersister.UpdateUserFunc(template));
            // Creating a user involves an update, so that's 1-2 for create and 3 for this update.
            assertEquals("Unexpected version.", 3, template.getVersion());

            final User retrieved = PersistenceUtil.process(conn, new UserPersister.RetrieveUserFunc(template.getId()));

            assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
            assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
            assertEquals("Unexpected active.", template.isActive(), retrieved.isActive());
        } finally {
            conn.close();
        }
    }

    public void testChangeHandleFails() throws SQLException {
        Connection conn = PersistenceUtil.getConnection();
        final User template = createTemplate(conn, "roger3").credentialedUser.getUser();
        // good
        try {
            template.setHandle("new handle");
            assertEquals("Handle must be allow to be updated", "new handle", template.getHandle());
        } catch (GeneralException e) {
            TestCase.fail("Failed to raise exception when changing handle.");
        } finally {
            conn.close();
        }
    }

    public void testDelete() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        final User template = createTemplate(conn, "roger4").credentialedUser.getUser();
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM user");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the udpate

            PersistenceUtil.process(conn, new UserPersister.DeleteUserFunc(template));

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM user");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long postCount = results.getLong(1);

            TestCase.assertEquals("New user count didn't match.", initialCount - 1, postCount);
            assertEquals("Bad ID", -1, template.getId());
            assertEquals("Bad ID", -1, template.getVersion());
        } finally {
            conn.close();
        }
    }

    private UserProfileStruct createTemplate(Connection conn, final String handle) {
        CredentialedUser template = new CredentialedUser();
        template.getUser().setActive(true);
        template.getUser().setHandle(handle);
        template.setPasswordClearText("pass");
        template.setResetQuestion("Favorite movie?");
        template.setResetAnswer("Lord of the Rings");
        Profile initialProfile = new Profile();
        initialProfile.setOrganizationId(2);
        UserProfileStruct userProfile = new UserProfileStruct(template, initialProfile);

        return credentialedUserPersister.createUserProfile(conn, userProfile);
    }
}
