package org.consumersunion.stories.common.shared.service.datatransferobject;

import java.io.Serializable;
import java.util.List;

import org.consumersunion.stories.common.client.service.datatransferobject.AssignableStory;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.User;

public class StoryAndStorytellerData implements Serializable, AssignableStory {
    private static final long serialVersionUID = -6643095287496011106L;

    private Story story;
    private Address address;
    private User user;
    private Profile profile;
    private List<String> tags;

    // For serialization
    StoryAndStorytellerData() {
    }

    public StoryAndStorytellerData(Story story) {
        this.story = story;
    }

    public StoryAndStorytellerData(
            Story story,
            List<String> tags,
            User user,
            Address address,
            Profile profile) {
        this.story = story;
        this.tags = tags;
        this.user = user;
        this.address = address;
        this.profile = profile;
    }

    public Story getStory() {
        return story;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(final List<String> tags) {
        this.tags = tags;
    }

    @Override
    public int getStoryId() {
        return story.getId();
    }

    public int getUserId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    public Profile getProfile() {
        return profile;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
