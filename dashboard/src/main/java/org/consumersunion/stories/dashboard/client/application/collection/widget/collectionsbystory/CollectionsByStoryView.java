package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbystory;

import java.util.Comparator;
import java.util.List;

import org.consumersunion.stories.common.client.ui.CollectionSuggestionOracle;
import org.consumersunion.stories.common.client.ui.EntitySuggestionOracle.EntitySuggestion;
import org.consumersunion.stories.common.client.widget.CollectionListItem;
import org.consumersunion.stories.common.client.widget.CollectionListItemFactory;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class CollectionsByStoryView extends ViewWithUiHandlers<CollectionsByStoryUiHandlers>
        implements CollectionsByStoryPresenter.MyView, CollectionListItem.ListItemHandler {
    interface Binder extends UiBinder<Widget, CollectionsByStoryView> {
    }

    private static final Comparator<CollectionSummary> collectionItemOrderComparator
            = new Comparator<CollectionSummary>() {
        @Override
        public int compare(CollectionSummary o1, CollectionSummary o2) {
            if (o1.isQuestionnaire() && !o2.isQuestionnaire()) {
                return -1;
            } else if (o2.isQuestionnaire() && !o1.isQuestionnaire()) {
                return 1;
            }

            return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        }
    };

    @UiField(provided = true)
    final SuggestBox itemBox;

    @UiField
    FlowPanel collectionsList;
    @UiField
    HTML collectionsEdit;
    @UiField
    FocusPanel collectionsListWrapper;

    private final CommonI18nLabels labels;
    private final CollectionListItemFactory collectionListItemFactory;
    private final Resources resources;

    private boolean editing;

    @Inject
    CollectionsByStoryView(
            Binder uiBinder,
            CommonI18nLabels labels,
            final CollectionSuggestionOracle collectionSuggestionOracle,
            CollectionListItemFactory collectionListItemFactory,
            Resources resources) {
        this.labels = labels;
        this.collectionListItemFactory = collectionListItemFactory;
        this.resources = resources;
        this.itemBox = new SuggestBox(collectionSuggestionOracle, new TextBox(), getSuggestionDisplay());

        initWidget(uiBinder.createAndBindUi(this));

        setEditing(false, labels);

        itemBox.setAutoSelectEnabled(true);

        initSuggestionBox();

        itemBox.setVisible(false);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                collectionSuggestionOracle.setFilter(getUiHandlers());
            }
        });
    }

    @Override
    public void setData(List<CollectionSummary> data) {
        clear();

        ImmutableList<CollectionSummary> sortedData = FluentIterable.from(data)
                .toSortedList(collectionItemOrderComparator);

        for (CollectionSummary collectionSummary : sortedData) {
            boolean canRemove = getUiHandlers().canRemoveCollection(collectionSummary);
            CollectionListItem item = collectionListItemFactory.create(collectionSummary, this, canRemove);
            collectionsList.add(item);
        }
    }

    @Override
    public void clear() {
        collectionsList.clear();
    }

    @Override
    public void onListItemClicked(CollectionSummary collection) {
        getUiHandlers().collectionDetails(collection.getId());
    }

    @Override
    public void onListItemRemoved(CollectionSummary collection) {
        getUiHandlers().removeFromCollection(collection);
    }

    @UiHandler("collectionsEdit")
    void onAddToCollectionClick(ClickEvent event) {
        if (!editing) {
            itemBox.refreshSuggestionList();
            $(collectionsListWrapper).css("box-shadow", "0 0 0 1px #000").css("background-color", "#fff");
            itemBox.setVisible(true);
            setFocus(true);
            setEditing(true, labels);
        } else {
            editComplete();
        }

        getUiHandlers().redraw();
    }

    private void setEditing(boolean editing, CommonI18nLabels labels) {
        this.editing = editing;
        if (editing) {
            collectionsEdit.getElement().setAttribute("data-tooltip", labels.clickToStopEditing());
        } else {
            collectionsEdit.getElement().setAttribute("data-tooltip", labels.clickToEdit());
        }
    }

    private void setFocus(final boolean focused) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                $(itemBox).delay(1, new Function() {
                    @Override
                    public void f() {
                        itemBox.setFocus(focused);
                    }
                });
            }
        });
    }

    private void initSuggestionBox() {
        itemBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                EntitySuggestion<Collection> selectedItem = (EntitySuggestion<Collection>) event.getSelectedItem();
                getUiHandlers().addStoryToCollection(selectedItem.getEntity());
                itemBox.setText("");
                setFocus(true);
            }
        });

        itemBox.getValueBox().addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent blurEvent) {
                // Delay is important here, or document.activeElement will return body
                $(itemBox.getValueBox()).delay(1, new Function() {
                    @Override
                    public void f() {
                        Element target = getActiveElement();
                        boolean targetIsContainer = target.equals(collectionsListWrapper.getElement());

                        if (targetIsContainer) {
                            setFocus(true);
                        }
                    }
                });
            }
        });
    }

    private void editComplete() {
        itemBox.setText("");
        itemBox.setVisible(false);
        setEditing(false, labels);
        setFocus(false);
        $(collectionsListWrapper).css("box-shadow", "none").css("background-color", "transparent");
    }

    private SuggestBox.DefaultSuggestionDisplay getSuggestionDisplay() {
        return new SuggestBox.DefaultSuggestionDisplay() {
            @Override
            protected PopupPanel createPopup() {
                PopupPanel popup = super.createPopup();
                popup.addStyleName(resources.generalStyleCss().addToCollectionSuggest());
                return popup;
            }

            @Override
            protected void showSuggestions(
                    SuggestBox suggestBox,
                    java.util.Collection<? extends SuggestOracle.Suggestion> suggestions,
                    boolean isDisplayStringHTML,
                    boolean isAutoSelectEnabled, SuggestBox.SuggestionCallback callback) {
                super.showSuggestions(suggestBox, suggestions, isDisplayStringHTML, isAutoSelectEnabled, callback);

                GQuery $popup = $(getPopupPanel());
                $popup.width(collectionsListWrapper.getOffsetWidth() + 2);
                $popup.css("left", $popup.left() - 3 + "px");
            }
        };
    }

    private native Element getActiveElement() /*-{
        return $wnd.document.activeElement;
    }-*/;
}
