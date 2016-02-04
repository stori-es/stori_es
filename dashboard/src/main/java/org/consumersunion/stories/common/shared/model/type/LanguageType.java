package org.consumersunion.stories.common.shared.model.type;

import org.consumersunion.stories.common.shared.model.Checkable;

public class LanguageType implements Checkable {
    private String label;
    private String code;
    private boolean selected;

    public LanguageType() {
        selected = false;
    }

    public LanguageType(String label, String code, Boolean selected) {
        this.label = label;
        this.code = code;
        this.selected = selected;
    }

    @Override
    public void setChecked(boolean checked) {
        selected = checked;
    }

    @Override
    public boolean getChecked() {
        return selected;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
