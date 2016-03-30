package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResourceLinks extends ResourceLinks {
    private List<ResourceLink> profiles;
    @JsonProperty("default_profile")
    private ResourceLink defaultProfile;

    public List<ResourceLink> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ResourceLink> profiles) {
        this.profiles = profiles;
    }

    public ResourceLink getDefaultProfile() {
        return defaultProfile;
    }

    public void setDefaultProfile(ResourceLink defaultProfile) {
        this.defaultProfile = defaultProfile;
    }
}
