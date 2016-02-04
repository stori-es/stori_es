package org.consumersunion.stories.server.business_logic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.Subscription;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.server.notification_channel.NotificationChannel;
import org.consumersunion.stories.server.notification_channel.NotificationEvent;
import org.consumersunion.stories.server.notification_channel.StoriesAddedEvent;
import org.consumersunion.stories.server.notification_channel.SubscriptionSummary;
import org.consumersunion.stories.server.persistence.SubscriptionPersister;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.inject.TypeLiteral;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public class NotificationServiceImplTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(SubscriptionPersister.class);
            forceMock(EventBus.class);

            bind(new TypeLiteral<List<NotificationChannel>>() {
            }).toInstance(new ArrayList<NotificationChannel>());
        }
    }

    private static final int TARGET = 1;

    @Inject
    NotificationServiceImpl notificationService;
    @Inject
    SystemEntityService systemEntityService;
    @Inject
    AuthorizationService authService;
    @Inject
    SubscriptionPersister subscriptionPersister;
    @Inject
    List<NotificationChannel> notificationChannels;

    @Test
    public void onNotification_storyAdded_notifyGoodChannels() {
        NotificationChannel notificationChannel1 = Mockito.mock(NotificationChannel.class);
        NotificationChannel notificationChannel2 = Mockito.mock(NotificationChannel.class);
        SystemEntity systemEntity = Mockito.mock(SystemEntity.class);
        NotificationEvent event = new StoriesAddedEvent(systemEntity, Lists.<Story>newArrayList());
        List<SubscriptionSummary> subscriptionSummaries = Lists.newArrayList();

        notificationChannels.add(notificationChannel1);
        notificationChannels.add(notificationChannel2);

        when(notificationChannel1.canHandle(NotificationTrigger.STORY_ADDED)).thenReturn(true);
        when(notificationChannel2.canHandle(NotificationTrigger.STORY_ADDED)).thenReturn(false);
        when(subscriptionPersister.getActiveSubscriptions(Matchers.same(systemEntity),
                eq(NotificationTrigger.STORY_ADDED)))
                .thenReturn(subscriptionSummaries);

        notificationService.onNotification(event);

        InOrder inOrder = Mockito.inOrder(notificationChannel1);
        inOrder.verify(notificationChannel1).handleAll(Matchers.same(event), Matchers.same(subscriptionSummaries));
        inOrder.verify(notificationChannel1).flush();

        Mockito.verify(notificationChannel2, Mockito.never()).handleAll(
                Matchers.same(event), Matchers.same(subscriptionSummaries));
    }

    @Test
    public void subscribe_collection() {
        Collection collection = Mockito.mock(Collection.class);
        when(systemEntityService.getSystemEntity(TARGET)).thenReturn(collection);
        when(authService.canRead(collection)).thenReturn(true);

        notificationService.subscribe(TARGET, NotificationTrigger.STORY_ADDED);
    }

    @Test
    public void subscribe_questionnaire() {
        Questionnaire questionnaire = Mockito.mock(Questionnaire.class);
        when(systemEntityService.getSystemEntity(TARGET)).thenReturn(questionnaire);
        when(authService.canRead(questionnaire)).thenReturn(true);

        notificationService.subscribe(TARGET, NotificationTrigger.STORY_ADDED);
    }

    @Test
    public void subscribe_questionnaireI15d() {
        QuestionnaireI15d questionnaire = Mockito.mock(QuestionnaireI15d.class);
        when(systemEntityService.getSystemEntity(TARGET)).thenReturn(questionnaire);
        when(authService.canRead(questionnaire)).thenReturn(true);

        notificationService.subscribe(TARGET, NotificationTrigger.STORY_ADDED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void subscribe_invalidTarget_fails() {
        Story story = Mockito.mock(Story.class);
        when(systemEntityService.getSystemEntity(TARGET)).thenReturn(story);
        when(authService.canRead(story)).thenReturn(true);

        notificationService.subscribe(TARGET, NotificationTrigger.STORY_ADDED);
    }

    @Test
    public void unsubscribe_deletesSubscription(SubscriptionPersister subscriptionPersister) {
        notificationService.unsubscribe(1, NotificationTrigger.STORY_ADDED);

        Mockito.verify(subscriptionPersister).setInactive(any(Subscription.class));
    }
}
