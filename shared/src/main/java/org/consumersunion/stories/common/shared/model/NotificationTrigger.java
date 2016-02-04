package org.consumersunion.stories.common.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum NotificationTrigger implements IsSerializable {
    STORY_ADDED(Collection.class);

    private final Class<? extends SystemEntity>[] validTargets;

    NotificationTrigger(Class<? extends SystemEntity>... validTargets) {
        this.validTargets = validTargets;
    }

    public Class<? extends SystemEntity>[] getValidTargets() {
        return validTargets;
    }
}
