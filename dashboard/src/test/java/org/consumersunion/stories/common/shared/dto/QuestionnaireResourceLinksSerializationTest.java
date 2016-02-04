package org.consumersunion.stories.common.shared.dto;

import java.io.IOException;

import org.consumersunion.stories.server.api.rest.mapper.ObjectMapperConfigurator;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class QuestionnaireResourceLinksSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize_doesNotIncludeTypeInfo() throws JsonProcessingException {
        // Given
        QuestionnaireResourceLinks links = new QuestionnaireResourceLinks();

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
                        "\"readers\":[],\"notes\":[],\"attachments\":[],\"collections\":[]," +
                        "\"next\":{\"href\":\"\"},\"responses\":[],\"autotags\":[]}";

        // When
        QuestionnaireResourceLinks value = objectMapper.readValue(json, QuestionnaireResourceLinks.class);

        // Then
        assertNotNull(value.getAttachments());
        assertNotNull(value.getNotes());
        assertNotNull(value.getResponses());
        assertNotNull(value.getAdministrators());
        assertNotNull(value.getCreator());
        assertNotNull(value.getCurators());
        assertNotNull(value.getOwner());
        assertNotNull(value.getReaders());
        assertNotNull(value.getCollections());
        assertNotNull(value.getNext());
        assertNotNull(value.getResponses());
        assertNotNull(value.getAutotags());
    }
}
