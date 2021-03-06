package org.consumersunion.stories.common.shared.dto;

import java.io.IOException;

import org.consumersunion.stories.server.api.rest.mapper.ObjectMapperConfigurator;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CollectionResourceLinksSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize_doesNotIncludeTypeInfo() throws JsonProcessingException {
        // Given
        CollectionResourceLinks links = new CollectionResourceLinks();

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
                        "\"content\":{\"href\":\"\"},\"stories\":[],\"notes\":[],\"attachments\":[]," +
                        "\"questionnaires\":[]}";

        // When
        CollectionResourceLinks value = objectMapper.readValue(json, CollectionResourceLinks.class);

        // Then
        assertNotNull(value.getAdministrators());
        assertNotNull(value.getCreator());
        assertNotNull(value.getCurators());
        assertNotNull(value.getOwner());
        assertNotNull(value.getReaders());
        assertNotNull(value.getStories());
        assertNotNull(value.getContent());
        assertNotNull(value.getNotes());
        assertNotNull(value.getAttachments());
        assertNotNull(value.getQuestionnaires());
    }
}
