package org.consumersunion.stories.dashboard.client.application.widget.card;

import java.util.Arrays;
import java.util.List;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class CardToolbarWidget extends Composite {
    interface Binder extends UiBinder<Widget, CardToolbarWidget> {
    }

    @UiField(provided = true)
    final CardToolbarButton edit;

    @UiField
    Resources resource;
    @UiField
    SpanElement activeToolbar;
    @UiField
    SpanElement activeIconWrapper;
    @UiField
    SpanElement save;
    @UiField
    Button confirm;
    @UiField
    SpanElement cancel;
    @UiField
    SpanElement activeIcon;
    @UiField
    HTMLPanel buttonsContainer;
    @UiField
    SpanElement saveWrapper;

    private final CommonI18nLabels commonI18nLabels;

    private CardToolbarButton activeButton;
    private CardToolbarHandler toolbarHandler;
    private boolean editMode;

    @Inject
    CardToolbarWidget(
            Binder uiBinder,
            CommonI18nLabels commonI18nLabels,
            CardToolbarButtonFactory buttonFactory) {
        this.commonI18nLabels = commonI18nLabels;
        this.edit = buttonFactory.createEditButton(new ToolbarButtonActionHandler() {
            @Override
            public void executeAction() {
                onDoneClicked();
            }

            @Override
            public void onButtonClicked() {
                setToEdit(true);
                toolbarHandler.edit();
            }
        });

        initWidget(uiBinder.createAndBindUi(this));

        setToEdit(false);

        initTooltips();
        bindClicks();
    }

    public void setButtons(CardToolbarButton... buttons) {
        setButtons(Arrays.asList(buttons));
    }

    public void setButtons(List<CardToolbarButton> buttons) {
        buttonsContainer.clear();

        for (final CardToolbarButton button : buttons) {
            button.asWidget().sinkEvents(Event.ONCLICK);
            button.asWidget().addHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    onButtonClicked(button);
                }
            }, ClickEvent.getType());

            buttonsContainer.add(button);
        }
    }

    public void setHandler(CardToolbarHandler toolbarHandler) {
        this.toolbarHandler = toolbarHandler;
    }

    public void setToEdit(boolean editEnabled) {
        this.editMode = editEnabled;
        if (editMode) {
            onButtonClicked(edit);
            $(saveWrapper).show();
            switchToBlue($(save));
        } else {
            $(activeToolbar).hide();
            $(edit).show();
            $(buttonsContainer).show();
        }
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void onSaveSuccess() {
        switchToBlue($(save));
    }

    public void onSaveFailed() {
        switchToBlue($(save));
    }

    @UiHandler("confirm")
    void onConfirmClicked(ClickEvent event) {
        activeButton.executeAction();
    }

    private void onDoneClicked() {
        switchToYellow($(save));
        toolbarHandler.save(true);
    }

    private void onButtonClicked(CardToolbarButton button) {
        activeButton = button;

        $(activeToolbar).show();
        $(edit).hide();
        $(saveWrapper).hide();
        $(buttonsContainer).hide();
        activeIcon.setClassName(resource.generalStyleCss().yellow());
        activeIcon.addClassName(button.getIcon());
        confirm.setText(button.getConfirmText());
    }

    private void bindClicks() {
        bindSaveClick();
        bindCancelClick();
    }

    private void bindSaveClick() {
        $(save).click(new Function() {
            @Override
            public void f() {
                switchToYellow($(save));
                toolbarHandler.save(false);
            }
        });
    }

    private void bindCancelClick() {
        $(cancel).click(new Function() {
            @Override
            public void f() {
                toolbarHandler.cancel();
            }
        });
    }

    private void initTooltips() {
        save.getParentElement().setAttribute("data-tooltip", commonI18nLabels.clickToSave());
    }

    private void switchToYellow(GQuery element) {
        element.addClass(resource.generalStyleCss().yellow()).removeClass(resource.generalStyleCss().blue());
    }

    private void switchToBlue(GQuery element) {
        element.addClass(resource.generalStyleCss().blue()).removeClass(resource.generalStyleCss().yellow());
    }
}
