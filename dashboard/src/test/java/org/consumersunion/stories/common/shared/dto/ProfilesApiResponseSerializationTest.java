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

public class ProfilesApiResponseSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize() throws JsonProcessingException {
        // Given
        Metadata metadata = new Metadata();
        List<ProfileResponse> profileResponses = new ArrayList<ProfileResponse>();
        ProfilesApiResponse response = new ProfilesApiResponse();
        response.setMetadata(metadata);
        response.setData(profileResponses);

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        metadata = JsonPath.from(json).getObject("meta", Metadata.class);
        profileResponses = JsonPath.from(json).get("profiles");

        assertNotNull(metadata);
        assertNotNull(profileResponses);
    }

    @Test
    public void deserialize() throws IOException {
        // Given
        String json = "{\"meta\":{},\"profiles\":[]}";

        // When
        ProfilesApiResponse response = objectMapper.readValue(json, ProfilesApiResponse.class);

        // Then
        assertNotNull(response.getData());
        assertNotNull(response.getMetadata());
    }
}
