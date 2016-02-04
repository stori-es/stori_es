package org.consumersunion.stories.common.shared.dto.tasks;

import java.util.Set;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.assistedinject.Assisted;

@JsonTypeName("addStoriesToCollection")
public class AddStoriesToCollectionTask extends Task {
    private Set<Integer> collectionIds;
    private String searchToken;
    private Integer collectionId;
    private Integer questionnaireId;

    @Inject
    public AddStoriesToCollectionTask(
            @Assisted("profileId") int profileId,
            @Assisted Set<Integer> collectionIds,
            @Assisted String searchToken,
            @Assisted("collectionId") Integer collectionId,
            @Assisted("questionnaireId") Integer questionnaireId) {
        super(profileId, TaskType.ADD_STORIES);

        this.collectionIds = collectionIds;
        this.searchToken = searchToken;
        this.collectionId = collectionId;
        this.questionnaireId = questionnaireId;
    }

    public AddStoriesToCollectionTask() {
    }

    public String getSearchToken() {
        return searchToken;
    }

    public void setSearchToken(String searchToken) {
        this.searchToken = searchToken;
    }

    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public Set<Integer> getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(Set<Integer> collectionIds) {
        this.collectionIds = collectionIds;
    }
}
