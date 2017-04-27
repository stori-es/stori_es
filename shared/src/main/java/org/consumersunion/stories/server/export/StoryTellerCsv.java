package org.consumersunion.stories.server.export;

import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.index.profile.ProfileDocument;

import com.google.common.base.Strings;

public class StoryTellerCsv extends BaseStoryExportCsv {
    private final String username;
    private final String givenName;
    private final String surname;
    private final String email;
    private final String phone;
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;
    private final Boolean subscription;
    private final String emailFormat;

    public StoryTellerCsv(ProfileDocument doc, Profile profile) {
        super(doc.getId(), profile.getCreated(), profile.getUpdated());

        username = Strings.nullToEmpty(doc.getHandle());
        givenName = Strings.nullToEmpty(doc.getGivenName());
        surname = Strings.nullToEmpty(doc.getSurname());
        email = Strings.nullToEmpty(doc.getPrimaryEmail());
        phone = Strings.nullToEmpty(doc.getPrimaryPhone());
        street = Strings.nullToEmpty(doc.getPrimaryAddress1());
        city = Strings.nullToEmpty(doc.getPrimaryCity());
        state = Strings.nullToEmpty(doc.getPrimaryState());
        zipCode = Strings.nullToEmpty(doc.getPrimaryPostalCode());
        subscription = doc.getUpdateOptIn();
        emailFormat = Strings.nullToEmpty(doc.getEmailFormat());
    }

    public String getUsername() {
        return username;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Boolean isSubscription() {
        return subscription;
    }

    public String getEmailFormat() {
        return emailFormat;
    }
}
