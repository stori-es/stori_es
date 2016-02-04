package org.consumersunion.stories.common.shared.dto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.server.api.rest.mapper.ObjectMapperConfigurator;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;

import static org.junit.Assert.assertNotNull;

public class CollectionsApiResponseSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize() throws JsonProcessingException {
        // Given
        Metadata metadata = new Metadata();
        List<CollectionResponse> collectionResponses = new ArrayList<CollectionResponse>();
        CollectionsApiResponse response = new CollectionsApiResponse();
        response.setMetadata(metadata);
        response.setData(collectionResponses);

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        metadata = JsonPath.from(json).getObject("meta", Metadata.class);
        collectionResponses = JsonPath.from(json).get("collections");

        assertNotNull(metadata);
        assertNotNull(collectionResponses);
    }

    @Test
    public void deserialize() throws IOException {
        // Given
        String json = "{\"meta\":{},\"collections\":[]}";

        // When
        CollectionsApiResponse collectionsApiResponse = objectMapper.readValue(json, CollectionsApiResponse.class);

        // Then
        assertNotNull(collectionsApiResponse.getData());
        assertNotNull(collectionsApiResponse.getMetadata());
    }
}
