package org.consumersunion.stories.common.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface LocaleLabels extends ConstantsWithLookup {
    @DefaultStringValue("")
    String UNKNOWN();

    @DefaultStringValue("English (en)")
    String ENGLISH();

    @DefaultStringValue("Spanish - Español (es)")
    String SPANISH();
}
