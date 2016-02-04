package org.consumersunion.stories.dashboard.client.application.account;

import java.util.Arrays;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.model.StorySortFieldDropDownItem;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.model.type.LanguageType;
import org.consumersunion.stories.dashboard.client.application.account.ui.LocalizationEditor;
import org.consumersunion.stories.dashboard.client.application.account.ui.PasswordDto;
import org.consumersunion.stories.dashboard.client.application.account.ui.PasswordEditor;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown.DropDownHandler;
import org.consumersunion.stories.dashboard.client.application.ui.EnumCell;
import org.consumersunion.stories.dashboard.client.application.ui.MapToggler;
import org.consumersunion.stories.dashboard.client.resource.ToggleButtonStyle;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValuePicker;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class AccountView extends ViewWithUiHandlers<AccountUiHandlers>
        implements AccountPresenter.MyView, MapToggler.MapTogglerHandler {
    interface Binder extends UiBinder<Widget, AccountView> {
    }

    public enum ViewItem {
        PASSWORD("Password"), LOCALIZATION("Localization");

        private final String label;

        ViewItem(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    private static final String ICON_COMMENT = "icon-comment";
    private static final String ICON_COG = "icon-cog";
    private static final Integer PASSWORD_VIEW = 0;
    private static final Integer STORIES_VIEW = 1;
    private static final Integer LOCALIZATION_VIEW = 2;

    @UiField(provided = true)
    final PasswordEditor passwordEditor;
    @UiField(provided = true)
    final ValuePicker<ViewItem> displaySelector;
    @UiField(provided = true)
    final LocalizationEditor localizationEditor;
    @UiField(provided = true)
    final LocalizationEditor organizationlocalizationEditor;
    @UiField(provided = true)
    final ClickableDropDown<StorySortFieldDropDownItem> sortDropDown;

    @UiField
    InlineLabel login;

    @UiField
    DeckPanel panelSwitcher;
    @UiField
    SimplePanel listStories;
    @UiField
    SimplePanel personProfilePanel;
    @UiField
    SimplePanel noteManagerPanel;
    @UiField
    Label storyButton;
    @UiField
    Label storiesLabel;
    @UiField
    InlineLabel name;
    @UiField
    TextBox editableName;
    @UiField
    InlineLabel lastName;
    @UiField
    TextBox editableLastName;
    @UiField
    HTMLPanel separator;
    @UiField
    TextBox search;
    @UiField
    HTML totalCount;
    @UiField
    TextBox editableLogin;
    @UiField(provided = true)
    final
    MapToggler mapToggler;
    @UiField
    SimplePanel mapContainer;
    @UiField
    Button updatePassword;

    private final StoryTellerDashboardI18nLabels storyTellerLabels;

    private User editedUser;
    private Profile editedProfile;

    @Inject
    AccountView(
            Binder uiBinder,
            PasswordEditor passwordEditor,
            LocalizationEditor localizationEditor,
            LocalizationEditor organizationlocalizationEditor,
            ToggleButtonStyle toggleStyle,
            StoryTellerDashboardI18nLabels storyTellerLabels,
            CommonI18nLabels labels,
            MapToggler mapToggler,
            ClickableDropDown<StorySortFieldDropDownItem> sortDropDown) {
        this.passwordEditor = passwordEditor;
        this.localizationEditor = localizationEditor;
        this.organizationlocalizationEditor = organizationlocalizationEditor;
        this.storyTellerLabels = storyTellerLabels;
        this.mapToggler = mapToggler;
        this.sortDropDown = sortDropDown;
        this.displaySelector = new ValuePicker<ViewItem>(new CellList<ViewItem>(new EnumCell<ViewItem>(), toggleStyle));

        initWidget(uiBinder.createAndBindUi(this));

        $(name).attr("data-tooltip", labels.clickToEdit());
        $(lastName).attr("data-tooltip", labels.clickToEdit());
        $(login).attr("data-tooltip", labels.clickToEdit());

        displaySelector.setAcceptableValues(Arrays.asList(ViewItem.values()));
        displaySelector.setValue(ViewItem.PASSWORD, false);
        panelSwitcher.showWidget(PASSWORD_VIEW);

        setupSortDropDown();
        setupDropDownEvents();
        mapToggler.addHandler(this);
        mapToggler.setVisible(false);
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == AccountPresenter.TYPE_SetListStories) {
            listStories.setWidget(content);
        } else if (slot == AccountPresenter.TYPE_SetPersonProfil) {
            personProfilePanel.setWidget(content);
        } else if (slot == AccountPresenter.TYPE_SetNoteManager) {
            noteManagerPanel.setWidget(content);
        } else if (slot == AccountPresenter.SLOT_MAP) {
            mapContainer.setVisible(content != null);
            mapContainer.setWidget(content);
        }
    }

    @Override
    public SortField getSort() {
        return sortDropDown.getSelection().getSortField();
    }

    @Override
    public void setSearchToken(String searchToken) {
        search.setText(searchToken);
    }

    @Override
    public void setStoriesCount(int count) {
        totalCount.setHTML(storyTellerLabels.storiesCount(String.valueOf(count)));
    }

    @Override
    public void setupAccountForm(User user, Profile profile) {
        editedUser = user;
        editedProfile = profile;
        name.setText(MoreObjects.firstNonNull(editedProfile.getGivenName(), "First name..."));
        lastName.setText(MoreObjects.firstNonNull(editedProfile.getSurname(), "Last name..."));
        login.setText(user.getHandle());
        passwordEditor.edit();
        switchPanelTo(PASSWORD_VIEW);
    }

    @Override
    public void setupLanguages(List<LanguageType> languageTypes) {
        localizationEditor.setLanguages(languageTypes);
    }

    @Override
    public void setupOrganizationLanguages(List<LanguageType> languageTypes) {
        organizationlocalizationEditor.setLanguages(languageTypes);
    }

    @Override
    public void onSwitchToMap() {
        getUiHandlers().switchToMap();
    }

    @Override
    public void onSwitchToList() {
        getUiHandlers().switchToList();
    }

    @Override
    public void setPasswordUpdateButtonEnabled(boolean enabled) {
        updatePassword.setEnabled(enabled);
    }

    @Override
    public void clearPassword() {
        passwordEditor.edit();
    }

    @UiHandler({"storyButton", "storiesLabel"})
    void onStoryClicked(ClickEvent event) {
        if (panelSwitcher.getVisibleWidget() == STORIES_VIEW) {
            switchPanelTo(PASSWORD_VIEW);
        } else {
            switchPanelTo(STORIES_VIEW);
        }
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

    @UiHandler("updatePassword")
    void onUpdatePasswordClicked(ClickEvent event) {
        PasswordDto password = passwordEditor.get();
        if (password != null) {
            updatePassword.setEnabled(false);
            getUiHandlers().changeAccountPassword(password.getOldPassword(), password.getNewPassword());
        } else {
            passwordEditor.edit();
        }
    }

    @UiHandler("displaySelector")
    void onDisplaySelectorChanged(ValueChangeEvent<ViewItem> event) {
        if (displaySelector.getValue() == ViewItem.PASSWORD) {
            panelSwitcher.showWidget(PASSWORD_VIEW);
        } else if (displaySelector.getValue() == ViewItem.LOCALIZATION) {
            panelSwitcher.showWidget(LOCALIZATION_VIEW);
        }
    }

    @UiHandler("login")
    void onLoginClicked(ClickEvent event) {
        switchToEdit(login, editableLogin);
    }

    @UiHandler("name")
    void onNameClicked(final ClickEvent e) {
        switchToEdit(name, editableName);
    }

    @UiHandler("lastName")
    void onLastNameClicked(final ClickEvent e) {
        switchToEdit(lastName, editableLastName);
    }

    @UiHandler("editableLogin")
    void onEditableLoginBlur(final BlurEvent e) {
        String newLogin = editableLogin.getText();
        if (!Strings.isNullOrEmpty(newLogin) && !login.getText().equals(newLogin)) {
            editedUser.setHandle(newLogin.trim());
            getUiHandlers().saveAccountSettings(editedUser);
        }
        login.setText(editedUser.getHandle());
        editableLogin.setVisible(false);
        login.setVisible(true);
    }

    @UiHandler("editableName")
    void onEditableNameBlur(final BlurEvent e) {
        if (!name.getText().equals(editableName.getText())) {
            editedProfile.setGivenName(editableName.getText().trim());
            getUiHandlers().saveProfile(editedProfile);
        }
        name.setText(MoreObjects.firstNonNull(editedProfile.getGivenName(), "First name..."));
        editableName.setVisible(false);
        name.setVisible(true);
    }

    @UiHandler("editableLastName")
    void onEditableLastNameTextBlur(final BlurEvent e) {
        if (!lastName.getText().equals(editableLastName.getText())) {
            editedProfile.setSurname(editableLastName.getText().trim());
            getUiHandlers().saveProfile(editedProfile);
        }
        lastName.setText(MoreObjects.firstNonNull(editedProfile.getSurname(), "Last name..."));
        editableLastName.setVisible(false);
        lastName.setVisible(true);
    }

    private void switchToEdit(Label readOnlyLabel, TextBox editableControl) {
        editableControl.setText(readOnlyLabel.getText());
        editableControl.setVisible(true);
        editableControl.setFocus(true);
        editableControl.setCursorPos(readOnlyLabel.getText().length());
        readOnlyLabel.setVisible(false);
    }

    private void switchPanelTo(Integer panel) {
        if (panel.equals(STORIES_VIEW)) {
            panelSwitcher.showWidget(STORIES_VIEW);
            displaySelector.setVisible(false);
            separator.setVisible(false);

            storiesLabel.setText("Preferences");
            storyButton.getElement().removeClassName(ICON_COMMENT);
            storyButton.getElement().addClassName(ICON_COG);
        } else {
            panelSwitcher.showWidget(PASSWORD_VIEW);
            displaySelector.setVisible(true);
            displaySelector.setValue(ViewItem.PASSWORD);
            separator.setVisible(true);

            storiesLabel.setText("Stories");
            storyButton.getElement().removeClassName(ICON_COG);
            storyButton.getElement().addClassName(ICON_COMMENT);
        }
    }

    private void setupSortDropDown() {
        sortDropDown.loadOptions(StorySortFieldDropDownItem.sortList(), false);
        sortDropDown.setSelection(StorySortFieldDropDownItem.defaultSortField());
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
