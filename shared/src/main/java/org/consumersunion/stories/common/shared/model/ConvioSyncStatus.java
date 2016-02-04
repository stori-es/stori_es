package org.consumersunion.stories.common.shared.model;

import java.io.Serializable;

/**
 * @see #getConvioSyncStatus
 */
public class ConvioSyncStatus implements Serializable {
    private Integer cons_id;
    private String convioSyncStatus;

    public ConvioSyncStatus() {
    } // needed by GWT serialization

    public ConvioSyncStatus(Integer cons_id, String convioSyncStatus) {
        this.cons_id = cons_id;
        this.convioSyncStatus = convioSyncStatus;
    }

    public Integer getCons_id() {
        return cons_id;
    }

    public String getConvioSyncStatus() {
        return convioSyncStatus;
    }
}
