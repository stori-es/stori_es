package org.consumersunion.stories.server.security;

import org.springframework.security.acls.domain.BasePermission;

public class CustomPermission extends BasePermission {

    private static final long serialVersionUID = 4446261138764718250L;

    public static final CustomPermission OBO = new CustomPermission(1 << 5, 'C'); // 32
    public static final CustomPermission INDEX = new CustomPermission(33, 'C'); // 32

    protected CustomPermission(int mask) {
        super(mask);
    }

    protected CustomPermission(int mask, char code) {
        super(mask, code);
    }
}
