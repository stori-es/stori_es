package org.consumersunion.stories.common.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface MultipleChoiceFormatLabels extends ConstantsWithLookup {
    @DefaultStringValue("Single Selection (Radio Buttons)")
    String RADIO_BUTTONS();

    @DefaultStringValue("Single Selection (Dropdown List)")
    String SELECT_SINGLE();

    @DefaultStringValue("Multiple Selection (List)")
    String SELECT_MULTIPLE();

    @DefaultStringValue("Multiple Selection (Checkboxes)")
    String CHECKBOXES();
}
