package org.consumersunion.stories.dashboard.client.application.widget.content;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.util.URLUtils;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.application.ui.TimeMetadataWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardContentEditWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardLocaleEditWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardSummaryWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardTitleWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarHandler;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarWidget;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public abstract class DocumentCardView<T extends Document, H extends DocumentCardUiHandlers>
        extends ViewWithUiHandlers<H>
        implements DocumentCardPresenter.MyView<T, H>, CardToolbarHandler {
    interface Binder extends UiBinder<Widget, DocumentCardView> {
    }

    @UiField(provided = true)
    protected final TimeMetadataWidget timeContainer;
    @UiField(provided = true)
    protected final CardContentEditWidget editContent;
    @UiField(provided = true)
    protected final CardToolbarWidget toolbar;
    @UiField(provided = true)
    protected final CardSummaryWidget cardSummary;
    @UiField(provided = true)
    protected final CardTitleWidget cardTitle;
    @UiField(provided = true)
    protected final CardLocaleEditWidget editLocale;

    protected final CommonI18nLabels commonI18nLabels;
    protected final StoryTellerDashboardI18nLabels dashboardLabels;

    @UiField
    protected Resources resource;
    @UiField
    protected HTMLPanel main;
    @UiField
    protected DivElement content;
    @UiField
    protected SimplePanel cardDetailPanel;
    @UiField
    protected DivElement cardDetails;

    protected ContentKind contentKind;

    @Inject
    DocumentCardView(
            Binder uiBinder,
            TimeMetadataWidget timeMetadataWidget,
            CardToolbarWidget toolbar,
            CardSummaryWidget cardSummary,
            CardTitleWidget cardTitle,
            CardContentEditWidget editContent,
            CardLocaleEditWidget editLocale,
            CommonI18nLabels commonI18nLabels,
            StoryTellerDashboardI18nLabels labels) {
        this.timeContainer = timeMetadataWidget;
        this.toolbar = toolbar;
        this.cardSummary = cardSummary;
        this.cardTitle = cardTitle;
        this.editContent = editContent;
        this.editLocale = editLocale;
        this.commonI18nLabels = commonI18nLabels;
        this.dashboardLabels = labels;

        initWidget(uiBinder.createAndBindUi(this));

        toolbar.setHandler(this);
        toolbar.setToEdit(false);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                switchToViewMode();
            }
        });

        $(cardDetails).hide().slideUp(0);
    }

    @Override
    public void init(final T document, String title, ContentKind contentKind) {
        this.contentKind = contentKind;

        String documentTitle = title == null ? commonI18nLabels.untitled() : title;
        cardTitle.init(documentTitle, contentKind);

        Address address = document.getAuthorAddress();
        String city = address == null ? "" : dashboardLabels.cityAndSate(address.getCity(), address.getState());
        String author = getUserName(document.getPrimaryAuthorFirstName(), document.getPrimaryAuthorLastName());

        boolean isProfileOwner = !"root root".equals(author);
        author = isProfileOwner ? author : "";

        cardSummary.init(contentKind, document, author, city, document.getPrimaryAuthor(), isProfileOwner);
        timeContainer.init(document);

        initEditSection(document);

        if (ContentKind.ATTACHMENT.equals(contentKind)) {
            $(cardTitle).unbind(BrowserEvents.CLICK);
            $(cardTitle).click(new Function() {
                @Override
                public void f() {
                    String link = URLUtils.appendDefaultProtocol(document.getPermalink());
                    Window.open(link, "_blank", "");
                }
            });
        }
    }

    @Override
    public void edit() {
        getUiHandlers().edit();
    }

    @Override
    public void cancel() {
        switchToViewMode();
        getUiHandlers().cancelEdit();
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == DocumentCardPresenter.SLOT_DETAILS) {
            cardDetailPanel.setWidget(content);
        }
    }

    @Override
    public void setForContentCard() {
        cardTitle.swapIcons(resource.cards().iconDetail(), resource.cards().iconCard());

        if (canBeOpened()) {
            cardTitle.getElement().setAttribute("data-tooltip", commonI18nLabels.clickToOpen());
            bindHeaderClickForDocumentCard();

            if ($(cardDetails).isVisible()) {
                $(cardDetails).slideUp();
            }
        }
    }

    @Override
    public void setForContentDetails() {
        cardTitle.swapIcons(resource.cards().iconCard(), resource.cards().iconDetail());

        if (canBeOpened()) {
            cardTitle.getElement().setAttribute("data-tooltip", commonI18nLabels.clickToClose());
            bindHeaderClickForDocumentDetails();

            $(cardDetails).slideDown();
        }
    }

    @Override
    public void setEditMode(boolean isEditMode) {
        if (isEditMode) {
            switchToEditMode();
        }
    }

    @Override
    public void saveFailed() {
        toolbar.onSaveFailed();
    }

    @Override
    public void saveSuccess(boolean done) {
        toolbar.onSaveSuccess();

        if (done) {
            switchToViewMode();
        }
    }

    protected abstract boolean canBeOpened();

    protected void initEditSection(T document) {
        if (getUiHandlers().hasLocale()) {
            editLocale.setValue(document.getLocale());
        } else {
            editLocale.removeFromParent();
        }
    }

    private void switchToViewMode() {
        getUiHandlers().setEditMode(false);

        toolbar.setToEdit(false);
        $(main).removeClass(resource.generalStyleCss().grayBackground());
        $(content).show();
        $(editContent).hide();
        $(editLocale).hide();
        cardTitle.switchToViewMode();
    }

    private void switchToEditMode() {
        getUiHandlers().setEditMode(true);

        toolbar.setToEdit(true);
        $(main).addClass(resource.generalStyleCss().grayBackground());
        $(content).hide();
        $(editContent).show();
        $(editLocale).show();
        cardTitle.switchToEditMode();

        editContent.clearErrors();
        editLocale.clearErrors();
    }

    private void bindHeaderClickForDocumentCard() {
        $(cardTitle).unbind(BrowserEvents.CLICK);
        $(cardTitle).click(new Function() {
            @Override
            public void f() {
                getUiHandlers().openCard(isEditMode(), false);
            }
        });
    }

    private boolean isEditMode() {
        return toolbar.isEditMode();
    }

    private void bindHeaderClickForDocumentDetails() {
        $(cardTitle).unbind(BrowserEvents.CLICK);
        $(cardTitle).click(new Function() {
            @Override
            public void f() {
                getUiHandlers().closeCard();
            }
        });
    }

    private String getUserName(String givenName, String surName) {
        String fullName;
        if (!Strings.isNullOrEmpty(givenName)) {
            fullName = givenName + " ";
        } else {
            fullName = "? ";
        }

        if (!Strings.isNullOrEmpty(surName)) {
            fullName = fullName + surName;
        } else {
            fullName = fullName + "?";
        }

        if (fullName.contains("? ?")) {
            fullName = "anonymous";
        }

        return fullName;
    }
}
