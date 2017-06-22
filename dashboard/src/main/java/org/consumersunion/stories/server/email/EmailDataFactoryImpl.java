package org.consumersunion.stories.server.email;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.notification_channel.NotificationEvent;
import org.consumersunion.stories.server.notification_channel.StoriesAddedEvent;
import org.consumersunion.stories.server.notification_channel.SubscriptionSummary;
import org.consumersunion.stories.server.persistence.StoryPersister;
import org.consumersunion.stories.server.persistence.StoryPersister.StoryPagedRetrieveParams;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

@Component
public class EmailDataFactoryImpl implements EmailDataFactory {
    private final StoryAddedEmailGenerator storyAddedEmailGenerator;
    private final StoryPersister storyPersister;

    @Inject
    EmailDataFactoryImpl(
            StoryAddedEmailGenerator storyAddedEmailGenerator,
            StoryPersister storyPersister) {
        this.storyAddedEmailGenerator = storyAddedEmailGenerator;
        this.storyPersister = storyPersister;
    }

    @Override
    public List<EmailData> create(NotificationEvent event, List<SubscriptionSummary> subscriptions) {
        switch (event.getEventType()) {
            case STORY_ADDED:
                return generateStoryAddedEmail(event, subscriptions);
            default:
                throw new IllegalArgumentException(event.getEventType().name());
        }
    }

    private List<EmailData> generateStoryAddedEmail(
            NotificationEvent event,
            final List<SubscriptionSummary> subscriptions) {
        if (subscriptions.isEmpty()) {
            return Lists.newArrayList();
        }

        StoriesAddedEvent storiesAddedEvent = (StoriesAddedEvent) event;
        final Collection collection = (Collection) storiesAddedEvent.getSystemEntity();

        List<StorySummary> storySummaries = getStorySummaries(storiesAddedEvent, collection);

        return FluentIterable.from(storySummaries)
                .transformAndConcat(new Function<StorySummary, List<EmailData>>() {
                    @Override
                    public List<EmailData> apply(StorySummary input) {
                        return getEmailDatasFromSubscriptions(input, subscriptions, collection);
                    }
                }).toList();
    }

    private List<EmailData> getEmailDatasFromSubscriptions(
            final StorySummary storySummary,
            List<SubscriptionSummary> subscriptions,
            final Collection collection) {
        return FluentIterable.from(subscriptions)
                .filter(new Predicate<SubscriptionSummary>() {
                    @Override
                    public boolean apply(SubscriptionSummary input) {
                        return input.getSubscription().isActive()
                                && !Strings.isNullOrEmpty(input.getProfileSummary().getPrimaryEmail());
                    }
                }).transform(new Function<SubscriptionSummary, EmailData>() {
                    @Override
                    public EmailData apply(final SubscriptionSummary input) {
                        return storyAddedEmailGenerator.generate(storySummary, collection, input);
                    }
                }).toList();
    }

    private List<StorySummary> getStorySummaries(StoriesAddedEvent event, Collection collection) {
        StoryPagedRetrieveParams params = new StoryPagedRetrieveParams(0, event.getStories().size(),
                StorySortField.TITLE_A_Z, true, generateIdsSearch(event.getStories()), collection.getId(), null,
                null, null, null, null, null, AuthConstants.ACCESS_MODE_ANY, null);

        return storyPersister.getStoriesPaged(params, true);
    }

    private String generateIdsSearch(List<Story> stories) {
        String ids = Joiner.on(" OR ").join(Lists.transform(stories, new Function<Story, String>() {
            @Override
            public String apply(Story input) {
                return String.valueOf(input.getId());
            }
        }));

        return "_id:(" + ids + ")";
    }
}
