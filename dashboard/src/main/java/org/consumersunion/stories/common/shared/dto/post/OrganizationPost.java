package org.consumersunion.stories.common.shared.dto.post;

import java.util.List;

import org.consumersunion.stories.common.shared.dto.ApiContact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class OrganizationPost {
    private final List<ApiContact> contacts;
    @JsonProperty("profile_ids")
    private final List<Integer> profileIds;
    @JsonProperty("questionnaire_ids")
    private final List<Integer> questionnaireIds;
    @JsonProperty("collection_ids")
    private final List<Integer> collectionIds;
    @JsonProperty("permission_ids")
    private final List<Integer> permissionIds;
    @JsonProperty("theme_ids")
    private final List<Integer> themeIds;

    private String name;
    @JsonProperty("short_name")
    private String shortName;
    @JsonProperty("default_permission_id")
    private Integer defaultPermissionId;
    @JsonProperty("default_theme_id")
    private Integer defaultThemeId;

    public OrganizationPost() {
        profileIds = Lists.newArrayList();
        contacts = Lists.newArrayList();
        questionnaireIds = Lists.newArrayList();
        collectionIds = Lists.newArrayList();
        permissionIds = Lists.newArrayList();
        themeIds = Lists.newArrayList();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<ApiContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<ApiContact> contacts) {
        clearAndAdd(this.contacts, contacts);
    }

    public List<Integer> getProfileIds() {
        return profileIds;
    }

    public void setProfileIds(List<Integer> profileIds) {
        clearAndAdd(this.profileIds, profileIds);
    }

    public List<Integer> getQuestionnaireIds() {
        return questionnaireIds;
    }

    public void setQuestionnaireIds(List<Integer> questionnaireIds) {
        clearAndAdd(this.questionnaireIds, questionnaireIds);
    }

    public List<Integer> getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(List<Integer> collectionIds) {
        clearAndAdd(this.collectionIds, collectionIds);
    }

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        clearAndAdd(this.permissionIds, permissionIds);
    }

    public Integer getDefaultPermissionId() {
        return defaultPermissionId;
    }

    public void setDefaultPermissionId(Integer defaultPermissionId) {
        this.defaultPermissionId = defaultPermissionId;
    }

    public List<Integer> getThemeIds() {
        return themeIds;
    }

    public void setThemeIds(List<Integer> themeIds) {
        clearAndAdd(this.themeIds, themeIds);
    }

    public Integer getDefaultThemeId() {
        return defaultThemeId;
    }

    public void setDefaultThemeId(Integer defaultThemeId) {
        this.defaultThemeId = defaultThemeId;
    }

    private <T> void clearAndAdd(List<T> receiver, List<T> toAdd) {
        receiver.clear();
        if (toAdd != null) {
            receiver.addAll(toAdd);
        }
    }

    public static class Builder {
        private String name;
        private String shortName;
        private List<ApiContact> contacts;
        private List<Integer> profileIds;
        private List<Integer> questionnaireIds;
        private List<Integer> collectionIds;
        private List<Integer> permissionIds;
        private Integer defaultPermissionId;
        private List<Integer> themeIds;
        private Integer defaultThemeId;

        private Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public Builder setContacts(List<ApiContact> contacts) {
            this.contacts = contacts;
            return this;
        }

        public Builder setProfileIds(List<Integer> profileIds) {
            this.profileIds = profileIds;
            return this;
        }

        public Builder setQuestionnaireIds(List<Integer> questionnaireIds) {
            this.questionnaireIds = questionnaireIds;
            return this;
        }

        public Builder setCollectionIds(List<Integer> collectionIds) {
            this.collectionIds = collectionIds;
            return this;
        }

        public Builder setPermissionIds(List<Integer> permissionIds) {
            this.permissionIds = permissionIds;
            return this;
        }

        public Builder setDefaultPermissionId(Integer defaultPermissionId) {
            this.defaultPermissionId = defaultPermissionId;
            return this;
        }

        public Builder setThemeIds(List<Integer> themeIds) {
            this.themeIds = themeIds;
            return this;
        }

        public Builder setDefaultThemeId(Integer defaultThemeId) {
            this.defaultThemeId = defaultThemeId;
            return this;
        }

        public OrganizationPost build() {
            OrganizationPost organizationPost = new OrganizationPost();
            organizationPost.setContacts(contacts);
            organizationPost.setShortName(shortName);
            organizationPost.setName(name);
            organizationPost.setCollectionIds(collectionIds);
            organizationPost.setDefaultPermissionId(defaultPermissionId);
            organizationPost.setDefaultThemeId(defaultThemeId);
            organizationPost.setPermissionIds(permissionIds);
            organizationPost.setProfileIds(profileIds);
            organizationPost.setQuestionnaireIds(questionnaireIds);
            organizationPost.setThemeIds(themeIds);

            return organizationPost;
        }
    }
}
