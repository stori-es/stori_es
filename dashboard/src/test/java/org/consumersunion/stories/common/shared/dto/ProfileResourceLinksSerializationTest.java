package org.consumersunion.stories.common.shared.dto;

import java.io.IOException;

import org.consumersunion.stories.server.api.rest.mapper.ObjectMapperConfigurator;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProfileResourceLinksSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize_doesNotIncludeTypeInfo() throws JsonProcessingException {
        // Given
        ProfileResourceLinks links = new ProfileResourceLinks();

        // When
        String json = objectMapper.writeValueAsString(links);

        // Then
        Object type = JsonPath.from(json).get("@type");
        assertNull(type);
    }

    @Test
    public void deserialize() throws IOException {
        // Given
        String json =
                "{\"creator\":{\"href\":\"\"},\"owner\":{\"href\":\"\"},\"administrators\":[],\"curators\":[]," +
                        "\"readers\":[]," +
                        "\"organization\":{\"href\":\"\"},\"user\":{\"href\":\"\"},\"stories\":[],\"notes\":[]}";

        // When
        ProfileResourceLinks value = objectMapper.readValue(json, ProfileResourceLinks.class);

        // Then
        assertNotNull(value.getNotes());
        assertNotNull(value.getAdministrators());
        assertNotNull(value.getCreator());
        assertNotNull(value.getCurators());
        assertNotNull(value.getOwner());
        assertNotNull(value.getReaders());
        assertNotNull(value.getOrganization());
        assertNotNull(value.getStories());
        assertNotNull(value.getUser());
    }
}
