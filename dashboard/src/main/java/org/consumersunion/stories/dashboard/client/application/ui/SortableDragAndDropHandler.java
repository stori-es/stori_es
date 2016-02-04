package org.consumersunion.stories.dashboard.client.application.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import gwtquery.plugins.draggable.client.events.DragEvent;
import gwtquery.plugins.draggable.client.events.DragEvent.DragEventHandler;
import gwtquery.plugins.draggable.client.gwt.DraggableWidget;
import gwtquery.plugins.droppable.client.DroppableOptions;
import gwtquery.plugins.droppable.client.events.DropEvent;
import gwtquery.plugins.droppable.client.events.DropEvent.DropEventHandler;
import gwtquery.plugins.droppable.client.events.OutDroppableEvent;
import gwtquery.plugins.droppable.client.events.OutDroppableEvent.OutDroppableEventHandler;
import gwtquery.plugins.droppable.client.events.OverDroppableEvent;
import gwtquery.plugins.droppable.client.events.OverDroppableEvent.OverDroppableEventHandler;

import static com.google.gwt.query.client.GQuery.$;

public class SortableDragAndDropHandler
        implements DropEventHandler, OverDroppableEventHandler, OutDroppableEventHandler, DragEventHandler {
    private final FlowPanel panel;
    private final int minIndex;

    private HandlerRegistration dragHandlerRegistration;
    private SimplePanel placeHolder;
    private int placeHolderIndex;

    public SortableDragAndDropHandler(FlowPanel panel) {
        this(panel, 0);
    }

    public SortableDragAndDropHandler(FlowPanel panel, int minIndex) {
        this.panel = panel;
        this.minIndex = minIndex;
        placeHolderIndex = -1;
    }

    @Override
    public void onDrag(DragEvent event) {
        maybeMovePlaceHolder(event.getHelper());
    }

    public void onDrop(DropEvent event) {
        DraggableWidget<?> draggable = event.getDraggableWidget();
        panel.insert(draggable, placeHolderIndex);
        reset();
    }

    @Override
    public void onOutDroppable(OutDroppableEvent event) {
        reset();
    }

    @Override
    public void onOverDroppable(OverDroppableEvent event) {
        DroppableOptions.AcceptFunction accept = event.getDroppableWidget().getAccept();
        if (accept != null && accept.acceptDrop(event.getDragDropContext())) {
            DraggableWidget<?> draggable = event.getDraggableWidget();
            createPlaceHolder(draggable, panel.getWidgetIndex(draggable));
            dragHandlerRegistration = draggable.addDragHandler(this);
        }
    }

    private void createPlaceHolder(Widget draggable, int initialPosition) {
        placeHolder = new SimplePanel();
        placeHolder.getElement().getStyle().setBorderStyle(Style.BorderStyle.DASHED);
        placeHolder.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        placeHolder.getElement().getStyle().setBorderColor("#000000");
        placeHolder.setHeight("" + $(draggable).height() + "px");
        placeHolder.setWidth("" + $(draggable).width() + "px");

        if (initialPosition != -1) {
            panel.insert(placeHolder, initialPosition);
            placeHolderIndex = initialPosition;
        }
    }

    private int getBeforeInsertIndex(Element draggableHelper) {
        if (panel.getWidgetCount() == 0) {
            return -1;
        }

        int draggableAbsoluteTop = draggableHelper.getAbsoluteTop();
        for (int i = 0; i < panel.getWidgetCount(); i++) {
            Widget w = panel.getWidget(i);
            int widgetAbsoluteTop = w.getElement().getAbsoluteTop();
            if (widgetAbsoluteTop > draggableAbsoluteTop) {
                return i;
            }
        }

        return -1;
    }

    private void maybeMovePlaceHolder(Element helper) {
        int beforeInsertIndex = getBeforeInsertIndex(helper);

        if (placeHolderIndex > 0 && beforeInsertIndex == placeHolderIndex) {
            return;
        }

        if (beforeInsertIndex < minIndex) {
            // move the place holder and keep its position
            panel.insert(placeHolder, minIndex);
            placeHolderIndex = minIndex;
        } else if (beforeInsertIndex >= minIndex) {
            // move the place holder and keep its position
            panel.insert(placeHolder, beforeInsertIndex);
            placeHolderIndex = beforeInsertIndex;
        } else {
            // insert the place holder at the end
            panel.add(placeHolder);
            placeHolderIndex = panel.getWidgetCount() - 1;
        }
    }

    private void reset() {
        dragHandlerRegistration.removeHandler();
        placeHolder.removeFromParent();
        placeHolder = null;
        dragHandlerRegistration = null;
        placeHolderIndex = -1;
    }
}
