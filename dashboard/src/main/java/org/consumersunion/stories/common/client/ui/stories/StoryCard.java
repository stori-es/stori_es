package org.consumersunion.stories.common.client.ui.stories;

import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

public interface StoryCard extends Redrawable {
    StorySummary getStorySummary();
}
