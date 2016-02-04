package org.consumersunion.stories.common.client.ui.form;

import org.consumersunion.stories.common.client.ui.form.controls.TextInput;

import com.google.gwt.junit.client.GWTTestCase;

public class I18nFormGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.consumersunion.stories.storiesJUnitI18nEs";
    }

    public void testMinLength() {
        Form form = new Form();

        form.add(new TextInput("foo", "foo", false));

        form.setValue("foo", "bar");

        form.setMinLength("foo", 10);
        assertFalse("Expected validation to fail.", form.validate());
        assertEquals("Unexpected error message.", "El valor debe ser al menos 10 de largo.",
                form.primForm.getRowError("foo"));
    }
}
