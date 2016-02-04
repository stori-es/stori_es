package org.consumersunion.stories.dashboard.client.application.profile.ui;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.ui.form.Form;
import org.consumersunion.stories.common.client.ui.form.controls.ListInput;
import org.consumersunion.stories.common.client.ui.form.controls.TextInput;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.Contact.MediumType;
import org.consumersunion.stories.dashboard.client.application.ui.EditorView;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class SocialEditor extends FocusPanel implements EditorView<Contact> {
    interface Binder extends UiBinder<Widget, SocialEditor> {
    }

    private static final Integer DISPLAY = 0;
    private static final Integer EDIT = 1;
    private static final String[] VALUES = Contact.MediumType.getSocialValue();
    private static final String[] LABELS = Contact.MediumType.getSocialLabel();

    @UiField
    DeckPanel editorPanel;
    @UiField
    HTMLPanel editPanel;
    @UiField
    Label label;
    @UiField
    HTML value;
    @UiField
    Label done;

    private final Contact socialContact;
    private final MessageDispatcher messageDispatcher;
    private final CommonI18nErrorMessages messages;

    private ContactEditorHandler handler;
    private Form form;

    @Inject
    SocialEditor(
            Binder uiBinder,
            MessageDispatcher messageDispatcher,
            CommonI18nErrorMessages messages,
            @Assisted Contact socialContactInfo) {
        this.messageDispatcher = messageDispatcher;
        this.messages = messages;
        this.socialContact = socialContactInfo;

        setWidget(uiBinder.createAndBindUi(this));
        init();
    }

    public void setHandler(ContactEditorHandler handler) {
        this.handler = handler;
    }

    @Override
    public void edit() {
        handler.onEdit(this);
        editPanel.clear();

        form = new Form();
        form.startHorizontalSet();

        ListInput typeList = new ListInput("", "type", VALUES, LABELS, true, false, false);
        if (!Strings.isNullOrEmpty(socialContact.getMedium())) {
            List<String> selected = new ArrayList<String>();
            selected.add(socialContact.getMedium());
            typeList.setValues(selected);
        }
        form.add(typeList);

        final TextInput textBox = form.add(
                new TextInput("", "value", true, null, null, "cu-person-profile-edit-value"));
        textBox.setFocus(true);

        form.setValue("value", socialContact.getValue());
        form.endHorizontalSet();

        editPanel.add(form);
        editorPanel.showWidget(EDIT);

        done.sinkEvents(Event.ONCLICK);
    }

    @Override
    public Contact get() {
        return socialContact;
    }

    @Override
    public void init() {
        if (!Strings.isNullOrEmpty(socialContact.getMedium())) {
            MediumType mediumType = MediumType.valueOf(socialContact.getMedium());
            String socialURL = mediumType.getUrl().replace("##", socialContact.getValue());

            StringBuilder builder = new StringBuilder();
            builder.append("<a href=\"");
            builder.append(socialURL);
            builder.append("\" target=\"_blank\">");
            builder.append(socialURL);
            builder.append("</a>");

            label.setText(mediumType.asString());
            value.setHTML(SafeHtmlUtils.fromTrustedString(builder.toString()));
        }

        editorPanel.showWidget(DISPLAY);
    }

    @UiHandler("edit")
    void onEditClicked(ClickEvent event) {
        edit();
    }

    @UiHandler("delete")
    void onDeleteClicked(ClickEvent event) {
        handler.removeSocial(socialContact);
    }

    @UiHandler("done")
    void onDoneClicked(ClickEvent event) {
        done.unsinkEvents(Event.ONCLICK);
        if (!Strings.isNullOrEmpty(form.getValue("value").trim()) && form.validate()) {
            if (form.validate()) {
                socialContact.setType(Contact.SOCIAL);
                socialContact.setMedium(form.getValue("type"));
                socialContact.setValue(form.getValue("value"));
                handler.onSaveSocial();
            }
        } else {
            done.sinkEvents(Event.ONCLICK);
            messageDispatcher.displayMessage(MessageStyle.ERROR, messages.contactInfoError());
        }
    }
}
