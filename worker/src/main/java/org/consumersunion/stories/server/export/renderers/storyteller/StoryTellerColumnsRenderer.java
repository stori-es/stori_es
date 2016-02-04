package org.consumersunion.stories.server.export.renderers.storyteller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.server.annotations.DateCreated;
import org.consumersunion.stories.server.annotations.DateModified;
import org.consumersunion.stories.server.export.StoryTellerCsv;
import org.consumersunion.stories.server.export.renderers.ColumnsRenderer;
import org.consumersunion.stories.server.export.renderers.DateRenderer;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import static org.consumersunion.stories.server.export.StoryColumnsName.CITY;
import static org.consumersunion.stories.server.export.StoryColumnsName.CREATED_AT;
import static org.consumersunion.stories.server.export.StoryColumnsName.EMAIL;
import static org.consumersunion.stories.server.export.StoryColumnsName.EMAIL_FORMAT;
import static org.consumersunion.stories.server.export.StoryColumnsName.GIVEN_NAME;
import static org.consumersunion.stories.server.export.StoryColumnsName.MODIFIED_AT;
import static org.consumersunion.stories.server.export.StoryColumnsName.PHONE;
import static org.consumersunion.stories.server.export.StoryColumnsName.PROFILE_HREF;
import static org.consumersunion.stories.server.export.StoryColumnsName.PROFILE_ID;
import static org.consumersunion.stories.server.export.StoryColumnsName.STATE;
import static org.consumersunion.stories.server.export.StoryColumnsName.STREET;
import static org.consumersunion.stories.server.export.StoryColumnsName.SUBSCRIPTION;
import static org.consumersunion.stories.server.export.StoryColumnsName.SURNAME;
import static org.consumersunion.stories.server.export.StoryColumnsName.USERNAME;
import static org.consumersunion.stories.server.export.StoryColumnsName.ZIPCODE;

@Component
public class StoryTellerColumnsRenderer implements ColumnsRenderer<StoryTellerCsv> {
    private final StoryTellerSubscriptionRenderer subscriptionRenderer;
    private final ProfileUrlRenderer profileUrlRenderer;
    private final DateRenderer<StoryTellerCsv> storyDateCreatedRenderer;
    private final DateRenderer<StoryTellerCsv> storyDateModifiedRenderer;

    @Inject
    public StoryTellerColumnsRenderer(
            StoryTellerSubscriptionRenderer subscriptionRenderer,
            ProfileUrlRenderer profileUrlRenderer,
            @DateCreated DateRenderer<StoryTellerCsv> storyDateCreatedRenderer,
            @DateModified DateRenderer<StoryTellerCsv> storyDateModifiedRenderer) {
        this.subscriptionRenderer = subscriptionRenderer;
        this.profileUrlRenderer = profileUrlRenderer;
        this.storyDateCreatedRenderer = storyDateCreatedRenderer;
        this.storyDateModifiedRenderer = storyDateModifiedRenderer;
    }

    @Override
    public List<String> render(StoryTellerCsv data) {
        ArrayList<String> values = Lists.newArrayList();

        values.add(data.getUsername());
        values.add(data.getId());
        values.add(data.getGivenName());
        values.add(data.getSurname());
        values.add(data.getEmail());
        values.add(data.getStreet());
        values.add(data.getCity());
        values.add(data.getState());
        values.add(data.getZipCode());
        values.add(data.getPhone());
        values.add(subscriptionRenderer.render(data));
        values.add(data.getEmailFormat());
        values.add(storyDateCreatedRenderer.render(data));
        values.add(storyDateModifiedRenderer.render(data));
        values.add(profileUrlRenderer.render(data));

        return values;
    }

    @Override
    public List<String> getColumns() {
        return Lists.newArrayList(USERNAME, PROFILE_ID, GIVEN_NAME, SURNAME, EMAIL, STREET, CITY, STATE, ZIPCODE,
                PHONE, SUBSCRIPTION, EMAIL_FORMAT, CREATED_AT, MODIFIED_AT, PROFILE_HREF);
    }
}
