package org.consumersunion.stories.dashboard.client.application.profile.ui;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.ui.form.Form;
import org.consumersunion.stories.common.client.ui.form.controls.ListInput;
import org.consumersunion.stories.common.client.ui.form.controls.TextInput;
import org.consumersunion.stories.common.client.ui.form.validators.EmailValidator;
import org.consumersunion.stories.common.client.ui.form.validators.PhoneNumberValidator;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.dashboard.client.application.ui.EditorView;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ContactEditor extends FocusPanel implements EditorView<Contact> {
    interface Binder extends UiBinder<Widget, ContactEditor> {
    }

    private static final Integer DISPLAY = 0;
    private static final Integer EDIT = 1;
    private static final String[] CONTACT_TYPE = new String[]{
            Contact.TYPE_HOME,
            Contact.TYPE_MOBILE,
            Contact.TYPE_OTHER,
            Contact.TYPE_WORK
    };

    @UiField
    DeckPanel editorPanel;
    @UiField
    HTMLPanel editPanel;
    @UiField
    Label label;
    @UiField
    Label value;
    @UiField
    Label done;
    @UiField
    DivElement status;
    @UiField
    FlowPanel contactMain;
    @UiField
    Resources resource;

    private final Contact contact;
    private final MessageDispatcher messageDispatcher;
    private final CommonI18nErrorMessages messages;

    private ContactEditorHandler handler;
    private Form form;

    @Inject
    public ContactEditor(Binder uiBinder,
            MessageDispatcher messageDispatcher,
            CommonI18nErrorMessages messages,
            StoryTellerDashboardI18nLabels labels,
            @Assisted Contact contactInfo) {
        this.messageDispatcher = messageDispatcher;
        this.messages = messages;
        this.contact = contactInfo;

        setWidget(uiBinder.createAndBindUi(this));
        init();
        GQuery.$(status).parent().attr("data-tooltip", labels.failedToDeliver());
    }

    public void setHandler(ContactEditorHandler handler) {
        this.handler = handler;
    }

    @Override
    public void init() {
        if (contact.getStatus().isError()) {
            GQuery.$(contactMain).addClass(resource.generalStyleCss().contactError());
        } else if (contact.getStatus().isWarning()) {
            GQuery.$(contactMain).addClass(resource.generalStyleCss().contactWarning());
        } else {
            GQuery.$(status).hide();
        }

        label.setText(contact.getType());
        value.setText(contact.getValue());
        editorPanel.showWidget(DISPLAY);
    }

    @Override
    public void edit() {
        handler.onEdit(this);
        editPanel.clear();

        form = new Form();
        form.startHorizontalSet();

        ListInput typeList = new ListInput("", "type", CONTACT_TYPE, CONTACT_TYPE, true, false, false);
        if (!Strings.isNullOrEmpty(contact.getType())) {
            List<String> selected = new ArrayList<String>();
            selected.add(contact.getType());
            typeList.setValues(selected);
        } else {
            contact.setType(Contact.TYPE_HOME);
        }

        form.add(typeList);

        final TextInput textBox = form.add(
                new TextInput("", "value", true, null, null, "cu-person-profile-edit-value"));
        textBox.setFocus(true);

        if (Contact.MediumType.EMAIL.name().equals(contact.getMedium())) {
            form.setValidator("value", new EmailValidator());
        }

        if (Contact.MediumType.PHONE.name().equals(contact.getMedium())) {
            form.setValidator("value", new PhoneNumberValidator());
        }

        form.setValue("value", contact.getValue());
        form.endHorizontalSet();

        editPanel.add(form);
        editorPanel.showWidget(EDIT);

        done.sinkEvents(Event.ONCLICK);
    }

    @Override
    public Contact get() {
        return contact;
    }

    @UiHandler("edit")
    void onEditClicked(ClickEvent event) {
        edit();
    }

    @UiHandler("delete")
    void onDeleteClicked(ClickEvent event) {
        handler.removeContact(contact);
    }

    @UiHandler("done")
    void onDoneClicked(ClickEvent event) {
        done.unsinkEvents(Event.ONCLICK);
        if (!Strings.isNullOrEmpty(form.getValue("value").trim()) && form.validate()) {
            String oldValue = contact.getValue();

            contact.setType(form.getValue("type"));
            contact.setValue(form.getValue("value"));
            handler.onSaveContact(oldValue, contact);
        } else {
            done.sinkEvents(Event.ONCLICK);
            messageDispatcher.displayMessage(MessageStyle.ERROR, messages.contactInfoError());
        }
    }
}
