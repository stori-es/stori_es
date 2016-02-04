package org.consumersunion.stories.common.shared.model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.i18n.SortFieldLabels;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.DropDownType;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.model.CollectionSortField.CREATED_NEW;
import static org.consumersunion.stories.common.shared.model.CollectionSortField.CREATED_OLD;
import static org.consumersunion.stories.common.shared.model.CollectionSortField.MODIFIED_NEW;
import static org.consumersunion.stories.common.shared.model.CollectionSortField.MODIFIED_OLD;
import static org.consumersunion.stories.common.shared.model.CollectionSortField.TITLE_A_Z;
import static org.consumersunion.stories.common.shared.model.CollectionSortField.TITLE_Z_A;

public class CollectionSortDropDownItem implements SortDropDownItem {
    @SuppressWarnings("GwtInconsistentSerializableClass")
    @Inject
    private static SortFieldLabels labels;

    private final CollectionSortField sortField;

    public CollectionSortDropDownItem(CollectionSortField sortField) {
        this.sortField = sortField;
    }

    CollectionSortDropDownItem() {
        this(null);
    }

    public static List<CollectionSortDropDownItem> getSortFields() {
        ArrayList<CollectionSortField> fields = Lists.newArrayList(TITLE_A_Z, TITLE_Z_A, CREATED_OLD, CREATED_NEW,
                MODIFIED_OLD,
                MODIFIED_NEW);

        return FluentIterable.from(fields)
                .transform(new Function<CollectionSortField, CollectionSortDropDownItem>() {
                    @Override
                    public CollectionSortDropDownItem apply(CollectionSortField input) {
                        return new CollectionSortDropDownItem(input);
                    }
                }).toList();
    }

    public static CollectionSortDropDownItem defaultSortField() {
        return new CollectionSortDropDownItem(TITLE_A_Z);
    }

    public static CollectionSortDropDownItem parse(String sortFieldString) {
        return new CollectionSortDropDownItem(CollectionSortField.valueOf(sortFieldString));
    }

    @Override
    public String getLabel() {
        return labels.getString(sortField.name());
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public DropDownType getDropDownType() {
        return DropDownType.COLLECTION_SORT_FIELD;
    }

    @Override
    public SortField getSortField() {
        return sortField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollectionSortDropDownItem that = (CollectionSortDropDownItem) o;
        return Objects.equal(sortField, that.sortField);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sortField);
    }
}
