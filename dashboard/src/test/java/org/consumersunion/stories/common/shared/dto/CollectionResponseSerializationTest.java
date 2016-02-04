package org.consumersunion.stories.common.shared.dto;

import org.consumersunion.stories.server.api.rest.mapper.ObjectMapperConfigurator;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CollectionResponseSerializationTest {
    private ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    /**
     * See https://consumersunion.atlassian.net/browse/TASK-1376
     */
    @Test
    public void isArchived_true() throws JsonProcessingException {
        // Given
        CollectionResponse collectionResponse = new CollectionResponse();
        collectionResponse.setArchived(true);

        // When
        String json = objectMapper.writeValueAsString(collectionResponse);

        // Then
        Boolean archived = JsonPath.from(json).get("archived");
        Boolean isArchived = JsonPath.from(json).get("is_archived");

        assertNull(archived);
        assertTrue(isArchived);
    }

    /**
     * See https://consumersunion.atlassian.net/browse/TASK-1376
     */
    @Test
    public void isArchived_false() throws JsonProcessingException {
        // Given
        CollectionResponse collectionResponse = new CollectionResponse();
        collectionResponse.setArchived(false);

        // When
        String json = objectMapper.writeValueAsString(collectionResponse);

        // Then
        Boolean archived = JsonPath.from(json).get("archived");
        Boolean isArchived = JsonPath.from(json).get("is_archived");

        assertNull(archived);
        assertFalse(isArchived);
    }
}
