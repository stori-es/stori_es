package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import java.util.Iterator;
import java.util.List;

import org.consumersunion.stories.common.client.ui.questionnaire.QuestionnaireRenderer;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.dashboard.client.application.questionnaire.ui.DegradedAnswerSetRenderer;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AnswerSetView extends ViewImpl implements AnswerSetPresenter.MyView {
    interface Binder extends UiBinder<Widget, AnswerSetView> {
    }

    @UiField(provided = true)
    final QuestionnaireRenderer questionnaireRenderer;
    @UiField(provided = false)
    DeckPanel answerSetPanel;
    @UiField(provided = false)
    DegradedAnswerSetRenderer degradedRenderer;

    private Questionnaire questionnaire;

    @Inject
    AnswerSetView(
            Binder uiBinder,
            QuestionnaireRenderer questionnaireRenderer) {
        this.questionnaireRenderer = questionnaireRenderer;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void displayData(Questionnaire questionnaire, AnswerSet answerSet) {
        this.questionnaire = questionnaire;
        if (questionnaire != null && answerSet != null && questionnaire.matches(answerSet)) {
            questionnaire.initBlocksKey();

            answerSetPanel.showWidget(0);
            questionnaireRenderer.init(questionnaire.getSurvey());
            questionnaireRenderer.setAnswers(answerSet, false);
        } else {
            degradedRenderer.setAnswerSet(answerSet);
            answerSetPanel.showWidget(1);
        }
    }

    @Override
    public void setStoryContent(String storyContent) {
        Block storyAsk = findStoryAskBlock(questionnaire);
        questionnaireRenderer.setText(storyAsk, storyContent);
    }

    private Block findStoryAskBlock(Questionnaire original) {
        List<Block> blocks = original.getSurvey().getBlocks();

        Iterator<Block> it = blocks.iterator();
        for (; it.hasNext(); ) {
            Block block = it.next();
            if (isStoryAsk(block)) {
                return block;
            }
        }

        return null;
    }

    private boolean isStoryAsk(Block block) {
        return BlockType.STORY_ASK_RICH.equals(block.getBlockType())
        		|| BlockType.STORY_ASK_PLAIN.equals(block.getBlockType());
    }
}
