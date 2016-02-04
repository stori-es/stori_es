package org.consumersunion.stories.server.api.rest.mapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.springframework.stereotype.Component;

@Provider
@Component
public class UnsupportedOperationExceptionMapper implements ExceptionMapper<UnsupportedOperationException> {
    private static final Logger LOGGER = Logger.getLogger(UnsupportedOperationExceptionMapper.class.getName());

    @Override
    public Response toResponse(UnsupportedOperationException exception) {
        // Java7 supports 'NOT_IMPLEMENTED', but 6 does not, so until everything gets upgraded:
        //return Response.status(Response.Status.NOT_IMPLEMENTED)

        LOGGER.log(Level.SEVERE, exception.getMessage(), exception);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorApiResponse(exception.getMessage()))
                .build();
    }
}
