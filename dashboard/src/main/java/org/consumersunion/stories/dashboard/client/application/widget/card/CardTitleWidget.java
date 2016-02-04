package org.consumersunion.stories.dashboard.client.application.widget.card;

import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class CardTitleWidget extends Composite {
    interface Binder extends UiBinder<Widget, CardTitleWidget> {
    }

    @UiField
    Resources resource;
    @UiField
    HTMLPanel cardInfo;
    @UiField
    DivElement icon;
    @UiField
    SpanElement title;
    @UiField
    DivElement cardType;

    private static final int ICON_SIZE = 64;
    private static final int MIN_WIDTH = 180;

    private final CommonI18nLabels labels;

    @Inject
    CardTitleWidget(
            Binder uiBinder,
            CommonI18nLabels labels) {
        this.labels = labels;
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void init(ContentKind contentKind) {
        init("", contentKind);
        switchToEditMode();
    }

    public void init(String title, ContentKind contentKind) {
        this.title.setInnerText(Strings.isNullOrEmpty(title) ? labels.untitled() : title);
        cardType.setInnerText(contentKind.toString());
        icon.addClassName(contentKind.getIcon());

        updateHeaderWidth(this.title);
    }

    public void swapIcons(String iconToRemove, String iconToAdd) {
        $(icon).removeClass(iconToRemove);
        $(icon).addClass(iconToAdd);
    }

    public void switchToViewMode() {
        $(title).show();
        $("." + resource.cards().cardType(), cardInfo)
                .removeClass(resource.cards().editPanel(), resource.cards().actionActive());
        updateHeaderWidth(title);
    }

    public void switchToEditMode() {
        $(title).hide();
        $("." + resource.cards().cardType(), cardInfo).addClass(resource.cards().editPanel());
        updateHeaderWidth(title);
    }

    public void switchToActionMode() {
        $("." + resource.cards().cardType(), cardInfo).addClass(resource.cards().actionActive());
        updateHeaderWidth(cardType);
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        updateHeaderWidth(title);
    }

    private void updateHeaderWidth(final Element titleElement) {
        final GQuery parent = $(cardInfo).parent();

        final boolean parentIsAnchor = "a".equalsIgnoreCase(parent.elements()[0].getTagName());
        if (parentIsAnchor) {
            parent.css("width", "100%");
        }

        $(cardInfo).removeAttr("style");
        $(cardInfo).delay(10, new Function() {
            @Override
            public void f() {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        int maxWidth = asWidget().getOffsetWidth();
                        $(cardInfo).width(
                                Math.max(Math.min(maxWidth, titleElement.getOffsetWidth() + ICON_SIZE), MIN_WIDTH));

                        if (parentIsAnchor) {
                            parent.css("width", "auto");
                        }
                    }
                });
            }
        });
    }
}
