package org.consumersunion.stories.common.shared.dto.tasks;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@type",
        defaultImpl = ExportTask.class)
@JsonSubTypes({
        @JsonSubTypes.Type(ExportTask.class),
        @JsonSubTypes.Type(AddStoriesToCollectionTask.class)})
public abstract class Task {
    private int id;
    private int profileId;
    private TaskStatus status;
    private TaskType taskType;
    private int count;
    private int total;
    private Date created;
    private Date modified;

    public Task(
            int profileId,
            TaskType taskType) {
        status = TaskStatus.PENDING;
        this.profileId = profileId;
        this.taskType = taskType;
    }

    protected Task() {
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @JsonIgnore
    public boolean isDone() {
        return TaskStatus.DONE.equals(status);
    }

    @JsonIgnore
    public boolean isCanceling() {
        return TaskStatus.CANCELING.equals(status);
    }

    @JsonIgnore
    public boolean isCanceled() {
        return TaskStatus.CANCELED.equals(status);
    }
}
