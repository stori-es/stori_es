package org.consumersunion.stories.common.shared.dto.post;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionnairePost extends AbstractEntityData {
    @JsonProperty("collection_ids")
    private List<Integer> collectionIds;
    @JsonProperty("story_tags")
    private Set<String> autoTags = new LinkedHashSet<String>();
    @JsonProperty("theme_id")
    private Integer theme;

    public static Builder builder() {
        return new Builder();
    }

    public List<Integer> getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(List<Integer> collectionIds) {
        this.collectionIds = collectionIds;
    }

    public Set<String> getAutoTags() {
        return autoTags;
    }

    public void setAutoTags(Set<String> autoTags) {
        clearAndAdd(this.autoTags, autoTags);
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public static class Builder extends AbstractEntityDataBuilder<Builder> {
        private List<Integer> collectionIds;
        private Set<String> autoTags;
        private Integer theme;

        private Builder() {
        }

        public Builder withCollectionIds(List<Integer> collectionIds) {
            this.collectionIds = collectionIds;
            return this;
        }

        public Builder withAutoTags(Set<String> autoTags) {
            this.autoTags = autoTags;
            return this;
        }

        public Builder withTheme(Integer theme) {
            this.theme = theme;
            return this;
        }

        public QuestionnairePost build() {
            QuestionnairePost questionnairePost = buildBase();
            questionnairePost.setCollectionIds(collectionIds);
            questionnairePost.setAutoTags(autoTags);
            questionnairePost.setTheme(theme);

            return questionnairePost;
        }

        @Override
        protected QuestionnairePost createEmpty() {
            return new QuestionnairePost();
        }
    }
}
