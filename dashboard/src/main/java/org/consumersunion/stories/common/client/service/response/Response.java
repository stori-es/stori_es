package org.consumersunion.stories.common.client.service.response;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class Response implements IsSerializable {

    private List<String> globalErrorMessages;
    private Map<String, String> fieldErrorMessages;
    private boolean loggedIn;
    private Boolean showErrorMessages;

    protected Response() {
        loggedIn = true;
        showErrorMessages = false;
        setGlobalErrorMessages(new LinkedList<String>());
        setFieldErrorMessages(new HashMap<String, String>());
    }

    /**
     * Copy constructor used to transfer error messages form one response to
     * another. This is useful when one service calls another and the response
     * types don't match exactly.
     */
    public Response(Response template) {
        loggedIn = true;
        showErrorMessages = false;
        setGlobalErrorMessages(template.getGlobalErrorMessages());
        setFieldErrorMessages(template.getFieldErrorMessages());
    }

    public void addGlobalErrorMessage(final String message) {
        this.globalErrorMessages.add(message);
    }

    public List<String> getGlobalErrorMessages() {
        return this.globalErrorMessages;
    }

    public Set<String> getFieldErrorKeys() {
        return fieldErrorMessages.keySet();
    }

    public Map<String, String> getFieldErrorMessages() {
        return fieldErrorMessages;
    }

    public String getFieldErrorMessage(final String key) {
        return fieldErrorMessages.get(key);
    }

    public void setGlobalErrorMessages(final List<String> globalErrorMessages) {
        this.globalErrorMessages = globalErrorMessages;
    }

    public void setFieldErrorMessages(final Map<String, String> fieldErrorMessages) {
        this.fieldErrorMessages = fieldErrorMessages;
    }

    public boolean isError() {
        return globalErrorMessages.size() > 0 || fieldErrorMessages.size() > 0;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Boolean getShowErrorMessages() {
        return showErrorMessages;
    }

    public void setShowErrorMessages(Boolean showErrorMessages) {
        this.showErrorMessages = showErrorMessages;
    }
}
