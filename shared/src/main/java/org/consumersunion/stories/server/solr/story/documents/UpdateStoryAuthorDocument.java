package org.consumersunion.stories.server.solr.story.documents;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Profile;

import com.google.common.collect.Maps;
import com.google.gwt.thirdparty.guava.common.collect.Lists;

public class UpdateStoryAuthorDocument {
    private Integer id;
    private String storyOwner;
    private String authorSurname;
    private String authorGivenName;
    private String authorFullName;
    private String authorPrimaryEmail;
    private Collection<String> authorEmails;
    private String authorPrimaryPhone;
    private Collection<String> authorPhones;

    public UpdateStoryAuthorDocument(
            int storyId,
            ProfileSummary profileSummary) {
        this.id = storyId;

        if (profileSummary != null) {
            Profile profile = profileSummary.getProfile();
            setAuthorSurname(profile.getSurname());
            setAuthorGivenName(profile.getGivenName());

            this.authorPrimaryEmail = profileSummary.getPrimaryEmail();
            this.authorEmails = profileSummary.getEmails();
            this.authorPrimaryPhone = profileSummary.getPrimaryPhone();
            this.authorPhones = profileSummary.getPhones();
            this.storyOwner = profile.getGivenName() + " " + profile.getSurname();
        }
    }

    public void setAuthorSurname(String authorSurname) {
        if (authorSurname != null && authorSurname.trim().length() == 0) {
            this.authorSurname = null;
        } else {
            this.authorSurname = authorSurname;
        }
        setAuthorFullName();
    }

    public void setAuthorGivenName(String authorGivenName) {
        if (authorGivenName != null && authorGivenName.trim().length() == 0) {
            this.authorGivenName = null;
        } else {
            this.authorGivenName = authorGivenName;
        }
        setAuthorFullName();
    }

    public void setAuthorFullName() {
        if (this.authorSurname != null && this.authorGivenName != null) {
            this.authorFullName = authorGivenName + " " + authorSurname;
        } else if (this.authorSurname != null) {
            this.authorFullName = authorSurname;
        } else if (this.authorGivenName != null) {
            this.authorFullName = authorGivenName;
        } else {
            this.authorFullName = null;
        }
    }

    public List<SolrInputDocument> toDocuments() {
        List<SolrInputDocument> documents = Lists.newArrayList();

        documents.add(createDocument("authorSurname", createSetterMap(authorSurname)));
        documents.add(createDocument("authorGivenName", createSetterMap(authorGivenName)));
        documents.add(createDocument("authorFullName", createSetterMap(authorFullName)));
        documents.add(createDocument("authorPrimaryEmail", createSetterMap(authorPrimaryEmail)));
        documents.add(createDocument("authorEmails", createSetterMap(authorEmails)));
        documents.add(createDocument("authorPrimaryPhone", createSetterMap(authorPrimaryPhone)));
        documents.add(createDocument("authorPhones", createSetterMap(authorPhones)));
        documents.add(createDocument("storyOwner", createSetterMap(storyOwner)));

        return documents;
    }

    private SolrInputDocument createDocument(String field, Map<String, Object> fieldModifier) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", String.valueOf(id));
        document.addField(field, fieldModifier);

        return document;
    }

    private Map<String, Object> createSetterMap(Object value) {
        HashMap<String, Object> fieldModifier = Maps.newHashMap();

        fieldModifier.put("set", value);

        return fieldModifier;
    }
}
