package org.consumersunion.stories.common.shared.dto;

import java.util.List;

public class CollectionResourceLinks extends BaseCollectionResourceLinks {
    private List<ResourceLink> questionnaires;

    public List<ResourceLink> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(List<ResourceLink> questionnaires) {
        this.questionnaires = questionnaires;
    }
}
