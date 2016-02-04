package org.consumersunion.stories.dashboard.client.application.profile.widget;

import java.util.List;

import org.consumersunion.stories.common.client.util.WidgetIds;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.application.profile.ui.NoteCell;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class NotesManagerView extends ViewWithUiHandlers<NotesManagerUiHandlers>
        implements NotesManagerPresenter.MyView {
    interface Binder extends UiBinder<Widget, NotesManagerView> {
    }

    @UiField(provided = true)
    final CellList<Document> noteList;

    @UiField
    TextArea noteText;
    @UiField
    Button submit;

    private final ListDataProvider<Document> dataProvider;

    @Inject
    NotesManagerView(
            Binder uiBinder,
            NoteCell noteCell,
            ListDataProvider<Document> dataProvider,
            CommonI18nLabels labels) {
        this.noteList = new CellList<Document>(noteCell);
        noteList.setPageSize(Integer.MAX_VALUE);
        this.dataProvider = dataProvider;

        initWidget(uiBinder.createAndBindUi(this));
        dataProvider.addDataDisplay(noteList);

        noteText.getElement().setAttribute("placeholder", labels.addCommentsPlaceHolder());
        noteText.getElement().setId(WidgetIds.NOTES_TEXTAREA);
        submit.getElement().setId(WidgetIds.NOTES_SUBMIT);
    }

    @Override
    public void setData(List<Document> data, Organization currentObo) {
        dataProvider.getList().clear();
        dataProvider.getList().addAll(data);
        dataProvider.refresh();
        noteText.setText("");
    }

    @Override
    public void clear() {
        noteList.setRowCount(0);
        noteText.setText("");
    }

    @UiHandler("submit")
    void onSubmit(ClickEvent event) {
        if (!Strings.isNullOrEmpty(noteText.getText())) {
            getUiHandlers().saveNote(noteText.getText());
        }
    }
}
