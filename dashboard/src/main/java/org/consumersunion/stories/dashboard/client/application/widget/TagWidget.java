package org.consumersunion.stories.dashboard.client.application.widget;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;

/**
 * Widget container for a single tag element. Has a click handler and delete handler defined through
 * the {@link org.consumersunion.stories.dashboard.client.application.widget.TagWidget.TagHandler}
 */
public class TagWidget implements IsWidget {
    interface Binder extends UiBinder<Widget, TagWidget> {
    }

    interface TagHandler {
        void onTagClicked(String tag);

        void onTagRemoved(String tag);
    }

    @UiField
    SpanElement remove;
    @UiField
    SpanElement tagContainer;
    @UiField
    HTMLPanel main;

    private final Widget widget;
    private final String tag;

    @Inject
    TagWidget(
            Binder binder,
            StoryTellerDashboardI18nLabels labels,
            @Assisted String tag,
            @Assisted TagHandler handler) {
        widget = binder.createAndBindUi(this);

        this.tag = tag;
        tagContainer.setInnerText(tag);

        setupTagClick(handler, labels);
        setupRemove(handler, labels);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    private void setupTagClick(final TagHandler handler, final StoryTellerDashboardI18nLabels labels) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                $(main).click(new Function() {
                    @Override
                    public void f() {
                        handler.onTagClicked(tag);
                    }
                }).attr("data-tooltip", labels.clickToSearch());
            }
        });
    }

    private void setupRemove(final TagHandler handler, final StoryTellerDashboardI18nLabels labels) {
        $(remove).click(new Function() {
            @Override
            public boolean f(Event e) {
                handler.onTagRemoved(tag);
                asWidget().removeFromParent();

                e.stopPropagation();

                return false;
            }
        }).mouseenter(new Function() {
            @Override
            public void f() {
                $(widget).removeClass("simptip-position-top");
                $(widget).attr("data-tooltip", "");
            }
        }).mouseleave(new Function() {
            @Override
            public void f() {
                $(widget).attr("data-tooltip", labels.clickToSearch());
                $(widget).addClass("simptip-position-top");
            }
        }).attr("data-tooltip", labels.clickToDelete());
    }
}
