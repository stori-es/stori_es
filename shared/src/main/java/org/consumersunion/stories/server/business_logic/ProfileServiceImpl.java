package org.consumersunion.stories.server.business_logic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.exception.BadRequestException;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.export.StoryExport;
import org.consumersunion.stories.server.export.StoryTellerCsv;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.elasticsearch.SortOrder;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.consumersunion.stories.server.index.elasticsearch.search.Search;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchBuilder;
import org.consumersunion.stories.server.index.profile.ProfileDocument;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.persistence.StoryTellersParams;
import org.springframework.stereotype.Service;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PRIVILEGED;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ROOT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final Indexer<ProfileDocument> profileIndexer;
    private final AuthorizationService authService;
    private final UserService userService;
    private final StoryService storyService;
    private final ProfilePersister profilePersister;

    @Inject
    ProfileServiceImpl(
            Indexer<ProfileDocument> profileIndexer,
            AuthorizationService authService,
            UserService userService,
            StoryService storyService,
            ProfilePersister profilePersister) {
        this.profileIndexer = profileIndexer;
        this.authService = authService;
        this.userService = userService;
        this.storyService = storyService;
        this.profilePersister = profilePersister;
    }

    @Override
    public ProfileSummary get(int id) {
        ProfileSummary profileSummary = profilePersister.getProfileSummary(id);

        if (profileSummary == null) {
            throw new NotFoundException();
        }

        if (!authService.canRead(profileSummary.getProfile())) {
            throw new NotAuthorizedException();
        }

        return profileSummary;
    }

    @Override
    public ProfileSummary getForOrganization(CredentialedUser user, Organization organization) {
        return profilePersister.getProfileByOrganization(organization.getId(), user.getId());
    }

    @Override
    public ProfileSummary createProfile(Profile profile) {
        User loggedInUser = userService.getLoggedInUser();
        if (profile.getUserId() != null && profile.getUserId() == loggedInUser.getId()
                || authService.isUserAuthorized(ROLE_ADMIN, profile.getOrganizationId())) {
            return profilePersister.create(profile);
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Override
    public StoryExport<StoryTellerCsv> exportStoryTellers(
            int profileId,
            Integer collectionId,
            Integer questionnaireId,
            int window) {
        try {
            int windowSize = 75;
            User user = userService.getUserForProfile(profileId);
            Profile userProfile = profilePersister.get(profileId);
            int organizationContext = userProfile.getOrganizationId();

            int accessMode = ACCESS_MODE_PRIVILEGED;
            if (authService.isSuperUser(user)) {
                accessMode = ACCESS_MODE_ROOT;
            }

            List<StoryTellerCsv> storyTellers = new ArrayList<StoryTellerCsv>();
            try {
                StoryTellersParams countParams = new StoryTellersParams(0, 1, StorySortField.CREATED_OLD, false,
                        collectionId, questionnaireId, accessMode, userService.getEffectiveSubject(user), null);
                int countStories = storyService.getStorytellerCount(countParams);

                // window starts at 0; bail out if we're asked for a window beyond what's available
                if (window * windowSize >= countStories) {
                    return new StoryExport<StoryTellerCsv>(storyTellers, countStories);
                }

                try {
                    SearchBuilder searchBuilder = SearchBuilder.newBuilder();
                    QueryBuilder queryBuilder = QueryBuilder.newBuilder();
                    if (collectionId != null) {
                        queryBuilder.withTerm("collections", collectionId);
                        searchBuilder.withSort("lastStoryDateByCollection." + collectionId, SortOrder.DESC);
                    }

                    if (questionnaireId != null) {
                        queryBuilder.withTerm("questionnaires", questionnaireId);
                        searchBuilder.withSort("lastStoryDateByCollection." + questionnaireId, SortOrder.DESC);
                    }

                    if (!authService.isSuperUser(user)) {
                        queryBuilder.withTerm("readAuths", organizationContext);
                    }

                    Search search = searchBuilder
                            .withQuery(queryBuilder.build())
                            .withSize(windowSize)
                            .withFrom(window * windowSize)
                            .build();

                    List<ProfileDocument> profileDocuments = profileIndexer.search(search);

                    if (!profileDocuments.isEmpty()) {
                        for (ProfileDocument doc : profileDocuments) {
                            Profile profile = profilePersister.get(doc.getId());
                            StoryTellerCsv storyTellerCsv = new StoryTellerCsv(doc, profile);

                            storyTellers.add(storyTellerCsv);
                        }
                    }

                    return new StoryExport<StoryTellerCsv>(storyTellers, countStories);
                } catch (Exception e) {
                    throw new GeneralException(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new StoryExport<StoryTellerCsv>(storyTellers, 0);
        } catch (NotLoggedInException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public void delete(int id) {
        if (!authService.isUserAuthorized(AuthConstants.ROLE_ADMIN, id)) {
            throw new NotAuthorizedException();
        }

        try {
            // Let FK handle profile dependencies
            profilePersister.delete(id);
        } catch (GeneralException ignored) {
            throw new BadRequestException();
        }
    }

    @Override
    public Profile updateProfile(Profile profile) {
        if (!authService.isUserAuthorized(ROLE_CURATOR, profile)) {
            throw new NotAuthorizedException();
        }

        return profilePersister.update(profile);
    }

    @Override
    public Profile getProfile(int id) {
        Profile profile = profilePersister.get(id);

        if (profile == null) {
            throw new NotFoundException();
        }

        if (!authService.isUserAuthorized(ROLE_READER, profile)) {
            throw new NotAuthorizedException();
        }

        return profile;
    }

    @Override
    public List<Integer> getOrganizationProfiles(int organizationId) {
        if (!authService.isUserAuthorized(ROLE_CURATOR, organizationId)) {
            throw new NotAuthorizedException();
        }

        return profilePersister.getProfileIdsForOrganization(organizationId);
    }
}
