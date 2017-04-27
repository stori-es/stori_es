package org.consumersunion.stories.server.index.profile;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.Script;
import org.consumersunion.stories.server.index.ScriptFactory;
import org.consumersunion.stories.server.index.elasticsearch.UpdateByQuery;
import org.consumersunion.stories.server.index.elasticsearch.query.Query;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.consumersunion.stories.server.index.story.StoryDocument;
import org.springframework.stereotype.Component;

@Component
public class UpdatePersonAddressIndexer {
    private final ScriptFactory scriptFactory;
    private final Indexer<ProfileDocument> profileIndexer;
    private final Indexer<StoryDocument> storyIndexer;

    @Inject
    public UpdatePersonAddressIndexer(
            ScriptFactory scriptFactory,
            Indexer<ProfileDocument> profileIndexer,
            Indexer<StoryDocument> storyIndexer) {
        this.scriptFactory = scriptFactory;
        this.profileIndexer = profileIndexer;
        this.storyIndexer = storyIndexer;
    }

    public void index(Address address) {
        this.index(0, address);
    }

    public void index(int idx, Address address) {
        if (idx == 0) { // then it's the primary address
            // First update the Person
            ProfileDocument profileDocument = profileIndexer.get(address.getEntity());
            if (profileDocument != null) {
                profileDocument.setPrimaryCity(address.getCity());
                profileDocument.setPrimaryState(address.getState());
                profileDocument.setPrimaryPostalCode(address.getPostalCode());
                profileDocument.setPrimaryAddress1(address.getAddress1());

                profileIndexer.index(profileDocument);
            }

            // Update the Stories
            Query query = QueryBuilder.newBuilder()
                    .withTerm("authorId", address.getEntity())
                    .build();

            Script updateAddressScript = scriptFactory.createUpdateAddressScript(address);

            storyIndexer.updateFromQuery(new UpdateByQuery(query, updateAddressScript));
        }
    }
}
