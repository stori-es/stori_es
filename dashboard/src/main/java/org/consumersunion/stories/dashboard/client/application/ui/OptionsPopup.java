package org.consumersunion.stories.dashboard.client.application.ui;

import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.collect.Sets;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PopupPanel;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_DOWN;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_PAGEDOWN;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_PAGEUP;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_RIGHT;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_UP;

public class OptionsPopup extends PopupPanel implements AttachEvent.Handler {
    private static final Set<Integer> scrollingKeyCodes =
            Sets.newHashSet(KEY_PAGEDOWN, KEY_PAGEUP, KEY_LEFT, KEY_RIGHT, KEY_UP, KEY_DOWN);

    @Inject
    private static Resources resources;

    private HandlerRegistration handlerRegistration;

    public OptionsPopup() {
        super(true);

        getElement().addClassName("cu-selection");
        sinkEvents(Event.ONMOUSEOUT | Event.ONMOUSEOVER);
        setStylePrimaryName(resources.generalStyleCss().dropDownMenu());
        addAttachHandler(this);
    }

    @Override
    public void onBrowserEvent(Event e) {
        super.onBrowserEvent(e);

        if (!DOM.isOrHasChild(getElement(), DOM.eventGetToElement(e))) {
            hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            handlerRegistration = Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
                @Override
                public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                    if (event.getTypeInt() == Event.ONMOUSEWHEEL) {
                        hide();
                    } else if (event.getTypeInt() == Event.ONKEYDOWN) {
                        if (scrollingKeyCodes.contains(event.getNativeEvent().getKeyCode())) {
                            hide();
                        }
                    }
                }
            });
        } else {
            handlerRegistration.removeHandler();
        }
    }
}
