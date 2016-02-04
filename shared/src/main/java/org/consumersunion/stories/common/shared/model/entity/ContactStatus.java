package org.consumersunion.stories.common.shared.model.entity;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public enum ContactStatus implements Serializable {
    UNVERIFIED,
    VERIFIED,
    BOUNCED,
    PERM_BOUNCED,
    COMPLAINTS,
    UNSUBSCRIBED;

    private final static List<ContactStatus> errorStatuses = Lists.newArrayList(PERM_BOUNCED, COMPLAINTS);
    private final static List<ContactStatus> warningStatuses = Lists.newArrayList(BOUNCED);

    public boolean isError() {
        return errorStatuses.contains(this);
    }

    public boolean isWarning() {
        return warningStatuses.contains(this);
    }
}
