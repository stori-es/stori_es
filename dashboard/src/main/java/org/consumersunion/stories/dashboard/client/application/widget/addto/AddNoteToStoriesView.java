package org.consumersunion.stories.dashboard.client.application.widget.addto;

import org.consumersunion.stories.common.client.util.WidgetIds;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.StorySelectField;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown;

import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

class AddNoteToStoriesView extends AbstractAddToView<AddToUiHandlers> implements AddNoteToStoriesPresenter.MyView {
    interface Binder extends UiBinder<Widget, AddNoteToStoriesView> {
    }

    @UiField(provided = true)
    final ClickableDropDown<StorySelectField> selectDropDown;

    @UiField
    HTMLPanel assignPanel;
    @UiField
    Button goAssign;
    @UiField
    TextArea noteText;

    private Function keyUpHandler;

    @Inject
    AddNoteToStoriesView(
            Binder uiBinder,
            CommonI18nLabels labels,
            ClickableDropDown<StorySelectField> selectDropDown) {
        super(selectDropDown);

        this.selectDropDown = selectDropDown;

        initWidget(uiBinder.createAndBindUi(this));

        noteText.getElement().setAttribute("placeholder", labels.addCommentsPlaceHolder());

        createKeyUpHandler();
        noteText.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                onNoteTextAttachedOrDetached(event);
            }
        });

        assignPanel.getElement().setId(WidgetIds.ADD_NOTE_TO_STORIES);
        selectDropDown.getElement().setId(WidgetIds.ADD_NOTE_TO_STORIES_DROPDOWN);
        goAssign.getElement().setId(WidgetIds.ADD_NOTE_TO_STORIES_GO_BUTTON);
    }

    @Override
    public String getNote() {
        return noteText.getText();
    }

    @Override
    public void onDisplay() {
        updateHeight();
    }

    @Override
    public void reset() {
        super.reset();

        noteText.setText("");
        goAssign.setEnabled(false);
    }

    private void onNoteTextAttachedOrDetached(AttachEvent event) {
        if (event.isAttached()) {
            $(noteText).keyup(keyUpHandler);
            updateHeight();
        } else {
            $(noteText).unbind(BrowserEvents.KEYUP, keyUpHandler);
        }
    }

    private void updateHeight() {
        $(noteText).delay(1, new Function() {
            @Override
            public void f() {
                $(noteText).height("auto");
                $(noteText).height($(noteText).get(0).getScrollHeight());
            }
        });
    }

    private void createKeyUpHandler() {
        keyUpHandler = new Function() {
            @Override
            public void f() {
                updateHeight();
                goAssign.setEnabled(!noteText.getText().isEmpty());
            }
        };
    }
}
