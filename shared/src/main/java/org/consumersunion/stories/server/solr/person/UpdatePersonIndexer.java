package org.consumersunion.stories.server.solr.person;

import java.util.Collection;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.solr.Indexer;

public class UpdatePersonIndexer implements Indexer {
    private final boolean updateContacts;
    private final Profile profile;
    private final String primaryEmail;
    private final String primaryPhone;

    public UpdatePersonIndexer(Profile profile, String primaryEmail, String primaryPhone) {
        this.profile = profile;
        this.primaryEmail = primaryEmail;
        this.primaryPhone = primaryPhone;
        this.updateContacts = false;
    }

    public UpdatePersonIndexer(Profile profile) {
        this.profile = profile;
        this.primaryEmail = null;
        this.primaryPhone = null;
        this.updateContacts = true;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("id:" + profile.getId());

        QueryResponse result = solrPersonServer.query(query);
        if (result.getResults().size() > 0) {
            ProfileDocument profileDocument = new ProfileDocument(result.getResults().get(0));
            profileDocument.setGivenName(profile.getGivenName());
            profileDocument.setFullName(profile.getGivenName() + " " + profile.getSurname());
            profileDocument.setSurname(profile.getSurname());
            if (updateContacts) {
                profileDocument.setPrimaryEmail(primaryEmail);
                profileDocument.setPrimaryPhone(primaryPhone);

                Collection<String> emails = profileDocument.getEmails();
                if (emails != null && !emails.contains(primaryEmail)) {
                    emails.add(primaryEmail);
                    profileDocument.setEmails(emails);
                }

                Collection<String> phones = profileDocument.getPhones();
                if (phones != null && !phones.contains(primaryPhone)) {
                    phones.add(primaryPhone);
                    profileDocument.setPhones(phones);
                }
            }

            solrPersonServer.add(profileDocument.toDocument());
            solrPersonServer.commit();
        }
    }
}
