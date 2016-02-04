package org.consumersunion.stories.server.rest.api.convio;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.consumersunion.stories.common.server.service.datatransferobject.ConvioConstituent;
import org.consumersunion.stories.common.shared.model.Organization;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class PollConvioOnly extends ConvioSyncTask {
    private String primaryEmail = null;

    protected ConvioConstituentResponse convioConstituentResponse;

    public PollConvioOnly(String primaryEmail, int orgId) {
        super(orgId);
        this.primaryEmail = primaryEmail;
    }

    public void synchronize() {
        Organization syncOrg = getSyncOrg();

        String getUserResource = getConvioEndpoint(syncOrg) + "?method=getUser&" + getSecurityChain(
                syncOrg) + "&" + RESPONSE_FORMAT;
        getUserResource += "&primary_email=" + primaryEmail;

        // Again, if we want to do the poll, but can't that puts us into the potentially stale state.
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            getRestTemplate().setErrorHandler(new ResponseErrorHandler() {
                private boolean hasError = false;

                @Override
                public void handleError(ClientHttpResponse arg0)
                        throws IOException {
                    hasError = true;
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(arg0.getBody(), writer);
                }

                @Override
                public boolean hasError(ClientHttpResponse arg0)
                        throws IOException {
                    return hasError;
                }
            });

            String jsonString = getRestTemplate().postForObject(getUserResource, null, String.class);
            convioConstituentResponse = objectMapper.readValue(jsonString, ConvioConstituentResponse.class);
        } catch (Exception e) {
            processCommunicationException(e);
        }
    }

    public ConvioConstituent getConvioConstituent() {
        if (convioConstituentResponse == null) {
            return null;
        } else {
            return convioConstituentResponse.getGetConsResponse();
        }
    }

    public static class ConvioConstituentResponse extends ConvioResponse {
        private ConvioConstituent getConsResponse;

        public ConvioConstituent getGetConsResponse() {
            return getConsResponse;
        }

        public void setGetConsResponse(ConvioConstituent getConsResponse) {
            this.getConsResponse = getConsResponse;
        }
    }
}
