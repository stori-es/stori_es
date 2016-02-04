package org.consumersunion.stories.dashboard.client.resource;

import com.arcbees.gquery.tooltip.client.TooltipResources;

public interface AddToTooltipResources extends TooltipResources {
    interface AddToTooltipStyle extends TooltipStyle {
    }

    @Override
    @Source("css/AddToTooltip.css")
    AddToTooltipStyle css();
}
