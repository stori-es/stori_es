package org.consumersunion.stories.common.client.ui;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.response.CollectionDataPagedResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.CollectionSortField;
import org.consumersunion.stories.common.shared.service.QuestionnaireMask;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class CollectionSuggestionOracle extends EntitySuggestionOracle<Collection> {
    private final RpcCollectionServiceAsync collectionService;

    private Predicate<CollectionData> filter;

    @Inject
    CollectionSuggestionOracle(RpcCollectionServiceAsync collectionService) {
        this.collectionService = collectionService;
    }

    public void setFilter(Predicate<CollectionData> filter) {
        this.filter = filter;
    }

    @Override
    protected void sendRequest(final Request request, final Callback callback) {
        // Because we're looking for 'owners', the permission mask really doesn't matter.
        collectionService.getCollections(
                new RetrievePagedCollectionsParams.Builder()
                        .withStart(0)
                        .withLength(5)
                        .withSortField(CollectionSortField.TITLE_A_Z)
                        .withAscending(true)
                        .withQuestionnaireMask(QuestionnaireMask.QUESTIONNAIRE_MASK_NON_QUESTIONNAIRES)
                        .withPermissionMask(ROLE_READER)
                        .withAccessMode(ACCESS_MODE_EXPLICIT)
                        .build(request.getQuery() + "*"),
                new ResponseHandler<CollectionDataPagedResponse>() {
                    @Override
                    public void handleSuccess(CollectionDataPagedResponse result) {
                        addSuggestions(wrapCollections(result.getData()), request, callback);
                    }
                }
        );
    }

    private List<EntitySuggestion<Collection>> wrapCollections(List<CollectionData> collections) {
        FluentIterable<CollectionData> fluentIterable = FluentIterable.from(collections);

        if (filter != null) {
            fluentIterable = fluentIterable.filter(filter);
        }

        return fluentIterable
                .transform(new Function<CollectionData, EntitySuggestion<Collection>>() {
                    @Override
                    public EntitySuggestion<Collection> apply(CollectionData input) {
                        return new EntitySuggestion<Collection>(input.getCollection());
                    }
                }).toList();
    }
}
