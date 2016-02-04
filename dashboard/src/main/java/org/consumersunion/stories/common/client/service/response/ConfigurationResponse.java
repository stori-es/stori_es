package org.consumersunion.stories.common.client.service.response;

public class ConfigurationResponse extends Response {
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(final String property) {
        this.property = property;
    }
}
