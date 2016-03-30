package org.consumersunion.stories.common.client.ui.block;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;

import static org.consumersunion.stories.common.shared.model.document.BlockType.STORY_TITLE;

public class QuestionElementFactory {
    private final ElementFactory elementFactory;

    @Inject
    QuestionElementFactory(
            ElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public QuestionElement create(Block element) {
        BlockType blockType = element.getBlockType();

        if (blockType == STORY_TITLE) {
            // This is special case because it's 'required'.
            return elementFactory.createTextQuestion((Question) element, true);
        } else {
            switch (blockType.getRenderType()) {
                case RICH_TEXT_AREA:
                    return elementFactory.createRichTextQuestion((Question) element);
                case TEXT_INPUT:
                    return elementFactory.createTextQuestion((Question) element, false);
                case TEXT_AREA:
                    return elementFactory.createTextAreaQuestion((Question) element);
                case SELECT:
                    return elementFactory.createSelectQuestion((Question) element);
                case RADIO:
                    return elementFactory.createRadioQuestion((Question) element);
                case CHECKBOX:
                    return elementFactory.createCheckBoxQuestion((Question) element);
                case DATE:
                    return elementFactory.createDateQuestion((Question) element);
                case CONTACT:
                    return elementFactory.createContactQuestion((ContactBlock) element);
                case ATTACHMENTS:
                    return elementFactory.createAttachmentQuestion((Question) element);
                case RATING_STARS:
                case RATING_NUMBERS:
                    return elementFactory.createRatingQuestion((RatingQuestion) element);
                default: // We basically try factories till we find one
                    // that satisfies, so unknown types are expected.
                    return null;
            }
        }
    }
}
