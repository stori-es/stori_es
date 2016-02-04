package org.consumersunion.stories.common.shared.api;

import org.consumersunion.stories.common.shared.model.SystemEntity;

public class ApiKey extends SystemEntity {
    public static class Builder {
        private Integer user;
        private String uuid;
        private String uuidBin;

        public Builder user(Integer user) {
            this.user = user;
            return this;
        }

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder uuidBin(String uuidBin) {
            this.uuidBin = uuidBin;
            return this;
        }

        public ApiKey create() {
            return new ApiKey(user, uuid, uuidBin);
        }
    }

    private Integer user;
    private String uuid;
    private String uuidBin;

    @SuppressWarnings("UnusedDeclaration" /* For serialization. */)
    public ApiKey() {
    }

    public ApiKey(
            Integer user,
            String uuid,
            String uuidBin) {
        this.user = user;
        this.uuid = uuid;
        this.uuidBin = uuidBin;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuidBin() {
        return uuidBin;
    }

    public void setUuidBin(String uuidBin) {
        this.uuidBin = uuidBin;
    }
}
