package org.consumersunion.stories.common.shared.model;

import java.util.List;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.entity.Entity;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Encapsulates the standard user data. Note that some data associated with the
 * user, such as login password, is not a part of this class. While it makes
 * sense to keep everything together in the database, you don't often want or
 * need to send over all the data, and in this case, there are very good reasons
 * not to.
 *
 * @author Zane Rocknebaugh
 */
public class User extends Entity {
    /**
     * @see #getHandle()
     */
    private String handle;
    /**
     * @see #isActive()
     */
    private boolean active;
    /**
     * @see #getDefaultProfile()
     */
    private int defaultProfile;
    private List<ProfileSummary> profiles = Lists.newArrayList();

    /**
     * New constructor.
     */
    public User() {
        super();
    }

    /**
     * Existing User constructor.
     */
    public User(int id, int version) {
        super(id, version);
    }

    /**
     * The user's unique handle.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * @see #getHandle()
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Indicates whether the user active. Inactive users may not log into the
     * system. Corresponds to the database column user.active.
     *
     * @return The active status of the user.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @see #isActive()
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ProfileSummary> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ProfileSummary> profiles) {
        this.profiles = profiles;
    }

    public ProfileSummary getProfileFor(int organization) {
        return findProfileForOrganization(organization);
    }

    public int getDefaultProfile() {
        return defaultProfile;
    }

    public void setDefaultProfile(int defaultProfile) {
        this.defaultProfile = defaultProfile;
    }

    public void updateProfile(ProfileSummary profileSummary) {
        ProfileSummary profileForOrganization = findProfileForOrganization(
                profileSummary.getProfile().getOrganizationId());
        profiles.remove(profileForOrganization);
        profiles.add(profileSummary);
    }

    public boolean isProfileOwner(final int entityId) {
        return Iterables.tryFind(profiles, new Predicate<ProfileSummary>() {
            @Override
            public boolean apply(ProfileSummary input) {
                return entityId == input.getProfile().getId();
            }
        }).isPresent();
    }

    private ProfileSummary findProfileForOrganization(final int organization) {
        return Iterables.tryFind(profiles, new Predicate<ProfileSummary>() {
            @Override
            public boolean apply(ProfileSummary input) {
                return input.getProfile().getOrganizationId() == organization;
            }
        }).orNull();
    }
}
