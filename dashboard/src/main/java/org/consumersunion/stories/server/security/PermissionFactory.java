package org.consumersunion.stories.server.security;

import org.springframework.security.acls.domain.DefaultPermissionFactory;

public class PermissionFactory extends DefaultPermissionFactory {
    public PermissionFactory() {
        super();

        registerPublicPermissions(CustomPermission.class);
    }
}
