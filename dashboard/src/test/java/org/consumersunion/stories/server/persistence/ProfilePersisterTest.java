package org.consumersunion.stories.server.persistence;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.service.GeneralException;

import junit.framework.TestCase;

public class ProfilePersisterTest extends SpringTestCase {
    @Inject
    private ProfilePersister profilePersister;

    public void testCreatePerson() {
        Profile profile = new Profile();
        profile.setGivenName("Test user");
        profile.setSurname("Created by JUnit");
        profile.setOrganizationId(2);
        profile.setUserId(32);
        profile = profilePersister.createProfile(profile);
        TestCase.assertTrue("New profile must have id", profile.getId() > 0);
        TestCase.assertTrue("New profile must have version", profile.getVersion() > 0);
    }

    public void testGetPerson() {
        ProfileSummary profileSummary = PersistenceUtil.process(new ProfilePersister.RetrieveProfileSummaryFunc(1001));
        Profile profile = profileSummary.getProfile();
        assertEquals("Unexpcted id", 1001, profile.getId());
        assertEquals("Unexpected org ID", 2, profile.getOrganizationId());
        TestCase.assertTrue("Person user must have version", profile.getVersion() > 0);
        assertEquals("Unexpected surname", "One", profile.getSurname());
        assertEquals("Unexpected givenName", "Person", profile.getGivenName());
    }

    /* TODO
     * this is actually more of a test for the persistance process itself and not specific to Person; but we don't have
     * a library for that yet so I'll leave it here for the moment though I don't think this is the best place
     ODOT */
    public void testRejectVersionMismatch() {
        Profile profile = new Profile();
        profile.setId(38);
        profile.setGivenName("ACL User");
        profile.setSurname("Updated by JUnit");
        profile.setOrganizationId(2);
        try {
            PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
            TestCase.fail(
                    "Updating existing profile with manually constructed object with wrong version should have " +
                            "failed," +
                            " but didn't.");
        } catch (GeneralException e) {
            assertTrue("Did not find expected substring 'Version mismatch.' in exception: " + e.getMessage(),
                    e.getMessage().contains("Version mismatch."));
        }
    }

    public void testDeletePerson() {
        PersistenceUtil.process(new ProfilePersister.DeleteProfileFunc(new Profile(1045, 2, 1)));

        try {
            PersistenceUtil.process(new ProfilePersister.RetrieveProfileSummaryFunc(1045));
        } catch (Exception e) {
            TestCase.assertTrue("Exception error does not match",
                    e.getMessage().contains("No Person with ID 45 found."));
        }
    }
}
