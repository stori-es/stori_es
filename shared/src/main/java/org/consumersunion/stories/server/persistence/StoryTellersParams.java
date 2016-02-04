package org.consumersunion.stories.server.persistence;

import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.server.persistence.params.PagedRetrieveParams;

public class StoryTellersParams extends PagedRetrieveParams {
    private Integer collectionId;
    private Integer questionnaireId;

    public StoryTellersParams(
            int start,
            int length,
            SortField sortField,
            boolean ascending,
            Integer collectionId,
            Integer questionnaireId,
            int relation,
            Integer effectiveId,
            String searchToken) {
        super(start, length, sortField, ascending, relation, effectiveId, searchToken);

        this.collectionId = collectionId;
        this.questionnaireId = questionnaireId;
    }

    public Integer getCollectionId() {
        return collectionId;
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

    @Override
    public StoryTellersParams noLimit() {
        return new StoryTellersParams(0, 0, getSortField(), isAscending(), getCollectionId(), getQuestionnaireId(),
                getAuthRelation(), getEffectiveId(), getSearchText());
    }
}
