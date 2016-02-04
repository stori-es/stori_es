package org.consumersunion.stories.server.rest.api.convio;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.consumersunion.stories.common.server.service.datatransferobject.ConvioConstituent;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.ProfilePersister;

/**
 * Task to retrieve Constituent data from Convio. The class is not used directly in normal operations, but is used on
 * the {@link ConvioRateLimitProbe} class and as the first step in the {@link PollConvioAndProcessResult}. On it's own
 * this class pulls data from the Convio DB, but doesn't do anything with it. For general operations, the class is
 * sub-classed, the {@link #synchronize()} method invoked, and the results accessed though the protected fields
 * {@link #profile} and {@link #constituentResponse}.
 */
public class PollConvioForConstituentData extends ConvioSyncTask {
    protected Integer profileId = null;
    protected Profile profile;
    protected ConvioConstituentResponse convioConstituentResponse;

    private final String primaryEmail = null;

    public PollConvioForConstituentData(int personId, int orgId) {
        super(orgId);
        this.profileId = personId;
    }

    public void synchronize() {
        // First we need to get the Person
        profile = PersistenceUtil.process(new ProfilePersister.RetrieveProfileSummaryFunc(profileId)).getProfile();

        if (Profile.MANUAL_CHECK.equals(profile.getConvioSyncStatus(getOrgId()).getConvioSyncStatus()) ||
                Profile.SYNC_REQUIRED.equals(profile.getConvioSyncStatus(getOrgId()).getConvioSyncStatus())) {
            return; // Nothing we can do.
        }

        // Make the call to Convio.
        final Organization syncOrg = getSyncOrg();
        // final String loginResource = getConvioEndpoint(syncOrg) + "?method=login&response_format=json&" +
        //		getSecurityChain(syncOrg) + "&user_name=" + syncOrg.getCrmApiLogin() + "&password=Pass1word";

        String getUserResource = getConvioEndpoint(syncOrg) + "?method=getUser&" + getSecurityChain(
                syncOrg) + "&" + RESPONSE_FORMAT;
        if (profile.getConvioSyncStatus(getOrgId()).getCons_id() != null) {
            getUserResource += "&cons_id=" + profile.getConvioSyncStatus(getOrgId()).getCons_id();
        } else if (primaryEmail != null) {
            getUserResource += "&primary_email=" + primaryEmail;
        } else { // We are in some state other than MANUAL_CHECK and SYNC_REQUIRED and we can't perform the poll, so
            // that
            // makes the status POTENTIALLY_STALE. Notice that we propogate any existing Cons ID.
            profile.setConvioSyncStatus(getOrgId(), profile.getConvioSyncStatus(getOrgId()).getCons_id(),
                    Profile.POTENTIALY_STALE);
            try {
                PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
            } catch (Exception e) {
                // Note this does NOT invoke 'this.setCommunicationException(e)'. It's not a communication error, which
                // triggers back off. This is a usage problem.
                throw new GeneralException("PollConvioAndProcessResult failed to update Person record to " +
                        "'POTENTIALLY_STALE'.");
            }
        }

        // Again, if we want to do the poll, but can't that puts us into the potentially stale state.
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final String jsonString =
                    getRestTemplate().postForObject(getUserResource,
                            null,
                            String.class);
            convioConstituentResponse = objectMapper.readValue(jsonString, ConvioConstituentResponse.class);
        } catch (Exception e) {
            profile.setConvioSyncStatus(getOrgId(), profile.getConvioSyncStatus(getOrgId()).getCons_id(),
                    Profile.POTENTIALY_STALE);
            try {
                PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
            } finally {
                processCommunicationException(e);
            }
            return;
        }
    }

    public Profile getProfile() {
        return profile;
    }

    public ConvioConstituent getConvioConstituent() {
        if (convioConstituentResponse == null) {
            return null;
        } else {
            return convioConstituentResponse.getGetConsResponse();
        }
    }

    public static class ConvioConstituentResponse extends ConvioResponse {
        private ConvioConstituent getConsResponse;

        public ConvioConstituent getGetConsResponse() {
            return getConsResponse;
        }

        public void setGetConsResponse(ConvioConstituent getConsResponse) {
            this.getConsResponse = getConsResponse;
        }
    }
}
