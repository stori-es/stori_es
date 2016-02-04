package org.consumersunion.stories.common.shared.dto;

import java.io.Serializable;

import org.consumersunion.stories.common.shared.model.entity.SortField;

public abstract class AuthParam<T extends AuthParam<T>> implements Serializable {
    private int start;
    private int length;
    private SortField sortField;
    private boolean ascending;
    private Integer effectiveId;
    private int authRelation;
    private Integer subject;

    public AuthParam(int start,
            int length,
            SortField sortField,
            boolean ascending,
            int authRelation,
            Integer effectiveId) {
        this(start, length, sortField, ascending, authRelation, effectiveId, null);
    }

    public AuthParam(int start,
            int length,
            SortField sortField,
            boolean ascending,
            int authRelation,
            Integer effectiveId,
            Integer subject) {
        this.start = start;
        this.length = length;
        this.sortField = sortField;
        this.ascending = ascending;
        this.authRelation = authRelation;
        this.effectiveId = effectiveId;
        this.subject = subject;
    }

    protected AuthParam() {
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public SortField getSortField() {
        return sortField;
    }

    public boolean isAscending() {
        return ascending;
    }

    public abstract T noLimit();

    public int getAuthRelation() {
        return authRelation;
    }

    public Integer getEffectiveId() {
        return effectiveId;
    }

    public void setAuthRelation(int authRelation) {
        this.authRelation = authRelation;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setSortField(SortField sortField) {
        this.sortField = sortField;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public void setEffectiveId(Integer effectiveId) {
        this.effectiveId = effectiveId;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }
}
