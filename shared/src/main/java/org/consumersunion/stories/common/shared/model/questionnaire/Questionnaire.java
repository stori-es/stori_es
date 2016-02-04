package org.consumersunion.stories.common.shared.model.questionnaire;

import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.util.GUID;

/**
 * A Questionnaire organizes a set of {@link Question}s generally connected to a
 * {@link Collection}.
 *
 * @author Zane Rockenbaugh
 */
public class Questionnaire extends QuestionnaireBase {
    /**
     * New questionnaire constructor.
     */
    public Questionnaire() {
        super();
    }

    /**
     * Existing questionnaire constructor.
     */
    public Questionnaire(final int id, final int version) {
        super(id, version);
    }

    public boolean matches(AnswerSet answerSet) {
        if (answerSet != null) {
            for (Answer answer : answerSet.getAnswers()) {
                if (getQuestionByLabel(answer.getLabel()) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void initBlocksKey() {
        for (Block element : getSurvey().getBlocks()) {
            element.setKey(GUID.get());
        }
    }
}
