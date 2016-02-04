package org.consumersunion.stories.common.client.ui.questionnaire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.common.client.ui.block.ElementFactoryImpl;
import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.client.ui.block.QuestionElementFactory;
import org.consumersunion.stories.common.shared.model.HasBlocks;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class QuestionnaireRenderer extends Composite {
    interface Binder extends UiBinder<Widget, QuestionnaireRenderer> {
    }

    @UiField
    HTMLPanel elementsPanel;

    private final QuestionElementFactory questionElementFactory;
    private final ElementFactoryImpl elementFactory;
    private final AnswerExtractor answerExtractor;
    private final Map<String, QuestionElement<?>> questions;

    @Inject
    QuestionnaireRenderer(
            Binder uiBinder,
            QuestionElementFactory questionElementFactory,
            ElementFactoryImpl elementFactory,
            AnswerExtractor answerExtractor) {
        this.questionElementFactory = questionElementFactory;
        this.elementFactory = elementFactory;
        this.answerExtractor = answerExtractor;
        this.questions = new HashMap<String, QuestionElement<?>>();

        initWidget(uiBinder.createAndBindUi(this));
    }

    public void init(HasBlocks blocksContainer) {
        elementsPanel.clear();
        questions.clear();

        for (Block element : blocksContainer.getBlocks()) {
            QuestionElement questionElement = questionElementFactory.create(element);
            if (questionElement != null) {
                addQuestionElement(questionElement);
            } else {
                IsWidget block = elementFactory.create(element);
                if (block != null) {
                    elementsPanel.add(block);
                }
            }
        }
    }

    public List<Answer> getAnswers() throws QuestionnaireNotValidException {
        return answerExtractor.extractAnswers(questions.values());
    }

    public void setAnswers(AnswerSet answerSet, Boolean enabled) {
        for (Answer answer : answerSet.getAnswers()) {
            QuestionElement<?> element = questions.get(answer.getLabel());
            if (element != null) {
                element.setAnswer(answer.getReportValues(), enabled);
            }
        }
    }

    private void addQuestionElement(QuestionElement<?> element) {
        elementsPanel.add(element);
        questions.put(element.getKey(), element);
    }
}
