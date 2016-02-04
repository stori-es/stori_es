package org.consumersunion.stories.server.amazon.model;

public class ComplainedRecipient implements HasEmailAddress {
    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
