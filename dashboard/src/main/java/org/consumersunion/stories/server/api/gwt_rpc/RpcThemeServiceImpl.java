package org.consumersunion.stories.server.api.gwt_rpc;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcThemeService;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.business_logic.ThemeService;
import org.consumersunion.stories.server.exception.BadRequestException;
import org.consumersunion.stories.server.exception.NotLoggedInException;

import net.lightoze.gwt.i18n.server.LocaleFactory;

public class RpcThemeServiceImpl extends RpcBaseServiceImpl implements RpcThemeService {
    @Inject
    private ThemeService themeService;

    @Override
    public DataResponse<Theme> getAll() {
        DataResponse<Theme> response = new DataResponse<Theme>();

        try {
            List<Theme> themes = themeService.getAll();
            response.setData(themes);
        } catch (GeneralException e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).error());
        } catch (BadRequestException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidRequest());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public DataResponse<Theme> getThemesForOrganization(int organizationId) {
        DataResponse<Theme> response = new DataResponse<Theme>();

        try {
            List<Theme> themes = themeService.getThemesForOrganization(organizationId);
            response.setData(themes);
        } catch (GeneralException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidRequest());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public Theme getTheme(int themeId) {
        return themeService.getTheme(themeId);
    }
}
