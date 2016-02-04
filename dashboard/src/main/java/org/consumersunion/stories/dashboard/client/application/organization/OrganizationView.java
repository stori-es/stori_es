package org.consumersunion.stories.dashboard.client.application.organization;

import java.util.Arrays;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.ui.form.RichTextToolbar;
import org.consumersunion.stories.common.client.ui.form.controls.RichTextEditor;
import org.consumersunion.stories.common.client.util.DateParser;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.dashboard.client.application.ui.EnumCell;
import org.consumersunion.stories.dashboard.client.resource.ToggleButtonStyle;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValuePicker;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class OrganizationView extends ViewWithUiHandlers<OrganizationUiHandlers>
        implements OrganizationPresenter.MyView {

    interface Binder extends UiBinder<Widget, OrganizationView> {
    }

    public enum ViewItem {
        INFO("Info"), PERMISSIONS("Permissions");

        private final String label;

        ViewItem(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    private static final Integer PERMISSIONS_VIEW = 0;
    private static final Integer INFO_VIEW = 1;
    private static final String CARET_DOWN_ICON = "icon-caret-down";
    private static final String CARET_RIGHT_ICON = "icon-caret-right";

    @UiField(provided = true)
    final RichTextToolbar toolbar;
    @UiField(provided = true)
    final ValuePicker<ViewItem> displaySelector;

    @UiField
    SimplePanel graphPanel;
    @UiField
    SimplePanel notesPanel;
    @UiField
    DeckPanel panelSwitcher;
    @UiField
    RichTextEditor permissionText;
    @UiField
    Label title;
    @UiField
    TextBox editableTitle;
    @UiField
    InlineLabel creationDate;
    @UiField
    Label updateDate;
    @UiField
    DivElement dateTooltip;
    @UiField
    SimplePanel attachmentsPanel;
    @UiField
    Button submitButton;
    @UiField
    Label helpButton;
    @UiField
    Label permissionHelp;
    @UiField
    HTML textEditableZone;
    @UiField
    DivElement viewContainer;
    @UiField
    Label editIcon;

    private final StoryTellerDashboardI18nLabels storyTellerLabels;
    private final DateParser dateParser;
    private final DateTimeFormat dateTimeFormat;
    private final MessageDispatcher messageDispatcher;

    private Organization currentOrganization;

    @Inject
    OrganizationView(
            Binder uiBinder,
            CommonI18nLabels labels,
            ToggleButtonStyle toggleStyle,
            StoryTellerDashboardI18nLabels storyTellerLabels,
            DateParser dateParser,
            MessageDispatcher messageDispatcher,
            RichTextToolbar richTextToolbar) {
        this.storyTellerLabels = storyTellerLabels;
        this.dateParser = dateParser;
        this.toolbar = richTextToolbar;
        this.messageDispatcher = messageDispatcher;
        this.dateTimeFormat = DateTimeFormat.getFormat("MMM d, yyyy h:mm:ss a zz");

        this.displaySelector = new ValuePicker<ViewItem>(new CellList<ViewItem>(new EnumCell<ViewItem>(), toggleStyle));

        initWidget(uiBinder.createAndBindUi(this));

        displaySelector.setAcceptableValues(Arrays.asList(ViewItem.values()));

        helpButton.addStyleName(CARET_DOWN_ICON);
        initTooltips(labels);
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == OrganizationPresenter.TYPE_storiesGrowthContent) {
            graphPanel.setWidget(content);
        } else if (slot == OrganizationPresenter.TYPE_NotesManagerContent) {
            notesPanel.setWidget(content);
        } else if (slot == OrganizationPresenter.TYPE_AttachmentsContent) {
            attachmentsPanel.setWidget(content);
        }
    }

    @Override
    public void setupOrganization(Organization organization) {
        currentOrganization = organization;
        title.setText(organization.getName());
        editableTitle.setText(organization.getName());
        editableTitle.setVisible(false);
        title.setVisible(true);
        setupCreationTime(organization);
        showPermissionsEditMode("", true);
        displaySelector.setValue(ViewItem.INFO, false);
        panelSwitcher.showWidget(INFO_VIEW);
    }

    @Override
    public void setOrganizationNameDisponibility(boolean available) {
        if (available) {
            editableTitle.removeStyleName("cu-error-text-field");
            editableTitle.addStyleName("cu-success-availability-text-field");
        } else {
            editableTitle.removeStyleName("cu-success-availability-text-field");
            editableTitle.addStyleName("cu-error-text-field");
        }
    }

    @Override
    public void loadPermissions(String text) {
        viewContainer.getStyle().setDisplay(Style.Display.BLOCK);
        switchHelpIcon(false);
        toolbar.setVisible(false);
        permissionText.setHTML(text);
        permissionText.setVisible(false);
        submitButton.setVisible(false);
        textEditableZone.setHTML(text);
    }

    @UiHandler("displaySelector")
    void onDisplaySelectorChanged(ValueChangeEvent<ViewItem> event) {
        if (displaySelector.getValue() == ViewItem.INFO) {
            panelSwitcher.showWidget(INFO_VIEW);
        } else if (displaySelector.getValue() == ViewItem.PERMISSIONS) {
            panelSwitcher.showWidget(PERMISSIONS_VIEW);
        }
    }

    @UiHandler("title")
    void onTitleClicked(ClickEvent event) {
        title.setVisible(false);
        editableTitle.setVisible(true);
        editableTitle.setFocus(true);
    }

    @UiHandler("editableTitle")
    void onTitleEdited(KeyUpEvent event) {
        String organizationName = editableTitle.getValue().trim();
        if (!currentOrganization.getName().equals(organizationName)) {
            getUiHandlers().checkOrganizationDisponibility(organizationName);
        }
    }

    @UiHandler("submitButton")
    void onPermissionSaved(ClickEvent event) {
        String permissions = permissionText.getHTML();
        if (!Strings.isNullOrEmpty(permissions)) {
            getUiHandlers().saveOrganizationPermissions(permissions);
        }
    }

    @UiHandler({"helpButton"})
    void onHelp(ClickEvent event) {
        switchHelpIcon(!permissionHelp.isVisible());
    }

    @UiHandler("textEditableZone")
    void onEdit(ClickEvent event) {
        showPermissionsEditMode(permissionText.getHTML(), false);
    }

    @UiHandler("editableTitle")
    void onTitleBlur(BlurEvent event) {
        String organizationName = editableTitle.getText().trim();
        if (!Strings.isNullOrEmpty(organizationName)) {
            if (!title.getText().equals(organizationName)) {
                currentOrganization.setName(organizationName);
                getUiHandlers().saveOrganization(currentOrganization);
            }
            editableTitle.setText(organizationName);
            editableTitle.setVisible(false);
            title.setVisible(true);
        } else {
            messageDispatcher.displayMessage(MessageStyle.ERROR, storyTellerLabels.titleRequired());
        }
    }

    private void showPermissionsEditMode(String text, Boolean showHelp) {
        viewContainer.getStyle().setDisplay(Style.Display.NONE);
        permissionHelp.setVisible(showHelp);
        toolbar.setVisible(true);
        permissionText.setVisible(true);
        permissionText.setHTML(text);
        submitButton.setVisible(true);
        switchHelpIcon(showHelp);
    }

    private void switchHelpIcon(Boolean showHelp) {
        if (showHelp) {
            helpButton.addStyleName(CARET_DOWN_ICON);
            helpButton.removeStyleName(CARET_RIGHT_ICON);
        } else {
            helpButton.removeStyleName(CARET_DOWN_ICON);
            helpButton.addStyleName(CARET_RIGHT_ICON);
        }
        permissionHelp.setVisible(showHelp);
    }

    private void setupCreationTime(Organization organization) {
        creationDate.setText(dateParser.parseDate(organization.getCreated()));
        updateDate.setText(storyTellerLabels.updatedDateAgo(dateParser.parseDate(organization.getUpdated())));

        if (organization.getCreated() != null && organization.getUpdated() != null) {
            dateTooltip.setAttribute("title",
                    storyTellerLabels.createdDateAgo(dateTimeFormat.format(organization.getCreated())) +
                            " " +
                            storyTellerLabels.updatedDateAgo(dateTimeFormat.format(organization.getUpdated())));
        }
    }

    private void initTooltips(CommonI18nLabels labels) {
        toolbar.initialize(permissionText);
        viewContainer.getStyle().setDisplay(Style.Display.NONE);
        permissionHelp.setText(labels.permissionHelp());
        title.getElement().setAttribute("data-tooltip", labels.clickToEdit());
        viewContainer.setAttribute("data-tooltip", labels.clickToEdit());
    }
}
