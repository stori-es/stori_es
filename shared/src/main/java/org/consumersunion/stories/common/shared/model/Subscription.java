package org.consumersunion.stories.common.shared.model;

import com.google.common.base.MoreObjects;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Subscription implements IsSerializable {
    private int profile;
    private int target;
    private NotificationTrigger type;
    private boolean active = true;

    public static Builder builder() {
        return new Builder();
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public NotificationTrigger getType() {
        return type;
    }

    public void setType(NotificationTrigger type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("profile", profile)
                .add("target", target)
                .add("type", type)
                .add("active", active)
                .toString();
    }

    public static class Builder {
        private int profile;
        private int target;
        private NotificationTrigger type;
        private boolean active;

        private Builder() {
        }

        public Builder withProfile(int profile) {
            this.profile = profile;
            return this;
        }

        public Builder withTarget(int target) {
            this.target = target;
            return this;
        }

        public Builder withType(NotificationTrigger type) {
            this.type = type;
            return this;
        }

        public Builder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public Subscription build() {
            Subscription subscription = new Subscription();

            subscription.setProfile(profile);
            subscription.setTarget(target);
            subscription.setType(type);
            subscription.setActive(active);

            return subscription;
        }
    }
}
