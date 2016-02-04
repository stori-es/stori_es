package org.consumersunion.stories.common.server.model;

import java.util.Date;

public class Verification {
    private int entityId;
    private String email;
    private String nonce;
    private Date created;

    public Verification(int entityId, String email, String nonce) {
        this(entityId, email, nonce, null);
    }

    public Verification(int entityId, String email, String nonce, Date created) {
        this.entityId = entityId;
        this.email = email;
        this.nonce = nonce;
        this.created = created;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
