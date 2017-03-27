package org.consumersunion.stories.server.solr.person;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.SolrServer;

public class NewPersonIndexer implements Indexer {
    private final Profile profile;
    private final String primaryEmail;
    private final String primaryPhone;

    public NewPersonIndexer(Profile profile, String primaryEmail, String primaryPhone) {
        this.profile = profile;
        this.primaryEmail = primaryEmail;
        this.primaryPhone = primaryPhone;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
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
                null, // first story date by collection
                null, // last story date by collection
                null, // story count by collection
                null); // read auths
        solrPersonServer.add(profileDocument.toDocument());
        solrPersonServer.commit();
    }
}
