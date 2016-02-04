package org.consumersunion.stories.common.client.service.response;

public class AuthorizationResponse extends Response {

    private boolean granted;

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(final boolean granted) {
        this.granted = granted;
    }
}
