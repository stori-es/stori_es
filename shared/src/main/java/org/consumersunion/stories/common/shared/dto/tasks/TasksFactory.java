package org.consumersunion.stories.common.shared.dto.tasks;

import java.util.Set;

import org.consumersunion.stories.common.shared.ExportContainer;
import org.consumersunion.stories.common.shared.ExportKind;

import com.google.inject.assistedinject.Assisted;

public interface TasksFactory {
    ExportTask createExportTask(
            @Assisted("profileId") int profileId,
            ExportKind kind,
            ExportContainer container,
            @Assisted("objectId") int objectId);

    AddStoriesToCollectionTask createAddStoriesTask(
            @Assisted("profileId") int profileId,
            Set<Integer> collectionIds,
            String searchToken,
            @Assisted("collectionId") Integer collectionId,
            @Assisted("questionnaireId") Integer questionnaireId);
}
