package org.consumersunion.stories.common.client.service;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryAndStorytellerData;
import org.consumersunion.stories.server.business_logic.ProfileService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/profile")
public interface RpcProfileService extends RemoteService {
    /**
     * @see ProfileService#get(int)
     */
    DatumResponse<ProfileSummary> retrieveProfile(int id);

    /**
     * Searches {@link Profile}s. Results are filtered by those users over which the
     * logged in user has read privileges.
     */
    PagedDataResponse<StoryAndStorytellerData> getStorytellers(int start, int count, SortField sortField,
            boolean ascending, String searchText, int relation);

    /**
     * Updates the profile. The logged in user must either have WRITE privileges
     * or be the same profile.
     */
    DatumResponse<ProfileSummary> update(Profile entity);

    /**
     * Retrieves set of {@link org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary}s
     * over which user has OBO rights.
     */
    DataResponse<ProfileSummary> getProfileSummaries();

    /**
     * Retrieves the set of {@link org.consumersunion.stories.common.client.service.datatransferobject
     * .ProfileSummary}s over which the user has OBO
     * rights. Logged in user must be root.
     */
    DataResponse<ProfileSummary> getProfileSummaries(int userId);

    /**
     * Sets the OBO role for the user, updating the effective role. User must
     * have OBO privs over the target. OBO privs do not chain.
     */
    DatumResponse<ProfileSummary> setActiveProfile(int profileId, boolean removeAttribute);
}
