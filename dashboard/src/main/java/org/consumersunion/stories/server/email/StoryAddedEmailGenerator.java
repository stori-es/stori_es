package org.consumersunion.stories.server.email;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.notification_channel.SubscriptionSummary;
import org.consumersunion.stories.server.util.HtmlStripper;
import org.consumersunion.stories.server.velocity.VelocityWrapper;
import org.consumersunion.stories.server.velocity.VelocityWrapperFactory;
import org.consumersunion.stories.server.velocity.template.TemplatesLocation;
import org.springframework.stereotype.Component;

@Component
public class StoryAddedEmailGenerator {
    private static final String TEMPLATE = TemplatesLocation.emails("storyAdded.vm");
    private static final String SUBJECT = "[stori.es] Story Added";

    private final VelocityWrapperFactory velocityWrapperFactory;
    private final HtmlStripper htmlStripper;

    @Inject
    StoryAddedEmailGenerator(
            VelocityWrapperFactory velocityWrapperFactory,
            HtmlStripper htmlStripper) {
        this.velocityWrapperFactory = velocityWrapperFactory;
        this.htmlStripper = htmlStripper;
    }

    /**
     * Generates the concrete {@link EmailData} for a given Story, Collection, and subscription. Note that the method
     * expects that {@code subscriptionSummary.getProfileSummary().getPrimaryEmail()} returns a valid email address.
     * This condition should be verified before invoking this method.
     *
     * @param storySummary
     * @param collection
     * @param subscriptionSummary
     * @return
     */
    public EmailData generate(
            StorySummary storySummary,
            Collection collection,
            SubscriptionSummary subscriptionSummary) {
        String fullText = htmlStripper.striptHtml(storySummary.getFullText());

        VelocityWrapper velocityWrapper = velocityWrapperFactory.create(TEMPLATE);

        velocityWrapper.put("storySummary", storySummary);
        velocityWrapper.put("fullText", fullText);
        velocityWrapper.put("collection", collection);
        velocityWrapper.put("subscriptionSummary", subscriptionSummary);
        velocityWrapper.put("protocolAndHost",
                System.getProperty("sys.unsubscribeProtocolAndHost", "http://localhost:8080"));

        String body = velocityWrapper.generate();

        ProfileSummary profileSummary = subscriptionSummary.getProfileSummary();

        return new EmailData(SUBJECT, body, profileSummary.getPrimaryEmail(),
                profileSummary.getProfile().getFullName());
    }
}
