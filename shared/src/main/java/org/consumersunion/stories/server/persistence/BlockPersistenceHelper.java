package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.common.server.model.questionnaire.StandardQuestionFactory;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock.NextDocument;
import org.consumersunion.stories.common.shared.model.document.TextImage;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Option;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;
import org.consumersunion.stories.i18n.CommonI18nLabels;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import net.lightoze.gwt.i18n.server.LocaleFactory;
import net.lightoze.gwt.i18n.server.LocaleProxy;

import static org.consumersunion.stories.common.shared.model.document.BlockType.AUDIO;
import static org.consumersunion.stories.common.shared.model.document.BlockType.VIDEO;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ATTACHMENT;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.CONTENT;

@Component
public class BlockPersistenceHelper {
    private static final StandardQuestionFactory stdQuestionFactory;

    static {
        LocaleProxy.initialize();
        stdQuestionFactory = new StandardQuestionFactory(LocaleFactory.get(CommonI18nLabels.class));
    }

    private final Provider<DocumentPersister> documentPersisterProvider;

    @Inject
    BlockPersistenceHelper(
            Provider<DocumentPersister> documentPersisterProvider) {
        this.documentPersisterProvider = documentPersisterProvider;
    }

    public void deleteBlocks(int id, Connection connection) throws SQLException {
        PreparedStatement deletequestionOptions = connection
                .prepareStatement("DELETE FROM question_options WHERE document=?");
        deletequestionOptions.setInt(1, id);
        deletequestionOptions.executeUpdate();

        PreparedStatement deleteContactBlock = connection
                .prepareStatement("DELETE FROM question_contact WHERE document=?");
        deleteContactBlock.setInt(1, id);
        deleteContactBlock.executeUpdate();

        PreparedStatement deletequestion = connection
                .prepareStatement("DELETE FROM block_question WHERE document=?");
        deletequestion.setInt(1, id);
        deletequestion.executeUpdate();

        PreparedStatement deleteContent = connection
                .prepareStatement("DELETE FROM block_content WHERE document=?");
        deleteContent.setInt(1, id);
        deleteContent.executeUpdate();

        PreparedStatement deleteMediaBlock = connection
                .prepareStatement("DELETE FROM block_image WHERE document=?");
        deleteMediaBlock.setInt(1, id);
        deleteMediaBlock.executeUpdate();

        PreparedStatement deleteDocumentBlock = connection
                .prepareStatement("DELETE FROM block_link WHERE document=?");
        deleteDocumentBlock.setInt(1, id);
        deleteDocumentBlock.executeUpdate();

        PreparedStatement deleteSubmit = connection
                .prepareStatement("DELETE FROM block_submit WHERE document=?");
        deleteSubmit.setInt(1, id);
        deleteSubmit.executeUpdate();

        PreparedStatement deleteRating = connection
                .prepareStatement("DELETE FROM block_rating WHERE document=?");
        deleteRating.setInt(1, id);
        deleteRating.executeUpdate();

        PreparedStatement deleteBlock = connection
                .prepareStatement("DELETE FROM block WHERE document=?");
        deleteBlock.setInt(1, id);
        deleteBlock.executeUpdate();
    }

    public void persistBlocks(int document, int version, List<Block> blocks, Connection connection)
            throws SQLException {
        if (!CollectionUtils.isEmpty(blocks)) {
            int index = 0;

            PreparedStatement insertBlock = connection
                    .prepareStatement(
                            "INSERT INTO block (document, version, idx, blockType) VALUES (?,?,?,?)");

            PreparedStatement insertQuestion = connection
                    .prepareStatement("INSERT INTO block_question (document, version, idx, dataType, "
                            + "label, required, multiselect, minLength, maxLength, startDate, text, helpText) VALUES " +
                            "(?,?,?,?,?,?,?,?,?,?,?,?)");

            PreparedStatement insertQuestionOptions = connection
                    .prepareStatement(
                            "INSERT INTO question_options (document, version, questionIdx, idx, reportValue, " +
                                    "displayValue) "
                                    + "VALUES (?,?,?,?,?,?)");

            PreparedStatement insertContentData = connection
                    .prepareStatement(
                            "INSERT INTO block_content (document, version, idx, content, textType) VALUES (?,?,?,?,?)");

            PreparedStatement insertTextImageData = connection
                    .prepareStatement(
                            "INSERT INTO block_image (document, version, idx, url, caption, alttext, position, size) " +
                                    "VALUES(?,?,?,?,?,?,?,?)");

            PreparedStatement insertMediaData = connection
                    .prepareStatement(
                            "INSERT INTO block_image (document, version, idx, url, caption, alttext) VALUES(?,?,?,?," +
                                    "?,?)");

            PreparedStatement insertDocumentData = connection
                    .prepareStatement("INSERT INTO block_link (document, version, idx, title, url) VALUES(?,?,?,?,?)");

            PreparedStatement insertContactData = connection
                    .prepareStatement(
                            "INSERT INTO question_contact (document, version, idx, opt, type) VALUES(?,?,?,?,?)");

            PreparedStatement insertSubmit = connection.prepareStatement(
                    "INSERT INTO block_submit (document, version, idx, prompt, size, position, nextDocument) VALUES " +
                            "(?,?,?,?,?,?,?)");

            PreparedStatement insertRating = connection.prepareStatement(
                    "INSERT INTO block_rating (document, version, idx, steps, start, end) VALUES (?, ?, ?, ?, ?, ?)");

            for (Block element : blocks) {
                insertBlock.setInt(1, document);
                insertBlock.setInt(2, version);
                insertBlock.setInt(3, index);
                insertBlock.setString(4, element.getBlockType().code());
                insertBlock.addBatch();

                if (element instanceof Content) {
                    insertContentData(document, version, index, insertContentData, (Content) element);
                } else if (element instanceof TextImageBlock) {
                    insertTextImageData(document, version, index, insertTextImageData, insertContentData,
                            (TextImageBlock) element);
                } else if (element instanceof ImageBlock) {
                    insertImageData(document, version, index, insertMediaData, (ImageBlock) element);
                } else if (element instanceof MediaBlock) {
                    insertMediaData(document, version, index, insertMediaData, (MediaBlock) element);
                } else if (element instanceof DocumentBlock) {
                    insertDocumentData(document, version, index, insertDocumentData, (DocumentBlock) element);
                } else if (element instanceof SubmitBlock) {
                    insertSubmitBlock(insertSubmit, document, version, index, (SubmitBlock) element);
                } else if (element instanceof Question) {
                    insertQuestionData(document, version, index, insertQuestion, insertQuestionOptions,
                            (Question) element);
                    if (element instanceof ContactBlock) {
                        insertContactData(document, version, index, insertContactData, (ContactBlock) element);
                    } else if (element instanceof RatingQuestion) {
                        insertRatingQuestion(document, version, index, insertRating, (RatingQuestion) element);
                    }
                }
                index++;
            }

            insertBlock.executeBatch();
            insertContentData.executeBatch();
            insertMediaData.executeBatch();
            insertDocumentData.executeBatch();
            insertTextImageData.executeBatch();

            insertQuestion.executeBatch();
            insertQuestionOptions.executeBatch();
            insertRating.executeBatch();
            insertContactData.executeBatch();
            insertSubmit.executeBatch();
        }
    }

    public ArrayList<Block> getBlocks(Document document, Connection conn) throws SQLException {
        PreparedStatement retrieveBlocks =
                conn.prepareStatement(
                        "SELECT b.document, b.version, b.idx, b.blockType "
                                + "FROM block b WHERE b.document=? AND b.version=? ORDER BY b.idx");
        retrieveBlocks.setInt(1, document.getId());
        retrieveBlocks.setInt(2, document.getVersion());
        // we will then retrieve the internationalized text and options
        // separately; this is inefficient in so
        // far as we're doing a select for each individual question, but the
        // i15d questions are more rarely
        // accessed, typically in a management / administrative role. Single
        // locale is by far the more common
        // access mode, which is handled in a separate persister in order to
        // support the select optimization

        PreparedStatement retrieveContentData = conn
                .prepareStatement(
                        "SELECT content, textType FROM block_content WHERE document=? AND version=? AND idx=?");
        PreparedStatement retrieveTextImageData = conn
                .prepareStatement(
                        "SELECT url, caption, altText, position, size FROM block_image WHERE document=? AND version=?" +
                                " AND idx=?");
        PreparedStatement retrieveMediaData =
                conn.prepareStatement(
                        "SELECT url, caption, alttext FROM block_image WHERE document=? AND version=? AND idx=?");
        PreparedStatement retrieveDocumentData =
                conn.prepareStatement(
                        "SELECT title, url FROM block_link WHERE document=? AND version=? AND idx=?");
        PreparedStatement retrieveContactData =
                conn.prepareStatement(
                        "SELECT opt, type FROM question_contact WHERE document=? AND version=? AND idx=?");
        PreparedStatement retrieveQuestionData =
                conn.prepareStatement(
                        "SELECT q.dataType, q.label, q.required, q.multiselect, q.minLength, q.maxLength, "
                                + "q.startDate, q.text, q.helpText " + "FROM block_question q "
                                + "WHERE q.document=? AND version=? AND q.idx=?");
        PreparedStatement retrieveOptions =
                conn.prepareStatement("SELECT displayValue, reportValue FROM question_options o "
                        + "WHERE o.document=? AND version=? AND o.questionIdx=? ORDER BY o.idx");
        PreparedStatement retrieveSubmitBlock = conn.prepareStatement(
                "SELECT prompt, size, position, nextDocument FROM block_submit " +
                        "WHERE document=? AND version=? AND idx=?");

        PreparedStatement retrieveRatingBlock = conn.prepareStatement(
                "SELECT steps, start, end FROM block_rating " +
                        "WHERE document=? AND version=? AND idx=?");

        ArrayList<Block> blocks = new ArrayList<Block>();
        ResultSet resultsBlocks = retrieveBlocks.executeQuery();

        while (resultsBlocks.next()) {
            blocks.add(getBlock(retrieveContentData,
                    retrieveTextImageData,
                    retrieveMediaData,
                    retrieveDocumentData,
                    retrieveContactData,
                    retrieveQuestionData,
                    retrieveOptions,
                    retrieveSubmitBlock,
                    retrieveRatingBlock,
                    resultsBlocks,
                    document,
                    conn));
        }

        return blocks;
    }

    private Block getBlock(
            PreparedStatement retrieveContentData,
            PreparedStatement retrieveTextImageData,
            PreparedStatement retrieveMediaData,
            PreparedStatement retrieveDocumentData,
            PreparedStatement retrieveContactData,
            PreparedStatement retrieveQuestionData,
            PreparedStatement retrieveOptions,
            PreparedStatement retrieveSubmitBlock,
            PreparedStatement retrieveRatingBlock,
            ResultSet resultsBlocks,
            Document document,
            Connection conn)
            throws SQLException {
        String documentType = resultsBlocks.getString(4);

        // is it content?
        if (BlockType.COLLECTION.code().equals(documentType)
                || BlockType.STORY.code().equals(documentType)
                || BlockType.PLAIN_TEXT.code().equals(documentType)
                || BlockType.CUSTOM_PERMISSIONS.code().equals(documentType)
                || BlockType.CONTENT.code().equals(documentType)
                || BlockType.SUBHEADER.code().equals(documentType)) {
            Content content = new Content();
            setBlockAttributes(content, resultsBlocks);

            retrieveContentData.setInt(1, content.getDocument());
            retrieveContentData.setInt(2, content.getVersion());
            retrieveContentData.setInt(3, content.getIdx());

            ResultSet dataResults = retrieveContentData.executeQuery();
            if (dataResults.next()) {
                content.setContent(dataResults.getString(1));
                String textType = dataResults.getString(2);
                if (dataResults.wasNull()) {
                    content.setTextType(null);
                } else {
                    content.setTextType(TextType.valueOf(textType));
                }
            }

            return content;
        } else if (BlockType.TEXT_IMAGE.code().equals(documentType)) {
            TextImageBlock block = new TextImageBlock();
            setBlockAttributes(block, resultsBlocks);

            retrieveContentData.setInt(1, block.getDocument());
            retrieveContentData.setInt(2, block.getVersion());
            retrieveContentData.setInt(3, block.getIdx());

            ResultSet dataResults = retrieveContentData.executeQuery();

            if (dataResults.next()) {
                block.setText(dataResults.getString(1));

                retrieveTextImageData.setInt(1, block.getDocument());
                retrieveTextImageData.setInt(2, block.getVersion());
                retrieveTextImageData.setInt(3, block.getIdx());

                ResultSet imageResults = retrieveTextImageData.executeQuery();

                if (imageResults.next()) {
                    TextImage textImage = new TextImage();

                    String url = imageResults.getString(1);
                    String caption = imageResults.getString(2);
                    String altText = imageResults.getString(3);
                    String position = imageResults.getString(4);
                    String size = imageResults.getString(5);

                    textImage.setUrl(url);
                    textImage.setCaption(caption);
                    textImage.setAltText(altText);
                    textImage.setPosition(TextImage.Position.fromCode(position));
                    textImage.setSize(TextImage.Size.fromCode(size));

                    if (!Strings.isNullOrEmpty(url)) {
                        block.setImage(textImage);
                    }
                }
            }

            return block;
        } else if (BlockType.IMAGE.code().equals(documentType)) {
            ImageBlock imageContent = new ImageBlock();
            setBlockAttributes(imageContent, resultsBlocks);

            retrieveMediaData.setInt(1, imageContent.getDocument());
            retrieveMediaData.setInt(2, imageContent.getVersion());
            retrieveMediaData.setInt(3, imageContent.getIdx());
            ResultSet dataResults = retrieveMediaData.executeQuery();

            while (dataResults.next()) {
                imageContent.setUrl(dataResults.getString(1));
                imageContent.setCaption(dataResults.getString(2));
                imageContent.setAltText(dataResults.getString(3));
            }

            return imageContent;
        } else if (VIDEO.code().equals(documentType) || AUDIO.code().equals(documentType)) {
            MediaBlock mediaBlock = new MediaBlock();
            setBlockAttributes(mediaBlock, resultsBlocks);

            retrieveMediaData.setInt(1, mediaBlock.getDocument());
            retrieveMediaData.setInt(2, mediaBlock.getVersion());
            retrieveMediaData.setInt(3, mediaBlock.getIdx());

            ResultSet dataResults = retrieveMediaData.executeQuery();
            while (dataResults.next()) {
                mediaBlock.setUrl(dataResults.getString(1));
                mediaBlock.setDescription(dataResults.getString(2));
                // For audio 'alttext' is mapped to 'title'.
                mediaBlock.setTitle(dataResults.getString(3));
            }

            return mediaBlock;
        } else if (BlockType.DOCUMENT.code().equals(documentType)) {
            DocumentBlock documentBlock = new DocumentBlock();
            setBlockAttributes(documentBlock, resultsBlocks);

            retrieveDocumentData.setInt(1, documentBlock.getDocument());
            retrieveDocumentData.setInt(2, documentBlock.getVersion());
            retrieveDocumentData.setInt(3, documentBlock.getIdx());
            ResultSet dataResults = retrieveDocumentData.executeQuery();

            while (dataResults.next()) {
                documentBlock.setTitle(dataResults.getString(1));
                documentBlock.setUrl(dataResults.getString(2));
            }

            return documentBlock;
        } else if (BlockType.SUBMIT.code().equals(documentType)) {
            return createSubmitBlock(retrieveSubmitBlock, resultsBlocks, document, conn);
        } else {
            Question question;
            if (BlockType.isContact(documentType)) {
                question = new ContactBlock();
            } else if (BlockType.RATING_STARS.code().equals(documentType) ||
		       BlockType.RATING_NUMBERS.code().equals(documentType)) {
                question = new RatingQuestion();
            } else {
                question = new Question();
            }

            setBlockAttributes(question, resultsBlocks);
            // grab the question specific data
            retrieveQuestionData.setInt(1, question.getDocument());
            retrieveQuestionData.setInt(2, question.getVersion());
            retrieveQuestionData.setInt(3, question.getIdx());
            // deal with the general question attributes
            ResultSet questionResults = retrieveQuestionData.executeQuery();
            if (questionResults.next()) {
                question.setDataType(questionResults.getString(1));
                question.setLabel(questionResults.getString(2));
                question.setRequired(questionResults.getBoolean(3));
                question.setMultiselect(questionResults.getBoolean(4));

                question.setMinLength(questionResults.getInt(5));
                if (questionResults.wasNull()) {
                    question.setMinLength(null);
                }

                question.setMaxLength(questionResults.getInt(6));
                if (questionResults.wasNull()) {
                    question.setMaxLength(null);
                }

                question.setStartDate(questionResults.getString(7));
                if (questionResults.wasNull()) {
                    question.setStartDate(null);
                }

                do {
                    question.setText(questionResults.getString(8));
                    question.setHelpText(questionResults.getString(9));
                } while (questionResults.next());

                // Options
                retrieveOptions.setInt(1, question.getDocument());
                retrieveOptions.setInt(2, question.getVersion());
                retrieveOptions.setInt(3, question.getIdx());

                ResultSet optionsResults = retrieveOptions.executeQuery();
                while (optionsResults.next()) {
                    question.addOption(optionsResults.getString(1), optionsResults.getString(2));
                }

                if (question instanceof ContactBlock) {
                    ContactBlock contactContent = (ContactBlock) question;
                    if (contactContent.getType() == null) {
                        contactContent.setType(contactContent.getBlockType().code().substring(0, 5));
                    }

                    retrieveContactData.setInt(1, contactContent.getDocument());
                    retrieveContactData.setInt(2, contactContent.getVersion());
                    retrieveContactData.setInt(3, contactContent.getIdx());
                    ResultSet dataResults = retrieveContactData.executeQuery();

                    while (dataResults.next()) {
                        contactContent.setOption(dataResults.getString(1));
                        contactContent.setDataType(dataResults.getString(2));
                    }
                } else if (question instanceof RatingQuestion) {
                    retrieveRatingBlock.setInt(1, question.getDocument());
                    retrieveRatingBlock.setInt(2, question.getVersion());
                    retrieveRatingBlock.setInt(3, question.getIdx());

                    ResultSet dataResults = retrieveRatingBlock.executeQuery();

                    RatingQuestion ratingQuestion = (RatingQuestion) question;
                    if (dataResults.next()) {
                        String stepType = dataResults.getString(1);
                        ratingQuestion.setStepType(
                                stepType == null ? null : RatingQuestion.StepType.fromCode(stepType));
                        ratingQuestion.setStartLabel(dataResults.getString(2));
                        ratingQuestion.setEndLabel(dataResults.getString(3));
                    }
                }
            }

            return question;
        }
    }

    private SubmitBlock createSubmitBlock(
            PreparedStatement retrieveSubmitBlock,
            ResultSet resultsBlocks,
            Document document,
            Connection conn) throws SQLException {
        SubmitBlock submitBlock = new SubmitBlock();
        setBlockAttributes(submitBlock, resultsBlocks);

        retrieveSubmitBlock.setInt(1, submitBlock.getDocument());
        retrieveSubmitBlock.setInt(2, submitBlock.getVersion());
        retrieveSubmitBlock.setInt(3, submitBlock.getIdx());

        ResultSet retrieveSubmitResult = retrieveSubmitBlock.executeQuery();

        while (retrieveSubmitResult.next()) {
            submitBlock.setPrompt(retrieveSubmitResult.getString(1));
            submitBlock.setSize(SubmitBlock.Size.valueOf(retrieveSubmitResult.getString(2)));
            submitBlock.setPosition(SubmitBlock.Position.valueOf(retrieveSubmitResult.getString(3)));

            DocumentPersister documentPersister = documentPersisterProvider.get();
            int documentId = retrieveSubmitResult.getInt(4);
            if (!retrieveSubmitResult.wasNull()) {
                Document nextDocument = documentPersister.get(documentId);
                submitBlock.setNextDocument(NextDocument.fromDocument(nextDocument));
            }

            List<Document> documents = documentPersister.retrieveDocumentsByEntityAndRelations(
                    document.getSystemEntity(), Lists.newArrayList(CONTENT, ATTACHMENT), conn);
            submitBlock.setNextDocuments(FluentIterable.from(documents)
                    .filter(new Predicate<Document>() {
                        @Override
                        public boolean apply(Document input) {
                            return CONTENT.equals(input.getSystemEntityRelation())
                                    || ATTACHMENT.equals(input.getSystemEntityRelation());
                        }
                    }).transform(new Function<Document, NextDocument>() {
                        @Nullable
                        @Override
                        public NextDocument apply(Document input) {
                            return NextDocument.fromDocument(input);
                        }
                    }).toList());
        }

        return submitBlock;
    }

    private void setBlockAttributes(Block block, ResultSet resultsFE) throws SQLException {
        block.setDocument(resultsFE.getInt(1));
        block.setVersion(resultsFE.getInt(2));
        block.setIdx(resultsFE.getInt(3));

        String blockType = resultsFE.getString(4);
	block.setBlockType(BlockType.valueOf(blockType));
    }

    private void insertSubmitBlock(
            PreparedStatement insertSubmit,
            int document,
            int version,
            int index,
            SubmitBlock submitBlock) throws SQLException {
        insertSubmit.setInt(1, document);
        insertSubmit.setInt(2, version);
        insertSubmit.setInt(3, index);
        insertSubmit.setString(4, submitBlock.getPrompt());
        insertSubmit.setString(5, submitBlock.getSize().name());
        insertSubmit.setString(6, submitBlock.getPosition().name());
        if (submitBlock.getNextDocument() == null) {
            insertSubmit.setNull(7, Types.INTEGER);
        } else {
            insertSubmit.setInt(7, submitBlock.getNextDocument().getDocumentId());
        }

        insertSubmit.addBatch();
    }

    private void insertContentData(
            int document,
            int version,
            int index,
            PreparedStatement insertContentData,
            Content content) throws SQLException {
        insertContentData.setInt(1, document);
        insertContentData.setInt(2, version);
        insertContentData.setInt(3, index);
        insertContentData.setString(4, content.getContent());
        if (content.getTextType() == null) {
            insertContentData.setNull(5, Types.VARCHAR);
        } else {
            insertContentData.setString(5, content.getTextType().name());
        }
        insertContentData.addBatch();
    }

    private void insertImageData(
            int document,
            int version,
            int index,
            PreparedStatement insertMediaData,
            ImageBlock imageBlock) throws SQLException {
        insertMediaData.setInt(1, document);
        insertMediaData.setInt(2, version);
        insertMediaData.setInt(3, index);
        insertMediaData.setString(4, imageBlock.getUrl());
        insertMediaData.setString(5, imageBlock.getCaption());
        insertMediaData.setString(6, imageBlock.getAltText());
        insertMediaData.addBatch();
    }

    private void insertRatingQuestion(
            int document,
            int version,
            int index,
            PreparedStatement insertRating,
            RatingQuestion question) throws SQLException {
        insertRating.setInt(1, document);
        insertRating.setInt(2, version);
        insertRating.setInt(3, index);
        insertRating.setString(4, question.getStepType().code());
        insertRating.setString(5, question.getStartLabel());
        insertRating.setString(6, question.getEndLabel());
        insertRating.addBatch();
    }

    private void insertMediaData(
            int document,
            int version,
            int index,
            PreparedStatement insertMediaData,
            MediaBlock mediaBlock) throws SQLException {
        insertMediaData.setInt(1, document);
        insertMediaData.setInt(2, version);
        insertMediaData.setInt(3, index);
        insertMediaData.setString(4, mediaBlock.getUrl());
        insertMediaData.setString(5, mediaBlock.getDescription());
        insertMediaData.setString(6, mediaBlock.getTitle());
        insertMediaData.addBatch();
    }

    private void insertTextImageData(
            int document,
            int version,
            int index,
            PreparedStatement insertTextImageData,
            PreparedStatement insertContentData,
            TextImageBlock element) throws SQLException {

        if (element.containsImage()) {
            insertTextImageData.setInt(1, document);
            insertTextImageData.setInt(2, version);
            insertTextImageData.setInt(3, index);
            insertTextImageData.setString(4, element.getImage().getUrl());
            insertTextImageData.setString(5, element.getImage().getCaption());
            insertTextImageData.setString(6, element.getImage().getAltText());
            insertTextImageData.setString(7, element.getImage().getPosition().getCode());
            insertTextImageData.setString(8, element.getImage().getSize().getCode());
            insertTextImageData.addBatch();
        }

        insertContentData.setInt(1, document);
        insertContentData.setInt(2, version);
        insertContentData.setInt(3, index);
        insertContentData.setString(4, element.getText());
        insertContentData.setString(5, TextType.HTML.name());
        insertContentData.addBatch();
    }

    private void insertDocumentData(
            int document,
            int version,
            int index,
            PreparedStatement insertDocumentData,
            DocumentBlock documentBlock) throws SQLException {
        insertDocumentData.setInt(1, document);
        insertDocumentData.setInt(2, version);
        insertDocumentData.setInt(3, index);
        insertDocumentData.setString(4, documentBlock.getTitle());
        insertDocumentData.setString(5, documentBlock.getUrl());
        insertDocumentData.addBatch();
    }

    private void insertContactData(
            int document,
            int version,
            int index,
            PreparedStatement insertContactData,
            ContactBlock contactBlock) throws SQLException {
        insertContactData.setInt(1, document);
        insertContactData.setInt(2, version);
        insertContactData.setInt(3, index);
        insertContactData.setString(4, contactBlock.getOption());
        insertContactData.setString(5, contactBlock.getDataType());

        insertContactData.addBatch();
    }

    private void insertQuestionData(
            int document,
            int version,
            int index,
            PreparedStatement insertQuestion,
            PreparedStatement insertQuestionOptions,
            Question rawQuestion) throws SQLException {

        Question question;
        if (rawQuestion.getBlockType().isStandard()) {
            question = cleanStandardQuestion(rawQuestion);
        } else {
            question = rawQuestion;
        }

        insertQuestion.setInt(1, document);
        insertQuestion.setInt(2, version);
        insertQuestion.setInt(3, index);
        insertQuestion.setString(4, question.getDataType());
        insertQuestion.setString(5, question.getLabel());
        insertQuestion.setBoolean(6, question.isRequired());
        insertQuestion.setBoolean(7, question.isMultiselect());

        if (question.getMinLength() == null) {
            insertQuestion.setNull(8, Types.INTEGER);
        } else {
            insertQuestion.setInt(8, question.getMinLength());
        }

        if (question.getMaxLength() == null) {
            insertQuestion.setNull(9, Types.INTEGER);
        } else {
            insertQuestion.setInt(9, question.getMaxLength());
        }

        if (Strings.isNullOrEmpty(question.getStartDate())) {
            insertQuestion.setNull(10, Types.VARCHAR);
        } else {
            insertQuestion.setString(10, question.getStartDate());
        }

        if (Strings.isNullOrEmpty(question.getText())) {
            insertQuestion.setNull(11, Types.VARCHAR);
        } else {
            insertQuestion.setString(11, question.getText());
        }

        if (Strings.isNullOrEmpty(question.getHelpText())) {
            insertQuestion.setNull(12, Types.VARCHAR);
        } else {
            insertQuestion.setString(12, question.getHelpText());
        }

        insertQuestion.addBatch();

        if (question.getOptions() != null) {
            List<Option> options = question.getOptions();
            int optionIdx = 0;
            for (Option option : options) {
                insertQuestionOptions.setInt(1, document);
                insertQuestionOptions.setInt(2, version);
                insertQuestionOptions.setInt(3, index);
                insertQuestionOptions.setInt(4, optionIdx);
                insertQuestionOptions.setString(5, option.getReportValue());
                insertQuestionOptions.setString(6, option.getDisplayValue());
                insertQuestionOptions.addBatch();
                optionIdx++;
            }
        }
    }

    private Question cleanStandardQuestion(Question question) {
        Question nQuestion =
	    stdQuestionFactory.createStandardQuestion(question.getBlockType());
        nQuestion.setLabel(question.getLabel());
        nQuestion.setRequired(question.isRequired());
        nQuestion.setDocument(question.getDocument());
        nQuestion.setText(question.getText());
        nQuestion.setHelpText(question.getHelpText());
        nQuestion.setMaxLength(question.getMaxLength());
        nQuestion.setMinLength(question.getMinLength());
        nQuestion.setOptions(question.getOptions());

        return nQuestion;
    }
}
