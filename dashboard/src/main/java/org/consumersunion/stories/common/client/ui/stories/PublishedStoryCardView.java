package org.consumersunion.stories.common.client.ui.stories;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import static com.google.gwt.query.client.GQuery.$;

public class PublishedStoryCardView extends ViewImpl implements PublishedStoryCardPresenter.MyView {
    interface Binder extends UiBinder<Widget, PublishedStoryCardView> {
    }

    private static final String UNTITLED = "Untitled";

    @UiField
    DivElement title;
    @UiField
    DivElement author;
    @UiField
    DivElement story;

    private final StoryTellerDashboardI18nLabels dashboardLabels;

    @Inject
    PublishedStoryCardView(Binder uiBinder,
            StoryTellerDashboardI18nLabels labels) {
        this.dashboardLabels = labels;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void init(StorySummary storySummary) {
        story.setInnerHTML(storySummary.getFullText());

        Story story = storySummary.getStory();

        String authorText;
        Address address = storySummary.getAddress();
        if (address == null || (address.getState() == null && address.getCity() == null)) {
            authorText = dashboardLabels.authorWithoutAddress(storySummary.getAuthorGivenName(), story.getCreated());
        } else if (address.getState() == null) {
            authorText = dashboardLabels.authorWithState(storySummary.getAuthorGivenName(), address.getState(),
                    story.getCreated());
        } else {
            authorText = dashboardLabels.authorWithAddress(storySummary.getAuthorGivenName(), address.getCity(),
                    address.getState(), story.getCreated());
        }

        String storyTitle = storySummary.getTitle();
        if (!Strings.isNullOrEmpty(storyTitle) && !UNTITLED.equals(storyTitle)) {
            title.setInnerText(storyTitle);
            author.setInnerText(authorText);
        } else {
            title.setInnerText(authorText);
            $(author).hide();
        }
    }
}
