package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.dashboard.client.resource.GeneralStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class PublicationTabView extends ViewWithUiHandlers<PublicationTabUiHandlers>
        implements PublicationTabPresenter.MyView {
    interface Binder extends UiBinder<Widget, PublicationTabView> {
    }

    @SuppressWarnings("GwtCssResourceErrors")
    interface Style extends CssResource {
        String clickablePermalink();
    }

    @UiField
    HTMLPanel permaLinkContainer;
    @UiField
    Label permalink;
    @UiField
    Label editLink;
    @UiField
    TextBox editableLink;
    @UiField
    Label permalinkEditButton;
    @UiField
    Label permalinkSaveButton;
    @UiField
    Button preview;
    @UiField
    Button toggleState;
    @UiField
    SpanElement activeState;
    @UiField
    SpanElement linkTooltip;
    @UiField
    Style style;

    private final GeneralStyle generalStyle;
    private final StoryTellerDashboardI18nLabels dashboardLabels;
    private final PermalinkUtil permalinkUtil;

    private boolean linkAvailable;
    private String permalinkUrl;
    private boolean isQuestionnaire;

    @Inject
    PublicationTabView(Binder uiBinder,
            Resources resources,
            StoryTellerDashboardI18nLabels dashboardLabels,
            PermalinkUtil permalinkUtil,
            CommonI18nLabels labels) {
        initWidget(uiBinder.createAndBindUi(this));

        generalStyle = resources.generalStyleCss();
        this.dashboardLabels = dashboardLabels;
        this.permalinkUtil = permalinkUtil;
        linkTooltip.setAttribute("data-tooltip", labels.clickToEdit());
    }

    @Override
    public void refreshView(Collection collection) {
        isQuestionnaire = collection.isQuestionnaire();

        editableLink.setVisible(false);
        editLink.setVisible(false);
        permalink.setVisible(true);
        permalinkEditButton.setVisible(true);
        permalinkSaveButton.setVisible(false);
        permalinkSaveButton.getElement().getStyle().setColor("#0059B2");
        editableLink.getElement().removeClassName("cu-error-text-field");
        editableLink.getElement().removeClassName("cu-success-availability-text-field");

        updatePermalink(collection);

        setPublished(collection.isPublished());
    }

    @Override
    public void updatePermalink(Collection collection) {
        String collectionPermalink = permalinkUtil.getPermalink(collection.getPermalink());

        String fullPermalink = permalinkUtil.getFullPermalink(collectionPermalink, collection.isQuestionnaire());
        permalink.setText(fullPermalink);
        editableLink.setText(collectionPermalink);
        editLink.setText(permalinkUtil.getStaticPart(collection));
        permalink.getElement().setAttribute("data-tooltip", fullPermalink);
    }

    @Override
    public void setPublished(boolean published) {
        if (published) {
            onSetToPublished();
        } else {
            onSetToDraft();
        }

        $(activeState).toggleClass(generalStyle.draftState(), !published);
    }

    @Override
    public void setLinkDisponibility(Boolean available) {
        linkAvailable = available;

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (linkAvailable) {
                    permalinkSaveButton.getElement().getStyle().setColor("#0059B2");
                    editableLink.getElement().removeClassName("cu-error-text-field");
                    editableLink.getElement().addClassName("cu-success-availability-text-field");
                } else {
                    editableLink.getElement().removeClassName("cu-success-availability-text-field");
                    editableLink.getElement().addClassName("cu-error-text-field");
                    permalinkSaveButton.getElement().getStyle().setColor("#dae1e5");
                }
            }
        });
    }

    @Override
    public void setPermalinkUrl(String url) {
        permalinkUrl = url;
    }

    @UiHandler("permalinkEditButton")
    void onPermalinkEditClicked(ClickEvent event) {
        linkAvailable = true;
        permalinkEditButton.setVisible(false);
        permalinkSaveButton.setVisible(true);
        permalink.setVisible(false);
        editLink.setVisible(true);
        editableLink.setVisible(true);
        editableLink.setFocus(true);
    }

    @UiHandler("editableLink")
    void onPermalinkChanged(KeyUpEvent event) {
        if (event.getNativeKeyCode() != KeyCodes.KEY_ENTER && !Strings.isNullOrEmpty(editableLink.getText())) {
            setLinkDisponibility(false);
            getUiHandlers().checkIfLinkExists(editableLink.getText());
        }
    }

    @UiHandler("permalinkSaveButton")
    void onPermalinkSaved(ClickEvent event) {
        if (linkAvailable) {
            if (!Strings.isNullOrEmpty(editableLink.getText())
                    && !permalink.getText().endsWith(editableLink.getText())) {
                getUiHandlers().updatePermalink(editableLink.getText());
            }

            editableLink.setVisible(false);
            editLink.setVisible(false);
            permalink.setVisible(true);
            permalinkEditButton.setVisible(true);
            permalinkSaveButton.setVisible(false);
        }
    }

    @UiHandler("toggleState")
    void onToggleState(ClickEvent event) {
        getUiHandlers().toggleState();
    }

    @UiHandler("preview")
    void onPreviewClicked(ClickEvent event) {
        openPermalink();
    }

    @UiHandler("permalink")
    void onPermalinkClicked(ClickEvent event) {
        if ($(permalink).hasClass(style.clickablePermalink())) {
            openPermalink();
        }
    }

    private void openPermalink() {
        permalinkUtil.openPermalink(permalinkUrl, isQuestionnaire);
    }

    private void onSetToPublished() {
        toggleState.setText(dashboardLabels.setToDraft());
        activeState.setInnerText(dashboardLabels.published());
        permalink.addStyleName(style.clickablePermalink());
        $(linkTooltip).hide();
    }

    private void onSetToDraft() {
        toggleState.setText(dashboardLabels.publish());
        activeState.setInnerText(dashboardLabels.draft());
        permalink.removeStyleName(style.clickablePermalink());
        $(linkTooltip).show();
    }
}
