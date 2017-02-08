package org.consumersunion.stories.dashboard.client.application.widget.card;

import javax.inject.Provider;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.ui.form.RichTextToolbar;
import org.consumersunion.stories.common.client.ui.form.controls.RichTextEditor;
import org.consumersunion.stories.common.client.util.HtmlSanitizerUtil;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class CardContentEditWidget extends Composite {
    interface Binder extends UiBinder<Widget, CardContentEditWidget> {
    }

    private static final int MAX_LENGTH = 500;

    @UiField
    Resources resource;
    @UiField
    TextBox contentTitle;
    @UiField
    SimplePanel summaryContainer;
    @UiField
    DivElement summaryError;
    @UiField
    DivElement contentTitleError;
    @UiField
    DivElement summaryTitle;

    private final StoryTellerDashboardI18nLabels labels;
    private final HtmlSanitizerUtil htmlSanitizerUtil;
    private final Provider<RichTextEditor> richTextAreaProvider;
    private final Provider<RichTextToolbar> richTextToolbarProvider;

    private Widget summary;

    @Inject
    CardContentEditWidget(
            Binder uiBinder,
            StoryTellerDashboardI18nLabels labels,
            HtmlSanitizerUtil htmlSanitizerUtil,
            Provider<RichTextEditor> richTextAreaProvider,
            Provider<RichTextToolbar> richTextToolbarProvider) {
        this.labels = labels;
        this.htmlSanitizerUtil = htmlSanitizerUtil;
        this.richTextAreaProvider = richTextAreaProvider;
        this.richTextToolbarProvider = richTextToolbarProvider;

        initWidget(uiBinder.createAndBindUi(this));

        summary = new TextArea();
        summary.getElement().setAttribute("maxlength", String.valueOf(MAX_LENGTH));
        summaryContainer.setWidget(summary);

        clearErrors();
    }

    public void init(ContentKind contentKind) {
        Document document = new Document();

        Block block = getBlockType(contentKind, null, null);

        document.addBlock(block);
        doInit(document, contentKind);
    }

    public void init(Document documentText, ContentKind contentKind) {
        doInit(documentText, contentKind);
    }

    public boolean validate() {
        clearErrors();

        boolean valid = true;
        Widget elementToFocus = null;

        if (isSummaryRequired() && getSummaryText().trim().isEmpty()) {
            valid = false;
            $(summaryError).show();
            elementToFocus = summaryContainer;
        }

        if (contentTitle.getText().trim().isEmpty()) {
            valid = false;
            $(contentTitleError).show();
            contentTitleError.focus();
            elementToFocus = contentTitle;
        }

        if (elementToFocus != null) {
            elementToFocus.getElement().focus();
        }

        return valid;
    }

    public String getTitle() {
        return contentTitle.getText();
    }

    public String getContentText() {
        return getSummaryText();
    }

    public void clearErrors() {
        $(contentTitleError).hide();
        $(summaryError).hide();
    }

    private Block getBlockType(ContentKind contentKind, String summary, TextType textType) {
        Block block;
        switch (contentKind) {
            case CONTENT:
                block = new TextImageBlock(Strings.nullToEmpty(summary));
                break;
            default:
                block = new Content(BlockType.CONTENT, summary, textType);
        }
        return block;
    }

    private void doInit(Document documentText, ContentKind contentKind) {
        String contentText = "";
        switch (contentKind) {
            case NOTE:
                GQuery sup = $("sup", summaryTitle);
                summaryTitle.setInnerText(labels.content());
                $(summaryTitle).append(sup);

                contentText = documentText.getFirstContent();
                break;
            case CONTENT:
            case STORY:
                contentText = initTextBlock(documentText);
                break;
            case QUESTIONNAIRE:
            case COLLECTION:
                if (ContentKind.QUESTIONNAIRE.equals(contentKind) || ContentKind.COLLECTION.equals(contentKind)) {
                    $("sup", summaryTitle).remove();
                }
                contentText = initTextBlock(documentText);
                break;
            case ATTACHMENT:
                contentText = documentText.getPermalink();
                summaryTitle.setInnerHTML(labels.urlRequiredLabel());
                switchToTextbox();
                break;
        }

        contentTitle.setText(documentText.getTitle());
        setSummaryText(contentText);
    }

    private String initTextBlock(Document documentText) {
        String contentText = documentText.getSummary();

        maybeChangeToRichText(documentText);

        return contentText;
    }

    private boolean isSummaryRequired() {
        return !$("sup", summaryTitle).isEmpty();
    }

    private void switchToTextbox() {
        TextBox textBox = new TextBox();
        textBox.setMaxLength(MAX_LENGTH);
        summaryContainer.setWidget(textBox);
        summary = textBox;
    }

    private void maybeChangeToRichText(Document documentText) {
        if (!documentText.getBlocks().isEmpty()) {
            Block textBlock = documentText.getFirstContentBlock();
            if (textBlock != null && (textBlock instanceof TextImageBlock
                    || TextType.HTML.equals(((Content) textBlock).getTextType()))) {
                RichTextEditor richTextEditor = richTextAreaProvider.get();
                RichTextToolbar toolbar = richTextToolbarProvider.get();
                bindRichTextEvents(richTextEditor);

                HTMLPanel htmlPanel = new HTMLPanel("");
                htmlPanel.add(toolbar);
                htmlPanel.add(richTextEditor);

                summaryContainer.setWidget(htmlPanel);

                toolbar.initialize(richTextEditor);

                summary = richTextEditor;
            }
        }
    }

    private void bindRichTextEvents(final RichTextEditor richTextEditor) {
        richTextEditor.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                int keyCode = event.getNativeKeyCode();

                boolean valid = (keyCode > 47 && keyCode < 58) || // number keys
                        keyCode == 32 || keyCode == 13 || // spacebar & return key(s)
                        (keyCode > 64 && keyCode < 91) || // letter keys
                        (keyCode > 95 && keyCode < 112) || // numpad keys
                        (keyCode > 185 && keyCode < 193) || // ;=,-./` (in order)
                        (keyCode > 218 && keyCode < 223);   // [\]' (in order)

                if (!event.isAnyModifierKeyDown() && valid
                        && getSelectionLength(richTextEditor.getElement()) == 0
                        && richTextEditor.getValue().length() >= MAX_LENGTH) {
                    event.preventDefault();
                }
            }
        });
        richTextEditor.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    $(richTextEditor).contents().find("body")
                            .on("paste", new Function() {
                                @Override
                                public boolean f(Event e) {
                                    maybeTruncateRichText(richTextEditor);
                                    return richTextEditor.getValue().length() < MAX_LENGTH;
                                }
                            });
                } else {
                    $(richTextEditor).off("paste");
                }
            }
        });
    }

    private void maybeTruncateRichText(final RichTextEditor richTextEditor) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (richTextEditor.getValue().length() >= MAX_LENGTH) {
                    richTextEditor.setValue(richTextEditor.getValue().substring(0, MAX_LENGTH));
                }
            }
        });
    }

    private native int getSelectionLength(Element e)/*-{
        if (e.contentWindow.document.selection) {
            return e.contentWindow.document.selection.createRange().text.length;
        }
        else {
            return e.contentWindow.document.getSelection().toString().length;
        }
    }-*/;

    private void setSummaryText(String contentText) {
        if (summary instanceof TextBoxBase) {
            ((TextBoxBase) summary).setText(contentText);
        } else {
            ((RichTextEditor) summary).setHTML(htmlSanitizerUtil.sanitize(Strings.nullToEmpty(contentText)));
        }
    }

    private String getSummaryText() {
        if (summary instanceof TextBoxBase) {
            return ((TextBoxBase) summary).getText();
        } else {
            return ((RichTextEditor) summary).getValue();
        }
    }
}
