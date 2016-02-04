package org.consumersunion.stories.server.api.rest.mapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;

@Provider
@Component
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {
    private static final Logger LOGGER = Logger.getLogger(JsonParseExceptionMapper.class.getName());

    @Override
    public Response toResponse(JsonParseException exception) {
        LOGGER.log(Level.SEVERE, exception.getMessage(), exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorApiResponse("The request is malformed"))
                .build();
    }
}
