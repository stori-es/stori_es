package org.consumersunion.stories.common.client.resource;

import com.google.gwt.i18n.client.Constants;

public interface RichTextToolbarStrings extends Constants {
    @DefaultStringValue("Toggle Bold")
    String bold();

    @DefaultStringValue("Create Link")
    String createLink();

    @DefaultStringValue("Toggle Italic")
    String italic();

    @DefaultStringValue("Toggle Strike Through")
    String strikeThrough();

    @DefaultStringValue("Insert Ordered List")
    String ol();

    @DefaultStringValue("Remove Formatting")
    String removeFormat();

    @DefaultStringValue("Remove Link")
    String removeLink();

    @DefaultStringValue("Insert Unordered List")
    String ul();

    @DefaultStringValue("Toggle Underline")
    String underline();

    @DefaultStringValue("Left indent")
    String leftIndent();

    @DefaultStringValue("Right indent")
    String rightIndent();
}
