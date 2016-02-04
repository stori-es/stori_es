package org.consumersunion.stories.common.client.ui;

import java.text.ParseException;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.util.WidgetIds;
import org.consumersunion.stories.common.client.widget.MessageStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class EnhancedPager extends Composite {
    interface Binder extends UiBinder<Widget, EnhancedPager> {
    }

    public interface Handler {
        void onGoToPage(Integer pageNumber);
    }

    @UiField(provided = true)
    final TextPager pager;

    @UiField
    IntegerBox pageNumber;
    @UiField
    Button goButton;

    private final MessageDispatcher messageDispatcher;
    private final CommonI18nErrorMessages messages;

    private Handler handler;

    @Inject
    public EnhancedPager(final Binder uiBinder,
            final MessageDispatcher messageDispatcher,
            final CommonI18nErrorMessages messages,
            final TextPager pager) {
        this.messageDispatcher = messageDispatcher;
        this.messages = messages;
        this.pager = pager;

        initWidget(uiBinder.createAndBindUi(this));

        setIds();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public TextPager getPager() {
        return pager;
    }

    public void resetPager() {
        pageNumber.setText("");
    }

    @UiHandler("goButton")
    void onGoToClicked(ClickEvent event) {
        processGoToPage();
    }

    @UiHandler("pageNumber")
    void onPageNumberKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            processGoToPage();
        }
    }

    private void processGoToPage() {
        try {
            Integer page = pageNumber.getValueOrThrow();
            if (page > 0 && page <= pager.getPageCount() + 1) {
                handler.onGoToPage(page);
                pageNumber.setText("");
            } else {
                messageDispatcher.displayMessage(MessageStyle.ERROR, messages.pageOutBounds());
            }
        } catch (ParseException e) {
        }
    }

    private void setIds() {
        pageNumber.getElement().setId(WidgetIds.GO_TO_PAGE_TEXTBOX);
        goButton.getElement().setId(WidgetIds.GO_TO_PAGE_BUTTON);
    }
}
