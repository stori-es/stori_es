package org.consumersunion.stories.server.api.gwt_rpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.service.RpcEntityService;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.AddressResponse;
import org.consumersunion.stories.common.client.service.response.ContactResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.business_logic.ContactService;
import org.consumersunion.stories.server.business_logic.NotificationService;
import org.consumersunion.stories.server.business_logic.SystemEntityService;
import org.consumersunion.stories.server.business_logic.TagsService;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.index.profile.UpdatePersonAddressIndexer;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.springframework.stereotype.Service;

import net.lightoze.gwt.i18n.server.LocaleFactory;
import net.lightoze.gwt.i18n.server.LocaleProxy;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;

@Service("entityService")
public class RpcEntityServiceImpl extends RpcBaseServiceImpl implements RpcEntityService {
    private static long serialVersionUID = -6789172092618591593L;

    static {
        if ("true".equals(System.getProperty("org.consumersunion.testMode"))) {
            LocaleProxy.initialize();
        }
    }

    @Inject
    private SystemEntityService systemEntityService;
    @Inject
    private ContactService contactService;
    @Inject
    private NotificationService notificationService;
    @Inject
    private TagsService tagsService;
    @Inject
    private UpdatePersonAddressIndexer updatePersonAddressIndexer;

    @Override
    public ActionResponse addTags(Set<Integer> entityIds, Set<String> tags) {
        ActionResponse response = new ActionResponse();
        try {
            User loggedInUser = userService.getLoggedInUser(true);

            if (entityIds != null && !entityIds.isEmpty() && loggedInUser != null) {
                List<SystemEntity> entities = systemEntityService.getSystemEntities(entityIds);

                for (SystemEntity entity : entities) {
                    tagsService.addTags(entity, tags);
                }
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public ActionResponse startWatching(int entityId, NotificationTrigger trigger) {
        ActionResponse actionResponse = new ActionResponse();
        try {
            notificationService.subscribe(entityId, trigger);
        } catch (Exception e) {
            actionResponse.addGlobalErrorMessage(e.getMessage());
        }

        return actionResponse;
    }

    @Override
    public ActionResponse stopWatching(int entityId, NotificationTrigger trigger) {
        ActionResponse actionResponse = new ActionResponse();
        try {
            notificationService.unsubscribe(entityId, trigger);
        } catch (Exception e) {
            actionResponse.addGlobalErrorMessage(e.getMessage());
        }

        return actionResponse;
    }

    @Override
    public ActionResponse setTags(SystemEntity entity, Set<String> tags) {
        ActionResponse response = new ActionResponse();
        try {
            try {
                tagsService.setTags(entity, tags);
            } catch (Exception e) {
                response.addGlobalErrorMessage(e.getLocalizedMessage());
                throw new GeneralException(e);
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public ActionResponse updateAutoTags(SystemEntity entity, Set<String> tags) {
        ActionResponse response = new ActionResponse();
        try {
            if (entity != null) {
                userService.getLoggedInUser(true);

                try {
                    tagsService.setAutoTags(entity, tags);
                } catch (Exception e) {
                    response.addGlobalErrorMessage(e.getLocalizedMessage());
                    throw new GeneralException(e);
                }
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public DatumResponse<Set<String>> getTags() {
        DatumResponse<Set<String>> response = new DatumResponse<Set<String>>();

        try {
            Set<String> tags = tagsService.getAllTags();
            response.setDatum(tags);
        } catch (Exception e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
            throw new GeneralException(e);
        }

        return response;
    }

    @Override
    public DatumResponse<Set<String>> getTags(SystemEntity entity) {
        DatumResponse<Set<String>> response = new DatumResponse<Set<String>>();

        try {
            try {
                Set<String> tags = tagsService.getTags(entity);
                response.setDatum(tags);
            } catch (Exception e) {
                response.addGlobalErrorMessage(e.getLocalizedMessage());
                throw new GeneralException(e);
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public DatumResponse<Set<String>> getAutoTags(SystemEntity entity) {
        DatumResponse<Set<String>> response = new DatumResponse<Set<String>>();

        try {
            Set<String> tags = tagsService.getAutoTags(entity);
            response.setDatum(tags);
        } catch (Exception e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
            throw new GeneralException(e);
        }

        return response;
    }

    @Override
    public AddressResponse getAddress(int entityId) {
        AddressResponse response = new AddressResponse();

        try {
            List<Address> addresses = contactService.getAddresses(entityId);
            response.setData(addresses);
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        } catch (NotAuthorizedException ignored) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }

        return response;
    }

    @Override
    public AddressResponse updateAddress(int idx, List<Address> addresses, int entityId) {
        AddressResponse response = updateAddress(addresses, entityId);
        updatePersonAddressIndexer.index(idx, addresses.get(idx));
        return response;
    }

    @Override
    public AddressResponse updateAddress(List<Address> addresses, int entityId) {
        AddressResponse response = new AddressResponse();

        try {
            List<Address> savedAddresses = contactService.saveAddresses(entityId, addresses);
            response.setData(savedAddresses);
        } catch (NotAuthorizedException ignored) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public ContactResponse saveContacts(int entityId, List<Contact> contacts) {
        ContactResponse response = new ContactResponse();
        try {
            List<Contact> savedContacts = contactService.saveContacts(entityId, contacts);

            response.setContacts(Contact.listToMediumMap(savedContacts));
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public ContactResponse saveContact(int entityId, String originalValue, Contact contact) {
        ContactResponse response = new ContactResponse();
        try {
            contactService.saveContact(entityId, originalValue, contact);

            return retrieveContacts(entityId);
        } catch (NotAuthorizedException ignored) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public ContactResponse saveSocialContacts(int entityId, List<Contact> contacts) {
        ContactResponse response = new ContactResponse();

        try {
            SystemEntity targetObj = systemEntityService.getSystemEntity(entityId);

            if (authService.isUserAuthorized(ROLE_CURATOR, targetObj)) {
                persistenceService.process(new ContactPersister.SaveSocialContactFunc(contacts, entityId));
                Map<String, List<Contact>> map = new HashMap<String, List<Contact>>();
                map.put(Contact.SOCIAL, contacts);
                response.setContacts(map);
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public ContactResponse retrieveContacts(int entityId) {
        ContactResponse response = new ContactResponse();

        try {
            List<Contact> contacts = contactService.getAllContacts(entityId);
            response.setContacts(Contact.listToMediumMap(contacts));
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        } catch (NotAuthorizedException ignored) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }

        return response;
    }

    @Override
    public ContactResponse retrieveSocialContacts(int entityId) {
        ContactResponse response = new ContactResponse();

        try {
            List<Contact> contacts = contactService.getSocialContacts(entityId);
            response.setContacts(Contact.listToMediumMap(contacts));
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        } catch (NotAuthorizedException ignored) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }

        return response;
    }
}
