package org.consumersunion.stories.common.client.service.datatransferobject;

import org.consumersunion.stories.common.shared.model.SortDropDownItem;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.DropDownType;

import com.google.common.base.Objects;

public class ProfileDropDownItem implements SortDropDownItem {
    private final ProfileSummary profileSummary;

    // For serialization
    ProfileDropDownItem() {
        this.profileSummary = null;
    }

    public ProfileDropDownItem(ProfileSummary profileSummary) {
        this.profileSummary = profileSummary;
    }

    public ProfileSummary getProfileSummary() {
        return profileSummary;
    }

    @Override
    public String getLabel() {
        return profileSummary.getOrganizationName();
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public DropDownType getDropDownType() {
        return DropDownType.PROFILE;
    }

    @Override
    public SortField getSortField() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProfileDropDownItem that = (ProfileDropDownItem) o;
        return Objects.equal(profileSummary, that.profileSummary);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(profileSummary);
    }
}
