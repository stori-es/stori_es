package org.consumersunion.stories.dashboard.client.application.widget;

import java.util.Set;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.dashboard.client.resource.GeneralStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class TagsView extends ViewWithUiHandlers<TagsUiHandlers>
        implements TagsPresenter.MyView, Focusable, TagWidget.TagHandler {
    interface Binder extends UiBinder<FocusPanel, TagsView> {
    }

    public static final int COMMA_CODE = 188;
    public static final int MAX_SIZE = 45;

    @UiField(provided = true)
    final SuggestBox itemBox;

    @UiField
    FlowPanel tags;
    @UiField
    FocusPanel main;

    private final TagWidgetFactory tagWidgetFactory;
    private final MessageDispatcher messageDispatcher;
    private final CommonI18nErrorMessages errorMessages;
    private final GeneralStyle style;

    private GQuery selectedTag;
    private Boolean enabled;

    @Inject
    TagsView(
            Binder uiBinder,
            TagWidgetFactory tagWidgetFactory,
            MessageDispatcher messageDispatcher,
            Resources resources,
            CommonI18nErrorMessages errorMessages) {
        this.tagWidgetFactory = tagWidgetFactory;
        this.messageDispatcher = messageDispatcher;
        this.errorMessages = errorMessages;
        this.itemBox = new SuggestBox(new MultiWordSuggestOracle());
        style = resources.generalStyleCss();

        initWidget(uiBinder.createAndBindUi(this));
        setEnabled(true);

        initSuggestionBox(messageDispatcher, errorMessages);

        itemBox.setAutoSelectEnabled(false);

        initKeyHandlers();

        itemBox.setVisible(false);
    }

    @Override
    public void setSuggestions(Set<String> tags) {
        ((MultiWordSuggestOracle) itemBox.getSuggestOracle()).clear();
        ((MultiWordSuggestOracle) itemBox.getSuggestOracle()).addAll(tags);
    }

    @Override
    public int getTabIndex() {
        return main.getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
        main.setAccessKey(key);
    }

    @Override
    public void setTabIndex(int index) {
        main.setTabIndex(index);
    }

    @Override
    public void setFocus(final boolean focused) {
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

    public void initialize() {
        itemBox.setText("");
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;

        itemBox.setVisible(enabled);
        itemBox.setFocus(true);
    }

    @Override
    public void onTagRemoved(String tag) {
        getUiHandlers().onTagRemoved(tag);
    }

    @Override
    public void addTag(String tag) {
        tags.add(tagWidgetFactory.create(tag, this));
    }

    @Override
    public void startEdit() {
        itemBox.setVisible(true);
        setFocus(true);
        $(main).css("box-shadow", "0 0 0 1px #000").css("background-color", "#fff");
    }

    @Override
    public void clearTags() {
        tags.clear();
    }

    @Override
    public void onTagClicked(String tag) {
        getUiHandlers().onTagClicked(tag);
    }

    private void onItemBoxKeyUp(Event event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyCodes.KEY_ENTER || keyCode == COMMA_CODE) {
            onEnterOrComma(keyCode);
        } else if (keyCode == KeyCodes.KEY_ESCAPE) {
            if (isShowingSuggestions()) {
                ((SuggestBox.DefaultSuggestionDisplay) itemBox.getSuggestionDisplay()).hideSuggestions();
            } else {
                itemBox.setText("");
                editComplete();
            }
        } else if (keyCode == KeyCodes.KEY_LEFT) {
            onLeftArrow();
        } else if (keyCode == KeyCodes.KEY_RIGHT) {
            onRightArrow();
        } else if (keyCode != KeyCodes.KEY_BACKSPACE) {
            // Backspace is handled on KeyDown
            deselectTag();
        }
    }

    private void onBackspace() {
        if (itemBox.getText().isEmpty()) {
            if (selectedTag == null) {
                onLeftArrow();
            } else {
                $(selectedTag).children().last().click();
                deselectTag();
                onLeftArrow();
            }
        }
    }

    private void onEnterOrComma(int keyCode) {
        if (keyCode == KeyCodes.KEY_ENTER && selectedTag != null) {
            onTagClicked($(selectedTag).children().first().text());
        } else {
            String value = getCurrentTag();
            value = value.replaceAll(",", "");

            if (!value.isEmpty() && value.length() < MAX_SIZE) {
                getUiHandlers().onTagAdded(value);

                itemBox.setText("");
            } else if (keyCode != COMMA_CODE && value.isEmpty() && !isShowingSuggestions()) {
                editComplete();
            } else if (keyCode == COMMA_CODE) {
                itemBox.setText("");
            } else {
                if (value.length() > MAX_SIZE) {
                    messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessages.tagToLong());
                }
            }
        }
    }

    private void deselectTag() {
        if (selectedTag != null) {
            selectedTag.removeClass(style.selectedTag());
            selectedTag = null;
        }
    }

    private void onLeftArrow() {
        GQuery tagsChildren = $(tags).children();
        if (selectedTag == null && !tagsChildren.isEmpty()) {
            selectedTag = tagsChildren.last();
            selectedTag.addClass(style.selectedTag());
        } else if (selectedTag != null) {
            selectedTag.removeClass(style.selectedTag());
            int index = tagsChildren.index(selectedTag.get(0)) - 1;
            index = index < 0 ? 0 : index;
            selectedTag = $(tagsChildren.get(index));
            selectedTag.addClass(style.selectedTag());
        }
    }

    private void onRightArrow() {
        if (selectedTag != null) {
            selectedTag.removeClass(style.selectedTag());

            GQuery tagsChildren = $(tags).children();
            int index = tagsChildren.index(selectedTag.get(0)) + 1;
            index = index >= tagsChildren.size() ? tagsChildren.size() - 1 : index;

            selectedTag = $(tagsChildren.get(index));
            selectedTag.addClass(style.selectedTag());
        }
    }

    private void initKeyHandlers() {
        asWidget().addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    $(itemBox).keyup(new Function() {
                        @Override
                        public boolean f(Event e) {
                            onItemBoxKeyUp(e);
                            return true;
                        }
                    }).keydown(new Function() {
                        @Override
                        public boolean f(Event e) {
                            if (e.getKeyCode() == KeyCodes.KEY_LEFT || e.getKeyCode() == KeyCodes.KEY_RIGHT) {
                                e.preventDefault();
                                return false;
                            } else if (e.getKeyCode() == KeyCodes.KEY_BACKSPACE) {
                                onBackspace();
                            }

                            return true;
                        }
                    });
                } else {
                    $(itemBox).unbind(BrowserEvents.KEYUP);
                    $(itemBox).unbind(BrowserEvents.KEYDOWN);
                }
            }
        });
    }

    private boolean isShowingSuggestions() {
        return ((SuggestBox.DefaultSuggestionDisplay) itemBox.getSuggestionDisplay()).isSuggestionListShowing();
    }

    private void editComplete() {
        itemBox.setVisible(false);
        setFocus(false);
        $(main).css("box-shadow", "none").css("background-color", "transparent");
        deselectTag();
        getUiHandlers().onEditComplete();
    }

    private void initSuggestionBox(
            final MessageDispatcher messageDispatcher,
            final CommonI18nErrorMessages errorMessages) {
        itemBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                // If not from ENTER key, otherwise leave processing to KeyUp handler
                if (Event.getCurrentEvent() == null) {
                    String value = itemBox.getValue().toLowerCase().trim();
                    getUiHandlers().onTagAdded(value);
                    itemBox.setText("");
                    setFocus(true);
                }
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
                        boolean targetIsPopup = $(target).parent().hasClass("suggestPopupContent");
                        boolean targetIsContainer = target.equals(main.getElement());

                        if (!targetIsPopup && !targetIsContainer) {
                            String value = getCurrentTag();
                            if (!Strings.isNullOrEmpty(value) && value.length() < MAX_SIZE) {
                                getUiHandlers().onTagAdded(value);
                                itemBox.setText("");
                            } else {
                                if (value.length() > MAX_SIZE) {
                                    messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessages.tagToLong());
                                }
                            }
                            editComplete();
                        } else if (targetIsContainer) {
                            setFocus(true);
                        }
                    }
                });
            }
        });
    }

    private String getCurrentTag() {
        return itemBox.getValueBox().getText().trim().toLowerCase();
    }

    private native Element getActiveElement() /*-{
        return $wnd.document.activeElement;
    }-*/;
}
