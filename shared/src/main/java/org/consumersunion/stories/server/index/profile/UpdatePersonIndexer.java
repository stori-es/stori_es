package org.consumersunion.stories.server.index.profile;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.index.Indexer;
import org.springframework.stereotype.Component;

@Component
public class UpdatePersonIndexer {
    private final Indexer<ProfileDocument> profileIndexer;

    @Inject
    UpdatePersonIndexer(Indexer<ProfileDocument> profileIndexer) {
        this.profileIndexer = profileIndexer;
    }

    public void index(Profile profile) {
        index(profile, null, null, true);
    }

    public void index(Profile profile, String primaryEmail, String primaryPhone) {
        index(profile, primaryEmail, primaryPhone, false);
    }

    private void index(Profile profile, String primaryEmail, String primaryPhone, boolean updateContacts) {
        ProfileDocument profileDocument = profileIndexer.get(profile.getId());
        if (profileDocument != null) {
            profileDocument.setGivenName(profile.getGivenName());
            profileDocument.setFullName(profile.getGivenName() + " " + profile.getSurname());
            profileDocument.setSurname(profile.getSurname());
            if (updateContacts) {
                profileDocument.setPrimaryEmail(primaryEmail);
                profileDocument.setPrimaryPhone(primaryPhone);

                List<String> emails = profileDocument.getEmails();
                if (emails != null && !emails.contains(primaryEmail)) {
                    emails.add(primaryEmail);
                    profileDocument.setEmails(emails);
                }

                List<String> phones = profileDocument.getPhones();
                if (phones != null && !phones.contains(primaryPhone)) {
                    phones.add(primaryPhone);
                    profileDocument.setPhones(phones);
                }
            }

            profileIndexer.index(profileDocument);
        }
    }
}
