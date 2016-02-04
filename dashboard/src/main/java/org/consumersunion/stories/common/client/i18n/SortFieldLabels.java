package org.consumersunion.stories.common.client.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface SortFieldLabels extends ConstantsWithLookup {
    @DefaultStringValue("Title (A to Z)")
    String TITLE_A_Z();

    @DefaultStringValue("Title (Z to A)")
    String TITLE_Z_A();

    @DefaultStringValue("Created (Oldest)")
    String CREATED_OLD();

    @DefaultStringValue("Created (Newest)")
    String CREATED_NEW();

    @DefaultStringValue("Modified (Oldest)")
    String MODIFIED_OLD();

    @DefaultStringValue("Modified (Newest)")
    String MODIFIED_NEW();

    @DefaultStringValue("Author")
    String STORYTELLER();

    @DefaultStringValue("City")
    String CITY();

    @DefaultStringValue("State")
    String STATE();
}
