package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.client.ui.block.QuestionElementFactory;
import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class QuestionBlockBuilder extends BlockBuilder {
    private BlockType formType;
    private QuestionElement<Question> preview;
    private Question value;

    @Inject
    QuestionBlockBuilder(
            Binder uiBinder,
            final QuestionElementFactory elementFactory,
            @Assisted QuestionElement<Question> questionElement,
            @Assisted BlockConfigurator<Question> questionConfigurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly) {
        super(uiBinder, questionElement, questionConfigurator, editMode, readOnly);

        this.preview = questionElement;
        this.value = questionConfigurator.getEditedValue();
        formType = value.getFormType();

        BlockType standardMeaning = value.getStandardMeaning();
        setShowDuplicate(standardMeaning == null
                || BlockType.RATING.equals(standardMeaning));

        questionConfigurator.setDoneCallback(new Callback<Question>() {
            @Override
            public void onSuccess(Question question) {
                if (formTypeChanged(question)) {
                    preview = elementFactory.create(question);
                    setPreviewView(preview);
                    formType = question.getFormType();
                }

                value = question;
                preview.display(value);
                handler.onBlockEdited(QuestionBlockBuilder.this);
                switchTo(PREVIEW);
            }

            @Override
            public void onCancel() {
                switchTo(PREVIEW);
            }
        });
    }

    @Override
    public Block getValue() {
        return value;
    }

    private boolean formTypeChanged(Question question) {
        BlockType valueFormType = formType;
        BlockType questionFormType = question.getFormType();

        return valueFormType == null && questionFormType != null
                || valueFormType != null && questionFormType == null
                || valueFormType != null && !valueFormType.equals(questionFormType);
    }
}
