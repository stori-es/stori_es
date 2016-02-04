package org.consumersunion.stories.common.client.ui;

import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.common.client.ui.EntitySuggestionOracle.EntitySuggestion;
import org.consumersunion.stories.common.client.ui.EntitySuggestionOracle.EntitySuggestionHandler;
import org.consumersunion.stories.common.shared.model.HasTitle;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class EntitySuggest<T extends HasTitle> extends Composite {
    interface Binder extends UiBinder<Widget, EntitySuggest> {
    }

    @UiField(provided = true)
    final SuggestBox itemBox;

    @UiField
    CommonResources resources;

    private Boolean enabled;
    private EntitySuggestionHandler<T> handler;

    @Inject
    EntitySuggest(
            Binder uiBinder,
            @Assisted SuggestOracle suggestOracle) {
        this.itemBox = new SuggestBox(suggestOracle);

        initWidget(uiBinder.createAndBindUi(this));
        setEnabled(true);

        itemBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                itemBox.setText("");
                if (handler != null) {
                    EntitySuggestion<T> selectedItem = (EntitySuggestion) event.getSelectedItem();
                    handler.onEntitySelected(selectedItem.getEntity());
                }
            }
        });

        itemBox.setAutoSelectEnabled(false);
    }

    public void initialize() {
        itemBox.setText("");
    }

    public void setPlaceHolder(String text) {
        itemBox.getElement().setAttribute("placeholder", text);
    }

    public void setHandler(EntitySuggestionHandler<T> handler) {
        this.handler = handler;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;

        GQuery.$(itemBox).toggleClass(resources.generalStyleCss().disabled(), !enabled);
        itemBox.setEnabled(enabled);
        itemBox.setFocus(enabled);
    }
}
