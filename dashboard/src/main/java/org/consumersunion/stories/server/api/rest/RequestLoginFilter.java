package org.consumersunion.stories.server.api.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.business_logic.ApiKeyService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
@Provider
@PreMatching
public class RequestLoginFilter implements ContainerRequestFilter {
    private final ApiKeyService keyService;
    private final UserService userService;
    private final ProfilePersister profilePersister;

    @Inject
    RequestLoginFilter(
            ApiKeyService keyService,
            UserService userService,
            ProfilePersister profilePersister) {
        this.keyService = keyService;
        this.userService = userService;
        this.profilePersister = profilePersister;
    }

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        if (userService.getLoggedInUser() == null) {
            handleApiKey(context);
            handleOrganizationContext(context);
        }
    }

    private void handleOrganizationContext(ContainerRequestContext context) {
        MultivaluedMap<String, String> queryParameters = context.getUriInfo().getQueryParameters();

        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser != null) {
            String organizationContext = queryParameters.getFirst(UrlParameters.ORGANIZATION_CONTEXT);

            Integer profileId = null;
            if (organizationContext != null) {
                int orgId = Integer.valueOf(organizationContext);
                ProfileSummary profileSummary = profilePersister.getProfileByOrganization(orgId, loggedInUser.getId());
                if (profileSummary != null) {
                    profileId = profileSummary.getProfile().getId();
                }
            } else {
                profileId = loggedInUser.getDefaultProfile();
            }

            if (profileId != null) {
                userService.setActiveProfileId(profileId);
            }
        }
    }

    private void handleApiKey(ContainerRequestContext context) {
        String authorization = context.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!Strings.isNullOrEmpty(authorization)) {
            String key = authorization.replaceFirst(SecurityContext.BASIC_AUTH + " ", "");

            keyService.loginWithApiKey(key);
        }
    }
}
