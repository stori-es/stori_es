package org.consumersunion.stories.common.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.common.client.widget.messages.InteractiveMessage;
import org.consumersunion.stories.common.client.widget.messages.InteractiveMessageContent;
import org.consumersunion.stories.common.client.widget.messages.MessageFactory;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.document;

public class MessagesView extends ViewImpl implements MessagesPresenter.MyView {
    interface Binder extends UiBinder<Widget, MessagesView> {
    }

    private static final int ANIMATION_LENGTH = 300;

    @UiField
    FlowPanel container;
    @UiField
    HTMLPanel main;
    @UiField
    DivElement dragger;
    @UiField
    SpanElement minimize;
    @UiField
    CommonResources res;
    @UiField
    CommonI18nLabels labels;
    @UiField
    DivElement header;
    @UiField
    SpanElement close;
    @UiField
    DivElement icons;

    private final MessageFactory messageFactory;
    private final List<Function> delayedMessages;

    private GQuery $messages;
    private int startY;
    private int startHeight;
    private GQuery $minimize;

    @Inject
    MessagesView(
            Binder binder,
            MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
        delayedMessages = new ArrayList<Function>();

        initWidget(binder.createAndBindUi(this));

        bind();
    }

    @Override
    public void onRemove(IsWidget message) {
        container.remove(message);

        if (container.getWidgetCount() == 0) {
            close();
        }
    }

    @Override
    public void displayMessage(MessageStyle style, String content) {
        final Widget message = messageFactory.createSimpleMessage(style, content);

        container.insert(message, 0);

        Function handleFunction = new Function() {
            @Override
            public void f() {
                removeMinimizedMessages();
                if (isMinimized()) {
                    displayMinimizedSimpleMessage(message);
                }

                $messages.show();
            }
        };

        maybeExecute(handleFunction);
    }

    @Override
    public void displayMessage(InteractiveMessageContent content) {
        final InteractiveMessage message = messageFactory.createInteractiveMessage(content);

        container.insert(message, 0);

        Function handleFunction = new Function() {
            @Override
            public void f() {
                removeMinimizedMessages();
                if (isMinimized()) {
                    displayMinimizedInteractiveMessage(message);
                }

                $messages.show();
            }
        };

        maybeExecute(handleFunction);
    }

    @Override
    public void clearMessages() {
        container.clear();
    }

    @Override
    protected void onAttach() {
        $messages = $("#messages");
        close();

        for (Function delayedMessage : delayedMessages) {
            delayedMessage.f();
        }
        delayedMessages.clear();
    }

    private void maybeExecute(Function handleFunction) {
        if ($messages == null) {
            delayedMessages.add(handleFunction);
        } else {
            handleFunction.f();
        }
    }

    private void bind() {
        final Function mouseMoveHandler = new Function() {
            @Override
            public boolean f(Event e) {
                $messages.height(startHeight - (e.getClientY() - startY));
                return false;
            }
        };
        final Function mouseUpHandler = new Function() {
            @Override
            public void f() {
                $messages.removeClass(res.generalStyleCss().noTransitions());
                $(document).unbind(Event.ONMOUSEMOVE, mouseMoveHandler);
                $("body *").css("cursor", null);
            }
        };

        $(dragger).mousedown(new Function() {
            @Override
            public boolean f(Event e) {
                startY = e.getClientY();
                startHeight = $messages.height();

                $(document).mousemove(mouseMoveHandler)
                        .one(Event.ONMOUSEUP, null, mouseUpHandler);
                $("body *").css("cursor", "ns-resize");
                $messages.addClass(res.generalStyleCss().noTransitions());

                return false;
            }
        });

        bindClose($(close));

        $minimize = $(minimize).add($(header));
        bindMinimize($minimize);
    }

    private void bindClose(GQuery $) {
        $.click(new Function() {
            @Override
            public boolean f(Event e) {
                close();

                return false;
            }
        });
    }

    private void close() {
        $messages.hide();
    }

    private void bindMinimize(final GQuery $minimize) {
        $minimize.one(Event.ONCLICK, null, new Function() {
            @Override
            public void f() {
                onMinimizeClicked();
            }
        });
    }

    private boolean isMinimized() {
        return $messages.hasClass(res.generalStyleCss().messagesMinimized());
    }

    private void onMinimizeClicked() {
        removeMinimizedMessages();

        $minimize.unbind(Event.ONCLICK);
        $(container).css("overflow", "hidden");

        if (isMinimized()) {
            $(container).hide().delay(1, new Function() {
                @Override
                public void f() {
                    $(container).show();
                }
            });
        } else {
            removeMinimizedMessages();
        }

        $messages.toggleClass(res.generalStyleCss().messagesMinimized());

        new Timer() {
            @Override
            public void run() {
                $(container).css("overflow", null);
                bindMinimize($minimize);

                String tooltip;
                if (isMinimized()) {
                    tooltip = labels.clickToOpen();
                    displayMinimizedMessage();
                } else {
                    tooltip = labels.clickToMinimize();
                }
                $(minimize).attr("data-tooltip", tooltip);
            }
        }.schedule(ANIMATION_LENGTH);
    }

    private void removeMinimizedMessages() {
        $(asWidget()).children().filter("." + res.generalStyleCss().messageMain()).remove();
    }

    private void displayMinimizedMessage() {
        if (container.getWidgetCount() > 0) {
            Widget message = $(container.getWidget(0)).widget();
            if (message instanceof InteractiveMessage) {
                displayMinimizedInteractiveMessage((InteractiveMessage) message);
            } else {
                displayMinimizedSimpleMessage(message);
            }
        }
    }

    private void displayMinimizedSimpleMessage(Widget message) {
        Function clickFunction = new Function() {
            @Override
            public void f() {
                onMinimizeClicked();
            }
        };

        GQuery icons = $(this.icons).clone();
        bindClose(icons.children().last());

        final GQuery $message = $(message).clone();
        $message.append(icons)
                .prependTo($(asWidget()))
                .click(clickFunction);
        icons.children().first().click(clickFunction);

        $messages.delay(10000, new Function() {
            @Override
            public void f() {
                $message.remove();
            }
        });
    }

    private void displayMinimizedInteractiveMessage(InteractiveMessage message) {
        Function clickFunction = new Function() {
            @Override
            public void f() {
                onMinimizeClicked();
            }
        };

        GQuery icons = $(this.icons).clone();
        bindClose(icons.children().last());

        final GQuery $message = $(message.asMinimized());
        $message.append(icons)
                .prependTo($(asWidget()))
                .click(clickFunction);
        icons.children().first().click(clickFunction);
    }
}
