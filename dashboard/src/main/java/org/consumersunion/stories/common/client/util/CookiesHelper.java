package org.consumersunion.stories.common.client.util;

import com.google.gwt.user.client.Cookies;

public class CookiesHelper {
    public String getCookie(String name) {
        return Cookies.getCookie(name);
    }

    public void removeCookie(String name) {
        Cookies.removeCookie(name);
    }
}
