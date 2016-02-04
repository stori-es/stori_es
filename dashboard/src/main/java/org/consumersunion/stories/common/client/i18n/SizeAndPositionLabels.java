package org.consumersunion.stories.common.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface SizeAndPositionLabels extends ConstantsWithLookup {
    @DefaultStringValue("Small")
    String SMALL();

    @DefaultStringValue("Medium")
    String MEDIUM();

    @DefaultStringValue("Large")
    String LARGE();

    @DefaultStringValue("Left")
    String LEFT();

    @DefaultStringValue("Center")
    String CENTER();

    @DefaultStringValue("Right")
    String RIGHT();
}
