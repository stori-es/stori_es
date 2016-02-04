package org.consumersunion.stories.common.shared.dto;

import java.io.IOException;

import org.consumersunion.stories.server.api.rest.mapper.ObjectMapperConfigurator;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class OrganizationResourceLinksSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize_doesNotIncludeTypeInfo() throws JsonProcessingException {
        // Given
        OrganizationResourceLinks links = new OrganizationResourceLinks();

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
                        "\"readers\":[],\"profiles\":[],\"questionnaires\":[],\"collections\":[],\"permissions\":[]," +
                        "\"themes\":[],\"notes\":[],\"attachments\":[],\"default_permission\":{\"href\":\"\"}," +
                        "\"default_theme\":{\"href\":\"\"}}";

        // When
        OrganizationResourceLinks value = objectMapper.readValue(json, OrganizationResourceLinks.class);

        // Then
        assertNotNull(value.getNotes());
        assertNotNull(value.getAdministrators());
        assertNotNull(value.getCreator());
        assertNotNull(value.getCurators());
        assertNotNull(value.getOwner());
        assertNotNull(value.getReaders());
        assertNotNull(value.getReaders());
        assertNotNull(value.getProfiles());
        assertNotNull(value.getQuestionnaires());
        assertNotNull(value.getCollections());
        assertNotNull(value.getPermissions());
        assertNotNull(value.getThemes());
        assertNotNull(value.getAttachments());
        assertNotNull(value.getDefaultPermission());
        assertNotNull(value.getDefaultTheme());
    }
}
