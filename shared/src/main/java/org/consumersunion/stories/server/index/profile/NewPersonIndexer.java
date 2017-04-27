package org.consumersunion.stories.server.index.profile;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.index.Indexer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class NewPersonIndexer {
    private final Provider<Indexer<ProfileDocument>> profileIndexerProvider;

    @Inject
    public NewPersonIndexer(
            @Qualifier("profileIndexer") Provider<Indexer<ProfileDocument>> profileIndexerProvider) {
        this.profileIndexerProvider = profileIndexerProvider;
    }

    public void index(Profile profile, String primaryEmail, String primaryPhone) {
        List<String> emails = new ArrayList<String>();
        emails.add(primaryEmail);
        List<String> phones = new ArrayList<String>();
        phones.add(primaryPhone);
        ProfileDocument profileDocument = new ProfileDocument(profile.getId(),
                profile.getGivenName(),
                profile.getSurname(),
                profile.getGivenName() + " " + profile.getSurname(),
                null, // handle
                primaryEmail,
                emails,
                null, // email format
                null, // primary address
                primaryPhone,
                phones,
                // the Collections and Questionnaires get updated as Stories are indexed
                null, // collections
                null, // questionnaires
                null, // update opt in
                null, // first story date
                null, // last story date
                0,
//                null,
//                null,
//                null,
                null); // read auths

        profileIndexerProvider.get().index(profileDocument);
    }
}
