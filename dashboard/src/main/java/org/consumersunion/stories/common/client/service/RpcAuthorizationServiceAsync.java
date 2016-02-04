package org.consumersunion.stories.common.client.service;

import java.util.List;

import org.consumersunion.stories.common.client.service.datatransferobject.DropDownItem;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.AuthorizationResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.DisplayAuthResponse;
import org.consumersunion.stories.common.shared.model.SystemEntity;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcAuthorizationServiceAsync {
    void grant(int subject, int operation, int target, AsyncCallback<ActionResponse> response);

    void deny(int subject, int operation, int target, AsyncCallback<ActionResponse> response);

    void isAuthorized(int subject, int operation, int target, AsyncCallback<AuthorizationResponse> response);

    void getValidTargets(int subject, int operation, String joinTable,
            AsyncCallback<DatumResponse<List<SystemEntity>>> response);

    void getValidTargets(int subject, int operation, AsyncCallback<DatumResponse<List<SystemEntity>>> response);

    void getValidPrincipals(int operation, int target,
            AsyncCallback<DatumResponse<List<SystemEntity>>> response);

    void itemAuthorized(DropDownItem dropDownItem, AsyncCallback<DisplayAuthResponse> response);

    void isSuperUser(AsyncCallback<DisplayAuthResponse> response);
}
