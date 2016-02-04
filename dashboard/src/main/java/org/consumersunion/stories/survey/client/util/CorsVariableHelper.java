package org.consumersunion.stories.survey.client.util;

public class CorsVariableHelper {
    public void registerCorsDomain(String domain) {
        registerCorsVariable(CorsVariables.DOMAIN, domain);
    }

    public void registerCorsPort(String port) {
        registerCorsVariable(CorsVariables.PORT, port);
    }

    private native void registerCorsVariable(String variable, String value) /*-{
        $wnd[variable] = value;
    }-*/;
}
