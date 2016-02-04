package org.consumersunion.stories.dashboard.client.application.widget.addto;

import java.util.Set;

import org.consumersunion.stories.common.client.util.WidgetIds;
import org.consumersunion.stories.common.shared.model.StorySelectField;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown;
import org.consumersunion.stories.dashboard.client.application.ui.token.TokenInput;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

class AddTagsToStoriesView extends AbstractAddToView<AddToUiHandlers>
        implements AddTagsToStoriesPresenter.MyView, TokenInput.Handler {
    interface Binder extends UiBinder<Widget, AddTagsToStoriesView> {
    }

    @UiField(provided = true)
    final ClickableDropDown<StorySelectField> selectDropDown;
    @UiField(provided = true)
    final TokenInput tags;

    @UiField
    HTMLPanel assignPanel;
    @UiField
    Button goAssign;

    @Inject
    AddTagsToStoriesView(
            Binder uiBinder,
            TokenInput tags,
            ClickableDropDown<StorySelectField> selectDropDown) {
        super(selectDropDown);

        this.tags = tags;
        this.selectDropDown = selectDropDown;

        initWidget(uiBinder.createAndBindUi(this));

        tags.setHandler(this);

        assignPanel.getElement().setId(WidgetIds.ADD_TAGS_TO_STORIES);
        selectDropDown.getElement().setId(WidgetIds.ADD_TAGS_TO_STORIES_DROPDOWN);
        goAssign.getElement().setId(WidgetIds.ADD_TAGS_TO_STORIES_GO_BUTTON);
    }

    @Override
    public void reset() {
        super.reset();

        tags.initialize();
        goAssign.setEnabled(false);
    }

    @Override
    public Set<String> getTags() {
        return tags.getTags();
    }

    @Override
    public void populateSuggestionsList(Set<String> tags) {
        this.tags.addSuggestion(tags);
    }

    @Override
    public void onTokenAdded(String token) {
        goAssign.setEnabled(!getTags().isEmpty());
    }

    @Override
    public void editComplete() {
    }

    @Override
    public void onTokenRemoved(String token) {
        goAssign.setEnabled(!getTags().isEmpty());
    }
}
