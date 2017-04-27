package org.consumersunion.stories.server.index.collection;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.server.index.Indexer;
import org.springframework.stereotype.Component;

@Component
public class UpdatedCollectionTagsIndexer {
    private final Indexer<CollectionDocument> collectionIndexer;

    @Inject
    public UpdatedCollectionTagsIndexer(Indexer<CollectionDocument> collectionIndexer) {
        this.collectionIndexer = collectionIndexer;
    }

    public void index(Collection collection, List<String> tags) {
        CollectionDocument collectionDocument = collectionIndexer.get(collection.getId());
        if (collectionDocument != null) {
            collectionDocument.setLastModified(new Date());
            collectionDocument.setTags(new LinkedHashSet<String>(tags));

            collectionIndexer.index(collectionDocument);
        }
    }
}
