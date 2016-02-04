package org.consumersunion.stories.server.helper;

import org.springframework.stereotype.Component;

@Component
public class EmailProperties {
    private static final int SMTP_PORT = 25;
    private static final String CHARSET = "utf-8";
    private static final String EMAIL_TYPE = "plain";
    private static final String SMTP_FROM = "SMTP_FROM";
    private static final String SMTP_FROM_NAME = "SMTP_FROM_NAME";
    private static final String SMTP_ENDPOINT = "SMTP_ENDPOINT";
    private static final String SMTP_USERNAME = "SMTP_USERNAME";
    private static final String SMTP_PASSWORD = "SMTP_PASSWORD";
    private static final String SMTP_OVERRIDE_EMAIL = "SMTP_OVERRIDE_EMAIL";

    private String smtpFrom;
    private String smtpFromName;
    private String smtpEndpoint;
    private String smtpUserName;
    private String smtpPassword;
    private String overrideRecipient;
    private boolean propertiesLoaded;

    public boolean isAvailable() {
        ensurePropertiesLoaded();
        return propertiesAreSet();
    }

    public int getSmtpPort() {
        ensurePropertiesLoaded();
        return SMTP_PORT;
    }

    public String getCharset() {
        ensurePropertiesLoaded();
        return CHARSET;
    }

    public String getEmailType() {
        ensurePropertiesLoaded();
        return EMAIL_TYPE;
    }

    public String getSmtpFrom() {
        ensurePropertiesLoaded();
        return smtpFrom;
    }

    public String getSmtpEndpoint() {
        ensurePropertiesLoaded();
        return smtpEndpoint;
    }

    public String getSmtpFromName() {
        ensurePropertiesLoaded();
        return smtpFromName;
    }

    public String getSmtpUserName() {
        ensurePropertiesLoaded();
        return smtpUserName;
    }

    public String getSmtpPassword() {
        ensurePropertiesLoaded();
        return smtpPassword;
    }

    public String getOverrideRecipient() {
        ensurePropertiesLoaded();
        return overrideRecipient;
    }

    private synchronized void ensurePropertiesLoaded() {
        if (!propertiesLoaded) {
            loadSystemProperties();

            propertiesLoaded = true;
        }
    }

    private void loadSystemProperties() {
        smtpFrom = System.getProperty(SMTP_FROM, smtpFrom);
        smtpFromName = System.getProperty(SMTP_FROM_NAME, smtpFromName);
        smtpEndpoint = System.getProperty(SMTP_ENDPOINT, smtpEndpoint);
        smtpUserName = System.getProperty(SMTP_USERNAME, smtpUserName);
        smtpPassword = System.getProperty(SMTP_PASSWORD, smtpPassword);
        overrideRecipient = System.getProperty(SMTP_OVERRIDE_EMAIL, overrideRecipient);
    }

    private boolean propertiesAreSet() {
        return smtpEndpoint != null && smtpUserName != null && smtpPassword != null;
    }
}
