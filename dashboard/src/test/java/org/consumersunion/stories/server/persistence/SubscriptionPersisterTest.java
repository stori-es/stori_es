package org.consumersunion.stories.server.persistence;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.Subscription;

import static org.consumersunion.stories.common.shared.model.NotificationTrigger.STORY_ADDED;

public class SubscriptionPersisterTest extends SpringTestCase {
    private static final int ALREAD_EXISTING_TARGET = 960;
    private static final int PROFILE = 1001;

    @Inject
    SubscriptionPersister subscriptionPersister;

    private final Subscription preExistingSubscription = Subscription.builder()
            .withProfile(PROFILE)
            .withTarget(ALREAD_EXISTING_TARGET)
            .withType(STORY_ADDED)
            .build();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        subscriptionPersister.deleteAll();
        subscriptionPersister.create(preExistingSubscription);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        subscriptionPersister.deleteAll();
    }

    public void test_get() {
        List<Subscription> subscriptions = subscriptionPersister.getSubscriptions(ALREAD_EXISTING_TARGET, STORY_ADDED);

        assertEquals(1, subscriptions.size());
    }

    public void test_create() {
        int target = 961;
        NotificationTrigger type = STORY_ADDED;

        subscriptionPersister.create(Subscription.builder()
                .withProfile(PROFILE)
                .withTarget(target)
                .withType(type)
                .build());

        List<Subscription> subscriptions = subscriptionPersister.getSubscriptions(target, type);
        assertEquals(1, subscriptions.size());
    }

    public void test_delete() {
        int target = 962;
        NotificationTrigger type = STORY_ADDED;
        Subscription subscription = Subscription.builder()
                .withProfile(PROFILE)
                .withTarget(target)
                .withType(type)
                .build();

        subscriptionPersister.create(subscription);

        subscriptionPersister.setInactive(subscription);

        List<Subscription> subscriptions = subscriptionPersister.getSubscriptions(target, type);
        assertFalse(subscriptions.get(0).isActive());
    }
}
