package org.consumersunion.stories.common.client.ui.block;

import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class SubmitBlockWidget extends Widget implements BlockElement<SubmitBlock> {
    interface Binder extends UiBinder<DivElement, SubmitBlockWidget> {
    }

    public static final String BUTTON_ID = "stories-submit-button";

    @UiField
    ButtonElement button;
    @UiField
    CommonResources commonResource;

    @Inject
    SubmitBlockWidget(
            Binder uiBinder,
            @Assisted SubmitBlock block) {
        setElement(uiBinder.createAndBindUi(this));

        button.setId(BUTTON_ID);

        display(block);
    }

    @Override
    public void display(SubmitBlock block) {
        button.setClassName("");
        updateSize(block);
        updatePosition(block);
        updatePrompt(block);
    }

    private void updatePrompt(SubmitBlock block) {
        button.setInnerText(block.getPrompt());
    }

    private void updatePosition(SubmitBlock block) {
        String positionClass = null;
        switch (block.getPosition()) {
            case LEFT:
                positionClass = commonResource.generalStyleCss().floatLeft();
                break;
            case CENTER:
                positionClass = commonResource.generalStyleCss().centerMargin();
                break;
            case RIGHT:
                positionClass = commonResource.generalStyleCss().floatRight();
                break;
            default:
                assert false : "Unexpected Submit button position";
        }

        if (positionClass != null) {
            button.addClassName(positionClass);
        }
    }

    private void updateSize(SubmitBlock block) {
        String sizeClass = null;
        switch (block.getSize()) {
            case SMALL:
                sizeClass = commonResource.generalStyleCss().buttonSmall();
                break;
            case MEDIUM:
                sizeClass = commonResource.generalStyleCss().buttonMedium();
                break;
            case LARGE:
                sizeClass = commonResource.generalStyleCss().buttonLarge();
                break;
            default:
                assert false : "Unexpected Submit button size";
        }

        if (sizeClass != null) {
            button.addClassName(sizeClass);
        }
    }
}
