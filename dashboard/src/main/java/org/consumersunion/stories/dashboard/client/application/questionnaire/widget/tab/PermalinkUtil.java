package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import org.consumersunion.stories.common.shared.model.Collection;

public interface PermalinkUtil {
    String getFullPermalink(String permalink, boolean isQuestionnaire);

    String getPermalink(String permalink);

    String getPermalink(String permalink, String previewKey);

    void openPermalink(String permalinkUrl, boolean isQuestionnaire);

    String getStaticPart(Collection collection);
}
