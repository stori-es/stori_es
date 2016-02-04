package org.consumersunion.stories.server.notification_channel;

import java.util.List;

import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.SystemEntity;

public class StoriesAddedEvent extends NotificationEvent {
    private final List<Story> stories;
    private final Profile profile;
    private final String primaryEmail;

    public StoriesAddedEvent(SystemEntity systemEntity, List<Story> stories) {
        this(systemEntity, stories, null, null);
    }

    public StoriesAddedEvent(SystemEntity systemEntity,
            List<Story> stories,
            Profile profile,
            String primaryEmail) {
        super(systemEntity, NotificationTrigger.STORY_ADDED);

        this.stories = stories;
        this.profile = profile;
        this.primaryEmail = primaryEmail;
    }

    public List<Story> getStories() {
        return stories;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }
}
