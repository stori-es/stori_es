package org.consumersunion.stories.dashboard.client.application.ui.token;

import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.util.WidgetIds;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.dashboard.client.resource.TokenListStyle;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class TokenInput extends FocusPanel implements LeafValueEditor<String> {
    interface Binder extends UiBinder<Widget, TokenInput> {
    }

    public interface Handler {
        void onTokenAdded(String token);

        void onTokenRemoved(String token);

        void editComplete();
    }

    public static final int COMMA_CODE = 188;
    public static final int MAX_SIZE = 45;

    private static final int SUGGESTIONS_LIMIT = 7;

    @UiField(provided = true)
    final CellList<String> tokenList;
    @UiField(provided = true)
    final SuggestBox itemBox;

    @UiField
    HTMLPanel main;

    private final ListDataProvider<String> dataProvider;
    private final MessageDispatcher messageDispatcher;
    private final CommonI18nErrorMessages errorMessages;

    private Boolean enabled;
    private Handler handler;

    @Inject
    TokenInput(
            Binder uiBinder,
            TokenListStyle tokenListStyle,
            TokenCellFactory tokenCellFactory,
            final MessageDispatcher messageDispatcher,
            final CommonI18nErrorMessages errorMessages) {
        this.messageDispatcher = messageDispatcher;
        this.errorMessages = errorMessages;
        this.dataProvider = new ListDataProvider<String>();
        this.itemBox = new SuggestBox(new MultiWordSuggestOracle());
        this.tokenList = new CellList<String>(tokenCellFactory.create(setupRemoveAction()), tokenListStyle);
        tokenList.setPageSize(Integer.MAX_VALUE);
        itemBox.setLimit(SUGGESTIONS_LIMIT);

        setWidget(uiBinder.createAndBindUi(this));
        setEnabled(true);
        dataProvider.addDataDisplay(tokenList);

        itemBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                String value = itemBox.getValue().toLowerCase().trim();
                if (!dataProvider.getList().contains(value)) {
                    dataProvider.getList().add(value);
                    dataProvider.refresh();
                }

                itemBox.setText("");
                if (handler != null) {
                    handler.onTokenAdded(suggestionSelectionEvent.getSelectedItem().getDisplayString());
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

                        if (!targetIsPopup) {
                            String value = itemBox.getValueBox().getText().trim().toLowerCase();
                            if (!Strings.isNullOrEmpty(value)
                                    && value.length() < MAX_SIZE
                                    && !dataProvider.getList().contains(value)) {
                                dataProvider.getList().add(value);
                                dataProvider.refresh();
                                itemBox.setText("");
                                if (handler != null) {
                                    handler.onTokenAdded(itemBox.getValueBox().getText());
                                }
                            } else {
                                if (value.length() > MAX_SIZE) {
                                    messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessages.tagToLong());
                                }
                            }
                        } else if (handler != null) {
                            handler.editComplete();
                        }
                    }
                });
            }
        });

        addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    $(main).click(new Function() {
                        @Override
                        public void f() {
                            setFocus(true);
                        }
                    });
                } else {
                    $(main).unbind(BrowserEvents.CLICK);
                }
            }
        });

        itemBox.setAutoSelectEnabled(false);
        itemBox.getValueBox().addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                onItemBoxKeyUp(event);
            }
        });

        getElement().setId(WidgetIds.TAG_INPUT);
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
        tokenList.setRowCount(0);
        dataProvider.getList().clear();
        itemBox.setText("");
    }

    @Override
    public void setValue(String value) {
        List<String> tags = Lists.newArrayList(Splitter.on(", ").trimResults().omitEmptyStrings().split(value));

        setTags(tags);
    }

    public void setTags(List<String> tags) {
        dataProvider.getList().clear();
        dataProvider.getList().addAll(tags);
        dataProvider.refresh();
    }

    @Override
    public String getValue() {
        return Joiner.on(",").join(dataProvider.getList());
    }

    public Set<String> getTags() {
        return Sets.newLinkedHashSet(dataProvider.getList());
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void addSuggestion(java.util.Collection<String> tags) {
        ((MultiWordSuggestOracle) itemBox.getSuggestOracle()).addAll(tags);
    }

    public void clearSuggestion() {
        ((MultiWordSuggestOracle) itemBox.getSuggestOracle()).clear();
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;

        itemBox.setVisible(enabled);
        itemBox.setFocus(true);
    }

    private void onItemBoxKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == COMMA_CODE) {
            String value = itemBox.getText().toLowerCase().trim();

            if (!Strings.isNullOrEmpty(value) && value.length() < MAX_SIZE) {
                if (event.getNativeKeyCode() == COMMA_CODE) {
                    value = value.substring(0, value.length() - 1);
                }

                if (!dataProvider.getList().contains(value)) {
                    dataProvider.getList().add(value);
                    dataProvider.refresh();
                }

                if (handler != null) {
                    handler.onTokenAdded(value);
                }

                itemBox.setText("");
            } else {
                if (value.length() > MAX_SIZE) {
                    messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessages.tagToLong());
                }
            }
        } else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
            itemBox.setText("");
            handler.editComplete();
        }
    }

    private ActionCell.Delegate<String> setupRemoveAction() {
        return new ActionCell.Delegate<String>() {
            @Override
            public void execute(String tag) {
                dataProvider.getList().remove(tag);
                dataProvider.refresh();

                if (handler != null) {
                    handler.onTokenRemoved(tag);
                }
            }
        };
    }

    private native Element getActiveElement() /*-{
        return $wnd.document.activeElement;
    }-*/;
}
