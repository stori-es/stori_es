package org.consumersunion.stories.common.shared.model;

import java.util.Date;

public class ResetPassword {
    public static final int NB_HOURS = 6;

    private String handle;
    private String nonce;
    private Date created;

    public ResetPassword(String handle, String nonce) {
        this(handle, nonce, null);
    }

    public ResetPassword(String handle, String nonce, Date created) {
        this.handle = handle;
        this.nonce = nonce;
        this.created = created;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
