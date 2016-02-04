package org.consumersunion.stories.common.client.service;

import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.server.business_logic.ThemeService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/theme")
public interface RpcThemeService extends RemoteService {
    /**
     * @see ThemeService#getAll()
     */
    DataResponse<Theme> getAll();

    /**
     * @see ThemeService#getThemesForOrganization(int)
     */
    DataResponse<Theme> getThemesForOrganization(int organizationId);

    /**
     * @see ThemeService#getTheme(int)
     */
    Theme getTheme(int theme);
}
