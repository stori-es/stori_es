package org.consumersunion.stories.server.index.story;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.Script;
import org.consumersunion.stories.server.index.ScriptFactory;
import org.consumersunion.stories.server.index.elasticsearch.UpdateByQuery;
import org.consumersunion.stories.server.index.elasticsearch.query.Query;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UpdateStoryAddressIndexer {
    private final Provider<Indexer<StoryDocument>> storyIndexerProvider;
    private final ScriptFactory scriptFactory;

    @Inject
    UpdateStoryAddressIndexer(
            @Qualifier("storyIndexer") Provider<Indexer<StoryDocument>> storyIndexerProvider,
            ScriptFactory scriptFactory) {
        this.storyIndexerProvider = storyIndexerProvider;
        this.scriptFactory = scriptFactory;
    }

    public void index(Address address) {
        index(0, address);
    }

    public void index(int idx, Address address) {
        if (idx == 0) { // then it's the primary address
            Indexer storyIndexer = storyIndexerProvider.get();

            Query query = QueryBuilder.newBuilder().withTerm("ownerId", address.getEntity()).build();
            Script updateAddressScript = scriptFactory.createUpdateAddressScript(address);

            storyIndexer.updateFromQuery(new UpdateByQuery(query, updateAddressScript));
        }
    }
}
