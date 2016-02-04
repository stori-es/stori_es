package org.consumersunion.stories.server.api.rest.mapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.springframework.stereotype.Component;

@Provider
@Component
public class NotLoggedInExceptionMapper implements ExceptionMapper<NotLoggedInException> {
    private static final Logger LOGGER = Logger.getLogger(NotLoggedInExceptionMapper.class.getName());

    @Override
    public Response toResponse(NotLoggedInException exception) {
        LOGGER.log(Level.SEVERE, exception.getMessage(), exception);

        return Response.status(Status.UNAUTHORIZED)
                .entity(new ErrorApiResponse(exception.getMessage()))
                .build();
    }
}
