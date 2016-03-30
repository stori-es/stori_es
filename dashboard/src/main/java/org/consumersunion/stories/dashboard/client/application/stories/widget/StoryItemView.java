package org.consumersunion.stories.dashboard.client.application.stories.widget;

import java.util.List;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.util.HtmlSanitizerUtil;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.datatransferobject.Documents;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.application.ui.TimeMetadataWidget;
import org.consumersunion.stories.dashboard.client.application.util.DocumentRenderer;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardSummaryWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardTitleWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarHandler;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarWidget;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class StoryItemView extends ViewWithUiHandlers<StoryItemUiHandlers>
        implements StoryItemPresenter.MyView, CardToolbarHandler {
    interface Binder extends UiBinder<Widget, StoryItemView> {
    }

    @UiField(provided = true)
    final TimeMetadataWidget timeContainer;
    @UiField(provided = true)
    final ValueListBox<Document> contentDocuments;
    @UiField(provided = true)
    final CardToolbarWidget toolbar;
    @UiField(provided = true)
    final CardSummaryWidget cardSummary;
    @UiField(provided = true)
    final CardTitleWidget cardTitle;

    @UiField
    DivElement tagsContainer;
    @UiField
    SimplePanel collections;
    @UiField
    SimplePanel tags;
    @UiField
    CheckBox checkbox;
    @UiField
    DivElement tagsHeader;
    @UiField
    Resources resource;
    @UiField
    HTMLPanel main;
    @UiField
    DivElement content;
    @UiField
    DivElement editContent;
    @UiField
    SpanElement defaultContentTitle;
    @UiField
    SpanElement removeDefaultContent;
    @UiField
    DivElement defaultContent;
    @UiField
    SpanElement noDefaultContentNoContent;
    @UiField
    SpanElement newContentTitle;
    @UiField
    SpanElement addContent;
    @UiField
    SpanElement noDefaultContent;
    @UiField
    AnchorElement cardTitleAnchor;
    @UiField
    DivElement rightColumn;

    private final CommonI18nLabels commonI18nLabels;
    private final StoryTellerDashboardI18nLabels dashboardLabels;
    private final HtmlSanitizerUtil htmlSanitizerUtil;

    @Inject
    StoryItemView(
            Binder uiBinder,
            TimeMetadataWidget timeMetadataWidget,
            CardToolbarWidget toolbar,
            CardSummaryWidget cardSummary,
            CardTitleWidget cardTitle,
            CommonI18nLabels commonI18nLabels,
            StoryTellerDashboardI18nLabels labels,
            HtmlSanitizerUtil htmlSanitizerUtil) {
        this.timeContainer = timeMetadataWidget;
        this.toolbar = toolbar;
        this.cardSummary = cardSummary;
        this.cardTitle = cardTitle;
        this.commonI18nLabels = commonI18nLabels;
        this.dashboardLabels = labels;
        this.htmlSanitizerUtil = htmlSanitizerUtil;
        contentDocuments = new ValueListBox<Document>(new DocumentRenderer());

        initWidget(uiBinder.createAndBindUi(this));

        updateTagsPosition();

        bindClicks();

        newContentTitle.setInnerText(labels.noContentAvailable());

        toolbar.setHandler(this);
        $(editContent).hide();
    }

    @Override
    public void init(StorySummary storySummary) {
        String storyTitle = storySummary.getTitle() == null ? commonI18nLabels.untitled() : storySummary.getTitle();

        cardTitle.init(storyTitle, ContentKind.STORY);

        Story story = storySummary.getStory();
        Address address = storySummary.getAddress();

        String storyContent = htmlSanitizerUtil.sanitize(storySummary.getSummary()).asString();
        String city = getCity(address);
        String author = getUserName(storySummary.getAuthorGivenName(), storySummary.getAuthorSurname());
        cardSummary.init(storyContent, author, city, story.getOwner());

        timeContainer.init(story);

        initTooltips();

        updateTagsPosition();

        defaultContentTitle.setInnerText(storyTitle);

        if (story.getDefaultContent() == null) {
            Documents documentsContainer = storySummary.getDocuments(SystemEntityRelation.BODY);
            if (documentsContainer.hasDocuments()) {
                $(noDefaultContent).show();
                $(noDefaultContentNoContent).hide();

                List<Document> documents = documentsContainer.getDocuments();
                contentDocuments.setValue(null);
                contentDocuments.setAcceptableValues(documents);
            } else {
                $(noDefaultContentNoContent).show();
                $(noDefaultContent).hide();
            }

            $(defaultContent).hide();
        } else {
            $(noDefaultContent).hide();
            $(noDefaultContentNoContent).hide();
            $(defaultContent).show();
        }
    }

    @Override
    public void save(boolean done) {
        doSave(done);
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
        if (slot == StoryItemPresenter.SLOT_COLLECTIONS) {
            collections.setWidget(content);
        } else if (slot == StoryItemPresenter.SLOT_TAGS) {
            tags.setWidget(content);
        }
    }

    @Override
    public void updateTagsPosition() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                Integer margin = $(rightColumn).outerHeight() - $(content).outerHeight()
                        - $(cardTitleAnchor).outerHeight() - $(tagsContainer).parent().outerHeight() - 15;
                margin = margin < 0 ? 0 : margin;
                $(StoryItemView.this.tagsContainer).css("margin-top", margin + "px");
            }
        });
    }

    @Override
    public void setForStoryCard(String historyToken) {
        cardTitle.swapIcons(resource.cards().iconDetail(), resource.cards().iconCard());
        cardTitle.getElement().setAttribute("data-tooltip", commonI18nLabels.clickToOpen());

        bindHeaderClickForStoryCard(historyToken);
    }

    @Override
    public void setEditMode(boolean isEditMode) {
        if (isEditMode) {
            switchToEditMode();
        }
    }

    @Override
    public void setForStoryDetails() {
        cardTitle.swapIcons(resource.cards().iconCard(), resource.cards().iconDetail());
        cardTitle.getElement().setAttribute("data-tooltip", commonI18nLabels.clickToClose());

        bindHeaderClickForStoryDetails();
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

    @Override
    public void showCheckbox(boolean showCheckbox) {
        checkbox.setVisible(showCheckbox);
    }

    @UiHandler("checkbox")
    void onStoriesSelected(ValueChangeEvent<Boolean> event) {
        getUiHandlers().onStorySelected();
    }

    @UiHandler("contentDocuments")
    void onContentDocumentChanged(ValueChangeEvent<Document> event) {
        getUiHandlers().setDefaultContent(event.getValue());
    }

    private void bindClicks() {
        bindTagsHeaderClick();
        bindRemoveDefaultClick();
        bindAddContentClick();
    }

    private void bindAddContentClick() {
        $(addContent).click(new Function() {
            @Override
            public void f() {
                getUiHandlers().addContent();
            }
        });
    }

    private void bindRemoveDefaultClick() {
        $(removeDefaultContent).click(new Function() {
            @Override
            public void f() {
                getUiHandlers().removeDefaultContent();
            }
        });
    }

    private void doSave(boolean done) {
        Document selectedDocument = contentDocuments.getValue();
        if ($(noDefaultContent).isVisible()) {
            getUiHandlers().setDefaultContent(selectedDocument);
        }
        getUiHandlers().save(done);
    }

    private void switchToViewMode() {
        getUiHandlers().setEditMode(false);

        toolbar.setToEdit(false);
        $(main).removeClass(resource.generalStyleCss().grayBackground());
        $(content).show();
        $(editContent).hide();
        cardTitle.switchToViewMode();
    }

    private void switchToEditMode() {
        getUiHandlers().setEditMode(true);
        getUiHandlers().enableTagsEdit();

        toolbar.setToEdit(true);
        $(main).addClass(resource.generalStyleCss().grayBackground());
        $(content).hide();
        $(editContent).show();
        cardTitle.switchToEditMode();
    }

    private void bindHeaderClickForStoryCard(String historyToken) {
        $(cardTitleAnchor).unbind(BrowserEvents.CLICK);
        cardTitleAnchor.setHref("#" + historyToken);
        $(cardTitleAnchor).click(new Function() {
            @Override
            public boolean f(Event e) {
                getUiHandlers().openCard(false);

                return false;
            }
        });
    }

    private void bindHeaderClickForStoryDetails() {
        $(cardTitleAnchor).unbind(BrowserEvents.CLICK);
        $(cardTitleAnchor).click(new Function() {
            @Override
            public void f() {
                getUiHandlers().closeCard();
            }
        });
    }

    private void bindTagsHeaderClick() {
        $(tagsHeader).click(new Function() {
            @Override
            public void f() {
                getUiHandlers().enableTagsEdit();
            }
        });
    }

    private void initTooltips() {
        tagsHeader.setAttribute("data-tooltip", commonI18nLabels.clickToEdit());
        removeDefaultContent.setAttribute("data-tooltip", commonI18nLabels.clickToRemove());
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

    private String getCity(Address address) {
        if (address == null || (address.getCity() == null && address.getState() == null)) {
            return "";
        }

        if (address.getCity() == null) {
            return address.getState();
        } else if (address.getState() == null) {
            return address.getCity();
        }

        return dashboardLabels.cityAndSate(address.getCity(), address.getState());
    }
}
