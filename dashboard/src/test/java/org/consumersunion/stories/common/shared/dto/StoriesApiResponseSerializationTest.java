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

public class StoriesApiResponseSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize() throws JsonProcessingException {
        // Given
        Metadata metadata = new Metadata();
        List<StoryResponse> basicResponseList = new ArrayList<StoryResponse>();
        StoriesApiResponse response = new StoriesApiResponse();
        response.setMetadata(metadata);
        response.setData(basicResponseList);

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        Metadata meta = JsonPath.from(json).getObject("meta", Metadata.class);
        List<BasicResponse> stories = JsonPath.from(json).get("stories");

        assertNotNull(meta);
        assertNotNull(stories);
    }

    @Test
    public void deserialize() throws IOException {
        // Given
        String json = "{\"meta\":{},\"stories\":[]}";

        // When
        StoriesApiResponse storiesApiResponse = objectMapper.readValue(json, StoriesApiResponse.class);

        // Then
        assertNotNull(storiesApiResponse.getData());
        assertNotNull(storiesApiResponse.getMetadata());
    }
}
