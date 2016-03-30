package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionstoken;

import java.util.Comparator;
import java.util.List;

import org.consumersunion.stories.common.client.ui.EntitySuggestionOracle.EntitySuggestion;
import org.consumersunion.stories.common.client.widget.ListItemHandler;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.HasTitle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.collect.FluentIterable;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public abstract class AbstractCollectionsTokenView<Summary, Dto extends HasTitle, Data>
        extends ViewWithUiHandlers<CollectionsTokenUiHandlers<Summary, Dto, Data>>
        implements CollectionsTokenView<Summary, Dto, Data>, ListItemHandler<Summary>, Comparator<Summary> {
    interface Binder extends UiBinder<Widget, AbstractCollectionsTokenView> {
    }

    static final CommonI18nLabels LABELS = GWT.create(CommonI18nLabels.class);

    private static final Binder BINDER = GWT.create(Binder.class);
    private static final Resources RESOURCES = GWT.create(Resources.class);

    @UiField(provided = true)
    final SuggestBox itemBox;

    @UiField
    FlowPanel collectionsList;
    @UiField
    HTML collectionsEdit;
    @UiField
    FocusPanel collectionsListWrapper;
    @UiField
    SpanElement title;
    @UiField
    SpanElement questionnaireIcon;
    @UiField
    SpanElement collectionIcon;

    private boolean editing;

    AbstractCollectionsTokenView(
            final SuggestOracle suggestOracle) {
        this.itemBox = new SuggestBox(suggestOracle, new TextBox(), getSuggestionDisplay());

        initWidget(BINDER.createAndBindUi(this));

        setEditing(false, LABELS);

        itemBox.setAutoSelectEnabled(true);

        initSuggestionBox();

        itemBox.setVisible(false);
    }

    @Override
    public void setData(List<Summary> data) {
        clear();

        List<Summary> sortedData = FluentIterable.from(data).toSortedList(this);

        for (Summary summary : sortedData) {
            boolean canRemove = getUiHandlers().canRemoveCollection(summary);
            IsWidget item = createItem(summary, canRemove);
            collectionsList.add(item);
        }
    }

    @Override
    public void clear() {
        collectionsList.clear();
    }

    @Override
    public void showQuestionnaireIcon() {
        $(questionnaireIcon).show();
    }

    @Override
    public void onListItemClicked(Summary summary) {
        getUiHandlers().collectionDetails(summary);
    }

    @Override
    public void onListItemRemoved(Summary summary) {
        getUiHandlers().removeCollection(summary);
    }

    protected abstract IsWidget createItem(Summary summary, boolean canRemove);

    @UiHandler("collectionsEdit")
    void onAddToCollectionClick(ClickEvent event) {
        if (!editing) {
            itemBox.refreshSuggestionList();
            $(collectionsListWrapper).css("box-shadow", "0 0 0 1px #000").css("background-color", "#fff");
            itemBox.setVisible(true);
            setFocus(true);
            setEditing(true, LABELS);
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
                EntitySuggestion<Dto> selectedItem = (EntitySuggestion<Dto>) event.getSelectedItem();
                getUiHandlers().onCollectionSelected(selectedItem.getEntity());
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
        setEditing(false, LABELS);
        setFocus(false);
        $(collectionsListWrapper).css("box-shadow", "none").css("background-color", "transparent");
    }

    private SuggestBox.DefaultSuggestionDisplay getSuggestionDisplay() {
        return new SuggestBox.DefaultSuggestionDisplay() {
            @Override
            protected PopupPanel createPopup() {
                PopupPanel popup = super.createPopup();
                popup.addStyleName(RESOURCES.generalStyleCss().addToCollectionSuggest());
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
