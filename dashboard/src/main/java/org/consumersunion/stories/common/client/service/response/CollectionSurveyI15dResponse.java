package org.consumersunion.stories.common.client.service.response;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;

public class CollectionSurveyI15dResponse extends Response {
    private Collection collection;
    private QuestionnaireI15d questionnaire;

    public CollectionSurveyI15dResponse() {
        super();
    }

    public CollectionSurveyI15dResponse(Response template) {
        super(template);
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(final Collection collection) {
        this.collection = collection;
    }

    public QuestionnaireI15d getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(final QuestionnaireI15d questionnaire) {
        this.questionnaire = questionnaire;
    }
}
