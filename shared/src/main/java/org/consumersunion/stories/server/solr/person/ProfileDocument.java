package org.consumersunion.stories.server.solr.person;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.solr.Document;

public class ProfileDocument implements Document {
    private static final String FIRST_DATE_STEM = "firstStoryDateByCollection_";
    private static final String LAST_DATE_STEM = "lastStoryDateByCollection_";
    private static final String STORY_COUNT_STEM = "storyCountByCollection_";

    private final int id;
    private final Collection<Integer> readAuths;

    private String givenName;
    private String surname;
    private String fullName;
    private String handle;
    private String primaryEmail;
    private Collection<String> emails;
    private String emailFormat;
    private String primaryCity;
    private String primaryState;
    private String primaryPostalCode;
    private String primaryAddress1;
    private String primaryPhone;
    private Collection<String> phones;
    private Boolean updateOptIn;
    private Collection<Integer> collections;
    private Collection<Integer> questionnaires;
    private Date firstStoryDate;
    private Date lastStoryDate;
    private int storyCount;
    private Map<Integer, Date> firstStoryDateByCollection;
    private Map<Integer, Date> lastStoryDateByCollection;
    private Map<Integer, Integer> storyCountByCollection;

    public ProfileDocument(
            int id,
            String givenName,
            String surname,
            String fullName,
            String handle,
            String primaryEmail,
            Collection<String> emails,
            String emailFormat,
            Address primaryAddress,
            String primaryPhone,
            Collection<String> phones,
            Boolean updateOptIn,
            Collection<Integer> collections,
            Collection<Integer> questionnaires,
            Date firstStoryDate,
            Date lastStoryDate,
            int storyCount, Map<Integer, Date> firstStoryDateByCollection,
            Map<Integer, Date> lastStoryDateByCollection, Map<Integer, Integer> storyCountByCollection,
            Collection<Integer> readAuths) {
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
        this.firstStoryDateByCollection = firstStoryDateByCollection;
        this.lastStoryDateByCollection = lastStoryDateByCollection;
        this.storyCountByCollection = storyCountByCollection;
        this.readAuths = readAuths;
    }

    @SuppressWarnings("unchecked")
    public ProfileDocument(SolrDocument document) {
        this.id = Integer.parseInt((String) document.getFieldValue("id"));
        this.givenName = (String) document.getFieldValue("givenName");
        this.surname = (String) document.getFieldValue("surname");
        this.fullName = (String) document.getFieldValue("fullName");
        this.handle = (String) document.getFieldValue("handle");
        this.primaryEmail = (String) document.getFieldValue("primaryEmail");
        this.emails = (Collection<String>) (Collection<?>) document.getFieldValues("emails");
        this.emailFormat = (String) document.getFieldValue("emailFormat");
        this.primaryCity = (String) document.getFieldValue("primaryCity");
        this.primaryState = (String) document.getFieldValue("primaryState");
        this.primaryPostalCode = (String) document.getFieldValue("primaryPostalCode");
        this.primaryAddress1 = (String) document.getFieldValue("primaryAddress1");
        this.primaryPhone = (String) document.getFieldValue("primaryPhone");
        this.phones = (Collection<String>) (Collection<?>) document.getFieldValues("phones");
        this.updateOptIn = (Boolean) document.getFieldValue("updateOptIn");
        setCollections((Collection<Integer>) (Collection<?>) document.getFieldValues("collections"));
        setQuestionnaires((Collection<Integer>) (Collection<?>) document.getFieldValues("questionnaires"));
        this.firstStoryDate = (Date) document.getFieldValue("firstStoryDate");
        this.lastStoryDate = (Date) document.getFieldValue("lastStoryDate");

        Integer count = (Integer) document.getFieldValue("storyCount");
        if (count == null) {
            this.storyCount = 0;
        } else {
            this.storyCount = count;
        }

        this.firstStoryDateByCollection = new HashMap<Integer, Date>();
        this.lastStoryDateByCollection = new HashMap<Integer, Date>();
        this.storyCountByCollection = new HashMap<Integer, Integer>();
        for (String fieldName : document.getFieldNames()) {
            if (fieldName.startsWith(FIRST_DATE_STEM)) {
                int collectionId = Integer.parseInt(fieldName.substring(27));
                firstStoryDateByCollection.put(collectionId, (Date) document.getFieldValue(fieldName));
            } else if (fieldName.startsWith(LAST_DATE_STEM)) {
                int collectionId = Integer.parseInt(fieldName.substring(26));
                lastStoryDateByCollection.put(collectionId, (Date) document.getFieldValue(fieldName));
            } else if (fieldName.startsWith(STORY_COUNT_STEM)) {
                int collectionId = Integer.parseInt(fieldName.substring(23));
                storyCountByCollection.put(collectionId, (Integer) document.getFieldValue(fieldName));
            }
            // else it's a non-dynamic field
        }

        this.readAuths = (Collection<Integer>) (Collection<?>) document.getFieldValues("readAuths");
    }

    @Override
    public SolrInputDocument toDocument() {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", id);
        document.addField("givenName", givenName);
        document.addField("surname", surname);
        document.addField("fullName", fullName);
        document.addField("handle", handle);
        document.addField("primaryEmail", primaryEmail);
        document.addField("emails", emails);
        document.addField("emailFormat", emailFormat);
        document.addField("primaryCity", primaryCity);
        document.addField("primaryState", primaryState);
        document.addField("primaryPostalCode", primaryPostalCode);
        document.addField("primaryAddress1", primaryAddress1);
        document.addField("primaryPhone", primaryPhone);
        document.addField("phones", phones);
        document.addField("updateOptIn", updateOptIn);
        document.addField("collections", collections);
        document.addField("questionnaires", questionnaires);
        document.addField("firstStoryDate", firstStoryDate);
        document.addField("lastStoryDate", lastStoryDate);
        document.addField("storyCount", storyCount);
        document.addField("readAuths", readAuths);
        if (firstStoryDateByCollection != null) {
            for (Entry<Integer, Date> entry : firstStoryDateByCollection.entrySet()) {
                document.addField(FIRST_DATE_STEM + entry.getKey(), entry.getValue());
            }
            for (Entry<Integer, Date> entry : lastStoryDateByCollection.entrySet()) {
                document.addField(LAST_DATE_STEM + entry.getKey(), entry.getValue());
            }
            for (Entry<Integer, Integer> entry : storyCountByCollection.entrySet()) {
                document.addField(STORY_COUNT_STEM + entry.getKey(), entry.getValue());
            }
        }

        return document;
    }

    public int getId() {
        return id;
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

    public Collection<String> getEmails() {
        return emails;
    }

    public void setEmails(Collection<String> emails) {
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

    public Collection<String> getPhones() {
        return phones;
    }

    public void setPhones(Collection<String> phones) {
        this.phones = phones;
    }

    public Boolean getUpdateOptIn() {
        return updateOptIn;
    }

    public void setUpdateOptIn(Boolean updateOptIn) {
        this.updateOptIn = updateOptIn;
    }

    public Collection<Integer> getCollections() {
        if (collections == null) {
            return new HashSet<Integer>();
        }
        return this.collections;
    }

    public void setCollections(Collection<Integer> collections) {
        if (collections == null) {
            this.collections = new HashSet<Integer>();
        } else {
            this.collections = collections;
        }
    }

    public Collection<Integer> getQuestionnaires() {
        if (questionnaires == null) {
            return new HashSet<Integer>();
        }
        return questionnaires;
    }

    public void setQuestionnaires(Collection<Integer> questionnaires) {
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

    public Map<Integer, Date> getFirstStoryDateByCollection() {
        return firstStoryDateByCollection;
    }

    public void setFirstStoryDateByCollection(Map<Integer, Date> firstStoryDateByCollection) {
        this.firstStoryDateByCollection = firstStoryDateByCollection;
    }

    public Map<Integer, Date> getLastStoryDateByCollection() {
        return lastStoryDateByCollection;
    }

    public void setLastStoryDateByCollection(Map<Integer, Date> lastStoryDateByCollection) {
        this.lastStoryDateByCollection = lastStoryDateByCollection;
    }

    public Map<Integer, Integer> getStoryCountByCollection() {
        return storyCountByCollection;
    }

    public void setStoryCountByCollection(Map<Integer, Integer> storyCountByCollection) {
        this.storyCountByCollection = storyCountByCollection;
    }

    public Collection<Integer> getReadAuths() {
        return readAuths;
    }
}
