package org.consumersunion.stories.dashboard.client.application.ui;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation;
import com.google.gwt.user.client.ui.Widget;

public class ScrollToAnimation extends Animation {
    private static final int HEADER_HEIGHT = 200;

    private final Element element;

    private int target;
    private int start;
    private int gap;

    public ScrollToAnimation(Element element, Widget target) {
        this.element = element;
        this.target = target.getAbsoluteTop();
    }

    @Override
    protected void onStart() {
        start = element.getScrollTop();
        target = Math.max(0, target - HEADER_HEIGHT);

        gap = Math.abs(target - start);
        super.onStart();
    }

    @Override
    protected void onUpdate(double progress) {
        double value;
        if (target < start) {
            value = start - gap * progress;
        } else {
            value = start + gap * progress;
        }
        element.setScrollTop((int) value);
    }

    @Override
    protected double interpolate(double progress) {
        return PropertiesAnimation.EasingCurve.easeInOut.interpolate(progress);
    }
}
