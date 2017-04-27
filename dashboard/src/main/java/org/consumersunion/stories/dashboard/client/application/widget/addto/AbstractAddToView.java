package org.consumersunion.stories.dashboard.client.application.widget.addto;

import org.consumersunion.stories.common.shared.model.StorySelectField;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown.DropDownHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiHandler;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

abstract class AbstractAddToView<H extends AddToUiHandlers> extends ViewWithUiHandlers<H> implements AddToView<H> {
    private final ClickableDropDown<StorySelectField> selectDropDown;

    private StorySelectField currentSelectField;

    protected AbstractAddToView(ClickableDropDown<StorySelectField> selectDropDown) {
        this.selectDropDown = selectDropDown;

        setupSelectDropDown();
        setupDropDownEvents();
    }

    @Override
    public void reset() {
        selectDropDown.setSelection(StorySelectField.CURRENT_PAGE_OF);
        currentSelectField = StorySelectField.CURRENT_PAGE_OF;
    }

    @Override
    public boolean isVisible() {
        return asWidget().getParent().isVisible();
    }

    @UiHandler("goAssign")
    void onGoAssign(ClickEvent event) {
        getUiHandlers().onGoClicked();
    }

    private void setupSelectDropDown() {
        selectDropDown.loadOptions(StorySelectField.selectList(this instanceof AddStoriesToCollectionsView), false);
        selectDropDown.setTitle(StorySelectField.CURRENT_PAGE_OF.getLabel());
    }

    private void setupDropDownEvents() {
        selectDropDown.setDropDownHandler(new DropDownHandler<StorySelectField>() {
            public void onLoadSpecificItem(StorySelectField item) {
                if (!item.equals(currentSelectField)) {
                    getUiHandlers().onStorySelectFieldChanged(item);
                }

                currentSelectField = item;
            }
        });
    }
}
