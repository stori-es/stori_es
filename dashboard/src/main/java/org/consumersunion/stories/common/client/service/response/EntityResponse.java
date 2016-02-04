package org.consumersunion.stories.common.client.service.response;

import java.util.List;

import org.consumersunion.stories.common.shared.service.datatransferobject.StoryAndStorytellerData;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Machin Class to encapsulate the response
 */

public class EntityResponse extends Response {

    List<StoryAndStorytellerData> stories;

    List<StoryAndStorytellerData> allStories;

    List<EntitySummary> entitySummary;

    public List<EntitySummary> getEntitySummary() {
        return entitySummary;
    }

    public void setEntitySummary(final List<EntitySummary> entitySummary) {
        this.entitySummary = entitySummary;
    }

    public List<StoryAndStorytellerData> getStories() {
        return stories;
    }

    public List<StoryAndStorytellerData> getAllStories() {
        return allStories;
    }

    public void setStories(final List<StoryAndStorytellerData> stories) {
        this.stories = stories;
    }

    public void setAllStories(final List<StoryAndStorytellerData> stories) {
        this.allStories = stories;
    }

    public class EntitySummary implements IsSerializable {
        private int id;
        private String title;
        private String permalink;
        private float score;

        public EntitySummary(final int id, final String title, final String permalink, final float score) {
            this.id = id;
            this.permalink = permalink;
            this.score = score;
            this.title = title;
        }

        EntitySummary() {
        }

        public int getId() {
            return id;
        }

        public void setId(final int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public String getPermalink() {
            return permalink;
        }

        public void setPermalink(final String permalink) {
            this.permalink = permalink;
        }

        public float getScore() {
            return score;
        }

        public void setScore(final float score) {
            this.score = score;
        }
    }
}
