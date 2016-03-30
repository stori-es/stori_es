package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.client.event.CollectionChangedEvent;
import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.HasId;
import org.consumersunion.stories.common.shared.model.HasTitle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import static com.google.gwt.query.client.GQuery.$;

public abstract class AbstractListItem<T extends HasTitle & HasId>
        implements IsWidget, CollectionChangedEvent.CollectionChangedHandler {
    interface Binder extends UiBinder<Widget, AbstractListItem> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);
    private static final StoryTellerDashboardI18nLabels LABELS = GWT.create(StoryTellerDashboardI18nLabels.class);

    @UiField
    Element remove;
    @UiField
    SpanElement container;
    @UiField
    HTMLPanel main;
    @UiField
    Element icon;

    protected final T content;

    private final Widget widget;

    AbstractListItem(
            EventBus eventBus,
            T content,
            final ListItemHandler<T> handler,
            final boolean canRemove,
            final boolean withClickAction) {
        widget = BINDER.createAndBindUi(this);

        this.content = content;
        container.setInnerText(content.getTitle());
        icon.addClassName(getContentKind(content).getIcon());

        eventBus.addHandler(CollectionChangedEvent.TYPE, AbstractListItem.this);
        asWidget().addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    onAttach(withClickAction, handler, canRemove);
                } else {
                    removeEvents();
                }
            }
        });
    }

    @Override
    public void onCollectionChanged(CollectionChangedEvent event) {
        Collection collection = event.getCollection();
        if (collection.getId() == content.getId()) {
            container.setInnerText(collection.getTitle());
        }
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    protected abstract ContentKind getContentKind(T content);

    private void setupClick(final ListItemHandler<T> handler) {
        $(main).click(new Function() {
            @Override
            public void f() {
                if (handler != null) {
                    handler.onListItemClicked(content);
                }
            }
        }).attr("data-tooltip", LABELS.clickToOpen());
    }

    private void setupRemove(
            final ListItemHandler<T> handler,
            boolean withClickAction) {
        $(main).css("paddingRight", "16px");
        GQuery $remove = $(remove);

        if (withClickAction) {
            $remove.mouseenter(new Function() {
                @Override
                public void f() {
                    $(widget).attr("data-tooltip", "")
                            .css("pointer-events", "none")
                            .removeClass("simptip-position-top");
                }
            }).mouseleave(new Function() {
                @Override
                public void f() {
                    $(widget).attr("data-tooltip", LABELS.clickToOpen())
                            .css("pointer-events", "all")
                            .addClass("simptip-position-top");
                }
            });
        }

        $remove.click(new Function() {
            @Override
            public boolean f(Event e) {
                if (handler != null) {
                    handler.onListItemRemoved(content);
                }

                e.stopPropagation();

                $(asWidget()).remove();

                return false;
            }
        }).attr("data-tooltip", LABELS.clickToRemove());
    }

    private void removeEvents() {
        $(main).unbind(BrowserEvents.CLICK);
        $(remove).unbind(BrowserEvents.CLICK)
                .unbind("mouseenter")
                .unbind("mouseleave");
    }

    private void onAttach(
            boolean withClickAction,
            ListItemHandler<T> handler,
            boolean canRemove) {
        if (withClickAction) {
            setupClick(handler);
        }

        if (canRemove) {
            setupRemove(handler, withClickAction);
        } else {
            remove.removeFromParent();
        }
    }
}
