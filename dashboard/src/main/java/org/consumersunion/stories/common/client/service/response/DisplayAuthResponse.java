package org.consumersunion.stories.common.client.service.response;

public class DisplayAuthResponse extends Response {

    private boolean isAuthorized;

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }
}
