package org.consumersunion.stories.common.client.util;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class URLUtils {
    private static final String GWT_DEV_MOD = "?gwt.codesvr=127.0.0.1:9997";

    public static String generateURL(String hostPage, String placeToken, Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(GWT.getHostPageBaseURL());
        builder.append(hostPage);

        if (!GWT.isScript()) {
            builder.append(GWT_DEV_MOD);
        }

        builder.append("#" + placeToken);

        for (String key : params.keySet()) {
            builder.append(";" + key + "=" + params.get(key));
        }

        return builder.toString();
    }

    public static String appendDefaultProtocol(String urlPart) {
        if (!urlPart.matches("[a-zA-Z]+://.+")) { // If no protocol indicated on the permalink, default to http://
            return Window.Location.getProtocol() + "//" + urlPart;
        } else {
            return urlPart;
        }
    }
}
