package org.consumersunion.stories.common.shared.model;

import java.util.List;

import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.DropDownType;

import com.google.common.collect.Lists;

public enum StorySelectField implements SortDropDownItem, SortField {
    CURRENT_PAGE_OF("current page of"),
    SELECTED("selected"),
    ENTIRE_SET("entire set of");

    private String label;

    StorySelectField(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static List<StorySelectField> selectList(boolean includeEntireSet) {
        List<StorySelectField> fields = Lists.newArrayList(values());

        if (!includeEntireSet) {
            fields.remove(ENTIRE_SET);
        }

        return fields;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public DropDownType getDropDownType() {
        return DropDownType.STORY_SELECT_FIELD;
    }

    @Override
    public SortField getSortField() {
        return this;
    }
}
