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

public class QuestionnairesApiResponseSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapperConfigurator().configure(new ObjectMapper());

    @Test
    public void serialize() throws JsonProcessingException {
        // Given
        Metadata metadata = new Metadata();
        List<QuestionnaireResponse> questionnaireResponses = new ArrayList<QuestionnaireResponse>();
        QuestionnairesApiResponse response = new QuestionnairesApiResponse();
        response.setMetadata(metadata);
        response.setData(questionnaireResponses);

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        metadata = JsonPath.from(json).getObject("meta", Metadata.class);
        questionnaireResponses = JsonPath.from(json).get("questionnaires");

        assertNotNull(metadata);
        assertNotNull(questionnaireResponses);
    }

    @Test
    public void deserialize() throws IOException {
        // Given
        String json = "{\"meta\":{},\"questionnaires\":[]}";

        // When
        QuestionnairesApiResponse response = objectMapper.readValue(json, QuestionnairesApiResponse.class);

        // Then
        assertNotNull(response.getData());
        assertNotNull(response.getMetadata());
    }
}
