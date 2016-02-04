package org.consumersunion.stories.server.rest.api.convio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.ProfilePersister;

/**
 * A {@link ConvioSyncTask} which polls Convio and takes the appropriate synchronization steps based on the current
 * synchronization state and results of the Convio request. The
 * <a href="https://docs.google.com/a/navigo.com/document/d/1T5xRQ1L8G6Ogb21u8W5OJzUcXBIlnVWsQePIpXtpg8U/edit#heading
 * =h.p6j0dz1rd767">Convio
 * Synchronization Flow</a> calls for synchronously polling the Convio system to prior to displaying data, or to check
 * data synchronization prior an update outside the edit window (due to timeout or attempt to update a potentially
 * stale {@link org.consumersunion.stories.common.shared.model.Profile}.
 * <p/>
 * <ul>
 * <li>If the <code>Person</code>s Convio sync status is <code>MANUAL_CHECK</code>, then no action is taken.</li>
 * <li>If the <code>Person</code>s Convio sync status is <code>SYNC_REQUIRED</code>, then no action is taken.</li>
 * <li>If the <code>Person</code> is in any other state, an attempt to contact the Convio DB is made.
 * <ul>
 * <li>If said attempt fails, the <code>Person</code> is placed into the <code>POTENTIALLY_STALE</code>
 * state.</li>
 * <li>If the poll succeeds, but there's no Constituent found, this is considered valid if the current Convio
 * sync status is <code>NOT_SYNCHRONIZED<code>; otherwise the <code>Person</code> status is changed to
 * <code>MANUAL_CHECK</code>.</li>
 * <li>If the polling request succeeds, and the <code>Person</code> is currently <code>SYNCHRONIZED</code>, then
 * the SYS data is updated. If the update fails, the <code>Person</code> is marked for <code>MANUAL_CHECK</code>.
 * If the SYS update succeeds, the <code>Person</code> remains <code>SYNCHRONIZED</code>.
 * <li>If the polling request succeeds, and the <code>Person</code> is currently <code>POTENTIALLY_STALE</code>,
 * then the data is checked, if it matches, then the <code>Person</code> is marked as <code>SYNCHRONIZED</code>
 * and if the data doesn't match, the <code>Person</code> is marked for <code>MANUAL_CHECK</code>.
 * </ul>
 * </li>
 * </ul>
 */
public class PollConvioAndProcessResult extends PollConvioForConstituentData {
    /**
     * @see #getDataMatches()
     */
    private final Integer userId;

    private Boolean dataDiffers;

    /**
     * Standard constructor. Unilke the {@link SyncFromSysPersonToConvioConstituent} Convio data is strictly segregated
     * by {@link Organization} so updates only occur in the context of an organization. Please refer to the
     * <a href="https://docs.google.com/a/navigo
     * .com/document/d/1T5xRQ1L8G6Ogb21u8W5OJzUcXBIlnVWsQePIpXtpg8U/edit#bookmark=id.9vypbpnyo31h">Personal Data
     * Profile</a>
     *
     * @param entityId
     * @param organizationId
     * @see #SyncSysPersonToConvioConstituentRequest(int, int)
     */
    public PollConvioAndProcessResult(int personId, int organizationId, Integer userId) {
        super(personId, organizationId);
        this.userId = userId;
    }

    @Override
    public void synchronize() {
        super.synchronize();

        if (profile == null) {
            return;
        }

        if (getConvioConstituent() == null
                && !Profile.NOT_SYNCHRONIZED.equals(profile.getConvioSyncStatus(getOrgId()).getConvioSyncStatus())) {
            // If there is no record, the only valid state is NOT_SYNCHRONIZED.
            profile.setConvioSyncStatus(getOrgId(), profile.getConvioSyncStatus(getOrgId()).getCons_id(),
                    Profile.MANUAL_CHECK);
            PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
            // In this case, we cannot determine whether 'dataMatches' and so leave the value null.
        } else if (getConvioConstituent() != null) {
            final ConvioPersonWrapper convioPersonWrapper = new ConvioPersonWrapper(profile, getConvioConstituent());

            if (convioPersonWrapper.isDataDiffers()) {
                dataDiffers = true;
                if (Profile.POTENTIALY_STALE.equals(profile.getConvioSyncStatus(getOrgId()).getConvioSyncStatus()) ||
                        Profile.NOT_SYNCHRONIZED.equals(
                                profile.getConvioSyncStatus(getOrgId()).getConvioSyncStatus())) {
                    profile.setConvioSyncStatus(getOrgId(), profile.getConvioSyncStatus(getOrgId()).getCons_id(),
                            Profile.MANUAL_CHECK);
                    PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
                } else if (!Profile.SYNCHRONIZED.equals(
                        profile.getConvioSyncStatus(getOrgId()).getConvioSyncStatus())) {
                    // This shouldn't happen.
                    throw new GeneralException(
                            "Unexpected Convio sync state: " + profile.getConvioSyncStatus(getOrgId()) + ".");
                } else {
                    // Otherwise, the Person and Constituent records are in a SYNCHRONIZED state, but the data differs,
                    // indicating we need to copy the data from the Convio DB into the SYS DB.
                    updateSysRecordsFromConvioConstituentRecord(convioPersonWrapper);
                }
            } else if (Profile.SYNCHRONIZED.equals(profile.getConvioSyncStatus(getOrgId()).getConvioSyncStatus())) {
                // Then 'dataDiffers', but we are synchronized, so we can mark edit window.
                markEditWindow();
            } else if (Profile.POTENTIALY_STALE.equals(profile.getConvioSyncStatus(getOrgId()).getConvioSyncStatus()) ||
                    Profile.NOT_SYNCHRONIZED.equals(profile.getConvioSyncStatus(getOrgId()).getConvioSyncStatus())) {
                // Data does not differ, so we can update the state to synchronized.
                dataDiffers = false;
                profile.setConvioSyncStatus(getOrgId(), profile.getConvioSyncStatus(getOrgId()).getCons_id(),
                        Profile.SYNCHRONIZED);
                PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
            }
        }
    }

    private void updateSysRecordsFromConvioConstituentRecord(ConvioPersonWrapper convioPersonWrapper) {
        try {
            profile.setGivenName(convioPersonWrapper.getConvioConstituent().getName().getFirst());
            profile.setSurname(convioPersonWrapper.getConvioConstituent().getName().getLast());
            profile = PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));

            convioPersonWrapper.updateConvioData(convioPersonWrapper.getConvioConstituent().getHome_phone(),
                    Contact.TYPE_HOME);
            convioPersonWrapper.updateConvioData(convioPersonWrapper.getConvioConstituent().getWork_phone(),
                    Contact.TYPE_WORK);
            convioPersonWrapper.updateConvioData(convioPersonWrapper.getConvioConstituent().getMobile_phone(),
                    Contact.TYPE_MOBILE);

            PersistenceUtil.process(new ContactPersister.SaveContactsFunc(convioPersonWrapper.getContacts()));

            // Now to do the same with the Convio addresses.
            convioPersonWrapper.updateConvioData(convioPersonWrapper.getConvioHomeAddress(), Address.RELATION_HOME);
            convioPersonWrapper.updateConvioData(convioPersonWrapper.getConvioWorkAddress(), Address.RELATION_WORK);
            convioPersonWrapper.updateConvioData(convioPersonWrapper.getConvioOtherAddress(), Address.RELATION_OTHER);

            PersistenceUtil.process(new ContactPersister.UpdateAddressForEntity(convioPersonWrapper.getAddresses()));
            markEditWindow();
        } catch (Exception e) {
            e.printStackTrace();
            profile.setConvioSyncStatus(getOrgId(), profile.getConvioSyncStatus(getOrgId()).getCons_id(),
                    Profile.POTENTIALY_STALE);
            try {
                PersistenceUtil.process(new ProfilePersister.UpdateProfileFunc(profile));
            } finally {
                // not much more we can do at this point
            }
        }
    }

    /**
     * Indicates whether the local and Convio data matched at the time of the poll. Because we possibly update the
     * data,
     * this should not be understand to imply anything about the current state of the data. NULL indicates "don't
     * know",
     * which may mean task hasn't been run or exited abnormally. In this case, the sync status should be kicked over to
     * MANUAL_CHECK, which effectively disconnects SYS and Convio for that object.
     */
    public Boolean getDataDiffers() {
        return dataDiffers;
    }

    private void markEditWindow() {
        if (userId != null) {
            final Connection conn = PersistenceUtil.getConnection();
            try {
                conn.setSavepoint();
                final PreparedStatement deleteStmt =
                        conn.prepareStatement("DELETE FROM personEditWindow " +
                                "WHERE editWindow < NOW() OR (personId=? AND userId=?)");
                final PreparedStatement editStmt =
                        conn.prepareStatement("INSERT INTO personEditWindow (personId, userId, editWindow) " +
                                "VALUES (?,?, NOW() + INTERVAL 15 MINUTE)");
                deleteStmt.setInt(1, profileId);
                deleteStmt.setInt(2, userId);
                deleteStmt.executeUpdate();
                editStmt.setInt(1, profileId);
                editStmt.setInt(2, userId);

                editStmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) { // Nothing much else we can do.
                    e.printStackTrace();
                }
                throw new GeneralException(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) { // Nothing much else we can do.
                    e.printStackTrace();
                }
            }
        }
    }
}
