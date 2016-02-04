package org.consumersunion.stories.common.shared;

public class AuthConstants {
    public static final int ROOT_ID = 0;

    public static final int ROLE_READER = 100;
    public static final int ROLE_CURATOR = 200;
    public static final int ROLE_ADMIN = 300;
    public static final int ROLE_OWNER = 400;

    // This is a special authorization outside of the 'roles' system.
    public static final int OPERATION_INDEX = 33;

    public static final int ACCESS_MODE_OWN = 1;
    public static final int ACCESS_MODE_PUBLIC = 2;
    public static final int ACCESS_MODE_PRIVILEGED = 3;
    public static final int ACCESS_MODE_ANY = 4;
    public static final int ACCESS_MODE_EXPLICIT = 5;
    public static final int ACCESS_MODE_PRIVATE = 6;
    public static final int ACCESS_MODE_ROOT = 7;
}
