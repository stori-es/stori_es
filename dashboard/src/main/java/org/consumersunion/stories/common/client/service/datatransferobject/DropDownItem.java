package org.consumersunion.stories.common.client.service.datatransferobject;

import java.io.Serializable;

import org.consumersunion.stories.common.shared.service.datatransferobject.DropDownType;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface DropDownItem extends Serializable, IsSerializable {
    String getLabel();

    boolean isVisible();

    DropDownType getDropDownType();
}
