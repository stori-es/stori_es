package org.consumersunion.stories.server.api.gwt_rpc;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcProfileService;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.service.response.Response;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryAndStorytellerData;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.business_logic.ProfileService;
import org.consumersunion.stories.server.business_logic.StoryService;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.persistence.StoryTellersParams;
import org.consumersunion.stories.server.persistence.UserPersister;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.params.PagedRetrieveParams;
import org.consumersunion.stories.server.rest.api.convio.SyncFromSysPersonToConvioConstituentRequestFactory;
import org.consumersunion.stories.server.solr.person.UpdatePersonIndexer;
import org.springframework.stereotype.Service;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.server.persistence.ProfilePersister.RetrieveProfileSummaryFunc;

@Service("profileService")
public class RpcProfileServiceImpl extends RpcBaseServiceImpl implements RpcProfileService {
    @Inject
    private UserPersister userPersister;
    @Inject
    private ProfilePersister profilePersister;
    @Inject
    private ProfileService profileService;
    @Inject
    private StoryService storyService;
    @Inject
    private SyncFromSysPersonToConvioConstituentRequestFactory syncFromSysPersonConvioFactory;

    @Override
    public DatumResponse<ProfileSummary> retrieveProfile(int id) {
        DatumResponse<ProfileSummary> response = new DatumResponse<ProfileSummary>();

        try {
            ProfileSummary profileSummary = profileService.get(id);
            response.setDatum(profileSummary);
        } catch (NotFoundException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidRequest());
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidRequest());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public PagedDataResponse<StoryAndStorytellerData> getStorytellers(
            int start,
            int length,
            SortField sortField,
            boolean ascending,
            String searchText,
            int relation) {
        PagedDataResponse<StoryAndStorytellerData> response = new PagedDataResponse<StoryAndStorytellerData>();
        try {
            User user = userService.getLoggedInUser(true);

            StoryTellersParams params = new StoryTellersParams(start, length, sortField, ascending, null, null,
                    relation, getEffectiveSubject(user), searchText);

            List<StoryAndStorytellerData> stories = getPagedStorytellers(params);
            response.setStart(start);
            response.setData(stories);

            int count = storyService.getStorytellerCount(params);
            response.setTotalCount(count);
        } catch (Exception e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }
        return response;
    }

    @Override
    public DatumResponse<ProfileSummary> update(Profile profile) {
        DatumResponse<ProfileSummary> response = new DatumResponse<ProfileSummary>();
        try {
            ProfileSummary profileSummary = persistenceService.process(new RetrieveProfileSummaryFunc(profile.getId()));
            Profile retrievedProfile = profileSummary.getProfile();
            if (authService.isUserAuthorized(ROLE_CURATOR, retrievedProfile)) {
                persistenceService.process(new ProfilePersister.UpdateProfileFunc(profile));
                ProfileSummary updatedProfile =
                        persistenceService.process(new RetrieveProfileSummaryFunc(profile.getId()));
                response.setDatum(updatedProfile);

                indexerService.process(new UpdatePersonIndexer(updatedProfile.getProfile()));
                storyService.updateAuthor(updatedProfile);
                syncFromSysPersonConvioFactory.create(updatedProfile.getProfile())
                        .queueSysToConvioUpdates(updatedProfile, isPersonSelfUpdate(profile.getId()),
                                getLoggedInUserId());
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException ignored) {
            response.setLoggedIn(false);
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
        }

        return response;
    }

    @Override
    public DataResponse<ProfileSummary> getProfileSummaries() {
        try {
            User user = userService.getLoggedInUser(true);
            return getProfileSummaries(user.getId());
        } catch (NotLoggedInException ignored) {
            DataResponse<ProfileSummary> response = new DataResponse<ProfileSummary>();
            response.setLoggedIn(false);
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
            return response;
        }
    }

    @Override
    public DataResponse<ProfileSummary> getProfileSummaries(int userId) {
        DataResponse<ProfileSummary> response = new DataResponse<ProfileSummary>();
        try {
            userService.getLoggedInUser(true);

            List<ProfileSummary> profileSummaries = retrieveProfiles(userId, response);
            response.setData(profileSummaries);
        } catch (NotLoggedInException ignored) {
            response.setLoggedIn(false);
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
        }
        return response;
    }

    @Override
    public DatumResponse<ProfileSummary> setActiveProfile(int profileId, boolean removeAttribute) {
        DatumResponse<ProfileSummary> response = new DatumResponse<ProfileSummary>();
        try {
            User loggedInUser = userService.getLoggedInUser(true);
            List<ProfileSummary> profileSummaries = retrieveProfiles(loggedInUser.getId(), response);
            if (response.isError()) {
                return response;
            }

            for (ProfileSummary profileSummary : profileSummaries) {
                if (profileSummary.getProfile().getId() == profileId) {
                    response.setDatum(profileSummary);
                    userService.setActiveProfileId(profileSummary.getProfile().getId());
                    return response;
                }
            }

            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotLoggedInException ignored) {
            response.setLoggedIn(false);
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
        }
        return response;
    }

    private Integer getLoggedInUserId() throws NotLoggedInException {
        User user = userService.getLoggedInUser();
        if (user == null) {
            return null;
        } else {
            return user.getId();
        }
    }

    private List<StoryAndStorytellerData> getPagedStorytellers(StoryTellersParams params) {
        ProcessFunc<? extends PagedRetrieveParams, List<StoryAndStorytellerData>> retrieveFunc =
                profilePersister.retrieveStorytellersPagedFunc(params);
        return persistenceService.process(retrieveFunc);
    }

    private List<ProfileSummary> retrieveProfiles(int targetUserId, Response response) {
        List<ProfileSummary> profileSummaries = retrieveProfiles(targetUserId);
        if (profileSummaries == null) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }
        return profileSummaries;
    }

    private List<ProfileSummary> retrieveProfiles(int targetUserId) {
        User targetUser = userPersister.get(targetUserId);
        try {
            if (authService.isUserAuthorized(ROLE_ADMIN, targetUser)) {
                return persistenceService.process(new ProfilePersister.RetrieveProfilesFunc(targetUserId));
            }
        } catch (NotLoggedInException ignored) {
        }

        return null;
    }

    /**
     * Determines whether or not a {@link Person} is 'self updating'. This is true if the authenticated {@link User}
     * has the same ID as the {@link Person} being updated.
     */
    private boolean isPersonSelfUpdate(int targetId) {
        User loggedInUser = userService.getLoggedInUser();

        return loggedInUser != null && loggedInUser.getId() == targetId;
    }
}
