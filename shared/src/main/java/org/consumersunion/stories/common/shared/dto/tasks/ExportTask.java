package org.consumersunion.stories.common.shared.dto.tasks;

import java.util.Date;

import org.consumersunion.stories.common.shared.ExportContainer;
import org.consumersunion.stories.common.shared.ExportKind;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

@JsonTypeName("export")
public class ExportTask extends Task {
    private ExportKind kind;
    private ExportContainer container;
    private int objectId;
    private String url;
    private Date expires;

    @AssistedInject
    public ExportTask(
            @Assisted("profileId") int profileId,
            @Assisted ExportKind kind,
            @Assisted ExportContainer container,
            @Assisted("objectId") int objectId) {
        super(profileId, TaskType.EXPORT);

        this.kind = kind;
        this.container = container;
        this.objectId = objectId;
    }

    public ExportTask() {
    }

    public ExportKind getKind() {
        return kind;
    }

    public void setKind(ExportKind kind) {
        this.kind = kind;
    }

    public ExportContainer getContainer() {
        return container;
    }

    public void setContainer(ExportContainer container) {
        this.container = container;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
