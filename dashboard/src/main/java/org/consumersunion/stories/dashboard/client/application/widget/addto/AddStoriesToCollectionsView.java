package org.consumersunion.stories.dashboard.client.application.widget.addto;

import org.consumersunion.stories.common.client.ui.CollectionSuggestionOracle;
import org.consumersunion.stories.common.client.ui.EntitySuggestionOracle;
import org.consumersunion.stories.common.client.util.WidgetIds;
import org.consumersunion.stories.common.client.widget.CollectionListItem;
import org.consumersunion.stories.common.client.widget.CollectionListItemFactory;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.StorySelectField;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class AddStoriesToCollectionsView extends AbstractAddToView<AddToCollectionsUiHandlers>
        implements AddStoriesToCollectionsPresenter.MyView {
    interface Binder extends UiBinder<Widget, AddStoriesToCollectionsView> {
    }

    @UiField(provided = true)
    final ClickableDropDown<StorySelectField> selectDropDown;
    @UiField(provided = true)
    final SuggestBox itemBox;

    @UiField
    HTMLPanel assignPanel;
    @UiField
    FocusPanel collectionsListWrapper;
    @UiField
    FlowPanel collectionsList;
    @UiField
    Button goAssign;
    @UiField
    SpanElement cancel;

    private final CommonI18nLabels labels;
    private final CollectionListItemFactory collectionListItemFactory;

    @Inject
    AddStoriesToCollectionsView(
            Binder uiBinder,
            CommonI18nLabels labels,
            CollectionListItemFactory collectionListItemFactory,
            CollectionSuggestionOracle collectionSuggestionOracle,
            ClickableDropDown<StorySelectField> selectDropDown) {
        super(selectDropDown);

        this.labels = labels;
        this.collectionListItemFactory = collectionListItemFactory;
        this.itemBox = new SuggestBox(collectionSuggestionOracle);
        this.selectDropDown = selectDropDown;

        itemBox.getValueBox().getElement().setAttribute("placeholder", labels.enterTargetCollectionNames());
        itemBox.setAutoSelectEnabled(true);

        initWidget(uiBinder.createAndBindUi(this));

        bind();
        setSuggestionFilter(collectionSuggestionOracle);

        assignPanel.getElement().setId(WidgetIds.ADD_STORIES_TO_COLLECTION);
        selectDropDown.getElement().setId(WidgetIds.ADD_STORIES_TO_COLLECTION_DROPDOWN);
        goAssign.getElement().setId(WidgetIds.ADD_STORIES_TO_COLLECTION_GO_BUTTON);
    }

    @Override
    public void updateCollectionLink(int numberOfCollections, int numberOfSelectedStories) {
        goAssign.setEnabled(numberOfCollections > 0 && numberOfSelectedStories > 0);
    }

    @Override
    public void onItemRemoved() {
        updatePlaceholder();
    }

    @Override
    public void reset() {
        super.reset();

        updateCollectionLink(0, 0);

        collectionsList.clear();
    }

    @UiHandler("collectionsListWrapper")
    void onCollectionsListWrapperClick(ClickEvent event) {
        itemBox.refreshSuggestionList();
        setFocus(true);
    }

    private void bind() {
        initSuggestionBox();

        $(cancel).click(new Function() {
            @Override
            public void f() {
                getUiHandlers().onCancelClicked();
            }
        });
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

    private void updatePlaceholder() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (collectionsList.getWidgetCount() == 0) {
                    itemBox.getValueBox().getElement().setAttribute("placeholder", labels.enterTargetCollectionNames());
                } else {
                    itemBox.getValueBox().getElement().removeAttribute("placeholder");
                }
            }
        });
    }

    private void initSuggestionBox() {
        itemBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                EntitySuggestionOracle.EntitySuggestion<Collection> selectedItem =
                        (EntitySuggestionOracle.EntitySuggestion<Collection>) event.getSelectedItem();

                CollectionSummary collectionSummary = new CollectionSummary(selectedItem.getEntity());
                CollectionListItem item =
                        collectionListItemFactory.create(collectionSummary, getUiHandlers(), true, false);
                collectionsList.add(item);
                getUiHandlers().onListItemAdded(collectionSummary);

                updatePlaceholder();
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
                        GQuery parentsUntil = $(target).parentsUntil(".gwt-SuggestBoxPopup");
                        boolean targetIsPopup = parentsUntil.size() > 1 && !parentsUntil.last().parent().isEmpty();

                        if (targetIsContainer || targetIsPopup) {
                            setFocus(true);
                        } else {
                            editComplete();
                        }
                    }
                });
            }
        });
    }

    private void editComplete() {
        updatePlaceholder();
        itemBox.setText("");
        setFocus(false);
    }

    private native Element getActiveElement() /*-{
        return $wnd.document.activeElement;
    }-*/;

    private void setSuggestionFilter(final CollectionSuggestionOracle collectionSuggestionOracle) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                collectionSuggestionOracle.setFilter(getUiHandlers());
            }
        });
    }
}
