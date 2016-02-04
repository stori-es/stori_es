package org.consumersunion.stories.common.shared.model.questionnaire;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.shared.model.document.Document;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("answerset")
@org.codehaus.jackson.annotate.JsonTypeName("answerset")
public class AnswerSet extends Document {
    /**
     * @see #getQuestionnaire()
     */
    private Integer questionnaire;
    private List<Answer> answers;
    private List<String> tags;

    public AnswerSet() {
        super();
        answers = new ArrayList<Answer>();
    }

    public AnswerSet(int id, int version) {
        super(id, version);
        answers = new ArrayList<Answer>();
    }

    /**
     * Returns the ID of the associated {@link Questionnaire}. Together with
     * {@link #getQuestionIdx()}, this resolves to a specific {@link Question}.
     */
    public Integer getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Integer questionnaire) {
        this.questionnaire = questionnaire;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public Answer getAnswerByLabel(String label) {
        if (label == null) {
            return null;
        }

        for (Answer answer : answers) {
            if (label.equals(answer.getLabel())) {
                return answer;
            }
        }
        return null;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        if (this.answers == null) {
            answers = new ArrayList<Answer>();
        }
        answers.add(answer);
    }

    public boolean hasAnswers() {
        return answers != null && answers.size() > 0;
    }

    /**
     * Returns the tags associated to the questionnaire. At the time of writing,
     * tags are only retrieved as needed so depending on the context may be
     * null/empty even when there are in fact tags associated to the
     * <code>AnswerSet</code>.
     */
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getText() {
        String text = "";
        for (Answer answer : answers) {
            text += " " + answer.getLabel() + ":";
            for (String value : answer.getReportValues()) {
                text += " " + value;
            }
        }
        return text.trim();
    }
}
