package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import java.util.LinkedHashSet;
import java.util.Set;

import org.consumersunion.stories.dashboard.client.application.ui.TagsTextConverter;
import org.consumersunion.stories.dashboard.client.application.ui.token.TokenInput;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class OnSubmitTabView extends ViewWithUiHandlers<OnSubmitTabUiHandlers>
        implements OnSubmitTabPresenter.MyView, TokenInput.Handler {
    interface Binder extends UiBinder<Widget, OnSubmitTabView> {
    }

    @UiField(provided = true)
    final TokenInput tokens;

    private final TagsTextConverter tagsTextConverter;

    @Inject
    OnSubmitTabView(
            Binder uiBinder,
            TokenInput tokens,
            TagsTextConverter tagsTextConverter) {
        this.tokens = tokens;
        this.tagsTextConverter = tagsTextConverter;

        initWidget(uiBinder.createAndBindUi(this));

        tokens.setHandler(this);
    }

    @Override
    public void setTags(Set<String> tags) {
        String tagsStr = tagsTextConverter.toText(tags);
        tokens.setValue(tagsStr);
    }

    @Override
    public void populateSuggestionList(Set<String> tags) {
        tokens.clearSuggestion();
        tokens.addSuggestion(tags);
    }

    @Override
    public void setFocus() {
        tokens.setFocus(true);
    }

    @Override
    public void onTokenAdded(String token) {
        Set<String> tags = new LinkedHashSet<String>();
        tags.addAll(tagsTextConverter.formatText(tokens.getValue()));
        getUiHandlers().saveQuestionnaireTags(tags);
    }

    @Override
    public void onTokenRemoved(String token) {
        Set<String> tags = new LinkedHashSet<String>();
        tags.addAll(tagsTextConverter.formatText(tokens.getValue()));
        getUiHandlers().saveQuestionnaireTags(tags);
    }

    @Override
    public void editComplete() {
    }
}
