package org.consumersunion.stories.common.client.service;

import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.shared.model.Theme;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcThemeServiceAsync {

    void getAll(final AsyncCallback<DataResponse<Theme>> callback);

    void getThemesForOrganization(int organizationId, final AsyncCallback<DataResponse<Theme>> callback);

    void getTheme(int theme, AsyncCallback<Theme> async);
}
