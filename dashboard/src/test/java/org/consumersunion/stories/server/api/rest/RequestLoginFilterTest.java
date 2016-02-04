package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.UriInfo;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.business_logic.ApiKeyService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class RequestLoginFilterTest {
    private static final int AN_ORG_CONTEXT = 12;

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(ProfilePersister.class);
        }
    }

    private static final String AN_API_KEY = "AN_API_KEY";
    private static final int USER_ID = 101;
    private static final int DEFAULT_PROFILE = 32;

    @Inject
    private RequestLoginFilter requestLoginFilter;
    @Inject
    private UserService userService;
    @Inject
    private ApiKeyService keyService;
    @Inject
    private ProfilePersister profilePersister;

    @Test
    public void filter_doesNotFilterLoggedInUserFromDashboard() throws Exception {
        ContainerRequestContext containerRequestContext = mock(ContainerRequestContext.class);
        given(userService.getLoggedInUser()).willReturn(mock(User.class));

        requestLoginFilter.filter(containerRequestContext);

        verify(containerRequestContext, never()).getHeaderString(anyString());
    }

    @Test
    public void filter_loginsWithApiKey_withoutContext_loginDefaultContext() throws Exception {
        ContainerRequestContext context = mock(ContainerRequestContext.class, RETURNS_DEEP_STUBS);
        User loggedInUser = new User(USER_ID, 1);
        loggedInUser.setDefaultProfile(DEFAULT_PROFILE);
        given(userService.getLoggedInUser()).willReturn(null, loggedInUser);
        given(context.getHeaderString(HttpHeaders.AUTHORIZATION)).willReturn(AN_API_KEY);

        requestLoginFilter.filter(context);

        verify(keyService).loginWithApiKey(AN_API_KEY);
        verify(userService).setActiveProfileId(DEFAULT_PROFILE);
    }

    @Test
    public void filter_loginsWithApiKey_withContext_loginContext() throws Exception {
        ContainerRequestContext context = mock(ContainerRequestContext.class);
        UriInfo uriInfo = mock(UriInfo.class);
        MultivaluedHashMap<String, String> params = new MultivaluedHashMap<String, String>();
        params.add(UrlParameters.ORGANIZATION_CONTEXT, String.valueOf(AN_ORG_CONTEXT));
        given(context.getUriInfo()).willReturn(uriInfo);
        given(uriInfo.getQueryParameters()).willReturn(params);

        User loggedInUser = new User(USER_ID, 1);
        given(userService.getLoggedInUser()).willReturn(null, loggedInUser);
        given(context.getHeaderString(HttpHeaders.AUTHORIZATION)).willReturn(AN_API_KEY);

        Profile profile = new Profile(DEFAULT_PROFILE, AN_ORG_CONTEXT, 1);
        given(profilePersister.getProfileByOrganization(AN_ORG_CONTEXT, USER_ID))
                .willReturn(new ProfileSummary(profile, null, false));

        requestLoginFilter.filter(context);

        verify(keyService).loginWithApiKey(AN_API_KEY);
        verify(userService).setActiveProfileId(DEFAULT_PROFILE);
    }
}
