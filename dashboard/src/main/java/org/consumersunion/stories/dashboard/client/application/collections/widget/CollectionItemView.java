package org.consumersunion.stories.dashboard.client.application.collections.widget;

import java.util.List;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.ExportKind;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.dashboard.client.application.ui.TimeMetadataWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardSummaryWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardTitleWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarButton;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarButtonFactory;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarHandler;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.ExportFormatWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.ToolbarButtonActionHandler;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class CollectionItemView extends ViewWithUiHandlers<CollectionItemUiHandlers>
        implements CollectionItemPresenter.MyView, CardToolbarHandler {
    interface Binder extends UiBinder<Widget, CollectionItemView> {
    }

    @UiField(provided = true)
    final TimeMetadataWidget timeContainer;
    @UiField(provided = true)
    final CardTitleWidget cardTitle;
    @UiField(provided = true)
    final CardSummaryWidget cardSummary;
    @UiField(provided = true)
    final CardToolbarWidget toolbar;
    @UiField(provided = true)
    final CollectionContentWidget editPanel;
    @UiField(provided = true)
    final ExportFormatWidget exportContent;
    @UiField(provided = true)
    final CollectionContentWidget copyPanel;
    @UiField(provided = true)
    final DeleteCollectionWidget deletePanel;

    @UiField
    DivElement rightColumn;
    @UiField
    DivElement tagsContainer;
    @UiField
    SimplePanel tags;
    @UiField
    DivElement tagsHeader;
    @UiField
    DivElement collectionsWrapper;
    @UiField
    CheckBox watching;
    @UiField
    AnchorElement cardTitleAnchor;
    @UiField
    HTMLPanel main;
    @UiField
    Resources resource;
    @UiField
    DivElement actionPanel;
    @UiField
    SimplePanel collections;

    private final CommonI18nLabels labels;
    private final CardToolbarButtonFactory buttonFactory;
    private final CommonI18nMessages messages;

    private Collection collection;
    private boolean allowOpen;
    private int leftScroll;
    private int topScroll;

    @Inject
    CollectionItemView(
            Binder uiBinder,
            CommonI18nLabels labels,
            TimeMetadataWidget timeContainer,
            CardTitleWidget cardTitle,
            CardSummaryWidget cardSummary,
            CardToolbarWidget toolbar,
            CardToolbarButtonFactory buttonFactory,
            CollectionContentWidget editPanel,
            CollectionContentWidget copyPanel,
            DeleteCollectionWidget deletePanel,
            ExportFormatWidget exportContent,
            CommonI18nMessages messages) {
        this.labels = labels;
        this.timeContainer = timeContainer;
        this.cardTitle = cardTitle;
        this.cardSummary = cardSummary;
        this.toolbar = toolbar;
        this.buttonFactory = buttonFactory;
        this.editPanel = editPanel;
        this.copyPanel = copyPanel;
        this.deletePanel = deletePanel;
        this.exportContent = exportContent;
        this.messages = messages;

        initWidget(uiBinder.createAndBindUi(this));

        bindTagsHeaderClick();

        updateTagsPosition();

        toolbar.setHandler(this);
    }

    @Override
    public void init(CollectionData collectionData, String historyToken) {
        collection = collectionData.getCollection();
        boolean questionnaire = collection.isQuestionnaire();

        ContentKind contentKind;
        if (questionnaire) {
            contentKind = ContentKind.QUESTIONNAIRE;
        } else {
            contentKind = ContentKind.COLLECTION;
        }

        cardTitle.init(messages.collectionTitleWithCount(collectionData.getTitle(), collectionData.getStoriesCount()),
                contentKind);

        String fullPrimaryAuthorName = collection.getBodyDocument().getFullPrimaryAuthorName();
        boolean isProfileOwner = !fullPrimaryAuthorName.equals("root root");
        int ownerId = isProfileOwner ? collection.getPrimaryAuthor() : collection.getOwner();
        String ownerName = isProfileOwner ? fullPrimaryAuthorName : collectionData.getOwner();

        cardSummary.init(contentKind, collection.getSummary(), ownerName, ownerId, isProfileOwner);

        toolbar.setButtons(createButtons(questionnaire));

        timeContainer.init(collection);

        initTooltips();

        updateTagsPosition();
        bindTitleClick(historyToken);
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == CollectionItemPresenter.SLOT_TAGS) {
            tags.setWidget(content);
        } else if (slot == CollectionItemPresenter.SLOT_SOURCE_OR_TARGET) {
            collections.setWidget(content);
        }
    }

    @Override
    public void updateTagsPosition() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                Integer margin = $(collectionsWrapper).height() - $(tagsContainer).outerHeight() + 50;
                margin = margin < 0 ? 0 : margin;
                $(CollectionItemView.this.tagsContainer).css("margin-top", margin + "px");
            }
        });
    }

    @Override
    public void setWatched(boolean watched) {
        watching.setValue(watched, false);
    }

    @Override
    public void setEditMode(boolean isEditMode) {
        if (isEditMode) {
            switchToEditMode();
        }
    }

    @Override
    public void setForCard(String historyToken) {
        cardTitle.swapIcons(resource.cards().iconDetail(), resource.cards().iconCard());
        cardTitle.getElement().setAttribute("data-tooltip", labels.clickToOpen());

        bindHeaderClickForStoryCard(historyToken);
    }

    @Override
    public void setForDetails() {
        cardTitle.swapIcons(resource.cards().iconCard(), resource.cards().iconDetail());
        cardTitle.getElement().setAttribute("data-tooltip", labels.clickToClose());

        bindHeaderClickForStoryDetails();
    }

    @Override
    public void switchToViewMode() {
        getUiHandlers().setEditMode(false);
        showAllCards();

        toolbar.setToEdit(false);
        $(main).removeClass(resource.generalStyleCss().grayBackground());
        $(cardSummary).show();
        $(actionPanel).children().hide();
        cardTitle.switchToViewMode();

        $(tagsContainer)
                .add($(collectionsWrapper))
                .add($(timeContainer))
                .add($(watching))
                .show();

        allowOpen = true;

        initTooltips();
    }

    @Override
    public void save(boolean done) {
        CollectionContent data = editPanel.getData();
        if (data.isValid()) {
            getUiHandlers().updateBodyDocument(data);
            doSave(done);
        } else {
            saveFailed();
        }
    }

    @Override
    public void edit() {
        switchToEditMode();
    }

    @Override
    public void cancel() {
        if ($(editPanel).isVisible()) {
            getUiHandlers().cancelEdit();
        }

        switchToViewMode();
        Window.scrollTo(leftScroll, topScroll);
    }

    @Override
    public void saveFailed() {
        toolbar.onSaveFailed();
    }

    @Override
    public void saveSuccess(boolean done, boolean isDetails) {
        toolbar.onSaveSuccess();

        if (done) {
            switchToViewMode();

            if (isDetails) {
                setForDetails();
            }
        }
    }

    @UiHandler("watching")
    void onWatchChanged(ValueChangeEvent<Boolean> event) {
        getUiHandlers().watchCollection(event.getValue());
    }

    private void doSave(boolean done) {
        getUiHandlers().save(done);
    }

    private void switchToActionMode(Object actionWidget) {
        leftScroll = Window.getScrollLeft();
        topScroll = Window.getScrollTop();

        $(main).addClass(resource.generalStyleCss().grayBackground());
        $(cardSummary).hide();
        $(actionPanel).children().hide();
        $(actionWidget).show();

        if (actionWidget != editPanel) {
            $(tagsContainer)
                    .add($(collectionsWrapper))
                    .add($(watching))
                    .hide();
            cardTitle.switchToActionMode();

            allowOpen = false;
            cardTitle.getElement().removeAttribute("data-tooltip");
        }
    }

    private void switchToEditMode() {
        getUiHandlers().setEditMode(true);
        getUiHandlers().enableTagsEdit();

        toolbar.setToEdit(true);
        switchToActionMode(editPanel);
        cardTitle.switchToEditMode();

        editPanel.init(collection.isQuestionnaire() ? ContentKind.QUESTIONNAIRE : ContentKind.COLLECTION,
                collection.getBodyDocument());
    }

    private void initTooltips() {
        cardTitle.getElement().setAttribute("data-tooltip", labels.clickToOpen());
    }

    private void bindTitleClick(String historyToken) {
        $(cardTitleAnchor).unbind(BrowserEvents.CLICK);
        cardTitleAnchor.setHref("#" + historyToken);
        $(cardTitleAnchor).click(new Function() {
            @Override
            public boolean f(Event e) {
                if (allowOpen) {
                    getUiHandlers().goToCollectionDetails();
                } else {
                    e.preventDefault();
                    e.stopPropagation();
                }

                return false;
            }
        });
    }

    private void bindHeaderClickForStoryCard(String historyToken) {
        $(cardTitleAnchor).unbind(BrowserEvents.CLICK);
        cardTitleAnchor.setHref("#" + historyToken);
        $(cardTitleAnchor).click(new Function() {
            @Override
            public boolean f(Event e) {
                getUiHandlers().openCard();

                return false;
            }
        });
    }

    private void bindHeaderClickForStoryDetails() {
        $(cardTitleAnchor).unbind(BrowserEvents.CLICK);
        $(cardTitleAnchor).click(new Function() {
            @Override
            public boolean f(Event e) {
                getUiHandlers().closeCard();

                return false;
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

    private List<CardToolbarButton> createButtons(boolean questionnaire) {
        ImmutableList.Builder<CardToolbarButton> builder = ImmutableList.builder();

        if (questionnaire) {
            builder.add(buttonFactory.createCopyButton(createCopyHandler()));
        }

        builder.add(buttonFactory.createExportButton(createExportHandler()));
        builder.add(buttonFactory.createDeleteButton(createDeleteHandler()));

        return builder.build();
    }

    private void showAllCards() {
        $("." + resource.cards().main())
                .add($("." + resource.generalStyleCss().navBar()))
                .add($("." + resource.generalStyleCss().navContent()))
                .show();
    }

    private void hideAllOtherCards() {
        $("." + resource.cards().main())
                .add($("." + resource.generalStyleCss().navBar()))
                .add($("." + resource.generalStyleCss().navContent()))
                .hide();
        $("." + resource.generalStyleCss().grayBackground()).show();
    }

    private ToolbarButtonActionHandler createCopyHandler() {
        return new ToolbarButtonActionHandler() {
            @Override
            public void executeAction() {
                CollectionContent data = copyPanel.getData();
                if (data.isValid()) {
                    getUiHandlers().copyQuestionnaire(data);
                }
            }

            @Override
            public void onButtonClicked() {
                copyPanel.resetForCopy();
                switchToActionMode(copyPanel);
                cardTitle.switchToEditMode();
                $(timeContainer).hide();

                hideAllOtherCards();
            }
        };
    }

    private ToolbarButtonActionHandler createExportHandler() {
        return new ToolbarButtonActionHandler() {
            @Override
            public void executeAction() {
                if (exportContent.validate()) {
                    getUiHandlers().export(exportContent.getValue());
                    switchToViewMode();
                }
            }

            @Override
            public void onButtonClicked() {
                exportContent.setValue(ExportKind.UNKNOWN);
                switchToActionMode(exportContent);
            }
        };
    }

    private ToolbarButtonActionHandler createDeleteHandler() {
        return new ToolbarButtonActionHandler() {
            @Override
            public void executeAction() {
                getUiHandlers().deleteCollection();
            }

            @Override
            public void onButtonClicked() {
                switchToActionMode(deletePanel);
                hideAllOtherCards();
            }
        };
    }
}
