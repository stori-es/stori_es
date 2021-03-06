package org.consumersunion.stories.server.api.rest.mapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Provider
@Component
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    private static final Logger LOGGER = Logger.getLogger(NotFoundExceptionMapper.class.getName());

    @Override
    public Response toResponse(NotFoundException exception) {
        LOGGER.log(Level.SEVERE, exception.getMessage(), exception);

        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorApiResponse(exception.getMessage()))
                .build();
    }
}
