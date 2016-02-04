package org.consumersunion.stories.server.business_logic;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.api.ApiKey;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.ApiKeyPersister;
import org.consumersunion.stories.server.persistence.UserPersister;
import org.consumersunion.stories.server.util.SecurityContextProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {
    private static final Logger LOGGER = Logger.getLogger(ApiKeyServiceImpl.class.getName());

    private final ApiKeyPersister apiKeyPersister;
    private final UserPersister userPersister;
    private final SecurityContextProvider securityContextProvider;

    @Inject
    ApiKeyServiceImpl(
            ApiKeyPersister apiKeyPersister,
            UserPersister userPersister,
            SecurityContextProvider securityContextProvider) {
        this.apiKeyPersister = apiKeyPersister;
        this.userPersister = userPersister;
        this.securityContextProvider = securityContextProvider;
    }

    @Override
    public void loginWithApiKey(String apiKeyUuid) {
        ApiKey key = getApiKeyByUuid(apiKeyUuid);

        fetchAndMaybeLoginUser(key);
    }

    private ApiKey getApiKeyByUuid(String apiKeyUuid) {
        ApiKey key = null;

        try {
            key = apiKeyPersister.getByUuid(apiKeyUuid);
        } catch (GeneralException e) {
            LOGGER.log(Level.WARNING, "Could not retrieve API key", e);
        }

        return key;
    }

    private void fetchAndMaybeLoginUser(ApiKey key) {
        if (key != null) {
            User user = getUserByKey(key);

            maybeLoginUser(user);
        }
    }

    private User getUserByKey(ApiKey key) {
        User user = null;

        try {
            user = userPersister.get(key.getUser());
        } catch (GeneralException e) {
            LOGGER.log(Level.WARNING, "Could not retrieve user for API key " + key.getUuid(), e);
        }

        return user;
    }

    private void maybeLoginUser(User user) {
        if (user != null) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getHandle(), "", null);

            securityContextProvider.get().setAuthentication(authenticationToken);
        }
    }
}
