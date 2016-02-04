package org.consumersunion.stories.common.shared.dto;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractCollectionResponse<L extends BaseCollectionResourceLinks> extends BasicResponse<L> {
    private String title;
    private String summary;
    private Set<String> tags;
    @JsonProperty("is_published")
    private boolean published;
    @JsonProperty("published_on")
    private Date publishedOn;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Date getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }
}
