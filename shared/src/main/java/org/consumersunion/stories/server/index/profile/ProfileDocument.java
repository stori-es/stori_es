package org.consumersunion.stories.server.index.profile;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.index.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@JsonAutoDetect(
        fieldVisibility = ANY,
        getterVisibility = NONE,
        setterVisibility = NONE)
public class ProfileDocument implements Document {
    @JsonProperty(access = WRITE_ONLY)
    private final String _type = "profiles";

    @JsonIgnore
    private int id;
    @JsonProperty(access = WRITE_ONLY)
    private String _id;
    @JsonProperty(access = WRITE_ONLY)
    private String _index;

    private Set<Integer> readAuths;
    private String givenName;
    private String surname;
    private String fullName;
    private String handle;
    private String primaryEmail;
    private List<String> emails;
    private String emailFormat;
    private String primaryCity;
    private String primaryState;
    private String primaryPostalCode;
    private String primaryAddress1;
    private String primaryPhone;
    private List<String> phones;
    private Boolean updateOptIn;
    private Set<Integer> collections;
    private Set<Integer> questionnaires;
    private Date firstStoryDate;
    private Date lastStoryDate;
    private int storyCount;
//    private Map<Integer, Date> firstStoryDateByCollection = Maps.newLinkedHashMap();
//    private Map<Integer, Date> lastStoryDateByCollection = Maps.newLinkedHashMap();
//    private Map<Integer, Integer> storyCountByCollection = Maps.newLinkedHashMap();

    ProfileDocument() {
    }

    public ProfileDocument(
            int id,
            String givenName,
            String surname,
            String fullName,
            String handle,
            String primaryEmail,
            List<String> emails,
            String emailFormat,
            Address primaryAddress,
            String primaryPhone,
            List<String> phones,
            Boolean updateOptIn,
            Set<Integer> collections,
            Set<Integer> questionnaires,
            Date firstStoryDate,
            Date lastStoryDate,
            int storyCount,
//            Map<Integer, Date> firstStoryDateByCollection,
//            Map<Integer, Date> lastStoryDateByCollection,
//            Map<Integer, Integer> storyCountByCollection,
            Set<Integer> readAuths) {
        this.id = id;
        this.givenName = givenName;
        this.surname = surname;
        this.fullName = fullName;
        this.handle = handle;
        this.primaryEmail = primaryEmail;
        this.emails = emails;
        this.emailFormat = emailFormat;

        if (primaryAddress != null) {
            this.primaryCity = primaryAddress.getCity();
            this.primaryState = primaryAddress.getState();
            this.primaryPostalCode = primaryAddress.getPostalCode();
            this.primaryAddress1 = primaryAddress.getAddress1();
        }

        this.primaryPhone = primaryPhone;
        this.phones = phones;
        this.updateOptIn = updateOptIn;
        this.collections = collections;
        this.questionnaires = questionnaires;
        this.firstStoryDate = firstStoryDate;
        this.lastStoryDate = lastStoryDate;
        this.storyCount = storyCount;
//        this.firstStoryDateByCollection = firstStoryDateByCollection;
//        this.lastStoryDateByCollection = lastStoryDateByCollection;
//        this.storyCountByCollection = storyCountByCollection;
        this.readAuths = readAuths;
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

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getEmailFormat() {
        return emailFormat;
    }

    public void setEmailFormat(String emailFormat) {
        this.emailFormat = emailFormat;
    }

    public String getPrimaryCity() {
        return primaryCity;
    }

    public void setPrimaryCity(String primaryCity) {
        this.primaryCity = primaryCity;
    }

    public String getPrimaryState() {
        return primaryState;
    }

    public void setPrimaryState(String primaryState) {
        this.primaryState = primaryState;
    }

    public String getPrimaryPostalCode() {
        return primaryPostalCode;
    }

    public void setPrimaryPostalCode(String primaryPostalCode) {
        this.primaryPostalCode = primaryPostalCode;
    }

    public String getPrimaryAddress1() {
        return primaryAddress1;
    }

    public void setPrimaryAddress1(String primaryAddress1) {
        this.primaryAddress1 = primaryAddress1;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public Boolean getUpdateOptIn() {
        return updateOptIn;
    }

    public void setUpdateOptIn(Boolean updateOptIn) {
        this.updateOptIn = updateOptIn;
    }

    public Set<Integer> getCollections() {
        if (collections == null) {
            return new HashSet<Integer>();
        }
        return this.collections;
    }

    public void setCollections(Set<Integer> collections) {
        if (collections == null) {
            this.collections = new HashSet<Integer>();
        } else {
            this.collections = collections;
        }
    }

    public Set<Integer> getQuestionnaires() {
        if (questionnaires == null) {
            return new HashSet<Integer>();
        }
        return questionnaires;
    }

    public void setQuestionnaires(Set<Integer> questionnaires) {
        if (questionnaires == null) {
            this.questionnaires = new HashSet<Integer>();
        } else {
            this.questionnaires = questionnaires;
        }
    }

    public Date getFirstStoryDate() {
        return firstStoryDate;
    }

    public void setFirstStoryDate(Date firstStoryDate) {
        this.firstStoryDate = firstStoryDate;
    }

    public Date getLastStoryDate() {
        return lastStoryDate;
    }

    public void setLastStoryDate(Date lastStoryDate) {
        this.lastStoryDate = lastStoryDate;
    }

    public int getStoryCount() {
        return storyCount;
    }

    public void setStoryCount(int storyCount) {
        this.storyCount = storyCount;
    }

//    public Map<Integer, Date> getFirstStoryDateByCollection() {
//        return firstStoryDateByCollection;
//    }
//
//    public void setFirstStoryDateByCollection(Map<Integer, Date> firstStoryDateByCollection) {
//        this.firstStoryDateByCollection = firstStoryDateByCollection;
//    }
//
//    public Map<Integer, Date> getLastStoryDateByCollection() {
//        return lastStoryDateByCollection;
//    }
//
//    public void setLastStoryDateByCollection(Map<Integer, Date> lastStoryDateByCollection) {
//        this.lastStoryDateByCollection = lastStoryDateByCollection;
//    }
//
//    public Map<Integer, Integer> getStoryCountByCollection() {
//        return storyCountByCollection;
//    }
//
//    public void setStoryCountByCollection(Map<Integer, Integer> storyCountByCollection) {
//        this.storyCountByCollection = storyCountByCollection;
//    }

    @Override
    public Set<Integer> getReadAuths() {
        return readAuths;
    }
}
