package org.consumersunion.stories.server.export.renderers.storyteller;

import org.consumersunion.stories.server.export.StoryTellerCsv;
import org.consumersunion.stories.server.export.renderers.ColumnRenderer;
import org.springframework.stereotype.Component;

@Component
public class StoryTellerSubscriptionRenderer implements ColumnRenderer<StoryTellerCsv> {
    @Override
    public String render(StoryTellerCsv data) {
        Boolean subscription = data.isSubscription();

        return subscription == null ? null : String.valueOf(subscription);
    }
}
