package org.consumersunion.stories.server.api.gwt_rpc;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcAuthorizationService;
import org.consumersunion.stories.common.client.service.datatransferobject.DropDownItem;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.AuthorizationResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.DisplayAuthResponse;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.service.datatransferobject.DropDownType;
import org.consumersunion.stories.dashboard.shared.MenuItem;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.business_logic.SystemEntityService;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.springframework.stereotype.Service;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROOT_ID;

@SuppressWarnings("serial")
@Service("authorizationService")
public class RpcAuthorizationServiceImpl extends RpcBaseServiceImpl implements RpcAuthorizationService {
    @Inject
    private SystemEntityService systemEntityService;

    /**
     * Allow authorized user to grant permissions. The granting of authorization
     * requires the authenticated user has the 'Administration' operation over
     * the target entity.
     *
     * @param subject   User to grant the permission
     * @param operation permission to be granted
     * @param target    Object to grant the permission
     */
    @Override
    public ActionResponse grant(int subject, int operation, int target) {
        ActionResponse response = new ActionResponse();
        try {
            SystemEntity targetObj = systemEntityService.getSystemEntity(target);
            if (authService.isUserAuthorized(ROLE_ADMIN, targetObj)) {
                authService.grant(subject, operation, target);
            } else {
                response.addGlobalErrorMessage("Invalid request.");
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    /**
     * Allows authorized users to explicitly deny authorization. The granting of
     * authorization requires the authenticated user has the 'Administration'
     * operation over the target entity. Generally, a denial is realized by
     * removing a grant, though in some cases it may result in an explicit
     * denial, for example, to override a a group grant.
     *
     * @param subject   User to grant the permission
     * @param operation permission to be deny
     * @param target    Object to deny the permission
     */
    @Override
    public ActionResponse deny(int subject, int operation, int target) {
        ActionResponse response = new ActionResponse();
        try {
            SystemEntity targetObj = systemEntityService.getSystemEntity(target);
            if (authService.isUserAuthorized(ROLE_ADMIN, targetObj)) {
                authService.deny(subject, target);
            } else {
                response.addGlobalErrorMessage("Invalid request.");
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    /**
     * Indicates whether the indicated principal is granted (or denied) the
     * operation over the target. An authenticated user may call this method
     * with themselves as principal. This allows clients to determine whether to
     * render certain options. To ask about other principals other than the
     * authenticated user, the authenticated user must have 'Administration'
     * privileges over the system, or or 'Administration' privileges over the
     * target or principal.
     *
     * @param subject   User to check if is authorize
     * @param operation Permission to be checked
     * @param target    Object to be checked
     * @return AuthorizationResponse
     */
    public AuthorizationResponse isAuthorized(int subject, int operation, int target) {
        AuthorizationResponse response = new AuthorizationResponse();
        try {
            try {
                SystemEntity subjectObj = systemEntityService.getSystemEntity(subject);
                SystemEntity targetObj = systemEntityService.getSystemEntity(target);
                if (authService.isUserAuthorized(ROLE_ADMIN, subjectObj)) {
                    response.setGranted(authService.isEntityAuthorized(subject, operation, targetObj));
                } else {
                    // TODO: i18n
                    response.addGlobalErrorMessage("Current user is not authorized to make this query.");
                }
            } catch (NotAuthorizedException ignored) {
                response.setGranted(false);
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }

    /**
     * Given a principal and operation, returns a list of all the valid targets.
     * Authenticated user may call this method with themselves as principal. To
     * call this method with an entity other than the authenticated user, the
     * user must have Administration privileges over the system or the
     * principal.
     *
     * @param subject   User to be checked
     * @param operation Permission to be checked
     * @return List<SystemEntity>
     */
    @Override
    public DatumResponse<List<SystemEntity>> getValidTargets(int subject, int operation) {
        return getValidTargets(subject, operation, null);
    }

    @Override
    public DatumResponse<List<SystemEntity>> getValidTargets(int subject, int operation, String joinTable) {
        DatumResponse<List<SystemEntity>> response = new DatumResponse<List<SystemEntity>>();
        try {
            SystemEntity subjectObj = systemEntityService.getSystemEntity(subject);
            if (authService.isUserAuthorized(ROLE_ADMIN, subjectObj)) {
                response.setDatum(authService.getValidTargets(subject, operation, joinTable));
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidRequest());
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }

    /**
     * Given an operation and target, returns a list of all the valid principals.
     * The authenticated user must have Administration privileges over the
     * system or target in order to make this call.
     *
     * @param operation Permission to be checked
     * @param target    Object to be checked
     * @return List<SystemEntity> list of Valid principals
     */
    @Override
    public DatumResponse<List<SystemEntity>> getValidPrincipals(int operation, int target) {
        DatumResponse<List<SystemEntity>> response = new DatumResponse<List<SystemEntity>>();
        try {
            SystemEntity targetObj = systemEntityService.getSystemEntity(target);

            if (authService.isUserAuthorized(ROLE_ADMIN, targetObj)) {
                response.setDatum(authService.getValidPrincipals(operation, target, null));
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException e) {
            response.setLoggedIn(false);
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
        }
        return response;
    }

    @Override
    public DisplayAuthResponse itemAuthorized(DropDownItem dropDownItem) {
        if (dropDownItem.getDropDownType().equals(DropDownType.MENU_ITEM)) {
            return processMenuAuthorizationDisplay((MenuItem) dropDownItem);
        }
        return defaultAuthorizationDisplay();
    }

    private DisplayAuthResponse processMenuAuthorizationDisplay(MenuItem item) {
        DisplayAuthResponse response = new DisplayAuthResponse();
        response.setAuthorized(true);

        boolean hasOrgAdminAuth = false;
        try {
            SystemEntity contextOrg = systemEntityService.getSystemEntity(userService.getContextOrganizationId());
            hasOrgAdminAuth = authService.isUserAuthorized(ROLE_ADMIN, contextOrg);
        } catch (Exception ignored) {
        }

        List<SystemEntity> targets;
        try {
            switch (item.getItemType()) {
                case CREATE_ACCOUNT:
                    if (!hasOrgAdminAuth) {
                        response.setAuthorized(false);
                    }
                    break;
                case UPDATE_ACCOUNT:
                    if (userService.getLoggedInUser(true).getId() != ROOT_ID) {
                        response.setAuthorized(false);
                    }
                    break;
                case CREATE_ORGANIZATION:
                    // Only available  for user root
                    if (userService.getLoggedInUser(true).getId() != ROOT_ID) {
                        response.setAuthorized(false);
                    }
                    break;
                case UPDATE_ORGANIZATION:
                    targets = getValidTargets(userService.getLoggedInUser(true).getId(), ROLE_CURATOR,
                            "organization").getDatum();
                    if ((targets == null || targets.size() == 0) && !hasOrgAdminAuth) {
                        response.setAuthorized(false);
                    }
                    break;
            }
        } catch (NotLoggedInException e) {
            response.setLoggedIn(false);
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
        }

        return response;
    }

    private DisplayAuthResponse defaultAuthorizationDisplay() {
        DisplayAuthResponse response = new DisplayAuthResponse();
        response.setAuthorized(true);
        return response;
    }

    public DisplayAuthResponse isSuperUser() {
        DisplayAuthResponse response = new DisplayAuthResponse();
        try {
            User loggedInUser = userService.getLoggedInUser(true);
            response.setAuthorized(loggedInUser.getId() == ROOT_ID);
        } catch (NotLoggedInException ignored) {
            response.setLoggedIn(false);
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
        }

        return response;
    }
}
