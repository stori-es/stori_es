package org.consumersunion.stories.dashboard.client.application.ui;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A basic extension of the GWT {@link com.google.gwt.user.client.ui.PopupPanel}
 * that sets standard configuration for modal usage. For convenience, the modal
 * uses a {@link com.google.gwt.user.client.ui.VerticalPanel}, whereas the
 * {@link com.google.gwt.user.client.ui.PopupPanel} extends the
 * {@link com.google.gwt.user.client.ui.SimplePanel}. Z: I have found in my
 * experience that one very often ends up adding a vertical panel immediately
 * anyway and the cost of the
 * {@link com.google.gwt.user.client.ui.VerticalPanel} when not needed is minor.
 *
 * @author zane
 */
public class Modal extends PopupPanel {
    private final VerticalPanel vp = new VerticalPanel();

    /**
     * Constructs a Modal with the given title. It's always good form to include
     * a title, so this is the only constructor offered.
     *
     * @param title
     */
    public Modal(String title) {
        super();

        setTitle(title);
        initialize();
    }

    // These are silly, but it is how it works at the moment.
    public void add(Widget w) {
        vp.add(w);
    }

    public boolean remove(Widget w) {
        return vp.remove(w);
    }

    private void initialize() {
        setModal(true);
        setAutoHideEnabled(false);
        setAnimationEnabled(false);
        setGlassEnabled(true);
        setWidget(vp);
    }
}
