package org.consumersunion.stories.dashboard.client.application.ui.builder;

import org.consumersunion.stories.dashboard.client.application.ui.block.MetaBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.MetaBlockHandler;

public interface MetaBlockFactory {
    MetaBlock createBlock(MetaBlockHandler handler);

    MetaBlockDrawer createDrawer(MetaBlockHandler handler);
}
