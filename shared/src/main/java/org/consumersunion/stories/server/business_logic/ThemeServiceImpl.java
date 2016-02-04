package org.consumersunion.stories.server.business_logic;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.exception.BadRequestException;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.persistence.OrganizationPersister;
import org.consumersunion.stories.server.persistence.ThemePersister;
import org.springframework.stereotype.Service;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Service
public class ThemeServiceImpl implements ThemeService {
    private final AuthorizationService authService;
    private final UserService userService;
    private final ThemePersister themePersister;
    private final OrganizationPersister organizationPersister;

    @Inject
    ThemeServiceImpl(
            AuthorizationService authService,
            UserService userService,
            ThemePersister themePersister,
            OrganizationPersister organizationPersister) {
        this.authService = authService;
        this.userService = userService;
        this.themePersister = themePersister;
        this.organizationPersister = organizationPersister;
    }

    @Override
    public List<Theme> getAll() {
        User user = userService.getLoggedInUser();
        if (authService.isSuperUser(user)) {
            return themePersister.retrieveAll();
        } else {
            Integer organizationId = userService.getContextOrganizationId();
            if (organizationId != null) {
                return themePersister.retrieveForOrganization(organizationId);
            }
        }

        throw new BadRequestException();
    }

    @Override
    public List<Theme> getThemesForOrganization(int organizationId) {
        Organization organization = organizationPersister.get(organizationId);

        if (authService.isUserAuthorized(ROLE_READER, organization)) {
            return themePersister.retrieveForOrganization(organizationId);
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Override
    public Theme getTheme(int themeId) {
        return themePersister.get(themeId);
    }

    @Override
    public void associateOrganisation(Organization organization, List<Integer> themeIds) {
        themePersister.associateOrganization(organization, themeIds);
    }
}
