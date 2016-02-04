package org.consumersunion.stories.common.client.ui.block.question;

import java.math.BigDecimal;

import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;

public class StarsRatingWidget extends AbstractRatingWidget implements AttachEvent.Handler {
    interface Binder extends UiBinder<Widget, StarsRatingWidget> {
    }

    interface Template extends SafeHtmlTemplates {
        @Template("<i class=\"icon-star-half\"></i>")
        SafeHtml halfStar();
    }

    private static final Binder binder = GWT.create(Binder.class);
    private static final Template template = GWT.create(Template.class);
    private static final String ANIMATION_END = "webkitAnimationEnd oanimationend msAnimationEnd animationend";
    private static final String ICON_STAR_HALF = ".icon-star-half";
    private static final String ACTIVE_CLASS = "active";
    private static final int ELEMENTS_BEFORE_STARS = 1;

    @UiField
    InlineLabel head;
    @UiField
    InlineLabel tail;
    @UiField
    HTMLPanel main;

    private final GQuery $stars;

    private boolean discrete;

    public StarsRatingWidget(RatingQuestion question) {
        super(question);

        widget = binder.createAndBindUi(this);

        setStepType(question.getStepType());
        $stars = $(".icon-stack", widget);

        widget.addAttachHandler(this);
    }

    @Override
    public void setQuestion(RatingQuestion question) {
        super.setQuestion(question);

        setStepType(question.getStepType());
    }

    @Override
    public void setValue(BigDecimal value) {
        super.setValue(value);

        updateStarsForValue();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        setEnabled(event.isAttached());

        boolean hasLabels = question.withLabels();
        head.setVisible(hasLabels);
        tail.setVisible(hasLabels);
        if (hasLabels) {
            head.setText(question.getStartLabel());
            tail.setText(question.getEndLabel());
        }
    }

    @Override
    public void setEnabled(boolean enable) {
        if (enable) {
            $stars.mouseover(new Function() {
                @Override
                public void f() {
                    onMouseOver($(this).index() - ELEMENTS_BEFORE_STARS);
                }
            }).mousemove(new Function() {
                @Override
                public void f() {
                    onMouseMove($(this), getEvent());
                }
            }).click(new Function() {
                @Override
                public void f() {
                    onClick($(this).index() - ELEMENTS_BEFORE_STARS, getEvent());
                }
            });
            $(main).mouseleave(new Function() {
                @Override
                public void f() {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            onMouseLeave();
                        }
                    });
                }
            });
        } else {
            $stars.unbind(BrowserEvents.MOUSEOVER)
                    .unbind(BrowserEvents.MOUSEMOVE)
                    .unbind(BrowserEvents.CLICK)
                    .parent().unbind("mouveleave");
        }
    }

    private void setStepType(RatingQuestion.StepType stepType) {
        boolean wasDiscrete = discrete;
        discrete = RatingQuestion.StepType.DISCRETE.equals(stepType);
        if ($stars != null && value != null && wasDiscrete != discrete) {
            removeHalfStars(ACTIVE_CLASS, true, 0);
            setValue(new BigDecimal(value.intValue()));
        }
    }

    private void onMouseMove(GQuery $star, Event event) {
        int index = $star.index() - ELEMENTS_BEFORE_STARS;
        if (!discrete) {
            handleStepsClass($star, event, index, ACTIVE_CLASS, true);
        }
    }

    private void onMouseOver(int index) {
        if (discrete) {
            handleDiscreteClass(index, ACTIVE_CLASS, true);
        }
    }

    private void onMouseLeave() {
        $stars.children().removeClass(ACTIVE_CLASS);
        updateStarsForValue();
    }

    private void onClick(int index, Event event) {
        if (discrete || !isFirstHalf($($stars.get(index)), event)) {
            setValue(new BigDecimal(index + 1));
        } else {
            setValue(new BigDecimal(index + .5d));
        }

        pulseStars(index);
    }

    private void updateStarsForValue() {
        double value = this.value == null ? 0 : this.value.doubleValue();
        if (discrete) {
            handleDiscreteClass((int) value - 1, ACTIVE_CLASS, true);
        } else {
            handleStepsClass(value, ACTIVE_CLASS, true);
        }
    }

    private void handleStepsClass(double value, String cssClass, boolean addOrRemove) {
        int i;
        for (i = 0; i < Math.floor(value); i++) {
            GQuery $star = $($stars.get(i));
            switchHalfStarToFull(cssClass, addOrRemove, $star);
        }

        if (hasValue()) {
            GQuery $star = $($stars.get(i));
            boolean isHalfStep = (i * 10) < Math.floor(value * 10d);
            if (isHalfStep) {
                addHalfStar(cssClass, addOrRemove, $star);
            } else {
                boolean isFullStep = (i * 10) != Math.floor(value * 10d);
                if (value > 0 && isFullStep) {
                    switchHalfStarToFull(cssClass, addOrRemove, $star);
                }
            }
            i++;
        }

        removeHalfStars(cssClass, addOrRemove, i);
    }

    private boolean hasValue() {
        return value != null;
    }

    private void switchHalfStarToFull(String cssClass, boolean addOrRemove, GQuery $star) {
        $star.children().toggleClass(cssClass, addOrRemove);
        removeHalfStar($star);
    }

    private void addHalfStar(String cssClass, boolean addOrRemove, GQuery $star) {
        if ($star.children(ICON_STAR_HALF).isEmpty()) {
            $star.append(template.halfStar().asString());
            $star.children().toggleClass(cssClass, !addOrRemove);
        }
    }

    private void handleDiscreteClass(int index, String cssClass, boolean addOrRemove) {
        int i;
        for (i = 0; i <= index; i++) {
            $($stars.get(i)).children().toggleClass(cssClass, addOrRemove);
        }

        for (; i < $stars.size(); i++) {
            $($stars.get(i)).children().toggleClass(cssClass, !addOrRemove);
        }
    }

    private void handleStepsClass(GQuery $star, Event event, int index, String cssClass, boolean addOrRemove) {
        int i;
        for (i = 0; i < index; i++) {
            $($stars.get(i)).children().toggleClass(cssClass, addOrRemove);
        }

        if (isFirstHalf($star, event)) {
            addHalfStar(cssClass, addOrRemove, $star);
        } else {
            switchHalfStarToFull(cssClass, addOrRemove, $star);
        }
        i++;

        removeHalfStars(cssClass, addOrRemove, i);
    }

    private void removeHalfStars(String cssClass, boolean addOrRemove, int startIndex) {
        for (; startIndex < $stars.size(); startIndex++) {
            GQuery $element = $($stars.get(startIndex));
            switchHalfStarToFull(cssClass, !addOrRemove, $element);
        }
    }

    private boolean isFirstHalf(GQuery $star, Event event) {
        return $star.innerWidth() / 2 > event.getClientX() - $star.offset().left;
    }

    private void pulseStars(int index) {
        int i;
        for (i = 0; i <= index; i++) {
            Element star = $stars.get(i);
            removePulse(star);

            $(star).children(":last-child")
                    .addClass("pulse")
                    .on(ANIMATION_END, new Function() {
                        @Override
                        public void f() {
                            removePulse($(this));
                        }
                    });
        }

        for (; i < $stars.size(); i++) {
            removePulse($stars.get(i));
        }
    }

    private void removePulse(Element star) {
        removePulse($(star).children());
    }

    private GQuery removePulse(GQuery $) {
        return $.removeClass("pulse")
                .off(ANIMATION_END);
    }

    private GQuery removeHalfStar(GQuery $star) {
        return $star.children(ICON_STAR_HALF).remove();
    }
}
