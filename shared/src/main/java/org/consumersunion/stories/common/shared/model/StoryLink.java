package org.consumersunion.stories.common.shared.model;

import java.io.Serializable;

public class StoryLink implements Serializable {
    // For serialization
    public StoryLink() {
    }

    public StoryLink(Integer story) {
        this(story, false);
    }

    public StoryLink(Integer story, Boolean isClearedForPublicInclusion) {
        super();

        this.story = story;
        this.isClearedForPublicInclusion = isClearedForPublicInclusion;
    }

    /**
     * @see #getStory()
     */
    private Integer story;

    /**
     * @see #getClearedForPublicInclusion
     */
    private Boolean isClearedForPublicInclusion;

    /**
     * The {@link Story} referenced by this link.
     */
    public Integer getStory() {
        return story;
    }

    /**
     * @see #getStory()
     */
    public void setStory(Integer story) {
        this.story = story;
    }

    /**
     * Indicates whether the referenced {@link Story} has been cleared for
     * public inclusion.
     */
    public Boolean getIsClearedForPublicInclusion() {
        return isClearedForPublicInclusion;
    }

    /**
     * @see #isClearedForPublicInclusion()
     */
    public void setIsClearedForPublicInclusion(Boolean isClearedForPublicInclusion) {
        this.isClearedForPublicInclusion = isClearedForPublicInclusion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StoryLink) {
            StoryLink oLink = (StoryLink) obj;
            if (this.hashCode() == oLink.hashCode()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() { // uhh... this should be explained
        return story;
    }
}
