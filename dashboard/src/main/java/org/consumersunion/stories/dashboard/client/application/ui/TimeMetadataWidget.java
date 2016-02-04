package org.consumersunion.stories.dashboard.client.application.ui;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.dashboard.client.application.util.StringUtil;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class TimeMetadataWidget extends Composite {
    interface Binder extends UiBinder<Widget, TimeMetadataWidget> {
    }

    @UiField
    DivElement createdDate;
    @UiField
    DivElement modifiedDate;
    @UiField
    DivElement time;
    @UiField
    Resources resource;
    @UiField
    HTMLPanel timeContainer;

    private final CommonI18nMessages messages;

    @Inject
    TimeMetadataWidget(
            Binder uiBinder,
            CommonI18nMessages messages) {
        this.messages = messages;

        initWidget(uiBinder.createAndBindUi(this));
    }

    public void init(SystemEntity systemEntity) {
        initTimeTooltip(systemEntity);

        createdDate.setInnerText(messages.createdDate(systemEntity.getCreated()));
        modifiedDate.setInnerText(messages.modifiedDate(systemEntity.getUpdated()));
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        $(timeContainer).add($(time)).hover(new Function() {
            @Override
            public void f() {
                $(time).addClass(resource.generalStyleCss().yellow());
                $(time).removeClass(resource.generalStyleCss().blue());
            }
        }, new Function() {
            @Override
            public void f() {
                $(time).addClass(resource.generalStyleCss().blue());
                $(time).removeClass(resource.generalStyleCss().yellow());
            }
        });
    }

    private void initTimeTooltip(SystemEntity systemEntity) {
        String timeTooltip = StringUtil.lineBreak(
                messages.createModifiedTimestamp(systemEntity.getCreated(), systemEntity.getUpdated()));

        $(timeContainer).attr("data-tooltip", timeTooltip);
    }
}
