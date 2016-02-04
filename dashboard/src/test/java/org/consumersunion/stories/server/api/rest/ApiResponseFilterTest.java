package org.consumersunion.stories.server.api.rest;

import java.net.URI;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.consumersunion.stories.common.shared.dto.ApiResponse;
import org.consumersunion.stories.common.shared.dto.Metadata;
import org.consumersunion.stories.common.shared.dto.ResponseStatus;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ApiResponseFilterTest {
    private ApiResponseFilter filter;

    @Before
    public void setUp() {
        filter = new ApiResponseFilter();
    }

    @Test
    public void filter_emptyMetadata_createsMetadata() throws Exception {
        ApiResponse apiResponse = new MockApiResponse();
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        UriInfo uriInfo = mock(UriInfo.class);
        String uri = "http://www.uri.com";
        given(requestContext.getUriInfo()).willReturn(uriInfo);
        given(uriInfo.getAbsolutePath()).willReturn(URI.create(uri));

        int statusCode = 500;
        ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
        Response.StatusType statusType = mock(Response.StatusType.class);
        given(responseContext.getEntity()).willReturn(apiResponse);
        given(responseContext.getStatusInfo()).willReturn(statusType);
        given(statusType.getStatusCode()).willReturn(statusCode);

        filter.filter(requestContext, responseContext);

        Metadata metadata = apiResponse.getMetadata();
        assertThat(metadata).isNotNull();
        assertThat(metadata.getSelf()).isEqualTo(uri);
        assertThat(metadata.getHttpCode()).isEqualTo(statusCode);
        assertThat(metadata.getStatus()).isEqualTo(ResponseStatus.ERROR);
    }

    private static class MockApiResponse implements ApiResponse {
        private Metadata metadata;
        private List data;

        @Override
        public Metadata getMetadata() {
            return metadata;
        }

        @Override
        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }

        @Override
        public List getData() {
            return data;
        }

        @Override
        public void setData(List data) {
            this.data = data;
        }
    }
}
