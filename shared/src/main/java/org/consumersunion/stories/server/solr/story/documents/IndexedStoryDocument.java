package org.consumersunion.stories.server.solr.story.documents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Address.GeoCodeStatus;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Strings;

/**
 * Captures all the 'interesting' bits of data regarding a {@link Story} in a single indexed document.
 */
public class IndexedStoryDocument implements org.consumersunion.stories.server.solr.Document {
    private Integer id;
    private Integer defaultContentId;
    private Integer answerSetId;
    private String answerText;
    private Integer storyVersion;
    private Integer authorId;
    private Integer ownerId;
    private String storyOwner;
    private String permalink;
    private String authorSurname;
    private String authorGivenName;
    private String authorFullName;
    private String authorPrimaryEmail;
    private Collection<String> authorEmails;
    private String authorPrimaryPhone;
    private Collection<String> authorPhones;
    private String authorCity;
    private String authorState;
    private String authorPostalCode;
    private String authorAddress1;
    private String authorLocation;
    private String title;
    private String primaryText;
    private Boolean storyBodyPrivacy;
    private Date created;
    private Date lastModified;
    private Set<String> tags;
    private List<String> collections;
    private Set<Integer> collectionsId;
    private String questionnaireTitle;
    private Integer questionnaireId;
    private Set<Integer> readAuths;
    private Set<String> admins;
    private List<String> storyNotes;
    private List<String> authorNotes;
    private String documentIds;

    public IndexedStoryDocument() {
        authorEmails = new HashSet<String>();
        tags = new LinkedHashSet<String>();
        collections = new ArrayList<String>();
        collectionsId = new LinkedHashSet<Integer>();
        readAuths = new LinkedHashSet<Integer>();
        admins = new LinkedHashSet<String>();
        storyNotes = new ArrayList<String>();
        authorNotes = new ArrayList<String>();
    }

    @SuppressWarnings("unchecked")
    public IndexedStoryDocument(SolrDocument document) {
        id = Integer.parseInt((String) document.getFieldValue("id"));
        defaultContentId = (Integer) document.getFieldValue("defaultContentId");
        answerSetId = (Integer) document.getFieldValue("answerSetId");
        answerText = (String) document.getFieldValue("answerText");
        storyVersion = (Integer) document.getFieldValue("storyVersion");
        authorId = (Integer) document.getFieldValue("authorId");
        ownerId = (Integer) document.getFieldValue("ownerId");
        storyOwner = (String) document.getFieldValue("storyOwner");
        permalink = (String) document.getFieldValue("permalink");
        authorSurname = (String) document.getFieldValue("authorSurname");
        authorGivenName = (String) document.getFieldValue("authorGivenName");
        authorFullName = (String) document.getFieldValue("authorFullName");
        authorPrimaryEmail = (String) document.getFieldValue("authorPrimaryEmail");
        authorEmails = (Collection<String>) (Collection<?>) document.getFieldValues("authorEmails");
        authorPrimaryPhone = (String) document.getFieldValue("authorPrimaryPhone");
        authorEmails = (Collection<String>) (Collection<?>) document.getFieldValues("authorPhones");
        authorCity = (String) document.getFieldValue("authorCity");
        authorState = (String) document.getFieldValue("authorState");
        authorPostalCode = (String) document.getFieldValue("authorPostalCode");
        authorAddress1 = (String) document.getFieldValue("authorAddress1");
        authorLocation = (String) document.getFieldValue("authorLocation");
        title = (String) document.getFieldValue("storyTitle");
        primaryText = (String) document.getFieldValue("primaryText");
        storyBodyPrivacy = (Boolean) document.getFieldValue("storyBodyPrivacy");
        created = (Date) document.getFieldValue("created");
        lastModified = (Date) document.getFieldValue("lastModified");

        Collection<Object> indexedTags = document.getFieldValues("storyTags");
        tags = new LinkedHashSet<String>();
        if (indexedTags != null) {
            for (Object item : indexedTags) {
                tags.add((String) item);
            }
        }

        Collection<Object> indexedCollections = document.getFieldValues("collections");
        collections = new ArrayList<String>();
        if (indexedCollections != null) {
            for (Object item : indexedCollections) {
                collections.add((String) item);
            }
        }

        Collection<Object> indexedCollectionsId = document.getFieldValues("collectionsId");
        collectionsId = new LinkedHashSet<Integer>();
        if (indexedCollectionsId != null) {
            for (Object item : indexedCollectionsId) {
                collectionsId.add((Integer) item);
            }
        }

        questionnaireTitle = (String) document.getFieldValue("questionnaireTitle");
        questionnaireId = (Integer) document.getFieldValue("questionnaireId");

        Collection<Object> indexedReadAuths = document.getFieldValues("readAuths");
        readAuths = new LinkedHashSet<Integer>();
        if (indexedReadAuths != null) {
            for (Object item : indexedReadAuths) {
                readAuths.add((Integer) item);
            }
        }

        Collection<Object> indexedAdmins = document.getFieldValues("admins");
        admins = new LinkedHashSet<String>();
        if (indexedAdmins != null) {
            for (Object item : indexedAdmins) {
                admins.add((String) item);
            }
        }

        Collection<Object> indexedNotes = document.getFieldValues("storyNotes");
        storyNotes = new ArrayList<String>();
        if (indexedNotes != null) {
            for (Object item : indexedNotes) {
                storyNotes.add((String) item);
            }
        }

        Collection<Object> indexedAuthorNotes = document.getFieldValues("authorNotes");
        authorNotes = new ArrayList<String>();
        if (indexedAuthorNotes != null) {
            for (Object item : indexedAuthorNotes) {
                authorNotes.add((String) item);
            }
        }

        documentIds = (String) document.getFieldValue("documentIds");
    }

    public IndexedStoryDocument(Story story,
            String answerText,
            ProfileSummary profileSummary,
            Address address,
            Document document,
            Integer answerSetId,
            List<org.consumersunion.stories.common.shared.model.Collection> collectionObjs,
            QuestionnaireI15d questionnaire,
            boolean storyBodyPrivacy,
            Set<Integer> readAuths,
            Set<String> admins,
            Set<String> tags,
            Map<String, Set<Integer>> documentIds) {
        this.id = story.getId();

        if (document != null) {
            this.defaultContentId = document.getId();
            setTitle(document.getTitle());
            this.primaryText = document.getFirstContent();
        }

        if (answerSetId != null) {
            this.answerSetId = answerSetId;
        }

        this.answerText = answerText;
        this.storyVersion = story.getVersion();
        this.authorId = story.getOwner();
        this.ownerId = story.getOwner();
        this.permalink = story.getPermalink();

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

        if (address != null) {
            setAddress(address);
        }

        this.created = story.getCreated();
        this.lastModified = story.getUpdated();
        this.storyBodyPrivacy = storyBodyPrivacy;
        this.readAuths = readAuths;
        this.admins = admins;
        this.tags = tags == null ? new LinkedHashSet<String>() : tags;
        this.collections = new ArrayList<String>();
        this.collectionsId = new LinkedHashSet<Integer>();

        try {
            if (collectionObjs != null) {
                for (org.consumersunion.stories.common.shared.model.Collection collection : collectionObjs) {
                    if (!collectionsId.contains(collection.getId())) {
                        JSONObject collectionJson = new JSONObject();
                        collectionJson.put("id", collection.getId());
                        collectionJson.put("title", collection.getTitle());
                        collections.add(collectionJson.toString());
                        collectionsId.add(collection.getId());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (documentIds != null && !documentIds.isEmpty()) {
            setDocumentIds(documentIds);
        }

        if (questionnaire != null) {
            this.questionnaireTitle = questionnaire.getTitle();
            this.questionnaireId = questionnaire.getId();
        }

        if (readAuths == null) {
            this.readAuths = new LinkedHashSet<Integer>();
        }
        if (admins == null) {
            this.admins = new LinkedHashSet<String>();
        }
    }

    public void setAddress(Address address) {
        setAuthorCity(address.getCity());
        setAuthorState(address.getState());
        setAuthorPostalCode(address.getPostalCode());
        this.authorAddress1 = address.getAddress1();
        this.authorLocation = extractPosition(address);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDefaultContentId(Integer defaultContentId) {
        this.defaultContentId = defaultContentId;
    }

    public void setAnswerSetId(Integer answerSetId) {
        this.answerSetId = answerSetId;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setStoryVersion(Integer storyVersion) {
        this.storyVersion = storyVersion;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setOwner(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public void setStoryOwner(String storyOwner) {
        this.storyOwner = storyOwner;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
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

    public void setAuthorPrimaryEmail(String authorPrimaryEmail) {
        this.authorPrimaryEmail = authorPrimaryEmail;
    }

    public void setAuthorEmails(Collection<String> authorEmails) {
        this.authorEmails = authorEmails;
    }

    public void setAuthorPhones(Collection<String> authorPhones) {
        this.authorPhones = authorPhones;
    }

    public void setAuthorPrimaryPhone(String authorPrimaryPhone) {
        this.authorPrimaryPhone = authorPrimaryPhone;
    }

    public void setAuthorCity(String authorCity) {
        if (authorCity != null && authorCity.trim().length() == 0) {
            this.authorCity = null;
        } else {
            this.authorCity = authorCity;
        }
    }

    public void setAuthorState(String authorState) {
        if (authorState != null && authorState.trim().length() == 0) {
            this.authorState = null;
        } else {
            this.authorState = authorState;
        }
    }

    public void setAuthorPostalCode(String authorPostalCode) {
        this.authorPostalCode = authorPostalCode;
    }

    public void setAuthorAddress1(String authorAddress1) {
        this.authorAddress1 = authorAddress1;
    }

    public String getAuthorLocation() {
        return authorLocation;
    }

    public void setAuthorLocation(String authorLocation) {
        this.authorLocation = authorLocation;
    }

    public void setTitle(String title) {
        if (title != null && title.trim().length() == 0) {
            this.title = null;
        } else {
            this.title = title;
        }
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public void setStoryBodyPrivacy(Boolean storyBodyPrivacy) {
        this.storyBodyPrivacy = storyBodyPrivacy;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setCollections(List<String> collections) {
        this.collections = collections;
    }

    public void setCollectionsId(Set<Integer> collectionsId) {
        this.collectionsId = collectionsId;
    }

    public void setReadAuths(Set<Integer> readAuths) {
        this.readAuths = readAuths;
    }

    public Set<String> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<String> admins) {
        this.admins = admins;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDefaultContentId() {
        return defaultContentId;
    }

    public Integer getAnswerSetId() {
        return answerSetId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public Integer getStoryVersion() {
        return storyVersion;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public String getStoryOwner() {
        return storyOwner;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public String getAuthorGivenName() {
        return authorGivenName;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public String getAuthorCity() {
        return authorCity;
    }

    public String getAuthorState() {
        return authorState;
    }

    public String getAuthorPostalCode() {
        return authorPostalCode;
    }

    public String getAuthorAddress1() {
        return authorAddress1;
    }

    public String getTitle() {
        return title;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public Boolean getStoryBodyPrivacy() {
        return storyBodyPrivacy;
    }

    public Date getCreated() {
        return created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public Set<String> getTags() {
        return tags;
    }

    public List<String> getCollections() {
        return collections;
    }

    public Set<Integer> getCollectionsId() {
        return collectionsId;
    }

    public String getQuestionnaireTitle() {
        return questionnaireTitle;
    }

    public void setQuestionnaireTitle(String questionnaireTitle) {
        this.questionnaireTitle = questionnaireTitle;
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public Set<Integer> getReadAuths() {
        return readAuths;
    }

    public List<String> getStoryNotes() {
        return storyNotes;
    }

    public void setStoryNotes(List<String> storyNotes) {
        this.storyNotes = storyNotes;
    }

    public List<String> getAuthorNotes() {
        return authorNotes;
    }

    public void setAuthorNotes(List<String> authorNotes) {
        this.authorNotes = authorNotes;
    }

    public void setDocumentIds(Map<String, ? extends java.util.Collection<Integer>> documentIds) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.documentIds = objectMapper.writeValueAsString(documentIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SolrInputDocument toDocument() {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("type", IndexedStoryDocument.class.getSimpleName());
        document.addField("id", String.valueOf(id));
        document.addField("storyVersion", storyVersion);
        document.addField("defaultContentId", defaultContentId);
        document.addField("answerSetId", answerSetId);
        document.addField("answerText", answerText);
        document.addField("authorId", authorId);
        document.addField("permalink", permalink);
        document.addField("authorSurname", authorSurname);
        document.addField("authorGivenName", authorGivenName);
        document.addField("authorFullName", authorFullName);
        document.addField("authorPrimaryEmail", authorPrimaryEmail);
        document.addField("authorEmails", authorEmails);
        document.addField("authorPrimaryPhone", authorPrimaryPhone);
        document.addField("authorPhones", authorPhones);
        document.addField("authorCity", authorCity);
        document.addField("authorState", authorState);
        document.addField("authorPostalCode", authorPostalCode);
        document.addField("authorAddress1", authorAddress1);
        document.addField("storyTitle", title);
        document.addField("primaryText", primaryText);
        document.addField("storyBodyPrivacy", storyBodyPrivacy);
        document.addField("created", created);
        document.addField("lastModified", lastModified);
        document.addField("ownerId", ownerId);
        document.addField("storyOwner", storyOwner);

        if (!Strings.isNullOrEmpty(authorLocation)) {
            document.addField("authorLocation", authorLocation);
        }

        if (tags != null) {
            for (String tag : tags) {
                document.addField("storyTags", tag);
            }
        }
        if (collections != null) {
            for (String collection : collections) {
                document.addField("collections", collection);
            }
        }
        if (collectionsId != null) {
            for (Integer collection : collectionsId) {
                document.addField("collectionsId", collection);
            }
        }
        document.addField("questionnaireTitle", questionnaireTitle);
        document.addField("questionnaireId", questionnaireId);
        if (readAuths != null) {
            for (Integer readAuth : readAuths) {
                document.addField("readAuths", readAuth);
            }
        }
        if (admins != null) {
            for (String admin : admins) {
                document.addField("admins", admin);
            }
        }
        if (storyNotes != null) {
            for (String note : storyNotes) {
                document.addField("storyNotes", note);
            }
        }
        if (authorNotes != null) {
            for (String note : authorNotes) {
                document.addField("authorNotes", note);
            }
        }

        document.setField("documentIds", documentIds);

        return document;
    }

    public String extractPosition(Address address) {
        if (!Strings.isNullOrEmpty(address.getGeoCodeStatus())) {
            GeoCodeStatus status = GeoCodeStatus.valueOf(address.getGeoCodeStatus());
            if (status == GeoCodeStatus.SUCCESS) {
                return address.getLatitude() + "," + address.getLongitude();
            }
        }

        return "";
    }
}
