package org.consumersunion.stories.common.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface TextBoxFormatLabels extends ConstantsWithLookup {
    @DefaultStringValue("Single Line")
    String SINGLE_LINE();

    @DefaultStringValue("Multiple Lines (Plain Text)")
    String PLAIN_TEXT();

    @DefaultStringValue("Multiple Lines (Rich Text)")
    String RICH_TEXT();
}
