package org.consumersunion.stories.common.client.ui.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.common.client.service.response.Response;
import org.consumersunion.stories.common.client.ui.TooltipBaloonPanel;
import org.consumersunion.stories.common.client.ui.form.controls.InputGroup;
import org.consumersunion.stories.common.client.ui.form.controls.RadioInput;
import org.consumersunion.stories.common.shared.service.GeneralException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class Form extends VerticalPanel {
    public static final boolean REQUIRED = true;
    public static final boolean OPTIONAL = false;

    @Inject
    private static CommonResources commonResources;

    public final PrimitiveTableInterface primForm = new PrimitiveTableInterface();
    private final FlexTable dataTable = new FlexTable();

    /**
     * Maps the input widgets to the panel containing the widget, where the
     * error message is added.
     */
    final private Map<String, VerticalPanel> messageMap = new HashMap<String, VerticalPanel>();
    final private Map<String, Integer> rowMap = new HashMap<String, Integer>();
    final private Map<String, UiFormControl> controlsMap = new HashMap<String, UiFormControl>();

    private int currentRow = 0;

    // would like to make final and take in constructor, but that's incompatible
    // with UiBinder
    private String inputPrefix;
    private HorizontalPanel horizontalSet;

    public Form() {
        // if Form is used from the questionnaire module, the Gin stuff doesn't
        // get injected, so commonResources might
        // be null
        if (commonResources != null) {
            commonResources.generalStyleCss().ensureInjected();
        } else {
            commonResources = GWT.create(CommonResources.class);
            commonResources.generalStyleCss().ensureInjected();
        }

        dataTable.getElement().addClassName("dataTable");
        super.add(dataTable);
    }

    @SuppressWarnings("unchecked")
    public <T extends UiFormControl> T add(final T fc) {
        if (fc instanceof RadioInput) {
            return (T) this.addFormControl((RadioInput) fc);
        } else {
            return this.addFormControl(fc);
        }
    }

    private <T extends UiFormControl> T addFormControl(final T control) {
        final boolean required = control.isRequired();
        final String labelText = control.getLabel();
        final String key = control.getKey();
        final String rowClass = control.getRowClass();
        controlsMap.put(key, control);
        return primForm.addStandardRow(new HTML(required ? labelText.equals("") ? labelText : labelText + " * "
                : labelText), key, required, control, rowClass);
    }

    private RadioInput addFormControl(final RadioInput radioInput) {
        final String groupName = inputPrefix + radioInput.getGroupName();
        radioInput.setGroupName(groupName);

        final String key = radioInput.getKey();
        final boolean required = radioInput.isRequired();
        final String label = radioInput.getLabel();

        controlsMap.put(key, radioInput);

        primForm.addStandardRow(new Label(required ? label + " * " : label), key, required, radioInput,
                "stories-radioGroup");
        return radioInput;
    }

    public void addSubWidget(final int colSpan, final Widget widget) {
        dataTable.setWidget(currentRow, 0, widget);
        dataTable.getFlexCellFormatter().setColSpan(currentRow, 0, colSpan);
        currentRow += 1;
    }

    public void setValidator(final String key, final Validator validator) {
        final UiFormControl c = controlsMap.get(key);
        if (c != null) {
            c.setValidator(validator);
        }
    }

    public void setMaxLength(final String key, final int length) {
        final UiFormControl c = controlsMap.get(key);
        if (c != null) {
            c.setMaxLength(length);
        }
    }

    public void setMinLength(final String key, final int length) {
        final UiFormControl c = controlsMap.get(key);
        if (c != null) {
            c.setMinLength(length);
        }
    }

    public void decorateLastWithHelp(final String title, final String text) {
        // grab the label
        final Widget label = dataTable.getWidget(currentRow - 1, 0);
        label.getElement().addClassName("cu-helpPanel");
        // no we'll put the label together with the help anchor in in a
        // horizontal
        // panel and set that as the widget for the cell
        final HorizontalPanel hp = new HorizontalPanel();
        // this is a workaround while we still have a non-GWTP, so we have to
        // support the old way for now
        final Widget helpWidget;
        if (commonResources != null) {
            helpWidget = new Image(commonResources.questionMark());
        } else {
            final String contextPath = RootPanel.getBodyElement().getAttribute("data-context-path");
            helpWidget = new Anchor("<img src=\"" + contextPath + "/images/question_mark_icon_16x16.png\" />", true);
            ((Anchor) helpWidget).setHref("#");
        }

        final TooltipBaloonPanel baloon = new TooltipBaloonPanel(text, title, false);
        ((HasMouseOverHandlers) helpWidget).addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(final MouseOverEvent arg0) {
                baloon.show(helpWidget.getAbsoluteLeft(), helpWidget.getAbsoluteTop()
                        - (helpWidget.getOffsetHeight() * 2));
            }
        });
        ((HasClickHandlers) helpWidget).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent arg0) {
                baloon.show(helpWidget.getAbsoluteLeft(), helpWidget.getAbsoluteTop()
                        - (helpWidget.getOffsetHeight() * 2));
            }
        });
        ((HasMouseOutHandlers) helpWidget).addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(final MouseOutEvent arg0) {
                if (baloon.isShowing() && baloon.isVisible()) {
                    baloon.hidePopupHelp();
                }
            }
        });
        hp.getElement().addClassName("cu-helpPanel");
        hp.add(label);
        hp.add(helpWidget);
        dataTable.setWidget(currentRow - 1, 0, hp);
    }

    /**
     * Checks all natively supported validations based on field requirement
     * status and additional constraints. The method will report error messages
     * (within the form table) as a side effect of this call.
     *
     * @return true if valid, false otherwise
     */
    public boolean validate() {
        boolean success = true;

        for (final String key : controlsMap.keySet()) {
            primForm.clearErrorMessage(key);
        }

        for (final UiFormControl control : controlsMap.values()) {
            try {
                control.validate();
            } catch (final InputValidationException e) {
                primForm.addRowError(control.getKey(), e.getMessage());
                success = false;
            }
        }

        // what's this weirdness? Well, to get collapse borders in firefox (3.0
        // at least), it's
        // necessary to blink separate borders for some crazy reason; otherwise
        // we get no
        // borders at all
        if (!success) {
            dataTable.getElement().setAttribute("style", "border-collapse: separate");
            new Timer() {
                @Override
                public void run() {
                    dataTable.getElement().removeAttribute("style");
                }
            }.schedule(200);
        }

        return success;
    }

    public void setValue(final String key, final String value) {
        final UiFormControl c = controlsMap.get(key);
        if (c == null) {
            return;
        } else if (c instanceof InputFormControl) {
            final List<String> values = new ArrayList<String>();
            values.add(value);
            setMultiValues(key, values);
        }
    }

    public String getValue(final String key) {
        final UiFormControl c = controlsMap.get(key);

        if (c instanceof SingleValueFormControl) {
            return ((SingleValueFormControl) c).getValue();
        } else if (c instanceof InputFormControl) {
            return getFirstValue((InputFormControl) c);
        }

        return null;
    }

    private String getFirstValue(final InputFormControl c) {
        final List<String> values = getMultiValue(c);

        if (values.size() > 0) {
            return values.get(0);
        } else {
            return "";
        }
    }

    public List<String> getMultiValue(final String key) {
        final UiFormControl control = controlsMap.get(key);

        if (control instanceof InputFormControl) {
            final InputFormControl c = (InputFormControl) control;
            return getMultiValue(c);
        } else {
            return Collections.emptyList();
        }
    }

    private List<String> getMultiValue(final InputFormControl c) {
        if (c != null) {
            return c.getValues();
        }
        return Collections.emptyList();
    }

    public void setMultiValues(final String key, final List<String> values) {
        final UiFormControl control = controlsMap.get(key);

        if (control == null) {
            throw new GeneralException("Invalid key for this method.");
        } else if (control instanceof InputFormControl) {
            ((InputFormControl) control).setValues(values);
        }
    }

    public void setEnabled(final boolean enabled) {
        for (final String key : controlsMap.keySet()) {
            setEnabled(key, enabled);
        }
    }

    public void setEnabled(final String key, final boolean enabled) {
        final UiFormControl c = controlsMap.get(key);
        c.setEnabled(enabled);
    }

    public boolean hasField(final String key) {
        return controlsMap.containsKey(key);
    }

    /**
     * The input prefix is used in cases where similar data tables (i.e., built
     * by the same code) will exist in the DOM at the same time. The input
     * prefix is necessary to keep radio buttons ('grouped by name' inputs)
     * functioning properly.
     * <p/>
     * If the desire is to have the multiple groups connected, then leave the
     * prefix blank (or set to same value). Otherwise, set a prefix for each
     * version in order to distinguish.
     * <p/>
     * The prefix is NOT used for the internal values. I.e., the 'getValue'
     * functions will use the un-prefixed names.
     */
    public String getInputPrefix() {
        return inputPrefix;
    }

    /**
     * @see #getInputPrefix
     */
    // would like to take in constructor, but not compatible with UiBinder
    public void setInputPrefix(final String inputPrefix) {
        this.inputPrefix = inputPrefix;
    }

    public void setRequired(final String key, final boolean required) {
        // TODO: Update the label to display "*"
        final UiFormControl c = controlsMap.get(key);
        if (c != null) {
            c.setRequired(required);
        }
    }

    @Override
    public void clear() {
        dataTable.clear();
    }

    public void clearRowMap() {
        rowMap.clear();
    }

    public String getKeyForEvent(final ClickEvent e) {
        // see SYSTWO-289
        final int row = primForm.getDataTable().getCellForEvent(e).getRowIndex();

        return getKeyForRowIndex(row);
    }

    public String getKeyForRowIndex(final int row) {
        for (final String key : rowMap.keySet()) {
            if (rowMap.get(key) == row) {
                return key;
            }
        }

        return null;
    }

    public void startHorizontalSet() {
        horizontalSet = new HorizontalPanel();
        horizontalSet.getElement().addClassName("form-horizontalSet");
        primForm.getDataTable().setWidget(currentRow, 0, horizontalSet);
        primForm.getDataTable().getFlexCellFormatter().setColSpan(currentRow, 0, 2);
    }

    public HorizontalPanel getHorizontalPanel() {
        return horizontalSet;
    }

    public void endHorizontalSet() {
        horizontalSet = null;
        currentRow += 1;
    }

    public void moveRow(final int sourceRow, int targetRow) {
        if (targetRow != sourceRow) {
            /*
             * String debugMsg = ""; for (String key : rowMap.keySet()) debugMsg
			 * += key + " " + rowMap.get(key) + "; "; Window.alert(debugMsg);
			 */

            // first, we remap the rowMap
            for (final String key : rowMap.keySet()) {
                final int i = rowMap.get(key);
                if (targetRow > sourceRow) {
                    if (i > sourceRow && i < targetRow) {
                        rowMap.put(key, i - 1);
                    } else if (i == sourceRow) {
                        rowMap.put(key, targetRow - 1);
                    }
                } else { // targetRow < sourceRow
                    if (i < sourceRow && i >= targetRow) {
                        rowMap.put(key, i + 1);
                    } else if (i == sourceRow) {
                        rowMap.put(key, targetRow);
                    }
                }
            }

            final FlexTable underlyingTable = primForm.getDataTable();
            final Widget widget0 = underlyingTable.getWidget(sourceRow, 0);
            underlyingTable.removeRow(sourceRow);
            // if the source row is less, then the target row is decremented
            // because the target is relative to the
            // original rows, but we're going to remove the source row
            if (sourceRow < targetRow) {
                targetRow -= 1;
            }
            underlyingTable.insertRow(targetRow);
            underlyingTable.setWidget(targetRow, 0, widget0);

			/*
             * debugMsg = ""; for (String key : rowMap.keySet()) debugMsg += key
			 * + " " + rowMap.get(key) + "; "; Window.alert(debugMsg);
			 */
        }
    }

    // ---------------------------------------
    // -------- Inner Classes ----------------
    // ---------------------------------------

    public class PrimitiveTableInterface {
        public FlexTable getDataTable() {
            return dataTable;
        }

        public void decrementCurrentRow() {
            currentRow -= 1;
        }

        public void clearErrorMessage(final String key) {
            if (controlsMap.containsKey(key)) {
                int rowIndex = rowMap.get(key);
                dataTable.getRowFormatter().removeStyleName(rowIndex, "errorRow");
                VerticalPanel inputBox = messageMap.get(key);

                if (inputBox.getWidgetCount() > 1) {
                    inputBox.remove(1);
                }
            }
        }

        public void addRowError(final String key, final String message) {
            dataTable.getRowFormatter().addStyleName(rowMap.get(key), "errorRow");
            final Label errorLabel = new Label(message);
            if (commonResources != null) {
                errorLabel.getElement().addClassName(commonResources.generalStyleCss().fieldErrorMessage());
            } else {
                errorLabel.getElement().addClassName("fieldErrorMessage");
            }
            final VerticalPanel inputBox = messageMap.get(key);
            if (inputBox != null) {
                inputBox.add(errorLabel);
            }
        }

        /**
         * Retrieves the row error message. Primarily useful for testing the
         * Form class.
         */
        public String getRowError(final String key) {
            final VerticalPanel inputBox = messageMap.get(key);
            if (inputBox.getWidgetCount() > 1) {
                return inputBox.getWidget(1).getElement().getFirstChild().getNodeValue();
            } else {
                return null;
            }
        }

        public void addRowMessage(final String key, final String message) {
            final Label messageLabel = new Label(message);
            final VerticalPanel inputBox = messageMap.get(key);
            messageLabel.getElement().addClassName("fieldMessage");
            inputBox.add(messageLabel);
        }

        public <T extends Widget> T addStandardRow(final Widget label, final String key, final boolean required,
                final T input, String rowClass) {
            return addStandardRow(label, key, required, input, false, rowClass);
        }

        public <T extends Widget> T addStandardRow(final Widget label, final String key, final boolean required,
                final T input, final boolean keepNormalLayout, String rowClass) {
            if (horizontalSet == null) {
                dataTable.setWidget(currentRow, 0, new InputGroup(label, createInputBox(key, input), rowClass));
                rowMap.put(key, currentRow);
                currentRow += 1;
            } else {
                final HorizontalPanel inputSet = new HorizontalPanel();
                inputSet.getElement().addClassName("form-inputSet");
                inputSet.add(label);
                inputSet.add(createInputBox(key, input));
                horizontalSet.add(inputSet);
                rowMap.put(key, currentRow);
            }
            return input;
        }

        private VerticalPanel createInputBox(final String key, final Widget inputOrValue) {
            final VerticalPanel inputBox = new VerticalPanel();
            inputBox.getElement().addClassName("form-inputBox");
            inputBox.add(inputOrValue);
            messageMap.put(key, inputBox);

            return inputBox;
        }

        public void processResponse(final Response response) {
            for (final String fieldKey : response.getFieldErrorKeys()) {
                addRowError(fieldKey, response.getFieldErrorMessage(fieldKey));
            }
        }
    }
}
