package org.consumersunion.stories.server.api.rest.merger;

import org.consumersunion.stories.common.shared.dto.post.CollectionPut;
import org.consumersunion.stories.common.shared.model.Collection;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class CollectionPutMerger extends AbstractMerger<Collection, CollectionPut> {
    @Override
    public void merge(Collection entity, CollectionPut dto) {
        maybeUpdateBodyDocument(dto, entity);

        if (dto.getPublished() != null) {
            entity.setPublished(dto.getPublished());
        }
    }

    private void maybeUpdateBodyDocument(CollectionPut collectionPut, Collection collection) {
        if (!Strings.isNullOrEmpty(collectionPut.getTitle())) {
            collection.getBodyDocument().setTitle(collectionPut.getTitle());
        }
        if (!Strings.isNullOrEmpty(collectionPut.getSummary())) {
            collection.getBodyDocument().setSummary(collectionPut.getSummary());
        }
    }
}
