package org.consumersunion.stories.dashboard.client.application.util;

import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcEntityServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;

public class TagsService {
    private final RpcEntityServiceAsync entityService;

    public interface TagsCallback {
        void onTagsReceived(Set<String> tags);
    }

    @Inject
    TagsService(RpcEntityServiceAsync entityService) {
        this.entityService = entityService;
    }

    public void loadTagsForSuggestionList(final TagsCallback tagsCallback) {
        entityService.getTags(new ResponseHandler<DatumResponse<Set<String>>>() {
            @Override
            public void handleSuccess(DatumResponse<Set<String>> result) {
                if (result.getGlobalErrorMessages().isEmpty()) {
                    tagsCallback.onTagsReceived(result.getDatum());
                }
            }
        });
    }
}
