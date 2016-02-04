package org.consumersunion.stories.common.server.service.datatransferobject;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConvioEmailObject implements IsSerializable {

    private String primary_address;
    private String secondary_address;
    private String hard_bounce_count;
    private String accepts_email;
    private String status;
    private String preferred_format;

    public String getPrimary_address() {
        return primary_address;
    }

    public void setPrimary_address(String primary_address) {
        this.primary_address = primary_address;
    }

    public String getSecondary_address() {
        return secondary_address;
    }

    public void setSecondary_address(String secondary_address) {
        this.secondary_address = secondary_address;
    }

    public String getHard_bounce_count() {
        return hard_bounce_count;
    }

    public void setHard_bounce_count(String hard_bounce_count) {
        this.hard_bounce_count = hard_bounce_count;
    }

    public String getAccepts_email() {
        return accepts_email;
    }

    public void setAccepts_email(String accepts_email) {
        this.accepts_email = accepts_email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreferred_format() {
        return preferred_format;
    }

    public void setPreferred_format(String preferred_format) {
        this.preferred_format = preferred_format;
    }
}
