package org.consumersunion.stories.common.shared.dto;

import java.util.List;

public class UserResourceLinks extends ResourceLinks {
    private List<ResourceLink> profiles;

    public List<ResourceLink> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ResourceLink> profiles) {
        this.profiles = profiles;
    }
}
