package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;

/**
 * Factor for the {@link BlockConfigurator} specific to each {@link
 * Question} type. E.g., text questions, radio questions, etc.
 */
public interface BlockConfiguratorFactory {
    TextQuestionConfigurator createTextQuestion(Question question);

    MultiSelectQuestionConfigurator createMultiSelectQuestion(Question question);

    DateQuestionConfigurator createDateQuestion(Question question);

    AttachmentQuestionConfigurator createAttachmentQuestion(Question question);

    ContactConfigurator createContactConfigurator(ContactBlock contactBlock);

    TextContentConfigurator createTextContent(Content content);

    TextImageConfigurator createTextImageConfigurator(TextImageBlock block);

    ImageConfigurator createImageContent(ImageBlock image);

    VideoConfigurator createVideoContent(MediaBlock video);

    AudioConfigurator createAudioContent(MediaBlock audio);

    DocumentConfigurator createDocumentContent(DocumentBlock document);

    CollectionStoryConfigurator createCollection(Content content);

    SubmitBlockConfigurator createSubmitConfigurator(SubmitBlock submitBlock);

    RatingQuestionConfigurator createRatingConfigurator(RatingQuestion question);
}
