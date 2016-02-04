package org.consumersunion.stories.common.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface RatingLabels extends ConstantsWithLookup {
    @DefaultStringValue("Numbers")
    String NUMBERS();

    @DefaultStringValue("Stars")
    String STARS();

    @DefaultStringValue("Discrete")
    String DISCRETE();

    @DefaultStringValue("Half-step")
    String HALF_STEP();
}
