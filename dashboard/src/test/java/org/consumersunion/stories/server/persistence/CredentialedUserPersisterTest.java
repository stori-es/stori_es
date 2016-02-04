package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister.RetrieveCredentialedUserFunc;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister.UpdateCredentialedUserFunc;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister.UserProfileStruct;

import junit.framework.TestCase;

public class CredentialedUserPersisterTest extends SpringTestCase {
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

            CredentialedUser user = createTemplate(conn, "fooHandleTest1").credentialedUser;

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM user");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long postCount = results.getLong(1);

            assertNotNull(user);
            TestCase.assertEquals("New user count didn't match.", initialCount + 1, postCount);
            TestCase.assertTrue("Bad ID", user.getUser().getId() > 0);
            // Version is '2' because the internal creation has to update the new user with the initial profile ID.
            assertEquals("Unexpected version", 2, user.getUser().getVersion());
        } finally {
            conn.close();
        }
    }

    public void testRetrieve() throws SQLException {
        Connection conn = PersistenceUtil.getConnection();
        try {
            CredentialedUser template = createTemplate(conn, "fooHandleTest2").credentialedUser;

            CredentialedUser retrieved = PersistenceUtil.process(conn,
                    new RetrieveCredentialedUserFunc(template.getUser().getId()));

            assertEquals("Unexpected ID.", template.getUser().getId(), retrieved.getUser().getId());
            assertEquals("Unexpected version.", template.getUser().getVersion(), retrieved.getUser().getVersion());
            assertEquals("Unexpected handle.", template.getUser().getHandle(), retrieved.getUser().getHandle());
            assertEquals("Unexpected active.", template.getUser().isActive(), retrieved.getUser().isActive());
            assertEquals("Unexpected PasswordHash.", template.getPasswordHash(), retrieved.getPasswordHash());
            assertEquals("Unexpected ResetAnswer.", template.getResetAnswer(), retrieved.getResetAnswer());
            assertEquals("Unexpected ResetQuestion.", template.getResetQuestion(), retrieved.getResetQuestion());
            assertEquals("Unexpected default profile.", template.getUser().getDefaultProfile(), retrieved.getUser()
                    .getDefaultProfile());
        } finally {
            conn.close();
        }
    }

    public void testUpdate() throws SQLException {
        Connection conn = PersistenceUtil.getConnection();
        try {
            CredentialedUser template = createTemplate(conn, "fooHandleTest3").credentialedUser;

            template.getUser().setActive(false);
            template.setPasswordClearText("pass1");
            template.setResetQuestion("Favorite song?");
            template.setResetAnswer("Paradise");
            CredentialedUser retrieved = PersistenceUtil.process(new UpdateCredentialedUserFunc(template));

            assertEquals("Unexpected ID.", template.getUser().getId(), retrieved.getUser().getId());
            assertEquals("Unexpected version.", template.getUser().getVersion(), retrieved.getUser().getVersion());
            assertEquals("Unexpected handle.", template.getUser().getHandle(), retrieved.getUser().getHandle());
            assertEquals("Unexpected active.", template.getUser().isActive(), retrieved.getUser().isActive());
            assertEquals("Unexpected PasswordHash.", template.getPasswordHash(), retrieved.getPasswordHash());
            assertEquals("Unexpected ResetAnswer.", template.getResetAnswer(), retrieved.getResetAnswer());
            assertEquals("Unexpected ResetQuestion.", template.getResetQuestion(), retrieved.getResetQuestion());
            assertEquals("Unexpected default profile.", template.getUser().getDefaultProfile(), retrieved.getUser()
                    .getDefaultProfile());
        } finally {
            conn.close();
        }
    }

    public void testDelete() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        CredentialedUser template = createTemplate(conn, "fooHandleTest4").credentialedUser;
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM user");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update
            PersistenceUtil.process(conn, new CredentialedUserPersister.DeleteCredentialedUserFunc(template));
            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM user");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long postCount = results.getLong(1);
            TestCase.assertEquals("New user count didn't match.", initialCount - 1, postCount);
        } finally {
            conn.close();
        }
    }

    public void testEncryptPassword() {
        CredentialedUserPersister persister = new CredentialedUserPersister();
        CredentialedUser testUser = new CredentialedUser();

        testUser.getUser().setHandle("testUser");

        testUser.setPasswordClearText("password");
        String encryptedPassword = PersistenceUtil.process(persister.getPasswordCrypted(testUser));
        TestCase.assertNotNull(encryptedPassword);

        testUser.setPasswordClearText("newPassword");
        String encryptedNewPassword = PersistenceUtil.process(persister.getPasswordCrypted(testUser));
        TestCase.assertNotNull(encryptedNewPassword);

        TestCase.assertNotSame(encryptedPassword, encryptedNewPassword);

        testUser.setPasswordClearText(null);
        assertNull(PersistenceUtil.process(persister.getPasswordCrypted(testUser)));
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
