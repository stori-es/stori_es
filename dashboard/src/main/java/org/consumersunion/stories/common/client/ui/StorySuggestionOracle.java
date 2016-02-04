package org.consumersunion.stories.common.client.ui;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PUBLIC;
import static org.consumersunion.stories.common.shared.model.StorySortField.CREATED_NEW;

public class StorySuggestionOracle extends EntitySuggestionOracle<StorySummary> {
    private class StorySuggestion extends EntitySuggestion<StorySummary> {
        public StorySuggestion(StorySummary storySummary) {
            super(storySummary);
        }

        @Override
        public String getDisplayString() {
            String displayString = super.getDisplayString();

            if (Strings.isNullOrEmpty(displayString)) {
                displayString = labels.untitled();
            }

            return displayString;
        }

        @Override
        public String getReplacementString() {
            String replacementString = super.getReplacementString();

            if (Strings.isNullOrEmpty(replacementString)) {
                replacementString = labels.untitled();
            }

            return replacementString;
        }
    }

    private final CommonI18nLabels labels;
    private final RpcStoryServiceAsync storyService;

    private Collection collection;

    @Inject
    StorySuggestionOracle(
            CommonI18nLabels labels,
            RpcStoryServiceAsync storyService) {
        this.labels = labels;
        this.storyService = storyService;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    @Override
    protected void sendRequest(final Request request, final Callback callback) {
        StorySearchParameters parameters =
                new StorySearchParameters(0, 5, collection.getId(), null, "storyTitle:" + request.getQuery() + "*",
                        null,
                        null, ACCESS_MODE_PUBLIC, CREATED_NEW);
        parameters.setIncludeFullText(true);

        storyService.getStories(parameters, new ResponseHandler<PagedDataResponse<StorySummary>>() {
            @Override
            public void handleSuccess(PagedDataResponse<StorySummary> result) {
                addSuggestions(wrapStories(result.getData()), request, callback);
            }
        });
    }

    private List<EntitySuggestion<StorySummary>> wrapStories(List<StorySummary> stories) {
        return FluentIterable.from(stories)
                .transform(new Function<StorySummary, EntitySuggestion<StorySummary>>() {
                    @Override
                    public EntitySuggestion<StorySummary> apply(StorySummary input) {
                        return new StorySuggestion(input);
                    }
                }).toList();
    }
}
