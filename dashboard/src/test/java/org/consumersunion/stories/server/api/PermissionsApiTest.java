package org.consumersunion.stories.server.api;

import java.util.List;

import org.consumersunion.stories.common.shared.dto.PermissionResponse;
import org.consumersunion.stories.common.shared.dto.PermissionsApiResponse;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.consumersunion.stories.common.shared.api.EndPoints.PERMISSIONS;

public class PermissionsApiTest extends ApiTestCase {
    public void testGetPermissions_notPermissions_returns404() {
        get(withUser1Login(), 110, NOT_FOUND, PermissionsApiResponse.class, PERMISSIONS);
    }

    public void testGetPermissions_returnsPermissions() {
        PermissionsApiResponse response = get(withUser1Login(), 20002, OK, PermissionsApiResponse.class, PERMISSIONS);
        List<PermissionResponse> permissions = response.getData();

        assertNotNull(permissions);
        assertEquals(1, permissions.size());
        PermissionResponse permissionResponse = permissions.get(0);
        assertNotNull(permissionResponse);
    }
}
