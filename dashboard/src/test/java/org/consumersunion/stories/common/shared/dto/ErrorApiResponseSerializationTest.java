package org.consumersunion.stories.common.shared.dto;

import java.io.IOException;

import org.consumersunion.stories.server.api.rest.mapper.ObjectMapperConfigurator;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ErrorApiResponseSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize() throws JsonProcessingException {
        // When
        String json = objectMapper.writeValueAsString(new ErrorApiResponse("some message"));

        // Then
        Metadata metadata = JsonPath.from(json).getObject("meta", Metadata.class);

        assertNotNull(metadata);
    }

    @Test
    public void deserialize() throws IOException {
        // Given
        String json = "{\"meta\":{}}";

        // When
        ErrorApiResponse storiesApiResponse = objectMapper.readValue(json, ErrorApiResponse.class);

        // Then
        assertNull(storiesApiResponse.getData());
        assertNotNull(storiesApiResponse.getMetadata());
    }
}
