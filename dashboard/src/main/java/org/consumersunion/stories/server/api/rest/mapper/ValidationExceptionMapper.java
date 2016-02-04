package org.consumersunion.stories.server.api.rest.mapper;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.consumersunion.stories.common.shared.dto.Message;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ResteasyViolationException;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

@Provider
@Component
public class ValidationExceptionMapper implements ExceptionMapper<ResteasyViolationException> {
    private static final Logger LOGGER = Logger.getLogger(ValidationExceptionMapper.class.getName());

    @Override
    public Response toResponse(ResteasyViolationException exception) {
        LOGGER.log(Level.SEVERE, exception.getMessage(), exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorApiResponse(createMessages(exception.getViolations())))
                .build();
    }

    private List<Message> createMessages(List<ResteasyConstraintViolation> violations) {
        return FluentIterable.from(violations)
                .transform(new Function<ResteasyConstraintViolation, Message>() {
                    @Override
                    public Message apply(ResteasyConstraintViolation input) {
                        return new Message(input.getMessage(), "");
                    }
                }).toList();
    }
}
