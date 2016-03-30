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
    private BlockType renderType;
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
        renderType = value.getRenderType();

        BlockType blockType = value.getBlockType();
        setShowDuplicate(blockType.isCustom() 
			 || BlockType.RATING_STARS.equals(blockType)
			 || BlockType.RATING_NUMBERS.equals(blockType));

        questionConfigurator.setDoneCallback(new Callback<Question>() {
            @Override
            public void onSuccess(Question question) {
                if (renderTypeChanged(question)) {
                    preview = elementFactory.create(question);
                    setPreviewView(preview);
                    renderType = question.getRenderType();
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

    private boolean renderTypeChanged(Question question) {
        BlockType newRenderType = question.getRenderType();

        return !renderType.equals(newRenderType);
    }
}
