package org.consumersunion.stories.common.client.ui.block;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;

import static org.consumersunion.stories.common.shared.model.document.BlockType.ATTACHMENTS;
import static org.consumersunion.stories.common.shared.model.document.BlockType.CHECKBOX;
import static org.consumersunion.stories.common.shared.model.document.BlockType.CONTACT;
import static org.consumersunion.stories.common.shared.model.document.BlockType.DATE;
import static org.consumersunion.stories.common.shared.model.document.BlockType.RADIO;
import static org.consumersunion.stories.common.shared.model.document.BlockType.RATING;
import static org.consumersunion.stories.common.shared.model.document.BlockType.RICH_TEXT_AREA;
import static org.consumersunion.stories.common.shared.model.document.BlockType.SELECT;
import static org.consumersunion.stories.common.shared.model.document.BlockType.STORY_ASK;
import static org.consumersunion.stories.common.shared.model.document.BlockType.STORY_TITLE;
import static org.consumersunion.stories.common.shared.model.document.BlockType.TEXT_AREA;
import static org.consumersunion.stories.common.shared.model.document.BlockType.TEXT_INPUT;

public class QuestionElementFactory {
    private final ElementFactory elementFactory;

    @Inject
    QuestionElementFactory(
            ElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public QuestionElement create(Block element) {
        BlockType standardMeaning = element.getStandardMeaning();
        BlockType formType = element.getFormType();

        if (STORY_ASK.equals(standardMeaning)
                && RICH_TEXT_AREA.equals(formType)) {
            return elementFactory.createRichTextQuestion((Question) element);
        } else if (STORY_ASK.equals(standardMeaning)) {
            return elementFactory.createTextAreaQuestion((Question) element);
        } else if (STORY_TITLE.equals(standardMeaning)) {
            return elementFactory.createTextQuestion((Question) element, true);
        } else if (TEXT_INPUT.equals(formType)) {
            return elementFactory.createTextQuestion((Question) element, false);
        } else if (TEXT_AREA.equals(formType)) {
            return elementFactory.createTextAreaQuestion((Question) element);
        } else if (RICH_TEXT_AREA.equals(formType)) {
            return elementFactory.createRichTextQuestion((Question) element);
        } else if (SELECT.equals(formType)) {
            return elementFactory.createSelectQuestion((Question) element);
        } else if (RADIO.equals(formType)) {
            return elementFactory.createRadioQuestion((Question) element);
        } else if (CHECKBOX.equals(formType)) {
            return elementFactory.createCheckBoxQuestion((Question) element);
        } else if (DATE.equals(formType)) {
            return elementFactory.createDateQuestion((Question) element);
        } else if (CONTACT.equals(formType)) {
            return elementFactory.createContactQuestion((ContactBlock) element);
        } else if (ATTACHMENTS.equals(formType)) {
            return elementFactory.createAttachmentQuestion((Question) element);
        } else if (RATING.equals(standardMeaning)) {
            return elementFactory.createRatingQuestion((RatingQuestion) element);
        } else {
            return null;
        }
    }
}
