package org.consumersunion.stories.common.client.service;

import java.util.List;

import org.consumersunion.stories.common.client.service.datatransferobject.DropDownItem;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.AuthorizationResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.DisplayAuthResponse;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.server.business_logic.AuthorizationService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/authorization")
public interface RpcAuthorizationService extends RemoteService {
    /**
     * @see AuthorizationService#grant(int, int, int)
     */
    ActionResponse grant(int subject, int operation, int target);

    /**
     * @see AuthorizationService#deny(Integer, int, Integer)
     */
    ActionResponse deny(int subject, int operation, int target);

    /**
     * @see AuthorizationService#isEntityAuthorized(int, int, SystemEntity)
     */
    AuthorizationResponse isAuthorized(int subject, int operation, int target);

    /**
     * @see AuthorizationService#getValidTargets(int, int, String)
     */
    DatumResponse<List<SystemEntity>> getValidTargets(int subject, int operation, String joinTable);

    /**
     * @see AuthorizationService#getValidTargets(int, int, String)
     */
    DatumResponse<List<SystemEntity>> getValidTargets(int subject, int operation);

    /**
     * @see AuthorizationService#getValidPrincipals(int, int, String)
     */
    DatumResponse<List<SystemEntity>> getValidPrincipals(int operation, int target);

    DisplayAuthResponse itemAuthorized(DropDownItem dropDownItem);

    /**
     * @see AuthorizationService#isSuperUser()
     */
    DisplayAuthResponse isSuperUser();
}
