package org.consumersunion.stories.server.business_logic;

import java.util.List;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.export.StoryExport;
import org.consumersunion.stories.server.export.StoryTellerCsv;

public interface ProfileService {
    /**
     * Retrieves the indicated profile. The logged in user must be associated to the {@link Profile}.
     */
    ProfileSummary get(int id);

    ProfileSummary getForOrganization(CredentialedUser user, Organization organization);

    ProfileSummary createProfile(Profile profile);

    StoryExport<StoryTellerCsv> exportStoryTellers(int userId, Integer collectionId, Integer questionnaireId,
            int window);

    void delete(int id);

    Profile updateProfile(Profile profile);

    Profile getProfile(int id);

    List<Integer> getOrganizationProfiles(int organizationId);
}
