package org.consumersunion.stories.common.shared.api;

public class EndPoints {
    public static final String ID = "{" + UrlParameters.ID + "}";

    public static final String API = "/api";

    public static final String COLLECTIONS = API + "/collections/";
    public static final String DOCUMENTS = API + "/documents/";
    public static final String NOTES = "/notes/";
    public static final String ORGANIZATIONS = API + "/organizations/";
    public static final String PERMISSIONS = API + "/permissions/";
    public static final String PROFILES = API + "/profiles/";
    public static final String QUESTIONNAIRES = API + "/questionnaires/";
    public static final String STORIES = API + "/stories/";
    public static final String THEMES = API + "/themes/";
    public static final String USERS = API + "/users/";

    public static final String ACCOUNT = "/account";
    public static final String RESET_PASSWORD = "/reset";
    public static final String EMAILS = "/emails";
    public static final String EMAIL = "/email";
    public static final String TASKS = "/tasks";
    public static final String CANCEL = "/cancel";
    public static final String SELF = "/self";

    public static String endsWithId(String path) {
        return path + ID;
    }
}
