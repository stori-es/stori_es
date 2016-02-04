package org.consumersunion.stories.common.shared.service.datatransferobject;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.client.service.datatransferobject.AssignableStory;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Address.GeoCodeStatus;
import org.consumersunion.stories.common.shared.model.HasTitle;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.IsSerializable;

public class StorySummary implements IsSerializable, AssignableStory, HasTitle {
    public static final int SUMMARY_SIZE = 200;

    protected String authorHandle;

    private Story story;
    private String title;
    private String summary;
    private String fullText;
    private String byline;
    private String authorGivenName;
    private String authorSurname;
    private String authorPrimaryEmail;
    private String authorPrimaryPhone;
    private Address address;
    private boolean selected;
    private List<String> notes;
    private Set<String> tags;
    private List<CollectionSummary> collections;
    private DocumentsContainer documents;

    StorySummary() {
        story = null;
        summary = "";
    }

    public StorySummary(
            Story story,
            String title,
            String summary,
            String authorHandle,
            String authorGivenName,
            String authorSurname,
            String authorPrimaryEmail,
            String authorPrimaryPhone,
            List<String> notes) {
        this(story, title, summary, authorHandle, authorGivenName, authorSurname, authorPrimaryEmail,
                authorPrimaryPhone, notes, new DocumentsContainer());
    }

    public StorySummary(
            Story story,
            String title,
            String summary,
            String authorHandle,
            String authorGivenName,
            String authorSurname,
            String authorPrimaryEmail,
            String authorPrimaryPhone,
            List<String> notes,
            DocumentsContainer documents) {
        this.story = story;
        this.title = title;
        this.summary = summary;
        this.authorHandle = authorHandle;
        this.authorGivenName = authorGivenName;
        this.authorSurname = authorSurname;
        this.authorPrimaryEmail = authorPrimaryEmail;
        this.authorPrimaryPhone = authorPrimaryPhone;
        this.documents = documents;
        if (notes != null) {
            this.notes = new LinkedList<String>();
            this.notes.addAll(notes);
        }
    }

    public StorySummary(
            Story story,
            String title,
            String summary,
            String authorHandle,
            String authorGivenName,
            String authorSurname,
            String authorPrimaryEmail,
            String authorPrimaryPhone,
            List<String> notes,
            Address address,
            DocumentsContainer documents) {
        this();
        this.story = story;
        this.title = title;
        this.summary = summary;
        this.authorHandle = authorHandle;
        this.authorGivenName = authorGivenName;
        this.authorSurname = authorSurname;
        this.authorPrimaryEmail = authorPrimaryEmail;
        this.authorPrimaryPhone = authorPrimaryPhone;
        this.documents = documents;
        this.notes = new LinkedList<String>();
        if (notes != null) {
            this.notes.addAll(notes);
        }
        this.address = address;
    }

    public Boolean isGeoCoded() {
        if (address != null && !Strings.isNullOrEmpty(address.getGeoCodeStatus())) {
            GeoCodeStatus status = GeoCodeStatus.valueOf(address.getGeoCodeStatus());
            return status == GeoCodeStatus.SUCCESS;
        }

        return false;
    }

    public Story getStory() {
        return story;
    }

    public String getByline() {
        return byline;
    }

    public int getStoryId() {
        return story.getId();
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getAuthor() {
        if (authorGivenName != null && authorSurname != null) {
            return authorGivenName + " " + authorSurname;
        } else if (authorGivenName != null) {
            return authorGivenName;
        } else if (authorSurname != null) {
            return authorSurname;
        } else {
            return null;
        }
    }

    public String getAuthorHandle() {
        return authorHandle;
    }

    public String getAuthorGivenName() {
        return authorGivenName;
    }

    public void setAuthorGivenName(String authorGivenName) {
        this.authorGivenName = authorGivenName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public void setAuthorSurname(String authorSurname) {
        this.authorSurname = authorSurname;
    }

    public String getAuthorPrimaryEmail() {
        return authorPrimaryEmail;
    }

    public void setAuthorPrimaryEmail(String authorPrimaryEmail) {
        this.authorPrimaryEmail = authorPrimaryEmail;
    }

    public String getAuthorPrimaryPhone() {
        return authorPrimaryPhone;
    }

    public void setAuthorPrimaryPhone(String authorPrimaryPhone) {
        this.authorPrimaryPhone = authorPrimaryPhone;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        if (notes != null) {
            this.notes = new LinkedList<String>();
            this.notes.addAll(notes);
        }
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getSummary() {
        if (Strings.isNullOrEmpty(summary)) {
            return "";
        }
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public List<CollectionSummary> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionSummary> collections) {
        this.collections = collections;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(story, summary, fullText, byline, authorHandle, authorGivenName, authorSurname,
                authorPrimaryEmail, authorPrimaryPhone, address, selected, notes, tags, collections);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        StorySummary other = (StorySummary) obj;
        return Objects.equal(this.story, other.story) && Objects.equal(this.summary, other.summary) && Objects.equal(
                this.fullText, other.fullText) && Objects.equal(this.byline, other.byline) && Objects.equal(
                this.authorHandle, other.authorHandle) && Objects.equal(this.authorGivenName,
                other.authorGivenName) && Objects.equal(this.authorSurname, other.authorSurname) && Objects.equal(
                this.authorPrimaryEmail, other.authorPrimaryEmail) && Objects.equal(this.authorPrimaryPhone,
                other.authorPrimaryPhone) && Objects.equal(this.address, other.address)
                && Objects.equal(this.selected, other.selected) && Objects.equal(
                this.notes, other.notes) && Objects.equal(this.tags, other.tags) && Objects.equal(this.collections,
                other.collections);
    }

    public DocumentsContainer getDocuments() {
        return documents;
    }

    public void setDocuments(DocumentsContainer documents) {
        this.documents = documents;
    }

    public Documents getDocuments(SystemEntityRelation relation) {
        return documents.getDocuments(relation);
    }

    public void addDocuments(List<Document> documentsToAdd) {
        documents.addDocuments(documentsToAdd);
    }

    public void addDocument(Document document) {
        documents.addDocument(document);
    }
}
