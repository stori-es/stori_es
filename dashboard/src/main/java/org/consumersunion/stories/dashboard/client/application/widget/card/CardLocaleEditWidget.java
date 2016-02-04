package org.consumersunion.stories.dashboard.client.application.widget.card;

import org.consumersunion.stories.common.client.i18n.LocaleLabels;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.collect.Lists;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class CardLocaleEditWidget extends Composite implements LeafValueEditor<Locale> {
    interface Binder extends UiBinder<Widget, CardLocaleEditWidget> {
    }

    @UiField(provided = true)
    final ValueListBox<Locale> localeListBox;

    @UiField
    Resources resource;
    @UiField
    DivElement localeError;

    @Inject
    CardLocaleEditWidget(
            Binder uiBinder,
            final LocaleLabels localeLabels) {
        localeListBox = new ValueListBox<Locale>(new AbstractRenderer<Locale>() {
            @Override
            public String render(Locale locale) {
                return locale == null ? "" : localeLabels.getString(locale.name());
            }
        });
        localeListBox.setValue(Locale.UNKNOWN);
        localeListBox.setAcceptableValues(Lists.newArrayList(Locale.values()));

        initWidget(uiBinder.createAndBindUi(this));

        clearErrors();
    }

    public boolean validate() {
        clearErrors();

        boolean valid = true;
        Widget elementToFocus = null;

        if (Locale.UNKNOWN.equals(localeListBox.getValue())) {
            valid = false;
            $(localeError).show();
            localeError.focus();
            elementToFocus = localeListBox;
        }

        if (elementToFocus != null) {
            elementToFocus.getElement().focus();
        }

        return valid;
    }

    @Override
    public Locale getValue() {
        return localeListBox.getValue();
    }

    @Override
    public void setValue(Locale value) {
        localeListBox.setValue(value);
    }

    public void clearErrors() {
        $(localeError).hide();
    }
}
