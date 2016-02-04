package org.consumersunion.stories.server.security.authentication;

import javax.inject.Inject;

import org.apache.commons.validator.routines.EmailValidator;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final EmailValidator emailValidator;
    private final CredentialedUserPersister credentialedUserPersister;

    @Inject
    public CustomAuthenticationProvider(
            EmailValidator emailValidator,
            CredentialedUserPersister credentialedUserPersister) {
        this.emailValidator = emailValidator;
        this.credentialedUserPersister = credentialedUserPersister;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        CredentialedUser user;
        if (emailValidator.isValid(username)) {
            user = credentialedUserPersister.getByEmail(username, false);
        } else {
            user = credentialedUserPersister.getByHandle(username, false);
        }

        if (user == null) {
            throw new UsernameNotFoundException("Invalid Username or Password. Please try again.");
        }

        authentication = new UsernamePasswordAuthenticationToken(user.getUser().getHandle(), password);

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid Username or Password. Please try again.");
        }

        Authentication customAuthentication = new CustomUserAuthentication(authentication);
        customAuthentication.setAuthenticated(true);
        return customAuthentication;
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
