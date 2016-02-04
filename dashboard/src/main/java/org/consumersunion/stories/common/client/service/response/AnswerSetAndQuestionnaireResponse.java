package org.consumersunion.stories.common.client.service.response;

import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;

public class AnswerSetAndQuestionnaireResponse extends Response {
    private AnswerSet answerSet;
    private Questionnaire questionnaire;

    public AnswerSetAndQuestionnaireResponse() {
        super();
    }

    public AnswerSetAndQuestionnaireResponse(Response template) {
        super(template);
    }

    public AnswerSet getAnswerSet() {
        return answerSet;
    }

    public void setAnswerSet(final AnswerSet answerSet) {
        this.answerSet = answerSet;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(final Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }
}
