package org.consumersunion.stories.dashboard.client.application.profile.ui;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.ui.form.Form;
import org.consumersunion.stories.common.client.ui.form.controls.ListInput;
import org.consumersunion.stories.common.client.ui.form.controls.TextInput;
import org.consumersunion.stories.common.client.ui.form.validators.ZipCodeValidator;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.util.StatesUtil;
import org.consumersunion.stories.dashboard.client.application.ui.EditorView;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class AddressEditor extends FocusPanel implements EditorView<Address> {
    interface Binder extends UiBinder<Widget, AddressEditor> {
    }

    private static final Integer DISPLAY = 0;
    private static final Integer EDIT = 1;
    private static final String[] CONTACT_TYPE = new String[]{
            Contact.TYPE_HOME,
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

    private final Address contactAddress;
    private final MessageDispatcher messageDispatcher;
    private final CommonI18nErrorMessages messages;

    private ContactEditorHandler handler;
    private Form form;

    @Inject
    AddressEditor(
            Binder uiBinder,
            MessageDispatcher messageDispatcher,
            CommonI18nErrorMessages messages,
            @Assisted Address address) {
        this.messageDispatcher = messageDispatcher;
        this.messages = messages;
        this.contactAddress = address;

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
        ListInput relationList = new ListInput("", "type", CONTACT_TYPE, CONTACT_TYPE, false, false, false);

        if (!Strings.isNullOrEmpty(contactAddress.getRelation())) {
            final List<String> selected = new ArrayList<String>();
            selected.add(contactAddress.getRelation());
            relationList.setValues(selected);
        }

        form.add(relationList);

        form.add(new TextInput("Street Address 1", "address1", false, null, null, "cu-person-profile-edit-value"));
        form.setValue("address1", contactAddress.getAddress1());

        form.add(new TextInput("Street Address 2", "address2", false, null, null, "cu-person-profile-edit-value"));
        form.setValue("address2", contactAddress.getAddress2());

        form.add(new TextInput("City", "city", false, null, null, "cu-person-profile-edit-value"));
        form.setValue("city", contactAddress.getCity());

        final ListInput stateList = new ListInput("State", "state", StatesUtil.STATE_OPTIONS.split(",\\s*"),
                StatesUtil.STATE_OPTIONS.split(",\\s*"), false, false, false, "cu-person-profile-edit-type");
        if (!Strings.isNullOrEmpty(contactAddress.getState())) {
            final List<String> selected = new ArrayList<String>();
            selected.add(contactAddress.getState());
            stateList.setValues(selected);
        }
        form.add(stateList);

        form.add(new TextInput("Zip Code", "zipCode", false, null, null, "cu-person-profile-edit-value"));
        form.setValidator("zipCode", new ZipCodeValidator());
        form.setValue("zipCode", contactAddress.getPostalCode());

        editPanel.add(form);
        editorPanel.showWidget(EDIT);
    }

    @Override
    public Address get() {
        return contactAddress;
    }

    @Override
    public void init() {
        label.setText(contactAddress.getRelation());
        value.setText(contactAddress.joinAddress());
        editorPanel.showWidget(DISPLAY);
    }

    @UiHandler("edit")
    void onEditClicked(ClickEvent event) {
        edit();
    }

    @UiHandler("delete")
    void onDeleteClicked(ClickEvent event) {
        handler.removeAddress(contactAddress);
    }

    @UiHandler("submit")
    void onSubmitClicked(ClickEvent event) {
        if (form.validate()) {
            if (!formIsEmpty()) {
                contactAddress.setRelation(form.getValue("type"));
                contactAddress.setAddress1(getAddress1());
                contactAddress.setAddress2(getAddress2());
                contactAddress.setCity(getCity());
                contactAddress.setState(form.getValue("state"));
                contactAddress.setPostalCode(getZipCode());
                contactAddress.setCountry("US");

                handler.onSaveAddress(contactAddress);
            } else {
                handler.onCancelAddress(contactAddress);
            }
        } else {
            messageDispatcher.displayMessage(MessageStyle.ERROR, messages.contactInfoError());
        }
    }

    private boolean formIsEmpty() {
        return Strings.isNullOrEmpty(getZipCode()) && Strings.isNullOrEmpty(getCity())
                && Strings.isNullOrEmpty(getAddress1()) && Strings.isNullOrEmpty(getAddress2());
    }

    private String getZipCode() {
        return form.getValue("zipCode");
    }

    private String getCity() {
        return form.getValue("city");
    }

    private String getAddress2() {
        return form.getValue("address2");
    }

    private String getAddress1() {
        return form.getValue("address1");
    }
}
