package org.consumersunion.stories.common.server.service.datatransferobject;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConvioNameObject implements IsSerializable {
    private String middle;
    private String last;
    private String prof_suffix;
    private String suffix;
    private String title;
    private String first;

    public ConvioNameObject() {
    }

    public ConvioNameObject(String first, String last) {
        this.first = first;
        this.last = last;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getProf_suffix() {
        return prof_suffix;
    }

    public void setProf_suffix(String prof_suffix) {
        this.prof_suffix = prof_suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }
}
