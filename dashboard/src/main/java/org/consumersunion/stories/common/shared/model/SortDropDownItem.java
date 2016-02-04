package org.consumersunion.stories.common.shared.model;

import org.consumersunion.stories.common.client.service.datatransferobject.DropDownItem;
import org.consumersunion.stories.common.shared.model.entity.SortField;

public interface SortDropDownItem extends DropDownItem {
    SortField getSortField();
}
