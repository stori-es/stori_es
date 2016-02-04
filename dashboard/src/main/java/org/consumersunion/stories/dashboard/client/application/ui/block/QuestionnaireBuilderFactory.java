package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.client.ui.block.BlockElement;
import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;

import com.google.inject.assistedinject.Assisted;

public interface QuestionnaireBuilderFactory {
    QuestionBlockBuilder createQuestionBuilder(
            QuestionElement<Question> questionElement,
            BlockConfigurator<Question> configurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly);

    ContactBlockBuilder createContactBuilder(
            QuestionElement<ContactBlock> questionElement,
            BlockConfigurator<ContactBlock> configurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly);

    ContentBlockBuilder createContentBuilder(
            ContentElement<Content> contentElement,
            BlockConfigurator<Content> configurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("showDuplicate") boolean showDuplicate,
            @Assisted("showRemove") boolean showRemove,
            @Assisted("readOnly") boolean readOnly);

    TextImageBlockBuilder createTextImageBuilder(
            ContentElement<TextImageBlock> element,
            BlockConfigurator<TextImageBlock> configurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("showDuplicate") boolean showDuplicate,
            @Assisted("showRemove") boolean showRemove,
            @Assisted("readOnly") boolean readOnly);

    ImageBlockBuilder createImageBuilder(
            ContentElement<ImageBlock> contentElement,
            BlockConfigurator<ImageBlock> configurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly);

    MediaBlockBuilder createMediaBuilder(
            ContentElement<MediaBlock> contentElement,
            BlockConfigurator<MediaBlock> configurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly);

    DocumentBlockBuilder createDocumentBuilder(
            ContentElement<DocumentBlock> contentElement,
            BlockConfigurator<DocumentBlock> configurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly);

    SubmitBlockBuilder createSubmitBuilder(
            @Assisted BlockElement<SubmitBlock> submitBlockWidget,
            @Assisted BlockConfigurator<SubmitBlock> submitBlockConfigurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly);
}
