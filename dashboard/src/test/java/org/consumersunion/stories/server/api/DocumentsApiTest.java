package org.consumersunion.stories.server.api;

import java.util.List;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.dto.ApiBlock;
import org.consumersunion.stories.common.shared.dto.ApiResponse;
import org.consumersunion.stories.common.shared.dto.DocumentResponse;
import org.consumersunion.stories.common.shared.dto.DocumentType;
import org.consumersunion.stories.common.shared.dto.DocumentsApiResponse;
import org.consumersunion.stories.common.shared.dto.ResourceLink;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockImage;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockOption;
import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockOptions;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockFormat;
import org.consumersunion.stories.common.shared.dto.post.DocumentPost;
import org.consumersunion.stories.common.shared.dto.post.DocumentPut;

import com.google.common.collect.Lists;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;

import static com.jayway.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.consumersunion.stories.common.shared.api.EndPoints.DOCUMENTS;
import static org.consumersunion.stories.common.shared.dto.ApiLocale.EN_US;
import static org.consumersunion.stories.common.shared.dto.DocumentType.ATTACHMENT;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStylePosition;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStyleSize;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockType;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockType.TEXT_BOX_QUESTION_BLOCK;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockType.TEXT_CONTENT_BLOCK;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockType.ZIP_CODE_QUESTION_BLOCK;

public class DocumentsApiTest extends ApiTestCase {
    private static final String A_TITLE = "my new document title";
    private static final String AN_URL = "http://www.google.ca";
    private static final String ANOTHER_TITLE = "ANOTHER_TITLE";

    public void testNoAuth_publicDocument_returnsDocument() {
        DocumentsApiResponse response = getDocument(given(), 110, OK, DocumentsApiResponse.class);
        List<DocumentResponse> documents = response.getData();
        assertNotNull(documents);
        assertEquals(1, documents.size());
        DocumentResponse documentData = documents.get(0);
        assertNotNull(documentData);
    }

    public void testdeleteDocument_withAuth_deletesDocument() {
        DocumentPost documentPost = DocumentPost.builder()
                .withTitle(A_TITLE)
                .withSource(new ResourceLink(AN_URL))
                .withDocumentType(DocumentType.ATTACHMENT)
                .withEntityId(1)
                .build();
        DocumentsApiResponse apiResponse = createDocument(documentPost);
        Integer id = apiResponse.getData().get(0).getId();

        delete(withUser1Login(), id, NO_CONTENT, EndPoints.DOCUMENTS);

        getDocument(withUser1Login(), id, Status.NOT_FOUND, DocumentsApiResponse.class);
    }

    public void testWithAuth_createDocument_returnsDocument() {
        ApiBlock apiBlock = new ApiBlock();
        apiBlock.setBlockType(TEXT_CONTENT_BLOCK);
        apiBlock.setFormat(ApiBlockFormat.SINGLE_LINE);
        apiBlock.setValue(A_TITLE);
        DocumentPost documentPost = DocumentPost.builder()
                .withTitle(A_TITLE)
                .withEntityId(1001)
                .withSource(new ResourceLink(AN_URL))
                .withDocumentType(ATTACHMENT)
                .withBlocks(Lists.newArrayList(apiBlock))
                .withEntityId(1)
                .build();

        DocumentsApiResponse apiResponse = createDocument(documentPost);

        DocumentResponse documentResponse = apiResponse.getData().get(0);
        assertThat(documentResponse.getTitle()).isEqualTo(A_TITLE);
        assertThat(documentResponse.getLinks().getSource().getHref()).isEqualTo(AN_URL);
    }

    public void testWithAuth_updateDocument_returnsDocument() {
        // given
        ApiBlock apiBlock = new ApiBlock();
        apiBlock.setBlockType(TEXT_CONTENT_BLOCK);
        apiBlock.setFormat(ApiBlockFormat.SINGLE_LINE);
        apiBlock.setValue(A_TITLE);
        DocumentPost documentPost = DocumentPost.builder()
                .withTitle(A_TITLE)
                .withEntityId(1001)
                .withSource(new ResourceLink(AN_URL))
                .withDocumentType(ATTACHMENT)
                .withBlocks(Lists.newArrayList(apiBlock))
                .build();
        DocumentsApiResponse apiResponse = createDocument(documentPost);
        DocumentResponse documentResponse = apiResponse.getData().get(0);

        // when
        DocumentPut documentPut = DocumentPut.builder()
                .withDocumentType(ATTACHMENT)
                .withLocale(EN_US)
                .withTitle(ANOTHER_TITLE).build();
        DocumentsApiResponse putApiResponse =
                put(withUser1Login(), documentResponse.getId(), OK, documentPut, DocumentsApiResponse.class, DOCUMENTS);

        // then
        DocumentResponse document = putApiResponse.getData().get(0);
        assertThat(document.getTitle()).isEqualTo(ANOTHER_TITLE);
    }

    public void testAllQuestionnaireBlocks() {
        DocumentsApiResponse response = getDocument(given(), 110, OK, DocumentsApiResponse.class);
        DocumentResponse documentData = response.getData().get(0);
        List<ApiBlock> blocks = documentData.getBlocks();

        assertEquals("Unexpected number of blocks.", 34, blocks.size());

        ApiBlock attachmentsQuestionBlock = blocks.get(0);
        assertEquals("Unexpected attachment type",
                ApiBlockType.ATTACHMENTS_QUESTION_BLOCK,
                attachmentsQuestionBlock.getBlockType());
        assertEquals("Unexpected 'text' value", "Attachments", attachmentsQuestionBlock.getValue());

        ApiBlock audioContentBlock = blocks.get(1);
        assertEquals("Unexpected attachment type",
                ApiBlockType.AUDIO_CONTENT_BLOCK,
                audioContentBlock.getBlockType());
        assertEquals("Unexpected 'href' value.",
                "http://foo.com/song.ogg",
                audioContentBlock.getAudio().getHref());

        assertEquals("Unexpected 'caption' value.",
                "a cool song",
                audioContentBlock.getAudio().getCaption());
        assertEquals("Unexpected 'title' value.",
                "this song is six words long",
                audioContentBlock.getAudio().getTitle());

        ApiBlock submitBlock = blocks.get(2);
        assertEquals("Unexpected attachment type",
                ApiBlockType.BUTTON_BLOCK,
                submitBlock.getBlockType());
        assertEquals("Unexpected 'next_document' value.",
                "http://localhost:" + RestAssured.port + DOCUMENTS + "8003",
                submitBlock.getNextDocument().getHref());

        checkTextInput(blocks.get(3), ApiBlockFormat.SINGLE_LINE, "Single Line Text");
        checkTextInput(blocks.get(4), ApiBlockFormat.MULTI_LINES_PLAIN_TEXT, "Multi Line Plain Text");
        checkTextInput(blocks.get(5), ApiBlockFormat.MULTI_LINES_RICH_TEXT, "Multi Line Rich Text");
        checkTextInput(blocks.get(6), ApiBlockFormat.SINGLE_LINE, "City", ApiBlockType.CITY_QUESTION_BLOCK);
        checkTextInput(blocks.get(7), ApiBlockFormat.SINGLE_LINE, "First Name", ApiBlockType.FIRST_NAME_QUESTION_BLOCK);
        checkTextInput(blocks.get(8), ApiBlockFormat.SINGLE_LINE, "Last Name", ApiBlockType.LAST_NAME_QUESTION_BLOCK);

        ApiBlock collectionBlock = blocks.get(9);
        assertEquals("Unexpected attachment type",
                ApiBlockType.COLLECTION_CONTENT_BLOCK,
                collectionBlock.getBlockType());
        assertEquals("Unexpected collection reference.",
                "http://localhost:" + RestAssured.port + EndPoints.COLLECTIONS + "4",
                collectionBlock.getCollection().getHref());

        ApiBlock dateBlock = blocks.get(10);
        assertEquals("Unexpected attachment type",
                ApiBlockType.DATE_QUESTION_BLOCK,
                dateBlock.getBlockType());
        assertEquals("Unexpected 'value' value", "Date", dateBlock.getValue());

        checkTextInput(blocks.get(11), ApiBlockFormat.MULTI_LINES_RICH_TEXT, "Story Ask",
                ApiBlockType.STORY_ASK_QUESTION_BLOCK);
        checkTextInput(blocks.get(12), ApiBlockFormat.SINGLE_LINE, "Story Title",
                ApiBlockType.STORY_TITLE_QUESTION_BLOCK);
        checkTextInput(blocks.get(13), ApiBlockFormat.SINGLE_LINE, "Street Address",
                ApiBlockType.STREET_ADDRESS_QUESTION_BLOCK);

        ApiBlock documentBlock = blocks.get(14);
        assertEquals("Unexpected attachment type",
                ApiBlockType.DOCUMENT_CONTENT_BLOCK,
                documentBlock.getBlockType());
        assertEquals("Unexpected 'value' value", "a document", documentBlock.getDocument().getTitle());
        assertEquals("Unexpected 'value' value", "http://foo.bar/doc.pdf", documentBlock.getDocument().getHref());

        checkSelectInput(blocks.get(15), ApiBlockType.EMAIL_FORMAT_QUESTION_BLOCK, "Preferred Email Format", 2);
        // Yes, the state count is bogus, it's only partially setup in the test data.
        checkSelectInput(blocks.get(16), ApiBlockType.STATE_QUESTION_BLOCK, "State", 7);
        checkSelectInput(blocks.get(17), ApiBlockType.MULTIPLE_CHOICE_QUESTION_BLOCK, "Select", 3);

        checkContact(blocks.get(18), ApiBlockType.EMAIL_QUESTION_BLOCK, "Email - Home");
        checkContact(blocks.get(19), ApiBlockType.EMAIL_QUESTION_BLOCK, "Email - Work");
        checkContact(blocks.get(20), ApiBlockType.EMAIL_QUESTION_BLOCK, "Email - Other");
        checkContact(blocks.get(21), ApiBlockType.PHONE_QUESTION_BLOCK, "Phone - Home");
        checkContact(blocks.get(22), ApiBlockType.PHONE_QUESTION_BLOCK, "Phone - Work");
        checkContact(blocks.get(23), ApiBlockType.PHONE_QUESTION_BLOCK, "Phone - Mobile");
        checkContact(blocks.get(24), ApiBlockType.PHONE_QUESTION_BLOCK, "Phone - Other");

        ApiBlock imageBlock = blocks.get(25);
        assertEquals("Unexpected attachment type",
                ApiBlockType.IMAGE_CONTENT_BLOCK,
                imageBlock.getBlockType());
        ApiBlockImage imageData = imageBlock.getImage();
        assertEquals("Unexpected 'alttext' value.", "image", imageData.getAltText());
        assertEquals("Unexpected 'caption' value.", "an image", imageData.getCaption());
        assertEquals("Unexpected 'href' value.", "http://foo.bar/image.png", imageData.getHref());

        ApiBlock imageTextBlock = blocks.get(26);
        assertEquals("Unexpected attachment type",
                TEXT_CONTENT_BLOCK,
                imageTextBlock.getBlockType());
        ApiBlockImage imageTextData = imageTextBlock.getImage();
        assertEquals("Unexpected 'alttext' value.", "image2", imageTextData.getAltText());
        assertEquals("Unexpected 'caption' value.", "another image", imageTextData.getCaption());
        assertEquals("Unexpected 'href' value.", "http://bar.baz/image.png", imageTextData.getHref());
        assertEquals("Unexpected 'position' value.", ApiBlockStylePosition.RIGHT,
                imageTextData.getHorizontalPosition());
        assertEquals("Unexpected 'size' value.", ApiBlockStyleSize.LARGE, imageTextData.getSize());
        assertEquals("Unexpected 'content' value.", "Some text", imageTextBlock.getValue());

        ApiBlock permissionBlock = blocks.get(27);
        assertEquals("Unexpected attachment type",
                ApiBlockType.PERMISSIONS_BLOCK,
                permissionBlock.getBlockType());
        assertEquals("Unexpectd 'value' value.", "All rights reserved.", permissionBlock.getValue());

        checkRating(blocks.get(28));
        checkRating(blocks.get(29));

        ApiBlock storyBlock = blocks.get(30);
        assertEquals("Unexpected attachment type",
                ApiBlockType.STORY_CONTENT_BLOCK,
                storyBlock.getBlockType());
        assertEquals("Unexpectd 'href' value.", "http://localhost:" + RestAssured.port + EndPoints.STORIES + "6",
                storyBlock.getStory().getHref());

        checkSelectInput(blocks.get(31), ApiBlockType.SUBSCRIPTION_QUESTION_BLOCK, "Opt In", 1);

        ApiBlock videoContentBlock = blocks.get(32);
        assertEquals("Unexpected attachment type",
                ApiBlockType.VIDEO_CONTENT_BLOCK,
                videoContentBlock.getBlockType());
        assertEquals("Unexpected 'href' value.",
                "http://foo.com/video.mp4",
                videoContentBlock.getVideo().getHref());
        assertEquals("Unexpected 'caption' value.",
                "a cool video",
                videoContentBlock.getVideo().getCaption());
        assertEquals("Unexpected 'title' value.",
                "vid vid",
                videoContentBlock.getVideo().getTitle());

        checkTextInput(blocks.get(33), ApiBlockFormat.SINGLE_LINE, "Zip Code", ZIP_CODE_QUESTION_BLOCK);
    }

    private DocumentsApiResponse createDocument(DocumentPost documentPost) {
        return postWithProfile1001(OK, documentPost, DocumentsApiResponse.class, DOCUMENTS);
    }

    private <T extends ApiResponse> T getDocument(RequestSpecification request,
            int id,
            Status expectedStatusCode,
            Class<T> bodyClass) {
        return get(request, id, expectedStatusCode, bodyClass, DOCUMENTS);
    }

    private void checkTextInput(ApiBlock block, ApiBlockFormat blockFormat, String text) {
        checkTextInput(block, blockFormat, text, TEXT_BOX_QUESTION_BLOCK);
    }

    private void checkTextInput(ApiBlock block, ApiBlockFormat blockFormat, String text,
            BlockConstants.ApiBlockType blockType) {
        assertEquals("Unexpected attachment type", blockType, block.getBlockType());
        assertEquals("Unexpected 'format' value", blockFormat, block.getFormat());
        assertEquals("Unexpected 'value' value", text, block.getValue());
    }

    private void checkSelectInput(ApiBlock block, BlockConstants.ApiBlockType apiBlockType, String text,
            int optionCount) {
        assertEquals("Unexpected attachment type", apiBlockType, block.getBlockType());
        assertEquals("Unexpected 'value' value", text, block.getValue());

        ApiBlockOptions optionsContainer = block.getOptions();
        List<ApiBlockOption> options = optionsContainer.getOptions();
        assertEquals(optionCount, options.size());
        // TODO: match each of the options. See TASK-1734
    }

    private void checkContact(ApiBlock block, ApiBlockType apiBlockType, String text) {
        assertEquals("Unexpected attachment type",
                apiBlockType,
                block.getBlockType());
        assertEquals("Unexpected 'value' value", text, block.getValue());
    }

    private void checkRating(ApiBlock block) {
        assertEquals("Unexpected attachment type", ApiBlockType.RATING_QUESTION_BLOCK, block.getBlockType());
    }
}
