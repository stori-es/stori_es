package org.consumersunion.stories.common.shared.service.datatransferobject;

import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A data transfer object containing all the data necessary to display an answer
 * set summary according to the UI. The summary avoids instantiating the
 * referenced components themselves.
 */
@JsonTypeName("answersetsummary")
@org.codehaus.jackson.annotate.JsonTypeName("answersetsummary")
public class AnswerSetSummary extends AnswerSet {
    private String questionnaireTitle;

    public AnswerSetSummary() {
    }

    public AnswerSetSummary(AnswerSet answerSet,
            String questionnaireTitle) {
        super(answerSet.getId(), answerSet.getVersion());

        this.questionnaireTitle = questionnaireTitle;

        setAnswers(answerSet.getAnswers());
        setAuthorAddress(answerSet.getAuthorAddress());
        setContributors(answerSet.getContributors());
        setCreated(answerSet.getCreated());
        setEntity(answerSet.getEntity());
        setEntityReferences(answerSet.getEntityReferences());
        setLocale(answerSet.getLocale());
        setOwner(answerSet.getOwner());
        setPermalink(answerSet.getPermalink());
        setPrimaryAuthor(answerSet.getPrimaryAuthor());
        setPrimaryAuthorFirstName(answerSet.getPrimaryAuthorFirstName());
        setPrimaryAuthorLastName(answerSet.getPrimaryAuthorLastName());
        setPublic(answerSet.isPublic());
        setQuestionnaire(answerSet.getQuestionnaire());
        setSystemEntityRelation(answerSet.getSystemEntityRelation());
        setTags(answerSet.getTags());
        setUpdated(answerSet.getUpdated());
    }

    public String getQuestionnaireTitle() {
        return questionnaireTitle;
    }

    public void setQuestionnaireTitle(String questionnaireTitle) {
        this.questionnaireTitle = questionnaireTitle;
    }
}
