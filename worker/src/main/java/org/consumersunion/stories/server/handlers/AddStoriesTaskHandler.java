package org.consumersunion.stories.server.handlers;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.tasks.AddStoriesToCollectionTask;
import org.consumersunion.stories.server.business_logic.CollectionService;
import org.springframework.stereotype.Component;

@Component
public class AddStoriesTaskHandler implements Handler<AddStoriesToCollectionTask> {
    private final CollectionService collectionService;

    @Inject
    AddStoriesTaskHandler(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Override
    public boolean canHandle(Object element) {
        return element instanceof AddStoriesToCollectionTask;
    }

    @Override
    public void handle(AddStoriesToCollectionTask task) throws Exception {
        collectionService.linkStoriesToCollection(task);
    }
}
