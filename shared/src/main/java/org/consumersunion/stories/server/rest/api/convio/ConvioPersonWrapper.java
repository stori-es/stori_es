package org.consumersunion.stories.server.rest.api.convio;

import java.util.List;
import java.util.regex.Pattern;

import org.consumersunion.stories.common.server.service.datatransferobject.ConvioAddressObject;
import org.consumersunion.stories.common.server.service.datatransferobject.ConvioConstituent;
import org.consumersunion.stories.common.server.service.datatransferobject.ConvioEmailObject;
import org.consumersunion.stories.common.server.service.datatransferobject.ConvioNameObject;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.modelmapper.ModelMapper;

public class ConvioPersonWrapper {
    private final Profile profile;
    private final ConvioConstituent convioConstituent;

    private List<Contact> contacts;
    private List<Address> addresses;

    public ConvioPersonWrapper(Profile profile) {
        this.profile = profile;
        initializeLists();

        final ConvioConstituent constituent = new ConvioConstituent();
        // First, set basic name and email data.
        constituent.setName(new ConvioNameObject(profile.getGivenName(), profile.getSurname()));
        // Now, the addresses. Note that we interpret the primary, first alternate, and second alternate as the home,
        // work, and 'other' addresses respectively. The Convio API documentation talks in terms of primary, alternate,
        // and second alternate, but the Convio UI labels these fields as 'Home', 'Work', and 'Other'. The second
        // interpretation matches our interpretation of contacts as well.
        Address primaryHomeAddress = null, primaryWorkAddress = null, primaryOtherAddress = null;
        for (Address address : addresses) {
            if (primaryHomeAddress == null && Address.RELATION_HOME.equals(address.getRelation())) {
                primaryHomeAddress = address;
            }
            if (primaryWorkAddress == null && Address.RELATION_WORK.equals(address.getRelation())) {
                primaryWorkAddress = address;
            }
            if (primaryOtherAddress == null && Address.RELATION_OTHER.equals(address.getRelation())) {
                primaryOtherAddress = address;
            }
        }
        if (primaryHomeAddress != null) {
            constituent.setPrimary_address(ConvioAddressObject.fromSysAddress(primaryHomeAddress));
        }
        if (primaryWorkAddress != null) {
            constituent.setAlternate_address1(ConvioAddressObject.fromSysAddress(primaryWorkAddress));
        }
        if (primaryOtherAddress != null) {
            constituent.setAlternate_address2(ConvioAddressObject.fromSysAddress(primaryOtherAddress));
        }

        // Basically, same thing with the contact information. The 'phone' is divided into home, work, and mobile,
        // while email is divided into primary and secondary.
        String primaryEmail = null, secondaryEmail = null, homePhone = null, workPhone = null, mobilePhone = null;
        for (Contact contact : contacts) {
            if (primaryEmail == null && "EMAIL".equals(contact.getMedium())) {
                primaryEmail = contact.getValue();
            }
            if (secondaryEmail == null && "EMAIL".equals(contact.getMedium())) {
                secondaryEmail = contact.getValue();
            }

            if (homePhone == null && "PHONE".equals(contact.getMedium()) && Contact.TYPE_HOME.equals(
                    contact.getType())) {
                homePhone = contact.getValue();
            }
            if (workPhone == null && "PHONE".equals(contact.getMedium()) && Contact.TYPE_WORK.equals(
                    contact.getType())) {
                workPhone = contact.getValue();
            }
            if (mobilePhone == null && "PHONE".equals(contact.getMedium()) && Contact.TYPE_MOBILE.equals(
                    contact.getType())) {
                mobilePhone = contact.getValue();
            }
        }

        if (primaryEmail != null) {
            constituent.setEmail(new ConvioEmailObject());
            constituent.getEmail().setPrimary_address(primaryEmail);
            if (secondaryEmail != null) {
                constituent.getEmail().setSecondary_address(secondaryEmail);
            }
        }
        constituent.setHome_phone(homePhone);
        constituent.setWork_phone(workPhone);
        constituent.setMobile_phone(mobilePhone);

        // handleEmailFormat();
        this.convioConstituent = constituent;
    }

    @SuppressWarnings("unchecked")
    private void initializeLists() {
        this.contacts = PersistenceUtil.process(new ContactPersister.RetrieveContactFunc(profile.getId()));
        this.addresses = PersistenceUtil.process(new ContactPersister.RetrieveAddress(profile.getId()));
    }

    public ConvioPersonWrapper(final Profile profile, ConvioConstituent convioConstituent) {
        this.profile = profile;
        this.convioConstituent = convioConstituent;

        initializeLists();
    }

    public Profile getProfile() {
        return profile;
    }

    public ConvioConstituent getConvioConstituent() {
        return convioConstituent;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public boolean isDataDiffers() {
        // The general test is: 'both null or empty' OR 'both not NULL and soft string matches'. We check:
        // - first name
        if (!(((profile.getGivenName() == null || profile.getGivenName().trim().length() == 0) &&
                (convioConstituent.getName().getFirst() == null ||
                        convioConstituent.getName().getFirst().trim().length() == 0)) ||
                (profile.getGivenName() != null &&
                        profile.getGivenName().equalsIgnoreCase(convioConstituent.getName().getFirst())))) {
            return true;
        }
        // - last name
        else if (!(((profile.getSurname() == null || profile.getSurname().trim().length() == 0) &&
                (convioConstituent.getName().getLast() == null ||
                        convioConstituent.getName().getLast().trim().length() == 0)) ||
                (profile.getSurname() != null &&
                        profile.getSurname().equalsIgnoreCase(convioConstituent.getName().getLast())))) {
            return true;
        }
        // - primary email
        else if (!(((getPrimarySysEmail() == null || getPrimarySysEmail().trim().length() == 0) &&
                (convioConstituent.getEmail().getPrimary_address() == null ||
                        convioConstituent.getEmail().getPrimary_address().trim().length() == 0)) ||
                (getPrimarySysEmail() != null && convioConstituent.getEmail() != null &&
                        getPrimarySysEmail().equalsIgnoreCase(convioConstituent.getEmail().getPrimary_address())))) {
            return true;
        }
        // - secondary email
        else if (!(((getSecondarySysEmail() == null || getSecondarySysEmail().trim().length() == 0) &&
                (convioConstituent.getEmail().getSecondary_address() == null ||
                        convioConstituent.getEmail().getSecondary_address().trim().length() == 0)) ||
                (getSecondarySysEmail() != null && convioConstituent.getEmail() != null &&
                        getSecondarySysEmail().equalsIgnoreCase(
                                convioConstituent.getEmail().getSecondary_address())))) {
            return true;
        }
        // - home phone
        else if (!(((getPrimarySysHomePhone() == null || getPrimarySysHomePhone().trim().length() == 0) &&
                (convioConstituent.getHome_phone() == null ||
                        convioConstituent.getHome_phone().trim().length() == 0)) ||
                (getPrimarySysHomePhone() != null &&
                        getPrimarySysHomePhone().equalsIgnoreCase(convioConstituent.getHome_phone())))) {
            return true;
        }
        // - work phone
        else if (!(((getPrimarySysWorkPhone() == null || getPrimarySysWorkPhone().trim().length() == 0) &&
                (convioConstituent.getWork_phone() == null ||
                        convioConstituent.getWork_phone().trim().length() == 0)) ||
                (getPrimarySysWorkPhone() != null &&
                        getPrimarySysWorkPhone().equalsIgnoreCase(convioConstituent.getWork_phone())))) {
            return true;
        }
        // - mobile phone
        else if (!(((getPrimarySysMobilePhone() == null || getPrimarySysMobilePhone().trim().length() == 0) &&
                (convioConstituent.getMobile_phone() == null ||
                        convioConstituent.getMobile_phone().trim().length() == 0)) ||
                (getPrimarySysMobilePhone() != null &&
                        getPrimarySysMobilePhone().equalsIgnoreCase(convioConstituent.getMobile_phone())))) {
            return true;
        }
        // - home address
        else if (!((getPrimarySysHomeAddress() == null && convioConstituent.getPrimary_address() == null) ||
                (getPrimarySysHomeAddress() != null &&
                        getPrimarySysHomeAddress().softMatch(getConvioHomeAddress())))) {
            return true;
        }
        // - work address
        else if (!((getPrimarySysWorkAddress() == null && convioConstituent.getAlternate_address1() == null) ||
                (getPrimarySysWorkAddress() != null &&
                        getPrimarySysWorkAddress().softMatch(getConvioWorkAddress())))) {
            return true;
        }
        // - other address
        else if (!((getPrimarySysOtherAddress() == null && convioConstituent.getAlternate_address2() == null) ||
                (getPrimarySysOtherAddress() != null &&
                        getPrimarySysOtherAddress().softMatch(getConvioOtherAddress())))) {
            return true;
        } else { // We have run the gauntlet and can find no fault.
            return false;
        }
    }

    public String getPrimarySysEmail() {
        for (Contact contact : contacts) {
            if ("EMAIL".equals(contact.getMedium())) {
                return contact.getValue();
            }
        }
        // If we fall through, that means there is no such email.
        return null;
    }

    public String getSecondarySysEmail() {
        boolean foundFirst = false;
        for (Contact contact : contacts) {
            if ("EMAIL".equals(contact.getMedium())) {
                if (!foundFirst) {
                    foundFirst = true;
                } else {
                    return contact.getValue();
                }
            }
        }
        // If we fall through, that means there is no such email.
        return null;
    }

    public String getPrimarySysHomePhone() {
        return getSysPhoneHelper(Contact.TYPE_HOME);
    }

    public String getPrimarySysWorkPhone() {
        return getSysPhoneHelper(Contact.TYPE_WORK);
    }

    public String getPrimarySysMobilePhone() {
        return getSysPhoneHelper(Contact.TYPE_MOBILE);
    }

    private String getSysPhoneHelper(final String type) {
        for (Contact contact : contacts) {
            if ("PHONE".equals(contact.getMedium()) && type.equals(contact.getType())) {
                return contact.getValue();
            }
        }
        // If we fall through, that means there is no such phone.
        return null;
    }

    public Address getPrimarySysHomeAddress() {
        return getSysAddressHelper(Address.RELATION_HOME);
    }

    public Address getPrimarySysWorkAddress() {
        return getSysAddressHelper(Address.RELATION_WORK);
    }

    public Address getPrimarySysOtherAddress() {
        return getSysAddressHelper(Address.RELATION_OTHER);
    }

    private Address getSysAddressHelper(final String relation) {
        for (Address address : addresses) {
            if (relation.equals(address.getRelation())) {
                return address;
            }
        }
        // If we fall through, that means there is no such address.
        return null;
    }

    public Address getConvioHomeAddress() {
        return getConvioAddressHelper(convioConstituent.getPrimary_address(), Address.RELATION_HOME);
    }

    public Address getConvioWorkAddress() {
        return getConvioAddressHelper(convioConstituent.getAlternate_address1(), Address.RELATION_WORK);
    }

    public Address getConvioOtherAddress() {
        return getConvioAddressHelper(convioConstituent.getAlternate_address2(), Address.RELATION_OTHER);
    }

    private Address getConvioAddressHelper(ConvioAddressObject convioAddressObject, final String relation) {
        if (convioAddressObject != null) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.addMappings(new StoriesAddressMapper());
            Address convertedAddress = modelMapper.map(convioAddressObject, Address.class);
            convertedAddress.setRelation(relation);
            convertedAddress.setEntity(profile.getId());

            return convertedAddress;
        } else {
            return null;
        }
    }

    private static final Pattern phoneRegularizer = Pattern.compile("\\D");

    public void updateConvioData(String convioPhone, final String phoneType) {
        // First deal with 'no data' from Convio.
        if (convioPhone == null || convioPhone.trim().length() == 0) {
            return;
        }

        // Regularize the Convio data.
        convioPhone = phoneRegularizer.matcher(convioPhone).replaceAll("");

        // First, we search the contacts for a match.
        Contact matchedContact = null;
        boolean typeMatched = false;
        for (Contact contact : contacts) {
            if ("PHONE".equals(contact.getMedium())) {
                // Contact.value cannot be null, so that's safe.
                String value = contact.getValue();
                value = phoneRegularizer.matcher(value).replaceAll("");

                if (value.equals(convioPhone)) {
                    matchedContact = contact;
                }
                if (phoneType.equals(contact.getType())) {
                    typeMatched = true;
                }
            }
        }

        // Now we have all the data set up to implement the merge as documented in the 'Data Synchronized' section:
        // https://docs.google.com/a/navigo.com/document/d/1T5xRQ1L8G6Ogb21u8W5OJzUcXBIlnVWsQePIpXtpg8U/edit#heading
        // =h.fp1e51t8f9sz
        if (matchedContact == null && typeMatched) {
            // Then we insert the new data just prior to the first matched type.
            for (Contact contact : contacts) {
                if ("PHONE".equals(contact.getMedium()) && phoneType.equals(contact.getType())) {
                    contacts.add(contacts.indexOf(contact),
                            new Contact(profile.getId(), "PHONE", phoneType, convioPhone));
                }
            }
        } else if (matchedContact == null && !typeMatched) {
            // Then we add the new data to the end of the list.
            contacts.add(new Contact(profile.getId(), "PHONE", phoneType, convioPhone));
        } else if (matchedContact != null) {
            // The data has been matched, if not the same type, then we update our local type. Note, 'typeMatched' tells
            // us if there is /any/ instance of the type in the list, not whether the 'matchedContact' is the same or
            // not. In any case, we want the types to match, so we unilateraly update.
            matchedContact.setType(phoneType);

            // Now we need to determine the position of the first Contact of the same type.
            int firstMatchIndex = -1;
            for (Contact contact : contacts) {
                if ("PHONE".equals(contact.getMedium()) && phoneType.equals(contact.getType())) {
                    firstMatchIndex = contacts.indexOf(contact);
                    break;
                }
            }
            // We expect 'firstMatchIndex' to always be something useful... afer all, 'matchedContact != null'.
            final int matchedIndex = contacts.indexOf(matchedContact);
            if (matchedIndex != firstMatchIndex) {
                contacts.remove(matchedIndex);
                contacts.add(firstMatchIndex, matchedContact);
            }
        }
    }

    public void updateConvioData(Address convioAddress, final String addressRelation) {
        // First deal with 'no data' from Convio.
        if (convioAddress == null) {
            return;
        }

        // First, we search the contacts for a match.
        Address matchedAddress = null;
        boolean relationMatched = false;
        for (Address address : addresses) {
            if (address.softMatch(convioAddress)) {
                matchedAddress = address;
            }
            if (addressRelation.equals(address.getRelation())) {
                relationMatched = true;
            }
        }

        // Now we have all the data set up to implement the merge as documented in the 'Data Synchronized' section:
        // https://docs.google.com/a/navigo.com/document/d/1T5xRQ1L8G6Ogb21u8W5OJzUcXBIlnVWsQePIpXtpg8U/edit#heading
        // =h.fp1e51t8f9sz
        if (matchedAddress == null && relationMatched) {
            // Then we insert the new data just prior to the first matched type.
            for (Address address : addresses) {
                if (addressRelation.equals(address.getRelation())) {
                    addresses.add(addresses.indexOf(address), convioAddress);
                    return;
                }
            }
        } else if (matchedAddress == null && !relationMatched) {
            // Then we add the new data to the end of the list.
            addresses.add(convioAddress);
        } else if (matchedAddress != null) {
            // The data has been matched, if not the same type, then we update our local type. Note, 'typeMatched' tells
            // us if there is /any/ instance of the type in the list, not whether the 'matchedContact' is the same or
            // not. In any case, we want the types to match, so we unilateraly update.
            matchedAddress.setRelation(addressRelation);

            // Now we need to determine the position of the first Contact of the same type.
            int firstMatchIndex = -1;
            for (Address address : addresses) {
                if (addressRelation.equals(address.getRelation())) {
                    firstMatchIndex = addresses.indexOf(address);
                    break;
                }
            }
            // We expect 'firstMatchIndex' to always be something useful... afer all, 'matchedContact != null'.
            final int matchedIndex = addresses.indexOf(matchedAddress);
            if (matchedIndex != firstMatchIndex) {
                addresses.remove(matchedIndex);
                addresses.add(firstMatchIndex, matchedAddress);
            }
        }
    }
}
