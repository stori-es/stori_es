package org.consumersunion.stories.server.api.rest.mapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.consumersunion.stories.server.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Provider
@Component
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    private static final Logger LOGGER = Logger.getLogger(BadRequestExceptionMapper.class.getName());

    @Override
    public Response toResponse(BadRequestException exception) {
        LOGGER.log(Level.SEVERE, exception.getMessage(), exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorApiResponse(exception.getMessage()))
                .build();
    }
}
