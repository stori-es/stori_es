package org.consumersunion.stories.server.notification_channel;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Subscription;
import org.consumersunion.stories.common.shared.model.SystemEntity;

import com.google.common.base.MoreObjects;

/**
 * Encapsulates a {@link org.consumersunion.stories.common.shared.model.Subscription} and related data :
 * the associated {@link org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary} and
 * related {@link org.consumersunion.stories.common.shared.model.SystemEntity}
 */
public class SubscriptionSummary {
    private final Subscription subscription;
    private final ProfileSummary profileSummary;
    private final SystemEntity systemEntity;

    public SubscriptionSummary(
            Subscription subscription,
            ProfileSummary profileSummary,
            SystemEntity systemEntity) {
        this.subscription = subscription;
        this.profileSummary = profileSummary;
        this.systemEntity = systemEntity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public ProfileSummary getProfileSummary() {
        return profileSummary;
    }

    public SystemEntity getSystemEntity() {
        return systemEntity;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("subscription", subscription)
                .add("profileSummary", profileSummary)
                .add("systemEntity", systemEntity)
                .toString();
    }

    public static class Builder {
        private Subscription subscription;
        private ProfileSummary profileSummary;
        private SystemEntity systemEntity;

        private Builder() {
        }

        public Builder subscription(Subscription subscription) {
            this.subscription = subscription;
            return this;
        }

        public Builder profileSummary(ProfileSummary profileSummary) {
            this.profileSummary = profileSummary;
            return this;
        }

        public Builder systemEntity(SystemEntity systemEntity) {
            this.systemEntity = systemEntity;
            return this;
        }

        public SubscriptionSummary build() {
            return new SubscriptionSummary(subscription, profileSummary, systemEntity);
        }
    }
}
