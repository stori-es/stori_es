package org.consumersunion.stories.common.client.util;

import java.util.Map;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;
import org.consumersunion.stories.common.shared.service.datatransferobject.AnswerSetSummary;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;

public class StoriesJsonEncoderDecoderImpl implements StoriesJsonEncoderDecoder {
    interface CollectionDataCodec extends JsonEncoderDecoder<CollectionData> {
    }

    interface StorySummaryCodec extends JsonEncoderDecoder<StorySummary> {
    }

    interface DocumentCodec extends JsonEncoderDecoder<Document> {
    }

    interface AnswerSetSummaryCodec extends JsonEncoderDecoder<AnswerSetSummary> {
    }

    interface QuestionCodec extends JsonEncoderDecoder<Question> {
    }

    interface MediaBlockCodec extends JsonEncoderDecoder<MediaBlock> {
    }

    interface ImageBlockCodec extends JsonEncoderDecoder<ImageBlock> {
    }

    interface DocumentBlockCodec extends JsonEncoderDecoder<DocumentBlock> {
    }

    interface ContactBlockCodec extends JsonEncoderDecoder<ContactBlock> {
    }

    interface ContentBlockCodec extends JsonEncoderDecoder<Content> {
    }

    interface SubmitBlockCodec extends JsonEncoderDecoder<SubmitBlock> {
    }

    interface RatingQuestionCodec extends JsonEncoderDecoder<RatingQuestion> {
    }

    interface TextImageBlockCodec extends JsonEncoderDecoder<TextImageBlock> {
    }

    private final Map<Class<?>, JsonEncoderDecoder> codecs = Maps.newHashMap();

    @Inject
    StoriesJsonEncoderDecoderImpl(
            StorySummaryCodec storySummaryCodec,
            DocumentCodec documentCodec,
            CollectionDataCodec collectionDataCodec,
            AnswerSetSummaryCodec answerSetSummaryCodec,
            QuestionCodec questionCodec,
            MediaBlockCodec mediaBlockCodec,
            ImageBlockCodec imageBlockCodec,
            DocumentBlockCodec documentBlockCodec,
            ContactBlockCodec contactBlockCodec,
            ContentBlockCodec contentBlockCodec,
            SubmitBlockCodec submitBlockCodec,
            RatingQuestionCodec ratingQuestionCodec,
            TextImageBlockCodec textImageBlockCodec) {
        codecs.put(StorySummary.class, storySummaryCodec);
        codecs.put(Document.class, documentCodec);
        codecs.put(CollectionData.class, collectionDataCodec);
        codecs.put(AnswerSetSummary.class, answerSetSummaryCodec);
        codecs.put(Question.class, questionCodec);
        codecs.put(MediaBlock.class, mediaBlockCodec);
        codecs.put(ImageBlock.class, imageBlockCodec);
        codecs.put(DocumentBlock.class, documentBlockCodec);
        codecs.put(ContactBlock.class, contactBlockCodec);
        codecs.put(Content.class, contentBlockCodec);
        codecs.put(SubmitBlock.class, submitBlockCodec);
        codecs.put(RatingQuestion.class, ratingQuestionCodec);
        codecs.put(TextImageBlock.class, textImageBlockCodec);
    }

    @Override
    public <T> String toJson(Class<T> clazz, T object) {
        return getJsonEncoderDecoder(clazz).encode(object).toString();
    }

    @Override
    public <T> T fromJson(Class<T> clazz, String json) {
        if (!Strings.isNullOrEmpty(json)) {
            return (T) getJsonEncoderDecoder(clazz).decode(new JSONObject(JsonUtils.safeEval(json)));
        } else {
            return null;
        }
    }

    @Override
    public <T> T clone(Class<T> clazz, T object) {
        return fromJson(clazz, toJson(clazz, object));
    }

    private <T> JsonEncoderDecoder getJsonEncoderDecoder(Class<T> clazz) {
        return codecs.get(clazz);
    }
}
