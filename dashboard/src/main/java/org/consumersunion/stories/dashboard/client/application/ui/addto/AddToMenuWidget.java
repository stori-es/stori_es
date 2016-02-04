package org.consumersunion.stories.dashboard.client.application.ui.addto;

import org.consumersunion.stories.common.client.util.WidgetIds;
import org.consumersunion.stories.dashboard.client.resource.AddToTooltipResources;

import com.arcbees.gquery.tooltip.client.Tooltip;
import com.arcbees.gquery.tooltip.client.TooltipOptions;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;

public class AddToMenuWidget extends Composite implements AttachEvent.Handler {
    interface Binder extends UiBinder<Widget, AddToMenuWidget> {
    }

    @UiField
    HTMLPanel main;

    private final AddToPopup addToPopup;
    private final Function documentClickHandler;
    private final AddToTooltipResources addToTooltipResources;

    private Tooltip tooltip;

    @Inject
    AddToMenuWidget(
            Binder uiBinder,
            AddToTooltipResources addToTooltipResources,
            AddToWidgetFactory addToWidgetFactory,
            @Assisted AddToMenuHandler addToMenuHandler) {
        initWidget(uiBinder.createAndBindUi(this));

        this.addToTooltipResources = addToTooltipResources;
        this.addToPopup = addToWidgetFactory.createAddToPopup(addToMenuHandler);

        main.getElement().setId(WidgetIds.ADD_TO_BUTTON);

        addAttachHandler(this);

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
            $(this).unbind(BrowserEvents.CLICK);
            $(Document.get().getBody()).unbind(BrowserEvents.CLICK, documentClickHandler);
        }
    }

    private void initTooltip() {
        TooltipOptions options = new TooltipOptions().withPlacement(TooltipOptions.TooltipPlacement.BOTTOM)
                .withContent(addToPopup)
                .withResources(addToTooltipResources)
                .withOffsetProvider(new TooltipOptions.TooltipOffsetProvider() {
                    @Override
                    public GQuery.Offset getOffset(Element element) {
                        int left = (addToPopup.getElement().getClientWidth() - element.getClientWidth()) / 2 + 4;
                        return new GQuery.Offset(left, 0);
                    }
                })
                .withTrigger(TooltipOptions.TooltipTrigger.MANUAL);
        tooltip = $(main).as(Tooltip.Tooltip).tooltip(options);
        addAddToClickHandler();
    }

    private void addAddToClickHandler() {
        $(this).click(new Function() {
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
