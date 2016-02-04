package org.consumersunion.stories.dashboard.client.application.profile;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.model.StorySortFieldDropDownItem;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown.DropDownHandler;
import org.consumersunion.stories.dashboard.client.resource.CardsStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.MoreObjects;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class ProfileView extends ViewWithUiHandlers<ProfileUiHandlers> implements ProfilePresenter.MyView {
    interface Binder extends UiBinder<Widget, ProfileView> {
    }

    @UiField(provided = true)
    final ClickableDropDown<StorySortFieldDropDownItem> sortDropDown;

    @UiField
    Label name;
    @UiField
    TextBox editableName;
    @UiField
    Label lastName;
    @UiField
    TextBox editableLastName;
    @UiField
    SimplePanel personProfilePanel;
    @UiField
    SimplePanel notesManagerPanel;
    @UiField
    SimplePanel otherStoriesPanel;
    @UiField
    TextBox search;
    @UiField
    HTML totalCount;
    @UiField
    SimplePanel attachmentsPanel;
    @UiField
    Resources resource;

    private final StoryTellerDashboardI18nLabels storyTellerLabels;
    private final NumberFormat numberFormat;

    private Profile editedProfile;

    @Inject
    ProfileView(
            Binder uiBinder,
            ClickableDropDown<StorySortFieldDropDownItem> sortDropDown,
            CommonI18nLabels labels,
            StoryTellerDashboardI18nLabels storyTellerLabels) {
        this.sortDropDown = sortDropDown;
        this.storyTellerLabels = storyTellerLabels;
        this.numberFormat = NumberFormat.getFormat("#,##0");

        initWidget(uiBinder.createAndBindUi(this));

        setupSortDropDown();
        setupDropDownEvents();

        $(name).attr("data-tooltip", labels.clickToEdit());
        $(lastName).attr("data-tooltip", labels.clickToEdit());

        search.getElement().setAttribute("placeholder", storyTellerLabels.searchStories());
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ProfilePresenter.SLOT_PROFILE) {
            personProfilePanel.setWidget(content);
        } else if (slot == ProfilePresenter.SLOT_NOTES) {
            notesManagerPanel.setWidget(content);
        } else if (slot == ProfilePresenter.SLOT_STORIES) {
            otherStoriesPanel.setWidget(content);
        } else if (slot == ProfilePresenter.SLOT_ATTACHMENTS) {
            attachmentsPanel.setWidget(content);
        }
    }

    @Override
    public void displayProfile(Profile profile) {
        editedProfile = profile;
        name.setText(MoreObjects.firstNonNull(profile.getGivenName(), "First name..."));
        lastName.setText(MoreObjects.firstNonNull(profile.getSurname(), "Last name..."));
    }

    @Override
    public StorySortFieldDropDownItem getSort() {
        return sortDropDown.getSelection();
    }

    @Override
    public void setStoriesCount(Integer count) {
        totalCount.setHTML(storyTellerLabels.storiesCount(numberFormat.format(count)));

        CardsStyle cardsStyle = resource.cards();
        $("." + cardsStyle.activeToolbar(), otherStoriesPanel).addClass(cardsStyle.smallToolbar());
    }

    @Override
    public void setSearchToken(String searchToken) {
        search.setText(searchToken);
    }

    @UiHandler("name")
    void onNameClicked(final ClickEvent e) {
        name.setVisible(false);
        editableName.setText(name.getText());
        editableName.setVisible(true);
        editableName.setFocus(true);
        editableName.setCursorPos(name.getText().length());
    }

    @UiHandler("editableName")
    public void onEditableNameBlur(final BlurEvent e) {
        if (!name.getText().equals(editableName.getText())) {
            editedProfile.setGivenName(editableName.getText().trim());
            getUiHandlers().savePerson(editedProfile);
        }
        name.setText(MoreObjects.firstNonNull(editedProfile.getGivenName(), "First name..."));
        editableName.setVisible(false);
        name.setVisible(true);
    }

    @UiHandler("lastName")
    void onLastNameClicked(final ClickEvent e) {
        lastName.setVisible(false);
        editableLastName.setVisible(true);
        editableLastName.setText(lastName.getText());
        editableLastName.setFocus(true);
        editableLastName.setCursorPos(lastName.getText().length());
    }

    @UiHandler("editableLastName")
    void onEditableLastNameTextBlur(final BlurEvent e) {
        if (!lastName.getText().equals(editableLastName.getText())) {
            editedProfile.setSurname(editableLastName.getText().trim());
            getUiHandlers().savePerson(editedProfile);
        }
        lastName.setText(MoreObjects.firstNonNull(editedProfile.getSurname(), "Last name..."));
        editableLastName.setVisible(false);
        lastName.setVisible(true);
    }

    @UiHandler("search")
    void onSearchKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            getUiHandlers().search(search.getText());
        }
    }

    @UiHandler("clearSearch")
    void onClearSearch(ClickEvent event) {
        search.setText("");
        getUiHandlers().search("");
    }

    private void setupSortDropDown() {
        sortDropDown.loadOptions(StorySortFieldDropDownItem.sortList(), false);
        sortDropDown.setTitle(StorySortFieldDropDownItem.defaultSortField().getLabel());
    }

    private void setupDropDownEvents() {
        sortDropDown.setDropDownHandler(new DropDownHandler<StorySortFieldDropDownItem>() {
            public void onLoadSpecificItem(StorySortFieldDropDownItem item) {
                sortDropDown.setTitle(item.getLabel());
                getUiHandlers().onSortChanged(item.getSortField());
            }
        });
    }
}
