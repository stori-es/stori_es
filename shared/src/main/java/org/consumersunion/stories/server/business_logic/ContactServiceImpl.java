package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Address.GeoCodeStatus;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.helper.geo.GeoCodingService;
import org.consumersunion.stories.server.helper.geo.Localisation;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.consumersunion.stories.server.rest.api.convio.SyncFromSysPersonToConvioConstituentRequestFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

@Service
public class ContactServiceImpl implements ContactService {
    private static final Logger LOGGER = Logger.getLogger(ContactServiceImpl.class.getName());

    private final AuthorizationService authorizationService;
    private final UserService userService;
    private final ProfileService profileService;
    private final SystemEntityService systemEntityService;
    private final SyncFromSysPersonToConvioConstituentRequestFactory syncFromSysConvioFactory;
    private final ContactPersister contactPersister;
    private final GeoCodingService geoCodingService;

    @Inject
    ContactServiceImpl(
            AuthorizationService authorizationService,
            UserService userService,
            ProfileService profileService,
            SystemEntityService systemEntityService,
            SyncFromSysPersonToConvioConstituentRequestFactory syncFromSysConvioFactory,
            ContactPersister contactPersister,
            GeoCodingService geoCodingService) {
        this.authorizationService = authorizationService;
        this.userService = userService;
        this.profileService = profileService;
        this.systemEntityService = systemEntityService;
        this.syncFromSysConvioFactory = syncFromSysConvioFactory;
        this.contactPersister = contactPersister;
        this.geoCodingService = geoCodingService;
    }

    @Override
    public List<Contact> getAllContacts(int entityId) {
        SystemEntity entity = systemEntityService.getSystemEntity(entityId);

        return getAllContacts(entity);
    }

    @Override
    public List<Contact> getAllContacts(SystemEntity systemEntity) {
        if (authorizationService.canRead(systemEntity)) {
            return contactPersister.retrieveContacts(systemEntity.getId());
        } else {
            throw new NotAuthorizedException();
        }
    }

    /**
     * Saves contacts associated to an {@link org.consumersunion.stories.common.shared.model.entity.Entity}.
     * Effective operator must have WRITE privileges oven the target Entity.
     */
    @Override
    public List<Contact> saveContacts(int entityId, List<Contact> contacts) {
        SystemEntity entity = systemEntityService.getSystemEntity(entityId);

        return saveContacts(entity, contacts);
    }

    @Override
    public List<Contact> saveContacts(SystemEntity systemEntity, List<Contact> contacts) {
        if (!authorizationService.canWrite(systemEntity)) {
            throw new NotAuthorizedException();
        }

        int entityId = systemEntity.getId();
        List<Contact> persistedContacts = setEntityId(contacts, entityId);

        persistedContacts = contactPersister.saveContacts(persistedContacts, entityId);

        // TODO: this was executed once for each contact but not using contacts data. Verify if required
        queueConvioUpdates(entityId);

        return persistedContacts;
    }

    @Override
    public void saveContact(int entityId, String originalValue, Contact contact) {
        SystemEntity entity = systemEntityService.getSystemEntity(entityId);
        if (!authorizationService.canWrite(entity)) {
            throw new NotAuthorizedException();
        }

        contactPersister.saveContact(entityId, originalValue, contact);

        // TODO: this was executed once for each contact but not using contacts data. Verify if required
        queueConvioUpdates(entityId);
    }

    @Override
    public List<Contact> getSocialContacts(int entityId) {
        SystemEntity entity = systemEntityService.getSystemEntity(entityId);

        return getSocialContacts(entity);
    }

    @Override
    public List<Contact> getSocialContacts(SystemEntity systemEntity) {
        if (authorizationService.canRead(systemEntity)) {
            return contactPersister.retrieveSocialContacts(systemEntity.getId());
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Override
    public List<Address> getAddresses(int entityId) {
        SystemEntity entity = systemEntityService.getSystemEntity(entityId);

        return getAddresses(entity);
    }

    @Override
    public List<Address> getAddresses(SystemEntity systemEntity) {
        if (authorizationService.canRead(systemEntity)) {
            return contactPersister.retrieveAddress(systemEntity.getId());
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Override
    public List<Address> saveAddresses(int entityId, List<Address> addresses) {
        SystemEntity entity = systemEntityService.getSystemEntity(entityId);

        return saveAddresses(entity, addresses);
    }

    @Override
    public List<Address> saveAddresses(SystemEntity systemEntity, List<Address> addresses) {
        if (!authorizationService.canWrite(systemEntity)) {
            throw new NotAuthorizedException();
        }

        int entityId = systemEntity.getId();
        for (Address address : addresses) {
            address.setEntity(entityId);
            geoLocateAddress(address);
        }

        List<Address> persistedAddresses = contactPersister.saveAddresses(addresses, systemEntity.getId());

        queueConvioUpdates(entityId);

        return persistedAddresses;
    }

    @Override
    public String getPrimaryEmail(User user) {
        String primaryEmail = contactPersister.retrievePrimaryEmail(user.getId());

        if (primaryEmail == null) {
            throw new NotFoundException();
        }

        return primaryEmail;
    }

    @Override
    public List<Contact> retrieveEmails(User user) {
        return contactPersister.retrieveEmails(user);
    }

    @Override
    public void saveContacts(List<Contact> existingContacts, Integer profileId, Connection conn) {
        contactPersister.saveContacts(existingContacts, profileId, conn);
    }

    /**
     * Determines whether or not a person is 'self updating'. This is true if the authenticated
     * {@link org.consumersunion.stories.common.shared.model.User} has the same ID as the person being updated.
     */
    private boolean isSameEntity(SystemEntity systemEntity, int targetId) {
        return systemEntity != null && systemEntity.getId() == targetId;
    }

    private List<Contact> setEntityId(List<Contact> contacts, final int entityId) {
        return Lists.transform(contacts, new Function<Contact, Contact>() {
            @Override
            public Contact apply(Contact contact) {
                contact.setEntityId(entityId);

                return contact;
            }
        });
    }

    private void geoLocateAddress(Address address) {
        GeoCodeStatus status = null;
        if (!Strings.isNullOrEmpty(address.getGeoCodeStatus())) {
            status = GeoCodeStatus.valueOf(address.getGeoCodeStatus());
        }

        if (status == null || status == GeoCodeStatus.SKIPPED) {
            try {
                Localisation location = geoCodingService.geoLocate(address);

                if (location != null) {
                    location.updateAddress(address);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "An error occured when geo coding an address.", e);
            }
        }
    }

    private void queueConvioUpdates(int entityId) {
        User currentUser = userService.getLoggedInUser();
        Integer currentUserId = currentUser == null ? null : currentUser.getId();

        ProfileSummary profileSummary;
        try {
            profileSummary = profileService.get(entityId);
        } catch (NotFoundException e) {
            profileSummary = null;
        }

        if (profileSummary != null) {
            // for each associated org we have a profile to sync (No organization contact synchronization on Convio)
            syncFromSysConvioFactory.create(profileSummary.getProfile())
                    .queueSysToConvioUpdates(profileSummary, isSameEntity(currentUser, entityId), currentUserId);
        }
    }
}
