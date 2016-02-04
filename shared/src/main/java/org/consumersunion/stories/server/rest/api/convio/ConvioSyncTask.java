package org.consumersunion.stories.server.rest.api.convio;

import org.codehaus.jackson.map.ObjectMapper;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.server.persistence.OrganizationPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public abstract class ConvioSyncTask {
    protected static final String RESPONSE_FORMAT = "response_format=json";

    public RestTemplate restTemplate;
    public ObjectMapper objectMapper;

    private final int orgId;

    private Exception communicationException;
    private String errorMessage;
    private String convioErrorCode;

    protected ConvioSyncTask(int orgId) {
        this.orgId = orgId;
    }

    public int getOrgId() {
        return orgId;
    }

    public abstract void synchronize();

    protected Organization getSyncOrg() {
        return PersistenceUtil.process(new OrganizationPersister.RetrieveOrganizationFunc(getOrgId()));
    }

    protected String getSecurityChain(Organization syncOrg) {
        String securityChain = "api_key=" + syncOrg.getCrmApiKey() +
                "&login_name=" + syncOrg.getCrmApiLogin() + "&v=1.0";
        // Given password value for CU calls (A more secure alternative is needed for the other partners)
        if (syncOrg.getId() == 2) {
            securityChain += "&login_password=Pass1word";
        }

        return securityChain;
    }

    protected String getConvioEndpoint(Organization syncOrg) {
        return syncOrg.getCrmEndpoint();
    }

    protected void processCommunicationException(Exception exception) {
        exception.printStackTrace();
        this.setCommunicationException(exception);

        // Notice processing the ConvioErrorResponse can itself generate an exception. We want to handle that case the
        // same as we handle other exceptions so we use this handled variable to allow us to do that.
        boolean handled = false;
        // The Spring REST stuff uses special exceptions to indicate different web failures. This one is specific to
        // 4xx request statti. This may be significant per the Convio spec, but I haven't actually checked (and did not
        // write original code but wanted to add this note as the meaning of the if/then branch is not immediately
        // clear).
        if (exception instanceof HttpClientErrorException) {
            try {
                this.communicationException = exception;
                objectMapper.readValue(((HttpClientErrorException) exception).getResponseBodyAsByteArray(),
                        ConvioErrorResponse.class);
                handled = true;
            } catch (Exception e) {
            } // and let fall through to the general exception handler
        }
        if (!handled) {
            this.setErrorMessage("Non-400 exception while processing convio access.");
        }
    }

    /**
     * Returns true if no evidence of an error can be found.
     */
    public boolean isServiceSuccess() {
        // In future, we might tighten this up... or not. It is a little loosey goosey, but I don't see any harm either.
        return this.communicationException == null && this.errorMessage == null && this.convioErrorCode == null;
    }

    public Exception getCommunicationException() {
        return communicationException;
    }

    public void setCommunicationException(Exception communicationsException) {
        this.communicationException = communicationsException;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getConvioErrorCode() {
        return convioErrorCode;
    }

    public void setConvioErrorCode(String convioErrorCode) {
        this.convioErrorCode = convioErrorCode;
    }

    protected RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        return restTemplate;
    }

    public static class ConvioResponse {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ConvioErrorResponse extends ConvioResponse {
        private String code;
        private Exception exception;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }
    }
}
