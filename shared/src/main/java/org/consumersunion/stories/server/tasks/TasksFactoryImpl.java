package org.consumersunion.stories.server.tasks;

import java.util.Set;

import org.consumersunion.stories.common.shared.ExportContainer;
import org.consumersunion.stories.common.shared.ExportKind;
import org.consumersunion.stories.common.shared.dto.tasks.AddStoriesToCollectionTask;
import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;
import org.consumersunion.stories.common.shared.dto.tasks.TasksFactory;
import org.springframework.stereotype.Component;

@Component
public class TasksFactoryImpl implements TasksFactory {
    @Override
    public ExportTask createExportTask(int profileId, ExportKind kind, ExportContainer container, int objectId) {
        return new ExportTask(profileId, kind, container, objectId);
    }

    @Override
    public AddStoriesToCollectionTask createAddStoriesTask(
            int profileId,
            Set<Integer> collectionIds,
            String searchToken,
            Integer collectionId,
            Integer questionnaireId) {

        return new AddStoriesToCollectionTask(profileId, collectionIds, searchToken, collectionId, questionnaireId);
    }
}
