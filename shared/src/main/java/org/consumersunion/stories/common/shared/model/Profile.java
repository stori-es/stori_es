package org.consumersunion.stories.common.shared.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.entity.Entity;
import org.consumersunion.stories.common.shared.model.entity.SortField;

import com.google.common.base.Strings;

/**
 * Encapsulates the standard user data. Note that some data associated with the
 * user, such as login password, is not a part of this class. While it makes
 * sense to keep everything together in the database, you don't often want or
 * need to send over all the data, and in this case, there are very good reasons
 * not to.
 *
 * @author Zane Rocknebaugh
 */
public class Profile extends Entity {
    public static final String NOT_SYNCHRONIZED = "NOT_SYNCHRONIZED";
    public static final String SYNCHRONIZED = "SYNCHRONIZED";
    public static final String POTENTIALY_STALE = "POTENTIALY_STALE";
    public static final String SYNC_REQUIRED = "SYNC_REQUIRED";
    public static final String MANUAL_CHECK = "MANUAL_CHECK";

    public enum ProfileSortField implements SortField {
        STORYTELLER, CITY, STATE, TITLE, ID
    }

    // Fixes the SerializationPolicy error..
    private ProfileSummary unused;

    private int organizationId;

    private Integer userId;

    /**
     * @see #getGivenName()
     */
    private String givenName;
    /**
     * @see #getSurname()
     */
    private String surname;

    private Map<Integer, ConvioSyncStatus> convioSyncStatti;

    /**
     * New constructor.
     */
    public Profile() {
        super();
        this.convioSyncStatti = new HashMap<Integer, ConvioSyncStatus>();
    }

    /**
     * Existing User constructor.
     */
    public Profile(int id, int organization, int version) {
        super(id, version);
        this.organizationId = organization;
        this.convioSyncStatti = new HashMap<Integer, ConvioSyncStatus>();
    }

    /**
     * The user's given name. In many western countries, this would correspond
     * to a user's first name. Corresponds to the database column
     * user.givenName.
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * @see #getGivenName()
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * The user's surname. In many countries, this corresponds to the user's
     * family name and in the western countries, the last name. Corresponds to
     * the database column user.surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @see #getSurname()
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Calculates full name according to given name first, surname second. If both null, returns null.
     *
     * @return
     */
    public String getFullName() {
        if (!Strings.isNullOrEmpty(givenName)) {
            if (!Strings.isNullOrEmpty(surname)) {
                return givenName + " " + surname;
            } else {
                return givenName;
            }
        }

        return Strings.nullToEmpty(surname);
    }

    /**
     * The current <code>Person</code> sync status with the Convio DB.
     */
    public ConvioSyncStatus getConvioSyncStatus(int orgId) {
        if (convioSyncStatti == null || !convioSyncStatti.containsKey(orgId)) {
            return new ConvioSyncStatus(null, NOT_SYNCHRONIZED);
        } else {
            return convioSyncStatti.get(orgId);
        }
    }

    public void setConvioSyncStatus(int orgId, Integer cons_id, String convioSyncStatus) {
        convioSyncStatti.put(orgId, new ConvioSyncStatus(cons_id, convioSyncStatus));
    }

    public void setConvioSyncStatus(Integer orgId, String convioSyncStatus) {
        Integer currentCons_id = convioSyncStatti.containsKey(orgId) ?
                convioSyncStatti.get(orgId).getCons_id() : null;
        convioSyncStatti.put(orgId, new ConvioSyncStatus(currentCons_id, convioSyncStatus));
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Map<Integer, ConvioSyncStatus> getConvioSyncStatti() {
        return convioSyncStatti;
    }

    protected Profile clone() {
        Profile profile = new Profile(this.getId(), this.getOrganizationId(), this.getVersion());
        profile.setId(getId());
        profile.setVersion(getVersion());
        profile.setGivenName(getGivenName());
        profile.setSurname(getSurname());
        for (Entry<Integer, ConvioSyncStatus> entry : convioSyncStatti.entrySet()) {
            profile.setConvioSyncStatus(entry.getKey(), entry.getValue().getCons_id(),
                    entry.getValue().getConvioSyncStatus());
        }

        return profile;
    }
}
