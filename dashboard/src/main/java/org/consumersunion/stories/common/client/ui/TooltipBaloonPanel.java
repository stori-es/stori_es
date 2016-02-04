package org.consumersunion.stories.common.client.ui;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * TooltipBaloonPanel component
 *
 * @author Machin
 */

public class TooltipBaloonPanel extends PopupPanel {

    // decides if balloon should hide after some time or not
    private boolean shouldHide = true;

    // how long will take the balloon to show up
    private final int delay = 300;

    /**
     * TooltipBaloonPanel constructor
     *
     * @param helptext   = text
     * @param title      = text
     * @param shouldHide = true for autohide popup after the delay time.
     */
    public TooltipBaloonPanel(String helptext, String title, boolean shouldHide) {
        // PopupPanel's constructor takes 'auto-hide' as its boolean
        // parameter.
        // If this is set, the panel closes itself automatically when the
        // user clicks outside of it.
        super(true);
        this.shouldHide = shouldHide;
        setAutoHideEnabled(true);
        setAnimationEnabled(false);
        addStyleName("baloonPanel");
        HTML label = new HTML(helptext);
        setWidget(label);
        setTitle(title);
    }

    public void show(int offsetX, int offsetY) {
        TooltipBaloonPanel.this.setPopupPosition(offsetX, offsetY);
        BaloonAnimation showBaloon = new BaloonAnimation();
        showBaloon.run(delay);
        super.show();

        if (shouldHide) {
            hidePopupHelp();
        }
    }

    public void hidePopupHelp() {
        BaloonAnimation hideAnim = new BaloonAnimation(false);
        // run hide animation after some time
        hideAnim.run(delay, Duration.currentTimeMillis());
        // after animation wil end, the balloon must be also hidden and
        // deteached from the page
        Timer t = new Timer() {
            @Override
            public void run() {
                TooltipBaloonPanel.this.hide();
            }
        };
        t.schedule(delay);
    }

    class BaloonAnimation extends Animation {
        boolean show = true;

        BaloonAnimation(boolean show) {
            super();
            this.show = show;
        }

        public BaloonAnimation() {
            this(true);
        }

        @Override
        protected void onUpdate(double progress) {
            double opacityValue = progress;
            if (!show) {
                opacityValue = 1.0 - progress;
            }
            TooltipBaloonPanel.this.getElement().getStyle().setOpacity(opacityValue);
        }
    }
}
