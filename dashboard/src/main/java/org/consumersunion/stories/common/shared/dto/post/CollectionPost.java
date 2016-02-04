package org.consumersunion.stories.common.shared.dto.post;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CollectionPost extends AbstractEntityData {
    @JsonProperty("story_ids")
    private List<Integer> storyIds = new ArrayList<Integer>();
    @JsonProperty("questionnaire_ids")
    private List<Integer> questionnaireIds = new ArrayList<Integer>();

    public static Builder builder() {
        return new Builder();
    }

    public List<Integer> getQuestionnaireIds() {
        return questionnaireIds;
    }

    public void setQuestionnaireIds(List<Integer> questionnaireIds) {
        this.questionnaireIds = ensureList(this.questionnaireIds);

        clearAndAdd(this.questionnaireIds, questionnaireIds);
    }

    public List<Integer> getStoryIds() {
        return storyIds;
    }

    public void setStoryIds(List<Integer> storyIds) {
        this.storyIds = ensureList(this.storyIds);

        clearAndAdd(this.storyIds, storyIds);
    }

    public static class Builder extends AbstractEntityDataBuilder<Builder> {
        private List<Integer> questionnaireIds;
        private List<Integer> storyIds;

        private Builder() {
        }

        public Builder withQuestionnaireIds(List<Integer> questionnaireIds) {
            this.questionnaireIds = questionnaireIds;
            return this;
        }

        public Builder withStoryIds(List<Integer> storyIds) {
            this.storyIds = storyIds;
            return this;
        }

        public CollectionPost build() {
            CollectionPost collectionPost = buildBase();
            collectionPost.setQuestionnaireIds(questionnaireIds);
            collectionPost.setStoryIds(storyIds);

            return collectionPost;
        }

        @Override
        protected CollectionPost createEmpty() {
            return new CollectionPost();
        }
    }
}
