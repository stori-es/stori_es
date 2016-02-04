package org.consumersunion.stories.dashboard.client.application.story.widget.navbar;

import org.consumersunion.stories.dashboard.client.application.widget.AddDocumentPopup;
import org.consumersunion.stories.dashboard.client.resource.AddDocumentTooltipResources;

import com.arcbees.gquery.tooltip.client.Tooltip;
import com.arcbees.gquery.tooltip.client.TooltipOptions;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import static com.google.gwt.query.client.GQuery.$;

public class NavigationBarView extends ViewImpl
        implements NavigationBarPresenter.MyView, AttachEvent.Handler {
    interface Binder extends UiBinder<Widget, NavigationBarView> {
    }

    @UiField
    Object addDocument;

    private final AddDocumentTooltipResources addDocumentTooltipResources;
    private final AddDocumentPopup addDocumentPopup;
    private final Function documentClickHandler;

    private Tooltip tooltip;

    @Inject
    NavigationBarView(
            Binder uiBinder,
            AddDocumentPopup addDocumentPopup,
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
        } else {
            tooltip.destroy();
            $(addDocument).unbind(BrowserEvents.CLICK);
            $(Document.get().getBody()).unbind(BrowserEvents.CLICK, documentClickHandler);
        }
    }

    @Override
    public void setAddContentEnabled(boolean enabled) {
        addDocumentPopup.setAddContentEnabled(enabled);
    }

    private void initTooltip() {
        TooltipOptions options = new TooltipOptions().withPlacement(TooltipOptions.TooltipPlacement.LEFT)
                .withContent(addDocumentPopup)
                .withResources(addDocumentTooltipResources)
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
