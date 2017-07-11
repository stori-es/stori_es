package org.consumersunion.stories.server.index.story;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@JsonAutoDetect(
        fieldVisibility = ANY,
        getterVisibility = NONE,
        setterVisibility = NONE)
public class StoryDocument implements org.consumersunion.stories.server.index.Document {
    @JsonProperty(access = WRITE_ONLY)
    private final String _type = "stories";

    @JsonIgnore
    private int id;
    @JsonProperty(access = WRITE_ONLY)
    private String _id;
    @JsonProperty(access = WRITE_ONLY)
    private String _index;

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
    private Date modified;
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

    public StoryDocument() {
        authorEmails = new HashSet<String>();
        tags = new LinkedHashSet<String>();
        collections = new ArrayList<String>();
        collectionsId = new LinkedHashSet<Integer>();
        readAuths = new LinkedHashSet<Integer>();
        admins = new LinkedHashSet<String>();
        storyNotes = new ArrayList<String>();
        authorNotes = new ArrayList<String>();
    }

    public StoryDocument(Story story,
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
        this.modified = story.getUpdated();
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
        if (address != null) {
            setAuthorCity(address.getCity());
            setAuthorState(address.getState());
            setAuthorPostalCode(address.getPostalCode());
            this.authorAddress1 = address.getAddress1();
            this.authorLocation = extractPosition(address);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = Integer.valueOf(id);
        this._id = id;
    }

    public void setId(int id) {
        this.id = id;
        this._id = String.valueOf(id);
    }

    @Override
    @JsonIgnore
    public String getType() {
        return _type;
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

    public void setModified(Date modified) {
        this.modified = modified;
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

    public Date getModified() {
        return modified;
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

    @Override
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

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public String getAuthorPrimaryEmail() {
        return authorPrimaryEmail;
    }

    public Collection<String> getAuthorEmails() {
        return authorEmails;
    }

    public String getAuthorPrimaryPhone() {
        return authorPrimaryPhone;
    }

    public Collection<String> getAuthorPhones() {
        return authorPhones;
    }

    public String getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(String documentIds) {
        this.documentIds = documentIds;
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
