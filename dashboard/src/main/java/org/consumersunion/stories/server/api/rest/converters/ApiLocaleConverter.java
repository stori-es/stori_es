package org.consumersunion.stories.server.api.rest.converters;

import org.consumersunion.stories.common.shared.dto.ApiLocale;
import org.consumersunion.stories.common.shared.model.Locale;
import org.springframework.stereotype.Component;

import com.google.common.base.Converter;

@Component
public class ApiLocaleConverter extends Converter<ApiLocale, Locale> {
    @Override
    protected Locale doForward(ApiLocale apiLocale) {
        switch (apiLocale) {
            case EN_US:
                return Locale.ENGLISH;
            case ES_US:
                return Locale.SPANISH;
        }

        throw new IllegalArgumentException();
    }

    @Override
    protected ApiLocale doBackward(Locale locale) {
        switch (locale) {
            case ENGLISH:
                return ApiLocale.EN_US;
            case SPANISH:
                return ApiLocale.ES_US;
        }

        throw new IllegalArgumentException();
    }
}
