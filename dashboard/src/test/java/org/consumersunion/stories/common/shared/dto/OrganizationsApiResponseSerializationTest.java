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

public class OrganizationsApiResponseSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize() throws JsonProcessingException {
        // Given
        Metadata metadata = new Metadata();
        List<OrganizationResponse> organizationResponses = new ArrayList<OrganizationResponse>();
        OrganizationsApiResponse response = new OrganizationsApiResponse();
        response.setMetadata(metadata);
        response.setData(organizationResponses);

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        metadata = JsonPath.from(json).getObject("meta", Metadata.class);
        organizationResponses = JsonPath.from(json).get("organizations");

        assertNotNull(metadata);
        assertNotNull(organizationResponses);
    }

    @Test
    public void deserialize() throws IOException {
        // Given
        String json = "{\"meta\":{},\"organizations\":[]}";

        // When
        OrganizationsApiResponse organizationsApiResponse = objectMapper.readValue(json,
                OrganizationsApiResponse.class);

        // Then
        assertNotNull(organizationsApiResponse.getData());
        assertNotNull(organizationsApiResponse.getMetadata());
    }
}
