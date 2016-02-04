package org.consumersunion.stories.server.business_logic;

import java.util.List;

import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.server.business_logic.interceptor.RequiresLoggedUser;

public interface ThemeService {
    /**
     * Get all themes. Used when creating a new organization. User mast be a System Admin.
     */
    @RequiresLoggedUser
    List<Theme> getAll();

    /**
     * Retrieves themes associated to an organization. User must have read privs over the organization. PUBLIC access is
     * not sufficient and ownership not supported for orgs.
     */
    List<Theme> getThemesForOrganization(int organizationId);

    Theme getTheme(int themeId);

    void associateOrganisation(Organization organization, List<Integer> themeIds);
}
