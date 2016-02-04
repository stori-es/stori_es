package org.consumersunion.stories.common.shared.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionnaireResponse extends AbstractCollectionResponse<QuestionnaireResourceLinks> {
    private QuestionnaireResourceLinks links;
    @JsonProperty("story_tags")
    private Set<String> autoTags;

    @Override
    public QuestionnaireResourceLinks getLinks() {
        return links;
    }

    @Override
    public void setLinks(QuestionnaireResourceLinks links) {
        this.links = links;
    }

    public void setAutoTags(Set<String> autoTags) {
        this.autoTags = autoTags;
    }

    public Set<String> getAutoTags() {
        return autoTags;
    }
}
