package org.consumersunion.stories.server.export;

import java.util.Date;

public abstract class BaseStoryExportCsv {
    private final String id;
    private final Date createdDate;
    private final Date modifiedDate;

    public BaseStoryExportCsv(
            int id,
            Date createdDate,
            Date modifiedDate) {
        this.id = String.valueOf(id);
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public String getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }
}
