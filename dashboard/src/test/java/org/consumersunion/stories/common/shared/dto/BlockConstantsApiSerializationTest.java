package org.consumersunion.stories.common.shared.dto;

import java.util.AbstractMap;
import java.util.Map;

import org.consumersunion.stories.common.shared.dto.blocks.ApiBlockStyles;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockFormat;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStylePosition;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStyleSize;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockType;
import org.consumersunion.stories.server.api.rest.mapper.ObjectMapperConfigurator;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.TypeLiteral;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JukitoRunner.class)
public class BlockConstantsApiSerializationTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            bindManyNamedInstances(new TypeLiteral<Map.Entry<ApiBlockType, String>>() {}, API_BLOCK_TYPE,
                    new AbstractMap.SimpleEntry(ApiBlockType.ATTACHMENTS_QUESTION_BLOCK, "AttachmentsQuestionBlock"),
                    new AbstractMap.SimpleEntry(ApiBlockType.LAST_NAME_QUESTION_BLOCK, "LastNameQuestionBlock"));

            bindManyNamedInstances(new TypeLiteral<Map.Entry<ApiBlockFormat, String>>() {}, API_BLOCK_FORMAT,
                    new AbstractMap.SimpleEntry(ApiBlockFormat.SINGLE_LINE, "single_line"),
                    new AbstractMap.SimpleEntry(ApiBlockFormat.MULTI_LINES_RICH_TEXT, "multiple_lines_rich_text"),
                    new AbstractMap.SimpleEntry(ApiBlockFormat.MULTI_LINES_PLAIN_TEXT, "multiple_lines_plain_text"));

            bindManyNamedInstances(new TypeLiteral<Map.Entry<ApiBlockStylePosition, String>>() {},
                    API_BLOCK_STYLE_POSITION,
                    new AbstractMap.SimpleEntry(ApiBlockStylePosition.LEFT, "left"),
                    new AbstractMap.SimpleEntry(ApiBlockStylePosition.CENTER, "center"),
                    new AbstractMap.SimpleEntry(ApiBlockStylePosition.RIGHT, "right"));

            bindManyNamedInstances(new TypeLiteral<Map.Entry<ApiBlockStyleSize, String>>() {}, API_BLOCK_STYLE_SIZE,
                    new AbstractMap.SimpleEntry(ApiBlockStyleSize.SMALL, "small"),
                    new AbstractMap.SimpleEntry(ApiBlockStyleSize.MEDIUM, "medium"),
                    new AbstractMap.SimpleEntry(ApiBlockStyleSize.LARGE, "large"));
        }
    }

    private static final String API_BLOCK_TYPE = "apiBlockTypeSerialization";
    private static final String API_BLOCK_FORMAT = "apiBlockFormatSerialization";
    private static final String API_BLOCK_STYLE_POSITION = "apiBlockStylePositionSerialization";
    private static final String API_BLOCK_STYLE_SIZE = "apiBlockStyleSizeSerialization";

    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    /**
     * Tests that the {@link BlockConstants} 'ApiBlockType' enumerations is properly converted to expected API values.
     * The test is not intended to verify all the values per-se, but rather tests that the JSON serialization has been
     * properly set up.
     * <p/>
     * See http://www.stori.es/api/website/#!/Documents/get_document
     */
    @Test
    public void apiBlockTypeSerialization(@All(API_BLOCK_TYPE) Map.Entry<ApiBlockType, String> expectedValue)
            throws JsonProcessingException {
        // Given
        ApiBlock ApiBlock = new ApiBlock();
        ApiBlock.setBlockType(expectedValue.getKey());

        // When
        String apiValue = objectMapper.writeValueAsString(ApiBlock.getBlockType());

        // Then
        assertNotNull("Unexpected null value from 'block_type'.", apiValue);
        // JSON value is literally '"foo"'
        assertEquals("Unexpected API response value.", "\"" + expectedValue.getValue().toString() + "\"", apiValue);
    }

    /**
     * Same as {@link #apiBlockTypeSerialization()} for the 'ApiBlockFormat' enumeration.
     */
    @Test
    public void apiBlockFormatSerialization(@All(API_BLOCK_FORMAT) Map.Entry<ApiBlockFormat, String> expectedValue)
            throws JsonProcessingException {
        // Given
        ApiBlock ApiBlock = new ApiBlock();
        ApiBlock.setFormat(expectedValue.getKey());

        // When
        String apiValue = objectMapper.writeValueAsString(ApiBlock.getFormat());

        // Then
        assertNotNull("Unexpected null value from 'block_type'.", apiValue);
        // JSON value is literally '"foo"'
        assertEquals("Unexpected API response value.", "\"" + expectedValue.getValue().toString() + "\"", apiValue);
    }

    @Test
    public void apiBlockStylePositionSerialization(
            @All(API_BLOCK_STYLE_POSITION) Map.Entry<ApiBlockStylePosition, String> expectedValue)
            throws JsonProcessingException {
        // Given
        ApiBlock ApiBlock = new ApiBlock();
        ApiBlockStyles styles = new ApiBlockStyles();
        styles.setHorizontalPosition(expectedValue.getKey());
        ApiBlock.setStyles(styles);

        // When
        String apiValue = objectMapper.writeValueAsString(ApiBlock.getStyles().getHorizontalPosition());

        // Then
        assertNotNull("Unexpected null value from 'block_type'.", apiValue);
        // JSON value is literally '"foo"'
        assertEquals("Unexpected API response value.", "\"" + expectedValue.getValue().toString() + "\"", apiValue);
    }

    @Test
    public void apiBlockStyleSizeSerialization(
            @All(API_BLOCK_STYLE_SIZE) Map.Entry<ApiBlockStyleSize, String> expectedValue)
            throws JsonProcessingException {
        // Given
        ApiBlock ApiBlock = new ApiBlock();
        ApiBlockStyles styles = new ApiBlockStyles();
        styles.setSize(expectedValue.getKey());
        ApiBlock.setStyles(styles);

        // When
        String apiValue = objectMapper.writeValueAsString(ApiBlock.getStyles().getSize());

        // Then
        assertNotNull("Unexpected null value from 'block_type'.", apiValue);
        // JSON value is literally '"foo"'
        assertEquals("Unexpected API response value.", "\"" + expectedValue.getValue().toString() + "\"", apiValue);
    }
}
