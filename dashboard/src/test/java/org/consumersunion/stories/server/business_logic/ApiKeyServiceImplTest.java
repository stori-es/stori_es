package org.consumersunion.stories.server.business_logic;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.api.ApiKey;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.persistence.ApiKeyPersister;
import org.consumersunion.stories.server.persistence.UserPersister;
import org.consumersunion.stories.server.util.SecurityContextProvider;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;

import com.google.common.base.Objects;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class ApiKeyServiceImplTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(UserPersister.class);
            forceMock(ApiKeyPersister.class);
        }
    }

    private static final String A_HANDLE = "A_HANDLE";
    private static final int USER_ID = 8;

    @Inject
    ApiKeyServiceImpl apiKeyService;
    @Inject
    ApiKeyPersister apiKeyPersister;
    @Inject
    UserPersister userPersister;
    @Inject
    SecurityContext securityContext;

    @Before
    public void setUp(SecurityContextProvider securityContextProvider) {
        given(securityContextProvider.get()).willReturn(securityContext);
    }

    @Test
    public void loginWithApiKey_withValidApiKey_setAuthentication() {
        String apiKeyUuid = "12345";
        User user = new User(USER_ID, 1);
        user.setHandle(A_HANDLE);
        ApiKey apiKey = new ApiKey(USER_ID, apiKeyUuid, apiKeyUuid);
        given(apiKeyPersister.getByUuid(same(apiKeyUuid))).willReturn(apiKey);
        given(userPersister.get(USER_ID)).willReturn(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(A_HANDLE, "", null);

        apiKeyService.loginWithApiKey(apiKeyUuid);

        verify(securityContext)
                .setAuthentication(argThat(new UsernamePasswordAuthenticationTokenMatcher(authenticationToken)));
    }

    @Test
    public void loginWithApiKey_invalidApiKey_noLogin() {
        String apiKeyUuid = "12345";
        User user = new User(USER_ID, 1);
        user.setHandle(A_HANDLE);
        given(apiKeyPersister.getByUuid(same(apiKeyUuid))).willReturn(null);
        given(userPersister.get(USER_ID)).willReturn(user);

        apiKeyService.loginWithApiKey(apiKeyUuid);

        verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void loginWithApiKey_invalidUserId_noLogin() {
        String apiKeyUuid = "12345";
        User user = new User(USER_ID, 1);
        user.setHandle(A_HANDLE);
        ApiKey apiKey = new ApiKey(USER_ID, apiKeyUuid, apiKeyUuid);
        given(apiKeyPersister.getByUuid(same(apiKeyUuid))).willReturn(apiKey);
        given(userPersister.get(USER_ID)).willReturn(null);

        apiKeyService.loginWithApiKey(apiKeyUuid);

        verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
    }

    private static class UsernamePasswordAuthenticationTokenMatcher
            extends ArgumentMatcher<UsernamePasswordAuthenticationToken> {
        private final UsernamePasswordAuthenticationToken authenticationToken;

        UsernamePasswordAuthenticationTokenMatcher(UsernamePasswordAuthenticationToken authenticationToken) {
            this.authenticationToken = authenticationToken;
        }

        @Override
        public boolean matches(Object item) {
            UsernamePasswordAuthenticationToken other = (UsernamePasswordAuthenticationToken) item;

            return Objects.equal(other.getPrincipal(), authenticationToken.getPrincipal())
                    && Objects.equal(other.getCredentials(), authenticationToken.getCredentials());
        }
    }
}
