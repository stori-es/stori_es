package org.consumersunion.stories.dashboard.client.application.profile.widget;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.service.RpcEntityServiceAsync;
import org.consumersunion.stories.common.client.service.response.AddressResponse;
import org.consumersunion.stories.common.client.service.response.ContactResponse;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.Contact.MediumType;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ContactsPresenter extends PresenterWidget<ContactsPresenter.MyView> implements ContactsUiHandlers {
    interface MyView extends View, HasUiHandlers<ContactsUiHandlers> {
        void displayAddress(List<Address> addresses);

        void displayContacts(List<Contact> emails, List<Contact> phones);

        void displaySocialContacts(List<Contact> socials);
    }

    private final RpcEntityServiceAsync entityService;

    private int pivotId;
    private boolean addressesLoaded;
    private boolean contactsLoaded;
    private boolean socialContactsLoaded;

    @Inject
    ContactsPresenter(
            EventBus eventBus,
            MyView view,
            RpcEntityServiceAsync entityService) {
        super(eventBus, view);

        this.entityService = entityService;

        getView().setUiHandlers(this);
    }

    public void initPresenter(int pivotId) {
        this.pivotId = pivotId;

        addressesLoaded = false;
        contactsLoaded = false;
        socialContactsLoaded = false;

        loadUserAddress();
        loadUserContacts();
        loadSocialContacts();
    }

    @Override
    public void saveContact(String originalValue, Contact contact) {
        entityService.saveContact(pivotId, originalValue, contact,
                new ResponseHandlerLoader<ContactResponse>() {
                    @Override
                    public void handleSuccess(ContactResponse result) {
                        onContactsReceived(result, messageDispatcher);
                    }
                });
    }

    @Override
    public void saveContacts(List<Contact> contacts) {
        entityService.saveContacts(pivotId, contacts,
                new ResponseHandlerLoader<ContactResponse>() {
                    @Override
                    public void handleSuccess(ContactResponse result) {
                        onContactsReceived(result, messageDispatcher);
                    }
                });
    }

    @Override
    public void saveSocials(List<Contact> contacts) {
        entityService.saveSocialContacts(pivotId, contacts, new ResponseHandlerLoader<ContactResponse>() {
            @Override
            public void handleSuccess(ContactResponse result) {
                List<Contact> socials = result.getContacts(Contact.SOCIAL);
                getView().displaySocialContacts(socials);
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Social contacts was successfully saved.");
            }
        });
    }

    @Override
    public void saveAddresses(int idx, List<Address> addresses) {
        for (Address address : addresses) {
            address.setEntity(pivotId);
        }

        entityService.updateAddress(idx, addresses, pivotId, new ResponseHandlerLoader<AddressResponse>() {
            @Override
            public void handleSuccess(AddressResponse result) {
                getView().displayAddress(result.getData());
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Address Information was successfully saved.");
            }
        });
    }

    @Override
    public void saveAddresses(List<Address> addresses) {
        entityService.updateAddress(addresses, pivotId, new ResponseHandlerLoader<AddressResponse>() {
            @Override
            public void handleSuccess(AddressResponse result) {
                getView().displayAddress(result.getData());
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Address successfully removed.");
            }
        });
    }

    private void onContactsReceived(ContactResponse result, MessageDispatcher messageDispatcher) {
        List<Contact> emails = result.getContacts(MediumType.EMAIL.name());
        List<Contact> phones = result.getContacts(MediumType.PHONE.name());
        getView().displayContacts(emails, phones);
        loadUserAddress();
        messageDispatcher.displayMessage(MessageStyle.SUCCESS,
                "Contact information was successfully saved.");
    }

    private void loadUserAddress() {
        entityService.getAddress(pivotId, new ResponseHandler<AddressResponse>() {
            @Override
            public void handleSuccess(AddressResponse result) {
                List<Address> address = result.getData() == null ? new ArrayList<Address>() : result.getData();
                getView().displayAddress(address);
                addressesLoaded = true;
                checkAllLoaded();
            }
        });
    }

    private void checkAllLoaded() {
        if (addressesLoaded && contactsLoaded && socialContactsLoaded) {
            HideLoadingEvent.fire(this);
        }
    }

    private void loadUserContacts() {
        entityService.retrieveContacts(pivotId, new ResponseHandler<ContactResponse>() {
            @Override
            public void handleSuccess(ContactResponse result) {
                if (result != null) {
                    List<Contact> emails = new ArrayList<Contact>();
                    if (result.getContacts(MediumType.EMAIL.name()) != null) {
                        emails = result.getContacts(MediumType.EMAIL.name());
                    }

                    List<Contact> phones = new ArrayList<Contact>();
                    if (result.getContacts(MediumType.PHONE.name()) != null) {
                        phones = result.getContacts(MediumType.PHONE.name());
                    }

                    getView().displayContacts(emails, phones);

                    contactsLoaded = true;
                    checkAllLoaded();
                }
            }
        });
    }

    private void loadSocialContacts() {
        entityService.retrieveSocialContacts(pivotId, new ResponseHandler<ContactResponse>() {
            @Override
            public void handleSuccess(ContactResponse result) {
                if (result != null) {
                    List<Contact> socials = new ArrayList<Contact>();
                    if (result.getContacts(Contact.SOCIAL) != null) {
                        socials = result.getContacts(Contact.SOCIAL);
                    }

                    getView().displaySocialContacts(socials);

                    socialContactsLoaded = true;
                    checkAllLoaded();
                }
            }
        });
    }
}
