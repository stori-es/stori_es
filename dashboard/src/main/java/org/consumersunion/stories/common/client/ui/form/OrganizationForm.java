package org.consumersunion.stories.common.client.ui.form;

import com.google.common.base.Strings;

public class OrganizationForm extends Form {

    public String validate(String defaultThemeField, String themeListField, String errorMessage) {
        if (!super.validate()) {
            errorMessage += "Please fill all the required fields";
        } else if (!validateTheme(defaultThemeField, themeListField)) {
            errorMessage += "Please choose a default theme among preselected values";
        }
        return errorMessage;
    }

    private boolean validateTheme(String defaultThemeField, String themeListField) {
        String defaultTheme = super.getValue(defaultThemeField);

        return !(!Strings.isNullOrEmpty(defaultTheme) && !super.getMultiValue(themeListField).contains(defaultTheme));
    }
}
