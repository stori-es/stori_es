package org.consumersunion.stories.common.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.i18n.SortFieldLabels;
import org.consumersunion.stories.common.shared.model.SortDropDownItem;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.DropDownType;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.model.StorySortField.CREATED_NEW;
import static org.consumersunion.stories.common.shared.model.StorySortField.CREATED_OLD;
import static org.consumersunion.stories.common.shared.model.StorySortField.MODIFIED_NEW;
import static org.consumersunion.stories.common.shared.model.StorySortField.MODIFIED_OLD;
import static org.consumersunion.stories.common.shared.model.StorySortField.TITLE_A_Z;
import static org.consumersunion.stories.common.shared.model.StorySortField.TITLE_Z_A;

public class StorySortFieldDropDownItem implements SortDropDownItem {
    @Inject
    private static SortFieldLabels labels;

    private final StorySortField storySortField;

    public StorySortFieldDropDownItem(StorySortField storySortField) {
        this.storySortField = storySortField;
    }

    StorySortFieldDropDownItem(){
        this(null);
    }

    public String getLabel() {
        return labels.getString(storySortField.name());
    }

    public static StorySortFieldDropDownItem defaultSortField() {
        return new StorySortFieldDropDownItem(CREATED_NEW);
    }

    public static List<StorySortFieldDropDownItem> sortList() {
        ArrayList<StorySortField> fields = Lists.newArrayList(TITLE_A_Z, TITLE_Z_A, CREATED_OLD, CREATED_NEW,
                MODIFIED_OLD, MODIFIED_NEW);

        return FluentIterable.from(fields)
                .transform(new Function<StorySortField, StorySortFieldDropDownItem>() {
                    @Override
                    public StorySortFieldDropDownItem apply(StorySortField input) {
                        return new StorySortFieldDropDownItem(input);
                    }
                }).toList();
    }

    public static SortDropDownItem parse(String sortFieldString) {
        return new StorySortFieldDropDownItem(StorySortField.valueOf(sortFieldString));
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public DropDownType getDropDownType() {
        return DropDownType.STORY_SORT_FIELD;
    }

    public StorySortField getSortField() {
        return storySortField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorySortFieldDropDownItem that = (StorySortFieldDropDownItem) o;
        return Objects.equal(storySortField, that.storySortField);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(storySortField);
    }
}
