package org.consumersunion.stories.common.shared.dto.post;

import java.util.LinkedHashSet;
import java.util.Set;

import org.consumersunion.stories.common.shared.dto.QuestionnaireResourceLinks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionnairePut extends AbstractEntityData {
    @JsonProperty("story_tags")
    private Set<String> autoTags = new LinkedHashSet<String>();
    @JsonProperty("is_published")
    private Boolean published;
    private QuestionnaireResourceLinks links;

    public static Builder builder() {
        return new Builder();
    }

    public Set<String> getAutoTags() {
        return autoTags;
    }

    public void setAutoTags(Set<String> autoTags) {
        clearAndAdd(this.autoTags, autoTags);
    }

    public QuestionnaireResourceLinks getLinks() {
        return links;
    }

    public void setLinks(QuestionnaireResourceLinks links) {
        this.links = links;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public static class Builder extends AbstractEntityDataBuilder<Builder> {
        private Set<String> autoTags;
        private Boolean published;
        private QuestionnaireResourceLinks links;

        private Builder() {
        }

        public Builder withPublished(Boolean published) {
            this.published = published;
            return this;
        }

        public Builder withAutoTags(Set<String> autoTags) {
            this.autoTags = autoTags;
            return this;
        }

        public Builder withLinks(QuestionnaireResourceLinks links) {
            this.links = links;
            return this;
        }

        public QuestionnairePut build() {
            QuestionnairePut put = buildBase();
            put.setPublished(published);
            put.setLinks(links);
            put.setAutoTags(autoTags);

            return put;
        }

        @Override
        protected QuestionnairePut createEmpty() {
            return new QuestionnairePut();
        }
    }
}
