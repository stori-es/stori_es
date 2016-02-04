package org.consumersunion.stories.dashboard.client.application.ui.block;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.ui.block.ElementFactory;
import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.client.ui.block.SubmitBlockWidget;
import org.consumersunion.stories.common.client.ui.block.question.RatingQuestionWidget;
import org.consumersunion.stories.common.shared.model.HasBlocks;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;
import org.consumersunion.stories.common.shared.model.type.ContactType;
import org.consumersunion.stories.common.shared.model.type.DataType;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfiguratorFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Multimap;

public class BlockBuilderFactory {
    private final BlockLabelHelper blockLabelHelper;
    private final StandardQuestionFactory questionFactory;
    private final ElementFactory elementFactory;
    private final QuestionnaireBuilderFactory builderFactory;
    private final BlockConfiguratorFactory configuratorFactory;

    @Inject
    BlockBuilderFactory(
            BlockLabelHelper blockLabelHelper,
            StandardQuestionFactory questionFactory,
            ElementFactory elementFactory,
            QuestionnaireBuilderFactory builderFactory,
            BlockConfiguratorFactory configuratorFactory) {
        this.blockLabelHelper = blockLabelHelper;
        this.questionFactory = questionFactory;
        this.elementFactory = elementFactory;
        this.builderFactory = builderFactory;
        this.configuratorFactory = configuratorFactory;
    }

    public BlockBuilder createNewCustomBlock(
            HasBlocks hasBlocks,
            BlockType type,
            boolean readOnly) {

        Block block;
        if (type == BlockType.SUBHEADER
                || type == BlockType.COLLECTION
                || type == BlockType.CONTENT
                || type == BlockType.STORY) {
            block = new Content();
        } else if (type == BlockType.IMAGE) {
            block = new ImageBlock();
        } else if (type == BlockType.AUDIO
                || type == BlockType.VIDEO) {
            block = new MediaBlock();
        } else if (type == BlockType.DOCUMENT) {
            block = new DocumentBlock();
        } else if (type == BlockType.TEXT_IMAGE) {
            block = new TextImageBlock();
        } else {
            Question question = new Question();
            question.setRequired(BlockType.isRequired(type));
            question.setLabel(blockLabelHelper.getUniqueLabel(hasBlocks));

            block = question;
        }

        block.setFormType(type);

        return create(block, readOnly);
    }

    public BlockBuilder createNewStandardBlock(
            HasBlocks hasBlocks,
            Multimap<BlockType, BlockBuilder> standardElements,
            BlockType blockType,
            BlockType formType,
            boolean readOnly) {
        Block block;
        if (blockType == BlockType.EMAIL) {
            block = createNewContact(hasBlocks, standardElements, DataType.DATA_EMAIL, blockType,
                    BlockType.emailElements(), BlockType.EMAIL_OTHER);
        } else if (blockType == BlockType.PHONE) {
            block = createNewContact(hasBlocks, standardElements, DataType.DATA_PHONE_NUMBER, blockType,
                    BlockType.phoneElements(), BlockType.PHONE_OTHER);
        } else if (blockType == BlockType.RATING) {
            block = new RatingQuestion();
            block.setFormType(formType);
            ((RatingQuestion) block).setLabel(blockLabelHelper.getUniqueLabel(hasBlocks));
        } else {
            block = questionFactory.createStandardQuestion(blockType, formType);
        }

        return create(block, readOnly);
    }

    public BlockBuilder create(Block element, boolean readOnly) {
        BlockBuilder blockBuilder = null;
        if (BlockType.STORY_ASK.equals(element.getStandardMeaning())
                && BlockType.RICH_TEXT_AREA.equals(element.getFormType())) {
            QuestionElement<Question> questionElement = elementFactory.createRichTextQuestion((Question) element);
            blockBuilder = addTextQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.STORY_ASK.equals(element.getStandardMeaning())) {
            QuestionElement<Question> questionElement = elementFactory.createTextAreaQuestion((Question) element);
            blockBuilder = addTextQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.STORY_TITLE.equals(element.getStandardMeaning())) {
            QuestionElement<Question> questionElement = elementFactory.createTextQuestion((Question) element, true);
            blockBuilder = addTextQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.TEXT_INPUT.equals(element.getFormType())) {
            QuestionElement<Question> questionElement = elementFactory.createTextQuestion((Question) element, false);
            blockBuilder = addTextQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.TEXT_AREA.equals(element.getFormType())) {
            QuestionElement<Question> questionElement = elementFactory.createTextAreaQuestion((Question) element);
            blockBuilder = addTextQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.RICH_TEXT_AREA.equals(element.getFormType())) {
            QuestionElement<Question> questionElement = elementFactory.createRichTextQuestion((Question) element);
            blockBuilder = addTextQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.SELECT.equals(element.getFormType())) {
            QuestionElement<Question> questionElement = elementFactory.createSelectQuestion((Question) element);
            blockBuilder = addMultipleChoiceQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.RADIO.equals(element.getFormType())) {
            QuestionElement<Question> questionElement = elementFactory.createRadioQuestion((Question) element);
            blockBuilder = addMultipleChoiceQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.CHECKBOX.equals(element.getFormType())) {
            QuestionElement<Question> questionElement = elementFactory.createCheckBoxQuestion((Question) element);
            blockBuilder = addMultipleChoiceQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.SUBHEADER.equals(element.getFormType())) {
            ContentElement<Content> contentElement = elementFactory.createHeader((Content) element);
            blockBuilder = addContentBuilder((Content) element, contentElement, readOnly);
        } else if (BlockType.TEXT_IMAGE.equals(element.getFormType())) {
            ContentElement<TextImageBlock> contentElement = elementFactory.createTextImage((TextImageBlock) element);
            blockBuilder = addTextImageBuilder((TextImageBlock) element, contentElement, readOnly);
        } else if (BlockType.CONTENT.equals(element.getFormType())) {
            ContentElement<Content> contentElement = elementFactory.createText((Content) element);
            blockBuilder = addContentBuilder((Content) element, contentElement, readOnly);
        } else if (BlockType.CUSTOM_PERMISSIONS.equals(element.getStandardMeaning())) {
            ContentElement<Content> contentElement = elementFactory.createText((Content) element);
            blockBuilder = addContentBuilder((Content) element, contentElement, readOnly);
        } else if (BlockType.DATE.equals(element.getFormType())) {
            QuestionElement<Question> questionElement = elementFactory.createDateQuestion((Question) element);
            blockBuilder = addDateQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.IMAGE.equals(element.getFormType())) {
            ContentElement<ImageBlock> contentElement = elementFactory.createImage((ImageBlock) element);
            blockBuilder = addImageBuilder((ImageBlock) element, contentElement, readOnly);
        } else if (BlockType.VIDEO.equals(element.getFormType())) {
            ContentElement<MediaBlock> contentElement = elementFactory.createVideo((MediaBlock) element);
            blockBuilder = addVideoBuilder((MediaBlock) element, contentElement, readOnly);
        } else if (BlockType.AUDIO.equals(element.getFormType())) {
            ContentElement<MediaBlock> contentElement = elementFactory.createAudio((MediaBlock) element);
            blockBuilder = addAudioBuilder((MediaBlock) element, contentElement, readOnly);
        } else if (BlockType.DOCUMENT.equals(element.getFormType())) {
            ContentElement<DocumentBlock> contentElement = elementFactory.createDocument((DocumentBlock) element);
            blockBuilder = addDocumentBuilder((DocumentBlock) element, contentElement, readOnly);
        } else if (BlockType.CONTACT.equals(element.getFormType())) {
            QuestionElement<ContactBlock> questionElement =
                    elementFactory.createContactQuestion((ContactBlock) element);
            blockBuilder = addContactQuestionBuilder((ContactBlock) element, questionElement, readOnly);
        } else if (BlockType.COLLECTION.equals(element.getFormType())) {
            ContentElement<Content> questionElement = elementFactory.createCollection((Content) element);
            blockBuilder = addCollectionStoryBuilder((Content) element, questionElement, readOnly);
        } else if (BlockType.STORY.equals(element.getFormType())) {
            ContentElement<Content> questionElement = elementFactory.createStory((Content) element);
            blockBuilder = addCollectionStoryBuilder((Content) element, questionElement, readOnly);
        } else if (BlockType.ATTACHMENTS.equals(element.getFormType())) {
            QuestionElement<Question> questionElement = elementFactory.createAttachmentQuestion((Question) element);
            blockBuilder = addAttachmentQuestionBuilder((Question) element, questionElement, readOnly);
        } else if (BlockType.SUBMIT.equals(element.getFormType())) {
            SubmitBlockWidget blockWidget = elementFactory.createSubmitButton((SubmitBlock) element);
            blockBuilder = addSubmitButton((SubmitBlock) element, blockWidget, readOnly);
        } else if (BlockType.RATING.equals(element.getStandardMeaning())) {
            RatingQuestionWidget blockWidget = elementFactory.createRatingQuestion((RatingQuestion) element);
            blockBuilder = addRatingQuestion((RatingQuestion) element, blockWidget, readOnly);
        }

        return blockBuilder;
    }

    private BlockBuilder addRatingQuestion(RatingQuestion element, RatingQuestionWidget blockWidget, boolean readOnly) {
        BlockConfigurator<RatingQuestion> configurator = configuratorFactory.createRatingConfigurator(element);

        return builderFactory.createQuestionBuilder((QuestionElement) blockWidget, (BlockConfigurator) configurator,
                false, readOnly);
    }

    private BlockBuilder addSubmitButton(SubmitBlock element, SubmitBlockWidget blockWidget, boolean readOnly) {
        BlockConfigurator<SubmitBlock> configurator = configuratorFactory.createSubmitConfigurator(element);

        return builderFactory.createSubmitBuilder(blockWidget, configurator, false, readOnly);
    }

    private BlockBuilder addTextQuestionBuilder(
            Question question,
            QuestionElement<Question> questionElement,
            boolean readOnly) {

        boolean isEdit = Strings.isNullOrEmpty(question.getText());

        BlockConfigurator<Question> configurator = configuratorFactory.createTextQuestion(question);
        return builderFactory.createQuestionBuilder(questionElement, configurator, isEdit, readOnly);
    }

    private BlockBuilder addMultipleChoiceQuestionBuilder(
            Question question,
            QuestionElement<Question> questionElement,
            boolean readOnly) {
        boolean isEdit = Strings.isNullOrEmpty(question.getText());

        BlockConfigurator<Question> configurator = configuratorFactory.createMultiSelectQuestion(question);
        return builderFactory.createQuestionBuilder(questionElement, configurator, isEdit, readOnly);
    }

    private BlockBuilder addDateQuestionBuilder(Question question, QuestionElement<Question> questionElement,
            boolean readOnly) {
        boolean isEdit = Strings.isNullOrEmpty(question.getText());

        BlockConfigurator<Question> configurator = configuratorFactory.createDateQuestion(question);
        return builderFactory.createQuestionBuilder(questionElement, configurator, isEdit, readOnly);
    }

    private BlockBuilder addAttachmentQuestionBuilder(Question question, QuestionElement<Question> questionElement,
            boolean readOnly) {
        boolean isEdit = Strings.isNullOrEmpty(question.getText());

        BlockConfigurator<Question> configurator = configuratorFactory.createAttachmentQuestion(question);
        return builderFactory.createQuestionBuilder(questionElement, configurator, isEdit, readOnly);
    }

    private BlockBuilder addContactQuestionBuilder(ContactBlock contactBlock,
            QuestionElement<ContactBlock> questionElement, boolean readOnly) {
        boolean isEdit = Strings.isNullOrEmpty(contactBlock.getText());

        BlockConfigurator<ContactBlock> configurator = configuratorFactory.createContactConfigurator(contactBlock);
        return builderFactory.createContactBuilder(questionElement, configurator, isEdit, readOnly);
    }

    private BlockBuilder addContentBuilder(
            Content content,
            ContentElement<Content> contentElement,
            boolean readOnly) {
        boolean isEdit = Strings.isNullOrEmpty(content.getContent());
        boolean canDuplicate = !BlockType.CUSTOM_PERMISSIONS.equals(content.getStandardMeaning());
        boolean canRemove = !BlockType.CUSTOM_PERMISSIONS.equals(content.getStandardMeaning());

        BlockConfigurator<Content> configurator = configuratorFactory.createTextContent(content);
        return builderFactory.createContentBuilder(contentElement, configurator, isEdit,
                canDuplicate, canRemove, readOnly);
    }

    private BlockBuilder addTextImageBuilder(
            TextImageBlock block,
            ContentElement<TextImageBlock> contentElement,
            boolean readOnly) {
        boolean isEdit = Strings.isNullOrEmpty(block.getText());
        boolean canDuplicate = !BlockType.CUSTOM_PERMISSIONS.equals(block.getStandardMeaning());
        boolean canRemove = !BlockType.CUSTOM_PERMISSIONS.equals(block.getStandardMeaning());

        BlockConfigurator<TextImageBlock> configurator = configuratorFactory.createTextImageConfigurator(block);
        return builderFactory.createTextImageBuilder(contentElement, configurator, isEdit,
                canDuplicate, canRemove, readOnly);
    }

    private BlockBuilder addCollectionStoryBuilder(
            Content content,
            ContentElement<Content> contentElement,
            boolean readOnly) {

        BlockConfigurator<Content> configurator = configuratorFactory.createCollection(content);

        return builderFactory.createContentBuilder(contentElement, configurator,
                Strings.isNullOrEmpty(content.getContent()), true, true, readOnly);
    }

    private BlockBuilder addImageBuilder(
            ImageBlock image,
            ContentElement<ImageBlock> contentElement,
            boolean readOnly) {

        BlockConfigurator<ImageBlock> configurator = configuratorFactory.createImageContent(image);

        return builderFactory.createImageBuilder(contentElement, configurator, !image.hasImage(), readOnly);
    }

    private BlockBuilder addVideoBuilder(
            MediaBlock video,
            ContentElement<MediaBlock> contentElement,
            boolean readOnly) {
        BlockConfigurator<MediaBlock> configurator = configuratorFactory.createVideoContent(video);

        return addMediaBuilder(video, contentElement, configurator, readOnly);
    }

    private BlockBuilder addAudioBuilder(
            MediaBlock audio,
            ContentElement<MediaBlock> contentElement,
            boolean readOnly) {

        BlockConfigurator<MediaBlock> configurator = configuratorFactory.createAudioContent(audio);

        return addMediaBuilder(audio, contentElement, configurator, readOnly);
    }

    private BlockBuilder addMediaBuilder(
            MediaBlock media,
            ContentElement<MediaBlock> contentElement,
            BlockConfigurator<MediaBlock> configurator,
            boolean readOnly) {

        boolean isEdit = Strings.isNullOrEmpty(media.getUrl());

        return builderFactory.createMediaBuilder(contentElement, configurator, isEdit, readOnly);
    }

    private BlockBuilder addDocumentBuilder(
            DocumentBlock document,
            ContentElement<DocumentBlock> contentElement,
            boolean readOnly) {
        boolean isEdit = Strings.isNullOrEmpty(document.getUrl());

        BlockConfigurator<DocumentBlock> configurator = configuratorFactory.createDocumentContent(document);
        return builderFactory.createDocumentBuilder(contentElement, configurator, isEdit, readOnly);
    }

    private ContactBlock createNewContact(
            HasBlocks hasBlocks,
            Multimap<BlockType, BlockBuilder> standardElements,
            DataType dataType,
            BlockType blockType,
            List<BlockType> validElements,
            BlockType defaultType) {
        ContactBlock contactBlock = new ContactBlock();
        contactBlock.setDataType(dataType.code());
        contactBlock.setFormType(BlockType.CONTACT);
        contactBlock.setRequired(BlockType.isRequired(blockType));

        BlockType standardType = defaultType;
        for (BlockType element : validElements) {
            if (!standardElements.containsKey(element)) {
                standardType = element;
                break;
            }
        }

        contactBlock.setStandardMeaning(standardType);
        contactBlock.setLabel(dataType.code() + blockLabelHelper.getUniqueId(hasBlocks, dataType));
        contactBlock.setOption(ContactType.fromBlockType(standardType).code());

        return contactBlock;
    }
}
