package org.consumersunion.stories.common.client.widget.messages;

import org.consumersunion.stories.common.client.widget.MessageStyle;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class SimpleMessage extends Composite {
    interface Binder extends UiBinder<Widget, SimpleMessage> {
    }

    private static final String TRANSITION_END = "transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd";

    @UiField
    Element icon;
    @UiField
    HTMLPanel main;
    @UiField
    SpanElement remove;
    @UiField
    ReplacePanel content;

    @Inject
    SimpleMessage(
            Binder uiBinder,
            MessagesContainer messagesContainer,
            @Assisted MessageStyle messageStyle,
            @Assisted String message) {
        this(uiBinder, messageStyle, new Label(message), messagesContainer);
    }

    SimpleMessage(
            Binder uiBinder,
            MessageStyle messageStyle,
            IsWidget widget,
            final MessagesContainer messagesContainer) {
        initWidget(uiBinder.createAndBindUi(this));

        main.addStyleName(messageStyle.getStyle());
        GQuery.$(icon).addClass(messageStyle.getIcon());
        content.setWidget(widget);

        GQuery.$(remove).click(new Function() {
            @Override
            public void f() {
                GQuery.$(asWidget())
                        .on(TRANSITION_END, new Function() {
                            @Override
                            public void f() {
                                messagesContainer.onRemove(SimpleMessage.this);
                            }
                        })
                        .css("marginLeft", "-100%");
            }
        });
    }
}
