package org.consumersunion.stories.dashboard.client.application.widget.card;

import org.consumersunion.stories.common.client.event.ClearContentEvent;
import org.consumersunion.stories.common.client.util.HtmlSanitizerUtil;
import org.consumersunion.stories.common.client.util.URLUtils;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.dashboard.client.resource.Resources;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static com.google.gwt.query.client.GQuery.$;

public class CardSummaryWidget extends Composite {
    interface Binder extends UiBinder<Widget, CardSummaryWidget> {
    }

    @UiField
    Resources resource;
    @UiField
    InlineHyperlink author;
    @UiField
    SpanElement city;
    @UiField
    Element summary;
    @UiField
    DivElement authorAndCity;
    @UiField
    SpanElement authorPrefix;

    private final EventBus eventBus;
    private final PlaceManager placeManager;
    private final HtmlSanitizerUtil htmlSanitizerUtil;

    private String ownerKind;
    private String ownerId;

    @Inject
    CardSummaryWidget(
            Binder uiBinder,
            EventBus eventBus,
            PlaceManager placeManager,
            HtmlSanitizerUtil htmlSanitizerUtil) {
        this.eventBus = eventBus;
        this.placeManager = placeManager;
        this.htmlSanitizerUtil = htmlSanitizerUtil;

        initWidget(uiBinder.createAndBindUi(this));
    }

    public void init(String content, String author, String city, Integer ownerId) {
        ownerKind = NameTokens.profile;
        setSummary(content);
        init(author, city, ownerId);
    }

    public void init(
            ContentKind contentKind, Document document, String author, String city, int ownerId, boolean isProfile) {
        ownerKind = isProfile ? NameTokens.profile : NameTokens.organization;
        switch (contentKind) {
            case STORY:
            case CONTENT:
                init(document, author, city, ownerId);
                break;
            case NOTE:
                init(document.getFirstContent(), author, city, ownerId);
                break;
            case ATTACHMENT:
                initAttachment(document.getPermalink(), author, city, ownerId);
                break;
            case RESPONSE:
                init(author, city, ownerId);
                break;
        }
    }

    public void init(ContentKind contentKind, String content, String owner, int ownerId, boolean isProfile) {
        switch (contentKind) {
            case QUESTIONNAIRE:
            case COLLECTION:
                ownerKind = isProfile ? NameTokens.profile : NameTokens.organization;
                setSummary(content);
                init(owner, null, ownerId);
                authorPrefix.setInnerText(isProfile ? "By " : "By staff at ");

                break;
            default:
                init(content, owner, null, null);
        }
    }

    public void init(ContentKind contentKind, Document document, String author, String city, int ownerId) {
        ownerKind = NameTokens.profile;

        init(contentKind, document, author, city, ownerId, true);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        if (event instanceof ClearContentEvent) {
            eventBus.fireEventFromSource(event, this);
        } else {
            super.fireEvent(event);
        }
    }

    private void init(Document document, String author, String city, int ownerId) {
        ownerKind = NameTokens.profile;

        setSummary(document.getSummary());
        init(author, city, ownerId);
    }

    private void initAttachment(String url, String author, String city, int ownerId) {
        init(author, city, ownerId);

        String oldClasses = $(summary).attr("class");

        AnchorElement anchorElement = com.google.gwt.dom.client.Document.get().createAnchorElement();
        url = URLUtils.appendDefaultProtocol(url);
        anchorElement.setHref(url);
        anchorElement.setTarget("_blank");
        anchorElement.setInnerText(url);
        anchorElement.setClassName(oldClasses);

        $(summary).replaceWith(anchorElement);
        summary = anchorElement;
    }

    private void init(String author, String city, Integer ownerId) {
        this.city.setInnerText(city);
        this.author.setText(author);
        this.ownerId = String.valueOf(ownerId);

        bindClicks();
    }

    private void bindClicks() {
        bindAuthorClick();
    }

    private void bindAuthorClick() {
        final PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(ownerKind)
                .with(ParameterTokens.id, ownerId)
                .build();

        author.setTargetHistoryToken(placeManager.buildHistoryToken(place));

        $(author).unbind(BrowserEvents.CLICK);
        author.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.stopPropagation();
                if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
                    event.preventDefault();
                    goToAuthor(place);
                }
            }
        });
    }

    private void goToAuthor(final PlaceRequest place) {
        ClearContentEvent.fire(this);
        placeManager.revealPlace(place);
    }

    private void setSummary(String content) {
        String documentContent = content == null ? "" : htmlSanitizerUtil.sanitize(content).asString();
        summary.setInnerHTML(documentContent);
    }
}
