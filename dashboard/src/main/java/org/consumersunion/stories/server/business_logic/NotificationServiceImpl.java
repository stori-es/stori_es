package org.consumersunion.stories.server.business_logic;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.Subscription;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.server.business_logic.interceptor.OnlyIfEmptyQueue;
import org.consumersunion.stories.server.cache.MemcacheKeys;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.notification_channel.NotificationChannel;
import org.consumersunion.stories.server.notification_channel.NotificationEvent;
import org.consumersunion.stories.server.notification_channel.NotificationEventHandler;
import org.consumersunion.stories.server.notification_channel.SubscriptionSummary;
import org.consumersunion.stories.server.persistence.SubscriptionPersister;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@Service
public class NotificationServiceImpl implements NotificationService, NotificationEventHandler {
    private final SystemEntityService systemEntityService;
    private final UserService userService;
    private final AuthorizationService authService;
    private final SubscriptionPersister subscriptionPersister;
    private final List<NotificationChannel> notificationChannels;

    @Inject
    NotificationServiceImpl(
            SystemEntityService systemEntityService,
            UserService userService,
            AuthorizationService authService,
            SubscriptionPersister subscriptionPersister,
            List<NotificationChannel> notificationChannels,
            EventBus eventBus) {
        this.systemEntityService = systemEntityService;
        this.userService = userService;
        this.authService = authService;
        this.subscriptionPersister = subscriptionPersister;
        this.notificationChannels = Collections.unmodifiableList(notificationChannels);

        eventBus.register(this);
    }

    @Override
    @Subscribe
    public void onNotification(NotificationEvent event) {
        SystemEntity systemEntity = event.getSystemEntity();
        NotificationTrigger eventType = event.getEventType();

        List<SubscriptionSummary> subscriptions = subscriptionPersister.getActiveSubscriptions(systemEntity, eventType);

        try {
            for (NotificationChannel channelHandler : notificationChannels) {
                if (channelHandler.canHandle(eventType)) {
                    channelHandler.handleAll(event, subscriptions);
                }
            }
        } finally {
            for (NotificationChannel channelHandler : notificationChannels) {
                channelHandler.flush();
            }
        }
    }

    @Override
    @Scheduled(cron = "0 0 0/1 * * *")
    @OnlyIfEmptyQueue(MemcacheKeys.NOTIFICATION_PROCESSING)
    public void cleanupSubscriptions() {
        subscriptionPersister.deleteInactives();
    }

    @Override
    public void subscribe(int target, NotificationTrigger event) {
        validateTarget(target, event);

        subscriptionPersister.create(Subscription.builder()
                .withType(event)
                .withTarget(target)
                .withProfile(userService.getActiveProfileId())
                .build());
    }

    @Override
    public void unsubscribe(int target, NotificationTrigger event) {
        subscriptionPersister.setInactive(Subscription.builder()
                .withType(event)
                .withTarget(target)
                .withProfile(userService.getActiveProfileId())
                .build());
    }

    private void validateTarget(int target, NotificationTrigger event) {
        SystemEntity systemEntity = systemEntityService.getSystemEntity(target);

        Preconditions.checkNotNull(systemEntity);

        if (authService.canRead(systemEntity)) {
            for (Class<? extends SystemEntity> validTarget : event.getValidTargets()) {
                if (validTarget.isInstance(systemEntity)) {
                    return;
                }
            }
        } else {
            throw new NotAuthorizedException();
        }

        throw new IllegalArgumentException(
                event.name() + " is not valid for " + systemEntity.getClass().getSimpleName());
    }
}
