package org.consumersunion.stories.dashboard.client.resource;

import com.arcbees.gquery.tooltip.client.TooltipResources;

public interface AddDocumentTooltipResources extends TooltipResources {
    interface AddDocumentTooltipStyle extends TooltipStyle {
    }

    @Override
    @Source("css/AddDocumentTooltip.css")
    AddDocumentTooltipStyle css();
}
