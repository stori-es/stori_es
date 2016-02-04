package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionnairesApiResponse extends AbstractApiResponse<QuestionnaireResponse> {
    @Override
    @JsonProperty("questionnaires")
    public List<QuestionnaireResponse> getData() {
        return super.getData();
    }

    @Override
    @JsonProperty("questionnaires")
    public void setData(List<QuestionnaireResponse> data) {
        super.setData(data);
    }
}
