package org.consumersunion.stories.dashboard.client.application.collection.ui;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.dashboard.client.resource.GeneralStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.common.base.Function;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static com.google.gwt.query.client.GQuery.$;

public class AttachedCollectionCell extends AbstractCell<CollectionSummary> {
    interface Template extends SafeHtmlTemplates {
        @Template("<a href=\"#{6}\" class=\"attachedCollections\">{0} ({1})</a>"
                + "<div class=\"{3} {4} simptip-position-top simptip-fade\" data-tooltip=\"{5}\" " +
                "style=\"{2}\"/></div></div>")
        SafeHtml attachedCollectionTemplate(SafeHtml title, String count, String deleteStyle, String deleteCollection,
                String deleteClass, String tooltip, String href);
    }

    private final PlaceManager placeManager;
    private final Template template;
    private final CommonI18nLabels labels;
    private final Delegate<CollectionSummary> delegateDetail;
    private final Delegate<CollectionSummary> delegateRemove;
    private final Function<CollectionSummary, Boolean> canDeleteDelegate;
    private final GeneralStyle generalStyle;

    @Inject
    AttachedCollectionCell(
            PlaceManager placeManager,
            Template template,
            Resources resources,
            CommonI18nLabels labels,
            @Assisted("detail") Delegate<CollectionSummary> delegateDetail,
            @Assisted("remove") Delegate<CollectionSummary> delegateRemove,
            @Assisted Function<CollectionSummary, Boolean> canDeleteDelegate) {
        super(BrowserEvents.CLICK);

        this.placeManager = placeManager;
        this.template = template;
        this.labels = labels;
        this.delegateDetail = delegateDetail;
        this.delegateRemove = delegateRemove;
        this.canDeleteDelegate = canDeleteDelegate;
        generalStyle = resources.generalStyleCss();
    }

    @Override
    public void onBrowserEvent(
            Context context,
            Element parent,
            CollectionSummary value,
            NativeEvent event,
            ValueUpdater<CollectionSummary> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        if (BrowserEvents.CLICK.equals(event.getType())) {
            EventTarget eventTarget = event.getEventTarget();
            if (!Element.is(eventTarget)) {
                return;
            }

            if ($(Element.as(eventTarget)).hasClass("attachedCollections")) {
                delegateDetail.execute(value);
            }

            if ($(Element.as(eventTarget)).hasClass(generalStyle.deleteCollection())) {
                delegateRemove.execute(value);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(Context context, CollectionSummary collection, SafeHtmlBuilder safeHtmlBuilder) {
        SafeHtml safeTitle = SafeHtmlUtils.fromString(collection.getTitle());
        String deleteStyle = !canDeleteDelegate.apply(collection) ? "display:none" : "";

        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.collection)
                .with(ParameterTokens.id, String.valueOf(collection.getId()))
                .build();
        String historyToken = placeManager.buildHistoryToken(placeRequest);

        safeHtmlBuilder.append(template.attachedCollectionTemplate(safeTitle, String.valueOf(collection.getNbStories()),
                deleteStyle, generalStyle.delete(), generalStyle.deleteCollection(), labels.clickToRemove(),
                historyToken));
    }
}
