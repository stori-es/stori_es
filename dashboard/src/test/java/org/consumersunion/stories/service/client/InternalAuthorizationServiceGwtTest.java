package org.consumersunion.stories.service.client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.entity.Entity;
import org.consumersunion.stories.server.business_logic.AuthorizationService;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_OWNER;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class InternalAuthorizationServiceGwtTest extends SpringTestCase {
    @Inject
    private AuthorizationService authService;

    public void testAuthorizationService() throws SQLException {
        internalGrantAndDenyAccess();
        isGranted();
        getValidPrincipals();
        internalGetValidTarget();
    }

    private void internalGrantAndDenyAccess() throws SQLException {
        final int PROFILE_ID = 1053;

        Authentication authentication = new TestAuthentication(new TestUserDetails());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Collection collection = new Collection();
        collection.setId(4);

        // Make first grant, then second to verify that we udpate properly.
        authService.grant(PROFILE_ID, ROLE_CURATOR, collection.getId());
        authService.grant(PROFILE_ID, ROLE_READER, collection.getId());

        final Connection conn = PersistenceUtil.getConnection();
        try {
            // This one does NOT select an the mask / role in order to make sure only one
            ResultSet resultsGrantWrite = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM acl_entry acl_e "
                            + "JOIN acl_object_identity acl_oi ON  acl_e.acl_object_identity = acl_oi.id "
                            + "WHERE acl_oi.object_id_identity = 4 and acl_e.sid =" + PROFILE_ID);
            if (!resultsGrantWrite.next()) {
                fail("No results");
            } else {
                assertEquals("Grant access for WRITE operation does not match", 1, resultsGrantWrite.getLong(1));
            }
            conn.commit();

            // upgrade role
            authService.grant(PROFILE_ID, ROLE_CURATOR, collection.getId());

            ResultSet resultsGrantRead = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM acl_entry acl_e "
                            + "JOIN acl_object_identity acl_oi ON  acl_e.acl_object_identity = acl_oi.id "
                            + "WHERE acl_oi.object_id_identity = 4 and acl_e.mask= " + ROLE_CURATOR + " and acl_e.sid" +
                            " =" + PROFILE_ID);
            if (!resultsGrantRead.next()) {
                fail("No results");
            } else {
                assertEquals("Grant access for READ operation does not match", 1, resultsGrantRead.getLong(1));
            }

            conn.commit();

            authService.deny(PROFILE_ID, collection.getId());

            ResultSet resultsDenyWrite = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM acl_entry acl_e "
                            + "JOIN acl_object_identity acl_oi ON  acl_e.acl_object_identity = acl_oi.id "
                            + "WHERE acl_oi.object_id_identity = 4 and acl_e.sid =" + PROFILE_ID);
            if (!resultsDenyWrite.next()) {
                fail("No results");
            } else {
                assertEquals("Deny access found grant operation does not match", 0, resultsDenyWrite.getLong(1));
            }
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error on retrail information after grant access to SystemEntity(Collection) #4 for acl_sid.id #2, " +
                    "eror message: "
                    + ex.getMessage());
        } finally {
            conn.close();
        }
    }

    private void isGranted() throws SQLException {
        SystemEntity user = new SystemEntity(1035, 1) {
        };
        SystemEntity user2 = new SystemEntity(1038, 1) {
        };

        Authentication authentication = new TestAuthentication(new TestUserDetails());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Collection collectionWithReadPermission = new Collection();
        collectionWithReadPermission.setId(36);

        Collection collectionWithWritePermission = new Collection();
        collectionWithWritePermission.setId(37);

        // Test only read permission set up
        assertTrue("User with id 1035 has READER roleover SystemEntity #36", authService
                .hasMinRole(user.getId(), ROLE_READER, collectionWithReadPermission.getId()));
        assertFalse("User with id 1035 does not have CURATOR role over SystemEntity #36", authService
                .hasMinRole(user.getId(), ROLE_CURATOR, collectionWithReadPermission.getId()));

        // Test curator role; remember curator implies reader.
        assertTrue("User with id 1035 does not have READER role over SystemEntity #37", authService
                .hasMinRole(user.getId(), ROLE_READER, collectionWithWritePermission.getId()));
        assertTrue("User with id 1035 has CURATOR role over SystemEntity #37", authService
                .hasMinRole(user.getId(), ROLE_CURATOR, collectionWithWritePermission.getId()));

        // Test not data registered for this user
        assertFalse("User with id 1038 does not have READ permission over SystemEntity #36", authService
                .hasMinRole(user2.getId(), ROLE_READER, collectionWithReadPermission.getId()));
    }

    private void getValidPrincipals() {
        Collection collectionWithOnlyOneTarget = new Collection();
        collectionWithOnlyOneTarget.setId(36);

        List<SystemEntity> onePrincipal = authService.getValidPrincipals(ROLE_ADMIN,
                collectionWithOnlyOneTarget.getId(), null);
        assertEquals("Collection #36 should have two entities with ADMIN permissions", 1, onePrincipal.size());
        assertEquals("Profile id does not match with Target for collection 36", 1001, onePrincipal.get(0).getId());

        Collection collectionWithNonReadTargets = new Collection();
        collectionWithNonReadTargets.setId(52);
        List<SystemEntity> nonReadPrincipals = authService.getValidPrincipals(ROLE_READER,
                collectionWithNonReadTargets.getId(), null);
        assertEquals("There must not be any target for Collection 5", 0, nonReadPrincipals.size());

        Collection collectionWithOnlyTwoTargets = new Collection();
        collectionWithOnlyTwoTargets.setId(4);

        List<SystemEntity> twoPrincipals = authService.getValidPrincipals(ROLE_READER,
                collectionWithOnlyTwoTargets.getId(), null);
        assertEquals("Collection 36 must has only two principal with READ permissions", 2, twoPrincipals.size());
        assertTrue("User id does not match with Target for collection 4", 1035 == twoPrincipals.get(0).getId()
                || 1001 == twoPrincipals.get(0).getId());
        assertTrue("User id does not match with Target for collection 4", 1035 == twoPrincipals.get(0).getId()
                || 1001 == twoPrincipals.get(0).getId());

        Collection collectionWithoutTargets = new Collection();
        collectionWithoutTargets.setId(39);
        List<SystemEntity> nonPrincipals = authService.getValidPrincipals(ROLE_READER,
                collectionWithoutTargets.getId(), null);
        assertEquals("There must not be any target for Collection 39", 0, nonPrincipals.size());
    }

    private void internalGetValidTarget() {
        final int PROFILE_1001 = 1001;
        final int PROFILE_1035 = 1035;
        final int PROFILE_1038 = 1038;

        List<SystemEntity> readListUser38 = authService.getValidTargets(PROFILE_1038, ROLE_OWNER,
                null);
        assertNotNull("Expected empty, but non-null result.", readListUser38);
        assertEquals(0, readListUser38.size());

        List<SystemEntity> readListUser35 = authService.getValidTargets(PROFILE_1035, ROLE_READER, null);
        assertEquals("There must be 6 objects inside readListUser35", 6, readListUser35.size());
        for (int i = 0; i < 6; i += 1) {
            int targetId = readListUser35.get(i).getId();
            assertTrue("Unexpected ID in target list: " + targetId,
                    targetId == 1 || targetId == 2 || targetId == 33 || targetId == 4
                            || targetId == 36 || targetId == 37);
        }

        List<SystemEntity> curatorListUser35 = authService.getValidTargets(PROFILE_1035,
                ROLE_CURATOR, null);
        assertEquals("There must be 3 object inside writeListUser35", 3, curatorListUser35.size());
        for (int i = 0; i < 3; i += 1) {
            int targetId = curatorListUser35.get(i).getId();
            assertTrue("Unexpected ID in target list: " + targetId,
                    targetId == 33 || targetId == 4 || targetId == 37);
        }

        List<SystemEntity> adminListUser1035 = authService.getValidTargets(PROFILE_1035, ROLE_ADMIN,
                null);
        assertEquals("There must be at 1 object inside writeListUser1", 1, adminListUser1035.size());
        for (SystemEntity systemEntity : adminListUser1035) {
            if (systemEntity.getId() == 4) {
                assertTrue("SystemEntity 4 must be inside readListUser1", systemEntity.getId() == 4);
            }
        }

        List<SystemEntity> writeListUser1 = authService.getValidTargets(PROFILE_1001, ROLE_CURATOR,
                null);
        assertNotNull("There's any SystemEntity with WRITE permission for user 1", writeListUser1);
    }

    class TestAuthentication implements Authentication {

        private static final long serialVersionUID = -1995799023773172143L;

        private UserDetails userDetails;
        private boolean authentication = true;

        public TestAuthentication(UserDetails userDetails) {
            this.userDetails = userDetails;
        }

        public TestAuthentication(UserDetails userDetails, boolean authentication) {
            this.userDetails = userDetails;
            this.authentication = authentication;
        }

        TestAuthentication() {
        }

        public UserDetails getUserDetails() {
            return this.userDetails;
        }

        public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
            return userDetails.getAuthorities();
        }

        public Object getCredentials() {
            return null;
        }

        public Object getDetails() {
            return null;
        }

        public Object getPrincipal() {
            return this.userDetails;
        }

        public boolean isAuthenticated() {
            return authentication;
        }

        public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
            this.authentication = arg0;
        }

        public String getName() {
            return userDetails.getUsername();
        }
    }

    class TestUserDetails extends Entity implements UserDetails {

        private String username;
        private String password;
        private List<GrantedAuthority> authorities;

        public TestUserDetails() {
            this(
                    "aclUser1",
                    "d482134925ca68003ef4fa7c2f019d41abd79672e438a990acdc5ac7fe9b8d8f3a7fd27664532bea5e832387a51a51e19f0e7ab5bf22f0d8075d3e963ef73f2a",
                    35, 1);
        }

        public TestUserDetails(String username, String password, int id, int version) {
            this.username = username;
            this.password = password;
            this.setId(id);
            this.setVersion(version);
            this.authorities = new ArrayList<GrantedAuthority>();
        }

        private static final long serialVersionUID = -4209656801949535063L;

        @Override
        public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
            return this.authorities;
        }

        @Override
        public String getPassword() {
            return this.password;
        }

        @Override
        public String getUsername() {
            return this.username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
