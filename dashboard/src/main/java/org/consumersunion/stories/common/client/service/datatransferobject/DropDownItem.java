package org.consumersunion.stories.common.client.service.datatransferobject;

import java.io.Serializable;

import org.consumersunion.stories.common.shared.service.datatransferobject.DropDownType;

public interface DropDownItem extends Serializable {
    String getLabel();

    boolean isVisible();

    DropDownType getDropDownType();
}
