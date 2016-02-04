package org.consumersunion.stories.server.rest.api.convio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.server.service.datatransferobject.ConvioConstituent;
import org.consumersunion.stories.common.server.service.datatransferobject.ConvioRequestUrlBuilder;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.business_logic.AuthorizationService;
import org.consumersunion.stories.server.persistence.PersistenceService;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.rest.api.convio.ConvioDataSynchronizationWorker;
import org.consumersunion.stories.server.rest.api.convio.ConvioPersonWrapper;
import org.consumersunion.stories.server.rest.api.convio.ConvioSyncTask;
import org.consumersunion.stories.server.rest.api.convio.PollConvioAndProcessResult;
import org.consumersunion.stories.server.rest.api.convio.SyncFromSysPersonToConvioConstituentRequestFactory;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class SyncFromSysPersonToConvioConstituentRequest extends ConvioSyncTask {
    private static Logger logger = Logger.getLogger(ConvioSyncTask.class.getName());

    private final AuthorizationService authService;
    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final ConvioDataSynchronizationWorker worker;
    private final SyncFromSysPersonToConvioConstituentRequestFactory factory;
    private final PersistenceService persistenceService;

    private Profile profile;

    private ConvioCreateOrUpdateResponse response;

    public SyncFromSysPersonToConvioConstituentRequest(
            SyncFromSysPersonToConvioConstituentRequestFactory factory,
            PersistenceService persistenceService,
            AuthorizationService authService,
            SupportDataUtilsFactory supportDataUtilsFactory,
            ConvioDataSynchronizationWorker worker,
            Profile profile) {
        super(profile.getOrganizationId());

        this.factory = factory;
        this.persistenceService = persistenceService;
        this.authService = authService;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.worker = worker;
        this.profile = profile;
    }

    public void queueSysToConvioUpdates(int personId, boolean selfUpdate, Integer userId, Connection conn) {
        queueSysToConvioUpdates(
                persistenceService.process(conn, new ProfilePersister.RetrieveProfileSummaryFunc(personId)), selfUpdate,
                userId);
    }

    public void queueSysToConvioUpdates(ProfileSummary profileSummary, boolean selfUpdate, Integer userId) {
        // There is a tension in the sequence of operations here. If we grab the connection here, we could prepare our
        // queries once and re-use within the associated orgs loops. The downside is that an exception kicks us out
        // of further org updates. On the other hand, if we grab the connection internal to the loop, it is possible
        // that we could have a problem with one org, but at least have a chance with others. We currently prefer the
        // second, potentially less efficient but more robust approach since it's early days and there may be bugs
        // which show up in under some conditions, but not others, meaning there's a better chance that some orgs work
        // while others do not. After we get some time under our belt, it may be worthwhile to flip the sequence to the
        // more efficient, less robust option.
        Profile profile = profileSummary.getProfile();
        for (Integer orgId : getAssociatedOrgs(profile.getId())) {
            String syncStatus = profile.getConvioSyncStatus(orgId).getConvioSyncStatus();
            if (Profile.SYNC_REQUIRED.equals(syncStatus)) {
                // This isn't a problem functionally; the design says to proceed. However, the fact that we're being
                // asked to update a record that appears to already be in queue could mean either a logic problem (it's
                // early days), a traffic jam, or attack. In practice, most of these situations would tend to push
                // data into MANUAL_CHECK as the initial polls would start to fail before you'd expect to get a lot of
                // double sync-requests queued up.
                logger.log(Level.WARNING,
                        "Unexpected asked to push to data from SYS to Convio for data which appears " +
                                "to alread by waiting for sync.");
            }
            boolean setToSync = false;
            if (selfUpdate) {
                setToSync = true;
            } else if (Profile.SYNCHRONIZED.equals(syncStatus) || Profile.SYNC_REQUIRED.equals(syncStatus)) {
                // Then we must check the edit window.
                long editWindow = getEditWindow(profile.getId(), userId);
                long currentTime = System.currentTimeMillis();
                if (currentTime < editWindow) {
                    setToSync = true;
                } else {
                    // Synchronized, but outside of edit window requires poll and check.
                    boolean success;
                    PollConvioAndProcessResult convioPoll = new PollConvioAndProcessResult(profile.getId(), orgId,
                            null);
                    try {
                        worker.process(convioPoll);
                        success = convioPoll.isServiceSuccess();
                        if (success && convioPoll.getProfile() != null) {
                            profile = convioPoll.getProfile(); // Refresh with possibly updated version.
                        }
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, e.getMessage());
                        e.printStackTrace();
                        success = false;
                    }

                    Boolean dataDiffers = convioPoll.getDataDiffers();
                    if (success && (dataDiffers != null && !dataDiffers)) {
                        setToSync = true;
                    } else { // if (!success || dataDiffers == null || dataDiffers) { // exhaustive
                        logger.log(Level.WARNING,
                                "Could not sync " + profile.getId() + "; Communication: " + success + "; Data " +
                                        "differs: " + (dataDiffers == null ? "indeterminate" : dataDiffers + "") +
                                        ("; status: " + profile.getConvioSyncStatus(
                                                orgId).getConvioSyncStatus()) + "; time: " + currentTime + "/" +
                                        editWindow);
                        profile.setConvioSyncStatus(orgId, Profile.MANUAL_CHECK);
                        PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
                    }
                }
            } else if (Profile.POTENTIALY_STALE.equals(syncStatus)) {
                logger.log(Level.WARNING, "Unexpceted 'POTENTIALLY_STALE' state encountered from Profile/Org " +
                        profile.getId() + "/" + orgId + " while attempting to synchronize.");
            }
            // Non-'selfUpdate' MANUAL_CHECK, nothing to do, unfortunate but valid state.
            // also_update_diagram_to_show_flow_of_all_actions_from_sync_required

            if (setToSync) { // Recall, 'setToSync' initially set false, then we look for positive evidence that it's
                // the right thing to do.
                profile.setConvioSyncStatus(orgId, Profile.SYNC_REQUIRED);
                worker.processAsync(factory.create(profile));
            }
        }
    }

    @Override
    public void synchronize() {
        // Notice that we don't do anything with the sync status or self update within this method. Once the change is
        // registered, we pull ahead. We could take cognizance of time horizon and drop updates or at least generate
        // warnings in the face of stale data.
        try {
            Organization syncOrg = getSyncOrg();
            if (syncOrg.getCrmEndpoint() == null) { // nothing to do
                return;
            }
            ConvioPersonWrapper convioPersonWrapper = new ConvioPersonWrapper(profile);
            ConvioConstituent convioConstituent = convioPersonWrapper.getConvioConstituent();
            Connection conn = PersistenceUtil.getConnection();
            try {
                SupportDataUtils dataUtils = supportDataUtilsFactory.create(conn);
                try {
                    String sysPreferredFormat = dataUtils.getPreferredEmailFormat(profile.getId(),
                            syncOrg.getId());
                    String convioPreferredFormat = "0"; // none
                    if ("HTML".equals(sysPreferredFormat)) {
                        convioPreferredFormat = "1";
                    } else if ("Plain Text".equals(sysPreferredFormat)) {
                        convioPreferredFormat = "2";
                    }
                    convioConstituent.getEmail().setPreferred_format(convioPreferredFormat);
                } catch (Exception e) { // Oh well. Let's move on.
                    e.printStackTrace();
                }
                try {
                    Boolean opt_in = dataUtils.getUpdatesOptIn(profile.getId(), syncOrg.getId());
                    convioConstituent.setDir_member_opt_in(opt_in == null || !opt_in ? "false" : "true");
                } catch (Exception e) { // Oh well. Let's move on.
                    e.printStackTrace();
                }
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Nothing much else we can do.
                }
            }

            String createResource = getConvioEndpoint(syncOrg) + "?method=createOrUpdate&" + getSecurityChain(
                    syncOrg) + "&" + RESPONSE_FORMAT;
            try {
                // Note it is not necessary to login prior to issuing the 'createOrUpdate'.
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
                response = objectMapper.readValue(
                        getRestTemplate().postForObject(
                                ConvioRequestUrlBuilder.buildRequestUrl(createResource, convioConstituent),
                                convioConstituent,
                                String.class), ConvioCreateOrUpdateResponse.class);

                // On the initial creation on the Convio side, we get a 'Constituent ID', the 'cons_id', which we save.
                // It's not clear the best way to handle the other cases... such as, what if we get an ID for
                // something that
                // has an ID and it doesn't match? For the most part, the edge cases shouldn't happen, and since they
                // are
                // unlikely and handling is unknown, we ignore them for the moment.
                int cons_id = Integer.parseInt(response.getCons_id());
                profile.setConvioSyncStatus(getOrgId(), cons_id, Profile.SYNCHRONIZED);
                PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
            } catch (Exception e) {
                e.printStackTrace();
                processCommunicationException(e);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Version mismatch.")) {
                // I don't believe we want to totally avoid this situation, though it should be rare and is warning
                // worthy. In any case, we will try to re-get the Person and mark for MANUAL_CHECK.
                logger.log(Level.WARNING, "Received 'version mismatch' exception while attempting to synchronize.");
                profile = PersistenceUtil
                        .process(new ProfilePersister.RetrieveProfileSummaryFunc(profile.getId())).getProfile();
                profile.setConvioSyncStatus(getOrgId(), Profile.MANUAL_CHECK);
                PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
            }
            e.printStackTrace();
        }
    }

    private List<Integer> getAssociatedOrgs(int personId) {
        List<Integer> organizations = new LinkedList<Integer>();

        for (SystemEntity entity :
                authService.getValidPrincipals(ROLE_READER, personId, "organization")) {
            organizations.add(entity.getId());
        }

        return organizations;
    }

    public ConvioCreateOrUpdateResponse getResponse() {
        return response;
    }

    @JsonRootName(value = "createOrUpdateConsResponse")
    public static class ConvioCreateOrUpdateResponse extends ConvioResponse {
        private String cons_id;

        // Must be explicit for the ObjectMapper JSON conversion.
        public ConvioCreateOrUpdateResponse() {
        }

        public String getCons_id() {
            return cons_id;
        }

        public void setCons_id(String cons_id) {
            this.cons_id = cons_id;
        }
    }

    private static long getEditWindow(int personId, Integer userId) {
        if (userId == null) {
            return 0;
        }

        Connection conn = PersistenceUtil.getConnection();
        try {
            PreparedStatement getEdit =
                    conn.prepareStatement("SELECT editWindow FROM personEditWindow WHERE personId=? AND userId=?");
            getEdit.setInt(1, personId);
            getEdit.setInt(2, userId);
            ResultSet results = getEdit.executeQuery();
            if (results.next()) {
                Timestamp window = results.getTimestamp(1);
                return window.getTime();
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new GeneralException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) { // Not much else we can do.
                e.printStackTrace();
            }
        }
    }
}
