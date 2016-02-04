package org.consumersunion.stories.common.client.ui.block;

import org.consumersunion.stories.common.client.ui.block.content.AudioContent;
import org.consumersunion.stories.common.client.ui.block.content.CollectionContentPresenter;
import org.consumersunion.stories.common.client.ui.block.content.DocumentContent;
import org.consumersunion.stories.common.client.ui.block.content.HeaderContent;
import org.consumersunion.stories.common.client.ui.block.content.ImageContent;
import org.consumersunion.stories.common.client.ui.block.content.TextContent;
import org.consumersunion.stories.common.client.ui.block.content.TextImageContent;
import org.consumersunion.stories.common.client.ui.block.content.VideoContent;
import org.consumersunion.stories.common.client.ui.block.question.AttachmentQuestion;
import org.consumersunion.stories.common.client.ui.block.question.CheckBoxQuestion;
import org.consumersunion.stories.common.client.ui.block.question.ContactQuestion;
import org.consumersunion.stories.common.client.ui.block.question.DateQuestion;
import org.consumersunion.stories.common.client.ui.block.question.RadioQuestion;
import org.consumersunion.stories.common.client.ui.block.question.RatingQuestionWidget;
import org.consumersunion.stories.common.client.ui.block.question.RichTextQuestion;
import org.consumersunion.stories.common.client.ui.block.question.SelectQuestion;
import org.consumersunion.stories.common.client.ui.block.question.TextAreaQuestion;
import org.consumersunion.stories.common.client.ui.block.question.TextQuestion;
import org.consumersunion.stories.common.client.ui.stories.PublishedStoryCardPresenter;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;

public interface ElementFactory {
    DocumentContent createDocument(DocumentBlock documentBlock);

    HeaderContent createHeader(Content content);

    ImageContent createImage(ImageBlock imageBlock);

    VideoContent createVideo(MediaBlock video);

    AudioContent createAudio(MediaBlock audio);

    TextContent createText(Content content);

    TextImageContent createTextImage(TextImageBlock content);

    RichTextQuestion createRichTextQuestion(Question question);

    TextAreaQuestion createTextAreaQuestion(Question question);

    TextQuestion createTextQuestion(Question question, Boolean withPlaceHolder);

    SelectQuestion createSelectQuestion(Question question);

    RadioQuestion createRadioQuestion(Question question);

    CheckBoxQuestion createCheckBoxQuestion(Question question);

    DateQuestion createDateQuestion(Question question);

    AttachmentQuestion createAttachmentQuestion(Question question);

    ContactQuestion createContactQuestion(ContactBlock contactBlock);

    CollectionContentPresenter createCollection(Content content);

    PublishedStoryCardPresenter createStory(Content content);

    SubmitBlockWidget createSubmitButton(SubmitBlock submitBlock);

    RatingQuestionWidget createRatingQuestion(RatingQuestion question);
}
