package org.consumersunion.stories.common.client.service.response;

import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

public class CollectionDataPagedResponse extends PagedDataResponse<CollectionData> {
    private int collectionsCount;
    private int questionnairesCount;

    public CollectionDataPagedResponse() {
        super();
    }

    public CollectionDataPagedResponse(Response template) {
        super(template);
    }

    public int getCollectionsCount() {
        return collectionsCount;
    }

    public void setCollectionsCount(int collectionsCount) {
        this.collectionsCount = collectionsCount;
        setTotalCount(collectionsCount + getQuestionnairesCount());
    }

    public int getQuestionnairesCount() {
        return questionnairesCount;
    }

    public void setQuestionnairesCount(int questionnairesCount) {
        this.questionnairesCount = questionnairesCount;
        setTotalCount(questionnairesCount + getCollectionsCount());
    }
}
