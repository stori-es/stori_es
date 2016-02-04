package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.client.event.CollectionChangedEvent;
import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

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
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.web.bindery.event.shared.EventBus;

public class CollectionListItem implements IsWidget, CollectionChangedEvent.CollectionChangedHandler {
    public interface ListItemHandler {
        void onListItemClicked(CollectionSummary collection);

        void onListItemRemoved(CollectionSummary collection);
    }

    interface Binder extends UiBinder<Widget, CollectionListItem> {
    }

    @UiField
    Element remove;
    @UiField
    SpanElement container;
    @UiField
    HTMLPanel main;
    @UiField
    Element icon;

    private final Widget widget;
    private final CollectionSummary collectionSummary;

    @AssistedInject
    CollectionListItem(
            Binder binder,
            StoryTellerDashboardI18nLabels labels,
            EventBus eventBus,
            @Assisted CollectionSummary collectionSummary,
            @Assisted ListItemHandler handler,
            @Assisted boolean canRemove) {
        this(binder, labels, eventBus, collectionSummary, handler, canRemove, true);
    }

    @AssistedInject
    CollectionListItem(
            Binder binder,
            final StoryTellerDashboardI18nLabels labels,
            final EventBus eventBus,
            @Assisted CollectionSummary collectionSummary,
            @Assisted final ListItemHandler handler,
            @Assisted("canRemove") final boolean canRemove,
            @Assisted("withClickAction") final boolean withClickAction) {
        widget = binder.createAndBindUi(this);

        this.collectionSummary = collectionSummary;
        container.setInnerText(collectionSummary.getTitle());
        icon.addClassName(getContentKind(collectionSummary).getIcon());

        eventBus.addHandler(CollectionChangedEvent.TYPE, CollectionListItem.this);
        asWidget().addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    onAttach(withClickAction, handler, labels, canRemove);
                } else {
                    removeEvents();
                }
            }
        });
    }

    @Override
    public void onCollectionChanged(CollectionChangedEvent event) {
        Collection collection = event.getCollection();
        if (collection.getId() == collectionSummary.getId()) {
            container.setInnerText(collection.getTitle());
        }
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    private ContentKind getContentKind(CollectionSummary collectionSummary) {
        if (collectionSummary.isQuestionnaire()) {
            return ContentKind.QUESTIONNAIRE;
        }

        return ContentKind.COLLECTION;
    }

    private void setupClick(
            final ListItemHandler handler,
            final StoryTellerDashboardI18nLabels labels) {
        GQuery.$(main).click(new Function() {
            @Override
            public void f() {
                if (handler != null) {
                    handler.onListItemClicked(collectionSummary);
                }
            }
        }).attr("data-tooltip", labels.clickToOpen());
    }

    private void setupRemove(
            final ListItemHandler handler,
            final StoryTellerDashboardI18nLabels labels,
            boolean withClickAction) {
        GQuery.$(main).css("paddingRight", "16px");
        GQuery $remove = GQuery.$(remove);
        $remove.click(new Function() {
            @Override
            public boolean f(Event e) {
                if (handler != null) {
                    handler.onListItemRemoved(collectionSummary);
                }

                e.stopPropagation();

                GQuery.$(asWidget()).remove();

                return false;
            }
        }).attr("data-tooltip", labels.clickToRemove());

        if (withClickAction) {
            $remove.mouseenter(new Function() {
                @Override
                public void f() {
                    GQuery.$(widget).removeClass("simptip-position-top");
                    GQuery.$(widget).attr("data-tooltip", "");
                }
            }).mouseleave(new Function() {
                @Override
                public void f() {
                    GQuery.$(widget).attr("data-tooltip", labels.clickToOpen());
                    GQuery.$(widget).addClass("simptip-position-top");
                }
            });
        }
    }

    private void removeEvents() {
        GQuery.$(main).unbind(BrowserEvents.CLICK);
        GQuery.$(remove).unbind(BrowserEvents.CLICK)
                .unbind("mouseenter")
                .unbind("mouseleave");
    }

    private void onAttach(
            boolean withClickAction,
            ListItemHandler handler,
            StoryTellerDashboardI18nLabels labels,
            boolean canRemove) {
        if (withClickAction) {
            setupClick(handler, labels);
        }

        if (canRemove) {
            setupRemove(handler, labels, withClickAction);
        } else {
            remove.removeFromParent();
        }
    }
}
