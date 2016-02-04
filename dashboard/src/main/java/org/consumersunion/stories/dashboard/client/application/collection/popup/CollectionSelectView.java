package org.consumersunion.stories.dashboard.client.application.collection.popup;

import java.util.List;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.ui.form.Form;
import org.consumersunion.stories.common.client.ui.form.controls.ListInput;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

/**
 * Base view for {@link Collection} selection modal pop up. See {@link CollectionSelectPresenter}.
 */
public class CollectionSelectView extends PopupViewWithUiHandlers<CollectionSelectUiHandlers>
        implements CollectionSelectPresenter.MyView {
    interface Binder extends UiBinder<PopupPanel, CollectionSelectView> {
    }

    private final String COLLECTION_LIST_FIELD_NAME = "collectionList";
    private final Integer LIST_COLLECTION = 0;
    private final Integer NEW_COLLECTION = 1;

    @UiField
    DeckPanel switchPanel;
    @UiField
    TextBox collectionTitle;
    @UiField
    Label errorLabel;
    @UiField
    Form form;
    @UiField
    Button newCollection;
    @UiField
    InlineLabel modalTitle;
    @UiField
    Label newLabel;
    @UiField
    Button done;
    @UiField
    Anchor cancel;

    private final StoryTellerDashboardI18nLabels storyLabels;

    private ListInput collectionsList;

    @Inject
    CollectionSelectView(
            EventBus eventBus,
            Binder uiBinder,
            StoryTellerDashboardI18nLabels storyLabels) {
        super(eventBus);

        this.storyLabels = storyLabels;

        initWidget(uiBinder.createAndBindUi(this));

        done.getElement().setId("collection-select-done");
        cancel.getElement().setId("collection-select-cancel");
    }

    @Override
    public void setData(List<CollectionData> collections) {
        switchPanel.showWidget(LIST_COLLECTION);

        if (collections != null && !collections.isEmpty()) {
            String[] options = new String[collections.size()];
            String[] values = new String[collections.size()];

            for (int i = 0; i < collections.size(); i++) {
                String collectionTitle = Strings.nullToEmpty(collections.get(i).getTitle());
                options[i] = collectionTitle.length() > 55 ?
                        collectionTitle.substring(0, 55) + "..." : collectionTitle;
                values[i] = "" + collections.get(i).getId();
            }

            if (collectionsList == null) {
                collectionsList = new ListInput("", COLLECTION_LIST_FIELD_NAME, options, values, false, false, true);
                collectionsList = form.add(collectionsList);
                collectionsList.getElement().setId("collection-add-select");
            } else {
                collectionsList.setLabelsAndOptions(options, values);
            }
        } else {
            if (collectionsList != null) {
                collectionsList.removeFromParent();
                collectionsList = null;
            }
        }
    }

    @Override
    public void linkActionTitle(Boolean forQuestionnaire) {
        newCollection.setText(storyLabels.createNew());
        if (forQuestionnaire) {
            modalTitle.setText(storyLabels.addSourceQuestionnaire());
            newLabel.setText(storyLabels.newQuestionnaire());
            $(collectionTitle).attr("placeholder", storyLabels.newQuestionnaireTitle());
        } else {
            modalTitle.setText(storyLabels.newCollection());
            newLabel.setText(storyLabels.newCollection());
            $(collectionTitle).attr("placeholder", storyLabels.newCollectionTitle());
        }
    }

    @Override
    public void restoreAssignmentTitle() {
        newCollection.setText(storyLabels.newCollection());
        modalTitle.setText(storyLabels.addTo());
        newLabel.setText(storyLabels.addTo());
        $(collectionTitle).attr("placeholder", storyLabels.newCollectionTitle());
    }

    @UiHandler("newCollection")
    void onNewCollectionClicked(ClickEvent event) {
        createNew();
    }

    @UiHandler("done")
    void onDoneClicked(ClickEvent event) {
        if (switchPanel.getVisibleWidget() == LIST_COLLECTION) {
            List<String> collectionIds = form.getMultiValue(COLLECTION_LIST_FIELD_NAME);
            getUiHandlers().associateNotification(collectionIds);
            hide();
            form.clear();
            collectionsList = null;
        } else if (switchPanel.getVisibleWidget() == NEW_COLLECTION) {
            if (Strings.isNullOrEmpty(collectionTitle.getText())) {
                errorLabel.setText(storyLabels.requiredCollectionTitle());
            } else {
                getUiHandlers().createAndAssociateNotification(collectionTitle.getText());
                hide();
            }
        }
    }

    @UiHandler("cancel")
    void onCancelClicked(ClickEvent event) {
        hide();
        form.clear();
        collectionsList = null;
    }

    private void createNew() {
        switchPanel.showWidget(NEW_COLLECTION);
        collectionTitle.setText("");
        errorLabel.setText("");
    }
}
