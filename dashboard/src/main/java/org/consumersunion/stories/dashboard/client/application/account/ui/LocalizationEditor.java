package org.consumersunion.stories.dashboard.client.application.account.ui;

import java.util.List;

import org.consumersunion.stories.common.client.widget.CheckboxCell;
import org.consumersunion.stories.common.shared.model.type.LanguageType;
import org.consumersunion.stories.dashboard.client.resource.CheckboxListStyle;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

public class LocalizationEditor extends Composite {
    interface Binder extends UiBinder<Widget, LocalizationEditor> {
    }

    @UiField(provided = true)
    final CellList<LanguageType> languagesList;

    private final ListDataProvider<LanguageType> dataProvider;

    @Inject
    LocalizationEditor(
            Binder uiBinder,
            CheckboxCell<LanguageType> checkboxCell,
            CheckboxListStyle checkboxListStyle,
            ListDataProvider<LanguageType> dataProvider) {
        this.dataProvider = dataProvider;
        this.languagesList = new CellList<LanguageType>(checkboxCell, checkboxListStyle);

        initWidget(uiBinder.createAndBindUi(this));
        dataProvider.addDataDisplay(languagesList);
    }

    public void setLanguages(List<LanguageType> languages) {
        dataProvider.setList(languages);
        languagesList.setRowCount(languages.size());
        dataProvider.refresh();
    }
}
