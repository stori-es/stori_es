package org.consumersunion.stories.server.velocity.template;

public class TemplatesLocation {
    public static final String MACROS = getBasePath() + "macros.vm";

    private static final String EMAILS = getBasePath() + "email/";

    public static String emails(String templateName) {
        return EMAILS + templateName;
    }

    private static String getBasePath() {
        Class<TemplatesLocation> locationClass = TemplatesLocation.class;
        return locationClass.getName().replace(".", "/").replace(locationClass.getSimpleName(), "");
    }
}
