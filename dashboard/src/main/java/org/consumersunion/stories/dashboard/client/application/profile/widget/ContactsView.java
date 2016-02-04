package org.consumersunion.stories.dashboard.client.application.profile.widget;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.dashboard.client.application.profile.ui.AddressEditor;
import org.consumersunion.stories.dashboard.client.application.profile.ui.ContactEditor;
import org.consumersunion.stories.dashboard.client.application.profile.ui.ContactEditorFactory;
import org.consumersunion.stories.dashboard.client.application.profile.ui.ContactEditorHandler;
import org.consumersunion.stories.dashboard.client.application.profile.ui.SocialEditor;
import org.consumersunion.stories.dashboard.client.application.ui.ConfirmationModal;
import org.consumersunion.stories.dashboard.client.application.ui.EditorView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ContactsView extends ViewWithUiHandlers<ContactsUiHandlers>
        implements ContactsPresenter.MyView, ContactEditorHandler {
    interface Binder extends UiBinder<Widget, ContactsView> {
    }

    @UiField
    HTMLPanel emailsPanel;
    @UiField
    HTMLPanel phoneNumbersPanel;
    @UiField
    HTMLPanel addressesPanel;
    @UiField
    HTMLPanel socialPanel;

    private final CommonI18nMessages messages;
    private final ContactEditorFactory contactEditorFactory;

    private List<Contact> contactEmailList;
    private List<Contact> contactPhoneList;
    private List<Contact> socialContactlist;
    private List<Address> addressList;

    private EditorView<?> currentEditor;

    @Inject
    ContactsView(
            Binder uiBinder,
            CommonI18nMessages messages,
            ContactEditorFactory contactEditorFactory) {
        this.messages = messages;
        this.contactEditorFactory = contactEditorFactory;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void displayAddress(List<Address> addresses) {
        addressesPanel.clear();
        addressList = addresses;
        currentEditor = null;

        if (addresses != null) {
            for (Address address : addresses) {
                AddressEditor editor = contactEditorFactory.createAddress(address);
                editor.setHandler(this);
                addressesPanel.add(editor);
            }
        }
    }

    @Override
    public void displayContacts(List<Contact> emails, List<Contact> phones) {
        emailsPanel.clear();
        contactEmailList = emails;
        currentEditor = null;

        if (emails != null) {
            for (Contact email : emails) {
                ContactEditor editor = contactEditorFactory.createContact(email);
                editor.setHandler(this);
                emailsPanel.add(editor);
            }
        }

        phoneNumbersPanel.clear();
        contactPhoneList = phones;

        if (phones != null) {
            for (Contact phone : phones) {
                ContactEditor editor = contactEditorFactory.createContact(phone);
                editor.setHandler(this);
                phoneNumbersPanel.add(editor);
            }
        }
    }

    @Override
    public void displaySocialContacts(List<Contact> socials) {
        socialPanel.clear();
        socialContactlist = socials;
        currentEditor = null;

        if (socials != null) {
            for (Contact contact : socials) {
                SocialEditor editor = contactEditorFactory.createSocialContact(contact);
                editor.setHandler(this);
                socialPanel.add(editor);
            }
        }
    }

    @Override
    public void onSaveContact(String originalContact, Contact contact) {
        if (originalContact == null) {
            getUiHandlers().saveContacts(getJoinedContacts());
        } else {
            getUiHandlers().saveContact(originalContact, contact);
        }
    }

    @Override
    public void onSaveSocial() {
        getUiHandlers().saveSocials(socialContactlist);
    }

    @Override
    public void onSaveAddress(Address address) {
        Integer index = addressList.indexOf(address);
        getUiHandlers().saveAddresses(index, addressList);
    }

    @Override
    public void onCancelAddress(Address address) {
        addressList.remove(address);
        displayAddress(addressList);
    }

    @Override
    public void removeContact(final Contact contact) {
        String conf = contactEmailList.contains(contact) ? messages.deleteEmailConf(contact.getValue())
                : messages.deletePhoneConf(contact.getValue());
        ConfirmationModal confirmationModal = new ConfirmationModal("Confirm Deletion", conf) {
            @Override
            protected void handleConfirm() {
                List<Contact> contacts = getJoinedContacts();
                contacts.remove(contact);
                getUiHandlers().saveContacts(contacts);
            }
        };
        confirmationModal.center();
    }

    @Override
    public void removeSocial(final Contact contact) {
        ConfirmationModal confirmationModal = new ConfirmationModal("Confirm Deletion",
                messages.deleteSocialAccount()) {
            @Override
            protected void handleConfirm() {
                socialContactlist.remove(contact);
                getUiHandlers().saveSocials(socialContactlist);
            }
        };
        confirmationModal.center();
    }

    @Override
    public void removeAddress(final Address address) {
        final ConfirmationModal confirmationModal = new ConfirmationModal("Confirm Deletion",
                messages.deleteAddressConf(address.getAddress1())) {
            @Override
            protected void handleConfirm() {
                addressList.remove(address);
                getUiHandlers().saveAddresses(addressList);
            }
        };
        confirmationModal.center();
    }

    @Override
    public void onEdit(EditorView<?> editor) {
        if (currentEditor != null) {
            if (currentEditor instanceof ContactEditor) {
                Contact contact = (Contact) currentEditor.get();
                remove(contactEmailList, contact);
                remove(contactPhoneList, contact);
            } else if (currentEditor instanceof AddressEditor) {
                Address address = (Address) currentEditor.get();
                remove(addressList, address);
            } else if (currentEditor instanceof SocialEditor) {
                Contact contact = (Contact) currentEditor.get();
                remove(socialContactlist, contact);
            }
            ((Widget) currentEditor).removeFromParent();
        }

        currentEditor = editor;
        currentEditor.init();
    }

    @UiHandler("addPhone")
    void onAddPhone(ClickEvent event) {
        if (contactPhoneList == null) {
            contactPhoneList = new ArrayList<Contact>();
        }

        Contact newPhone = new Contact();
        newPhone.setMedium(Contact.MediumType.PHONE.name());
        ContactEditor editor = contactEditorFactory.createContact(newPhone);
        editor.setHandler(this);

        contactPhoneList.add(newPhone);
        phoneNumbersPanel.add(editor);

        editor.edit();
    }

    @UiHandler("addEmail")
    void onAddEmail(ClickEvent event) {
        if (contactEmailList == null) {
            contactEmailList = new ArrayList<Contact>();
        }

        Contact newMail = new Contact();
        newMail.setMedium(Contact.MediumType.EMAIL.name());
        ContactEditor editor = contactEditorFactory.createContact(newMail);
        editor.setHandler(this);

        contactEmailList.add(newMail);
        emailsPanel.add(editor);

        editor.edit();
    }

    @UiHandler("addAddress")
    void onAddAddress(ClickEvent event) {
        if (addressList == null) {
            addressList = new ArrayList<Address>();
        }

        Address newAddress = new Address();
        AddressEditor editor = contactEditorFactory.createAddress(newAddress);
        editor.setHandler(this);

        addressList.add(newAddress);
        addressesPanel.add(editor);

        editor.edit();
    }

    @UiHandler("addSocial")
    void onAddSocialContact(ClickEvent event) {
        if (socialContactlist == null) {
            socialContactlist = new ArrayList<Contact>();
        }

        Contact newSocial = new Contact();
        SocialEditor editor = contactEditorFactory.createSocialContact(newSocial);
        editor.setHandler(this);

        socialContactlist.add(newSocial);
        socialPanel.add(editor);

        editor.edit();
    }

    private List<Contact> getJoinedContacts() {
        List<Contact> joinedList = new ArrayList<Contact>();
        if (contactEmailList != null) {
            joinedList.addAll(contactEmailList);
        }

        if (contactPhoneList != null) {
            joinedList.addAll(contactPhoneList);
        }
        return joinedList;
    }

    private void remove(List<?> list, Object elementToRemove) {
        if (list != null) {
            list.remove(elementToRemove);
        }
    }
}
