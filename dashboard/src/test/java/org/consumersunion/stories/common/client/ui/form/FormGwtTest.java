package org.consumersunion.stories.common.client.ui.form;

import java.util.Arrays;
import java.util.List;

import org.consumersunion.stories.common.client.ui.form.controls.CheckBoxInput;
import org.consumersunion.stories.common.client.ui.form.controls.InputGroup;
import org.consumersunion.stories.common.client.ui.form.controls.ListInput;
import org.consumersunion.stories.common.client.ui.form.controls.RadioInput;
import org.consumersunion.stories.common.client.ui.form.controls.TextInput;
import org.consumersunion.stories.common.client.ui.form.validators.EmailValidator;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Label;

public class FormGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.consumersunion.stories.storiesJUnit";
    }

    /**
     * Verify that when we set a field a text field, we are able to correctly
     * retrieve the value.
     */
    public void testGetSetTextInput() {
        Form form = new Form();

        form.add(new TextInput("foo", "foo", false));

        form.setValue("foo", "bar");

        assertEquals("bar", form.getValue("foo"));
    }

    /**
     * Verify that when we set a value in a Radio field, we are able to
     * correctly retrieve the value.
     */
    public void testGetSetRadioInput() {
        Form form = new Form();
        String options[] = {"value1", "value2", "value3"};

        form.add(new RadioInput("radiokey", "radiokey", "radioGroup", options, options, true, null));

        form.setValue("radiokey", "value1");

        assertNotNull("Expected a value but got null", form.getValue("radiokey"));
    }

    /**
     * Verify that when we set a value in a CheckBox field, we are able to
     * correctly retrieve the value.
     */
    public void testGetSetCheckBoxInput() {
        Form form = new Form();
        String[] checkLabels = new String[]{"something"};
        String[] checkValues = new String[]{"true"};
        form.add(new CheckBoxInput("ckeckboxKey", "ckeckboxKey", checkLabels, checkValues, true));

        form.setValue("ckeckboxKey", "true");

        assertNotNull(form.getValue("ckeckboxKey"));
    }

    /**
     * Verify that when we set a value in a List field, we are able to correctly
     * retrieve the value.
     */
    public void testGetSetListInput() {
        Form form = new Form();
        String options[] = {"value1", "value2", "value3"};

        form.add(new ListInput("listKey", "listKey", options, options, true, true, false));

        form.setValue("listKey", "value2");

        assertNotNull(form.getValue("listKey"));
    }

    /**
     * Verify that the min length for a field is the correct value
     */
    public void testMinLength() {
        Form form = new Form();

        form.add(new TextInput("foo", "foo", false));

        form.setValue("foo", "bar");

        form.setMinLength("foo", 10);
        assertFalse("Expected validation to fail.", form.validate());
        assertEquals("Unexpected error message.", "Value must be at least 10 characters long.",
                form.primForm.getRowError("foo"));

        form.setValue("foo", "this is a long value");
        assertTrue("Expected validation to succeed.", form.validate());
    }

    /**
     * Verify that the max length for a field is the correct value
     */
    public void testMaxLength() {
        Form form = new Form();

        form.add(new TextInput("foo", "foo", false));

        form.setValue("foo", "this is a long value");

        form.setMaxLength("foo", 10);
        assertFalse("Expected validation to fail.", form.validate());

        form.setValue("foo", "foo");
        assertTrue("Expected validation to succeed.", form.validate());
    }

    /**
     * Verify that a field is required
     */
    public void testSetRequired() {
        Form form = new Form();

        form.add(new TextInput("foo", "foo", false));

        form.setRequired("foo", true);
        assertFalse("Expected validation to fail.", form.validate());

        form.setRequired("foo", false);
        assertTrue("Expected validation to succeed.", form.validate());
    }

    /**
     * Verify email format checking.
     */
    public void testSetTypeEmail() {
        Form form = new Form();

        form.add(new TextInput("foo", "foo", false));

        form.setValue("foo", "test@acme.com");

        form.setValidator("foo", new EmailValidator());
        assertTrue("Expected validation to succeed.", form.validate());

        form.setValue("foo", "test-acme.com");
        assertFalse("Expected validation to fail.", form.validate());
    }

    /**
     * Get the key of a row in a doubleClickEvent for the Flextable
     */
    public void testGetKeyForEvent() {
        final Form form = new Form();

        form.add(new TextInput("foo", "foo", false));
        form.setValue("foo", "test@acme.com");

        form.primForm.getDataTable().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                String key = form.getKeyForEvent(e);
                assertNotNull(key);
                assertEquals("Same key", "foo", key);
            }
        });

        DomEvent.fireNativeEvent(Document.get().createDblClickEvent(0, 0, 0, 0, 0, false, false, false, false),
                form.primForm.getDataTable());
    }

    public void testAddListMultiVariables() {
        Form form = new Form();
        String options[] = {"STRING_OPTION", "NAME_OPTION", "EMAIL_OPTION", "INTEGER_OPTION", "FLOAT_OPTION",
                "ZIPCODE_OPTION"};
        form.add(new ListInput("testid", "testid", options, options, false, false, true));
        String optionsSelected[] = {"STRING_OPTION", "EMAIL_OPTION", "FLOAT_OPTION"};
        form.setMultiValues("testid", Arrays.asList(optionsSelected));
        List<String> result = form.getMultiValue("testid");
        assertEquals("Same result", optionsSelected.length, result.size());
    }

    public void testAddCheckMultiVariables() {
        Form form = new Form();
        String options[] = {"STRING_OPTION", "NAME_OPTION", "EMAIL_OPTION", "INTEGER_OPTION", "FLOAT_OPTION",
                "ZIPCODE_OPTION"};
        form.add(new CheckBoxInput("testidck", "testidck", options, options, false));
        String optionsSelected[] = {"STRING_OPTION", "EMAIL_OPTION", "FLOAT_OPTION"};
        form.setMultiValues("testidck", Arrays.asList(optionsSelected));
        List<String> result = form.getMultiValue("testidck");
        assertEquals("Same result", result.size(), optionsSelected.length);
    }

    public void testSetMultiValuesException() {
        Form form = new Form();
        String error = "";
        try {
            form.add(new TextInput("foo", "foo", false));
            String optionsSelected[] = {"STRING_OPTION", "EMAIL_OPTION", "FLOAT_OPTION"};
            form.setMultiValues("testidck", Arrays.asList(optionsSelected));
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertEquals("Throws exception", "Invalid key for this method.", error);
    }

    public void testMoveRow() {
        Form form = new Form();
        form.add(new TextInput("0", "0", false));
        form.add(new TextInput("1", "1", false));
        form.add(new TextInput("2", "2", false));
        form.add(new TextInput("3", "3", false));
        form.add(new TextInput("4", "4", false));
        assertEquals("Expected Result", "0,1,2,3,4", getRowOrder(form));
        form.moveRow(0, 5);
        assertEquals("Expected Result", "1,2,3,4,0", getRowOrder(form));
        form.moveRow(4, 0);
        assertEquals("Expected Result", "0,1,2,3,4", getRowOrder(form));
        form.moveRow(0, 4);
        assertEquals("Expected Result", "1,2,3,0,4", getRowOrder(form));
        form.moveRow(3, 0);
        assertEquals("Expected Result", "0,1,2,3,4", getRowOrder(form));
    }

    public String getRowOrder(Form form) {
        Label label0 = (Label) ((InputGroup) form.primForm.getDataTable().getWidget(0, 0)).getWidget(0);
        Label label1 = (Label) ((InputGroup) form.primForm.getDataTable().getWidget(1, 0)).getWidget(0);
        Label label2 = (Label) ((InputGroup) form.primForm.getDataTable().getWidget(2, 0)).getWidget(0);
        Label label3 = (Label) ((InputGroup) form.primForm.getDataTable().getWidget(3, 0)).getWidget(0);
        Label label4 = (Label) ((InputGroup) form.primForm.getDataTable().getWidget(4, 0)).getWidget(0);
        return label0.getText() + "," + label1.getText() + "," + label2.getText() + "," + label3.getText() + ","
                + label4.getText();
    }
}
