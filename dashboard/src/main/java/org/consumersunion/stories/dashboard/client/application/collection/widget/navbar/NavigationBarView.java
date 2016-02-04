package org.consumersunion.stories.dashboard.client.application.collection.widget.navbar;

import org.consumersunion.stories.dashboard.client.resource.AddDocumentTooltipResources;
import org.consumersunion.stories.dashboard.client.resource.Resources;
import org.consumersunion.stories.dashboard.client.util.NavigationBarElement;

import com.arcbees.gquery.tooltip.client.Tooltip;
import com.arcbees.gquery.tooltip.client.TooltipOptions;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class NavigationBarView extends ViewWithUiHandlers<NavigationBarUiHandlers>
        implements NavigationBarPresenter.MyView, AttachEvent.Handler {
    interface Binder extends UiBinder<Widget, NavigationBarView> {
    }

    @UiField
    Object addDocument;
    @UiField
    Button documentsButton;
    @UiField
    Button builderButton;
    @UiField
    Button storiesButton;
    @UiField
    Button summaryButton;
    @UiField
    HTMLPanel main;
    @UiField
    Resources res;
    @UiField
    DivElement addButton;

    private final AddDocumentTooltipResources addDocumentTooltipResources;
    private final CollectionAddDocumentPopup addDocumentPopup;
    private final Function documentClickHandler;

    private Tooltip tooltip;

    @Inject
    NavigationBarView(
            Binder uiBinder,
            CollectionAddDocumentPopup addDocumentPopup,
            AddDocumentTooltipResources addDocumentTooltipResources) {
        initWidget(uiBinder.createAndBindUi(this));

        this.addDocumentTooltipResources = addDocumentTooltipResources;
        this.addDocumentPopup = addDocumentPopup;

        asWidget().addAttachHandler(this);

        documentClickHandler = new Function() {
            @Override
            public void f() {
                onDocumentClicked();
            }
        };
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            initTooltip();

            $(Document.get().getBody()).click(documentClickHandler);

            onButtonClicked(summaryButton, NavigationBarElement.SUMMARY);
        } else {
            tooltip.destroy();
            $(addDocument).unbind(BrowserEvents.CLICK);
            $(Document.get().getBody()).unbind(BrowserEvents.CLICK, documentClickHandler);
        }
    }

    @Override
    public void setAddDocumentVisible(boolean visible) {
        if (visible) {
            $(addButton).show();
        } else {
            $(addButton).hide();
        }
    }

    @Override
    public void select(NavigationBarElement element) {
        selectButton(element);
    }

    @UiHandler("summaryButton")
    void onSummaryClicked(ClickEvent event) {
        onButtonClicked(summaryButton, NavigationBarElement.SUMMARY);
    }

    @UiHandler("storiesButton")
    void onStoriesClicked(ClickEvent event) {
        onButtonClicked(storiesButton, NavigationBarElement.STORIES);
    }

    @UiHandler("builderButton")
    void onBuilderClicked(ClickEvent event) {
        onButtonClicked(builderButton, NavigationBarElement.BUILDER);
    }

    @UiHandler("documentsButton")
    void onDocumentsClicked(ClickEvent event) {
        onButtonClicked(documentsButton, NavigationBarElement.DOCUMENTS);
    }

    private void onButtonClicked(Button button, NavigationBarElement element) {
        if (!isSelected(button)) {
            selectButton(element);

            getUiHandlers().goTo(element);
        }
    }

    private void selectButton(NavigationBarElement element) {
        Button button = null;
        switch (element) {
            case SUMMARY:
                button = summaryButton;
                break;
            case STORIES:
                button = storiesButton;
                break;
            case BUILDER:
                button = builderButton;
                break;
            case DOCUMENTS:
                button = documentsButton;
                break;
        }

        String selectedTab = res.generalStyleCss().selectedTab();
        $("." + selectedTab, main).removeClass(selectedTab);
        button.addStyleName(selectedTab);

        if (button == documentsButton) {
            $(addButton).show();
        } else {
            $(addButton).hide();
        }
    }

    private boolean isSelected(Button button) {
        return $(button).hasClass(res.generalStyleCss().selectedTab());
    }

    private void initTooltip() {
        TooltipOptions options = new TooltipOptions().withPlacement(TooltipOptions.TooltipPlacement.LEFT)
                .withContent(addDocumentPopup)
                .withResources(addDocumentTooltipResources)
                .withContainer("parent")
                .withTrigger(TooltipOptions.TooltipTrigger.MANUAL);
        tooltip = $(addDocument).as(Tooltip.Tooltip).tooltip(options);
        addClickHandler();
    }

    private void addClickHandler() {
        $(addDocument).click(new Function() {
            @Override
            public boolean f(Event e) {
                tooltip.toggle();

                return false;
            }
        });
    }

    private void onDocumentClicked() {
        if (tooltip.isVisible()) {
            tooltip.hide();
        }
    }
}
