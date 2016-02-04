package org.consumersunion.stories.server.util;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfigurator {
    @Bean
    public EmailValidator emailValidator() {
        return EmailValidator.getInstance();
    }
}
