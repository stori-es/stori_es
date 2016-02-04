/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.consumersunion.stories.common.client.ui.form;

import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.common.client.resource.RichTextToolbarImages;
import org.consumersunion.stories.common.client.resource.RichTextToolbarStrings;
import org.consumersunion.stories.common.client.util.URLUtils;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.InitializeEvent;
import com.google.gwt.event.logical.shared.InitializeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.user.client.ui.RichTextArea.FontSize;

/**
 * A sample toolbar for use with
 * {@link com.google.gwt.user.client.ui.RichTextArea}. It provides a simple UI
 * for all rich text formatting, dynamically displayed only for the available
 * functionality.
 */
public class RichTextToolbar extends Composite implements InitializeHandler {
    interface Binder extends UiBinder<Widget, RichTextToolbar> {
    }

    /**
     * We use an inner EventHandler class to avoid exposing event methods on the
     * RichTextToolbar itself.
     */
    private class EventHandler implements ClickHandler, KeyUpHandler, ChangeHandler {
        public void onClick(ClickEvent event) {
            Widget sender = (Widget) event.getSource();

            if (sender == bold) {
                formatter.toggleBold();
            } else if (sender == italic) {
                formatter.toggleItalic();
            } else if (sender == underline) {
                formatter.toggleUnderline();
            } else if (sender == strikeThrough) {
                formatter.toggleStrikethrough();
            } else if (sender == createLink) {
                String url = Window.prompt("Enter a link URL:", "http://");
                if (url != null) {
                    formatter.createLink(URLUtils.appendDefaultProtocol(url));
                }
            } else if (sender == removeLink) {
                formatter.removeLink();
            } else if (sender == ol) {
                formatter.insertOrderedList();
            } else if (sender == ul) {
                formatter.insertUnorderedList();
            } else if (sender == leftIndent) {
                formatter.leftIndent();
            } else if (sender == rightIndent) {
                formatter.rightIndent();
            } else if (sender == removeFormat) {
                formatter.removeFormat();
            } else if (sender == richText) {
                // We use the RichTextArea's onKeyUp event to update the toolbar
                // status.
                // This will catch any cases where the user moves the cursur
                // using the
                // keyboard, or uses one of the browser's built-in keyboard
                // shortcuts.
                updateStatus();
            }
        }

        public void onKeyUp(KeyUpEvent event) {
            Widget sender = (Widget) event.getSource();
            if (sender == richText) {
                // We use the RichTextArea's onKeyUp event to
                // updateShowAggregation the toolbar status.
                // This will catch any cases where the user moves the cursur
                // using the
                // keyboard, or uses one of the browser's built-in keyboard
                // shortcuts.
                updateStatus();
            }
        }

        @Override
        public void onChange(ChangeEvent changeEvent) {
            Widget sender = (Widget) changeEvent.getSource();
            if (sender == fonts) {
                int selectedIndex = fonts.getSelectedIndex();
                if (selectedIndex > -1) {
                    formatter.setFontName(fonts.getValue(selectedIndex));
                }
            } else if (sender == fontSize) {
                int selectedIndex = fontSize.getSelectedIndex();
                if (selectedIndex > -1) {
                    String selectedValue = fontSize.getValue(selectedIndex);
                    if (selectedValue.equals(XX_SMALL)) {
                        formatter.setFontSize(FontSize.XX_SMALL);
                    } else if (selectedValue.equals(X_SMALL)) {
                        formatter.setFontSize(FontSize.X_SMALL);
                    } else if (selectedValue.equals(SMALL)) {
                        formatter.setFontSize(FontSize.SMALL);
                    } else if (selectedValue.equals(MEDIUM)) {
                        formatter.setFontSize(FontSize.MEDIUM);
                    } else if (selectedValue.equals(LARGE)) {
                        formatter.setFontSize(FontSize.LARGE);
                    } else if (selectedValue.equals(X_LARGE)) {
                        formatter.setFontSize(FontSize.X_LARGE);
                    } else if (selectedValue.equals(XX_LARGE)) {
                        formatter.setFontSize(FontSize.XX_LARGE);
                    }
                }
            }
        }
    }

    private static final String DROID_SANS = "'Droid Sans', sans-serif";
    private static final String FONT_SIZE = "FontSize";
    private static final String FONT_NAME = "FontName";
    private static final String XX_SMALL = "XX Small";
    private static final String X_SMALL = "X Small";
    private static final String SMALL = "Small";
    private static final String MEDIUM = "Medium";
    private static final String LARGE = "Large";
    private static final String X_LARGE = "X Large";
    private static final String XX_LARGE = "XX Large";

    @UiField
    HTMLPanel topPanel;

    private final RichTextToolbarImages richTextToolbarImages;
    private final RichTextToolbarStrings richTextToolbarStrings;
    private final CommonResources commonResources;
    private final EventHandler handler;

    private RichTextArea richText;
    private RichTextArea.Formatter formatter;
    private ListBox fonts;
    private ListBox fontSize;
    private ToggleButton bold;
    private ToggleButton italic;
    private ToggleButton underline;
    private ToggleButton strikeThrough;
    private PushButton ol;
    private PushButton ul;
    private PushButton leftIndent;
    private PushButton rightIndent;
    private PushButton createLink;
    private PushButton removeLink;
    private PushButton removeFormat;

    @Inject
    RichTextToolbar(Binder binder,
            RichTextToolbarImages richTextToolbarImages,
            RichTextToolbarStrings richTextToolbarStrings,
            CommonResources commonResources) {
        this.richTextToolbarImages = richTextToolbarImages;
        this.richTextToolbarStrings = richTextToolbarStrings;
        this.commonResources = commonResources;
        handler = new EventHandler();

        initWidget(binder.createAndBindUi(this));

        commonResources.richTextToolbarStyle().ensureInjected();
    }

    public void initialize(RichTextArea richText) {
        this.richText = richText;
        this.formatter = richText.getFormatter();

        richText.addInitializeHandler(this);

        if (formatter != null) {
            topPanel.add(fonts = createFonts());
            topPanel.add(fontSize = createFontSize());
            topPanel.add(createSeparator());
            topPanel.add(bold = createToggleButton(richTextToolbarImages.bold(), richTextToolbarStrings.bold()));
            topPanel.add(italic = createToggleButton(richTextToolbarImages.italic(), richTextToolbarStrings.italic()));
            topPanel.add(underline = createToggleButton(richTextToolbarImages.underline(),
                    richTextToolbarStrings.underline()));
            topPanel.add(strikeThrough = createToggleButton(richTextToolbarImages.strikeThrough(),
                    richTextToolbarStrings.strikeThrough()));
            topPanel.add(createSeparator());
            topPanel.add(ol = createPushButton(richTextToolbarImages.ol(), richTextToolbarStrings.ol()));
            topPanel.add(ul = createPushButton(richTextToolbarImages.ul(), richTextToolbarStrings.ul()));
            topPanel.add(leftIndent = createPushButton(richTextToolbarImages.outdent(),
                    richTextToolbarStrings.leftIndent()));
            topPanel.add(rightIndent = createPushButton(richTextToolbarImages.indent(),
                    richTextToolbarStrings.rightIndent()));
            topPanel.add(createSeparator());
            topPanel.add(createLink = createPushButton(richTextToolbarImages.createLink(),
                    richTextToolbarStrings.createLink()));
            topPanel.add(removeLink = createPushButton(richTextToolbarImages.removeLink(),
                    richTextToolbarStrings.removeLink()));
            topPanel.add(createSeparator());
            topPanel.add(removeFormat = createPushButton(richTextToolbarImages.removeFormat(),
                    richTextToolbarStrings.removeFormat()));

            richText.addKeyUpHandler(handler);
            richText.addClickHandler(handler);
        }
    }

    @Override
    public void onInitialize(InitializeEvent event) {
        Document document = IFrameElement.as(richText.getElement()).getContentDocument();
        BodyElement body = document.getBody();
        body.setAttribute("style", "background-color: #fff; font-size: small; font-family: " + DROID_SANS);

        StyleElement style = createElement(commonResources.generalStyleCss().getText());
        Element headElement = document.getElementsByTagName("head").getItem(0);

        HeadElement head = HeadElement.as(headElement);
        head.insertBefore(style, head.getFirstChild());

        if (formatter != null) {
            formatter.setFontName(DROID_SANS);
        }
    }

    private StyleElement createElement(String contents) {
        StyleElement style = Document.get().createStyleElement();
        style.setPropertyString("language", "text/css");
        style.setInnerText(contents);
        return style;
    }

    private PushButton createPushButton(ImageResource img, String tip) {
        PushButton pb = new PushButton(new Image(img));
        pb.addClickHandler(handler);
        pb.setTitle(tip);
        pb.getElement().setAttribute("style", "display:inline-block");
        return pb;
    }

    private ToggleButton createToggleButton(ImageResource img, String tip) {
        ToggleButton tb = new ToggleButton(new Image(img));
        tb.addClickHandler(handler);
        tb.setTitle(tip);

        tb.getElement().setAttribute("style", "display:inline-block");
        return tb;
    }

    private Label createSeparator() {
        Label separator = new Label();
        separator.setStyleName(commonResources.richTextToolbarStyle().separator());
        return separator;
    }

    private ListBox createFonts() {
        ListBox fontsList = new ListBox();
        fontsList.addItem("Serif", "Georgia, serif");
        fontsList.addItem("Sans-Serif", DROID_SANS);
        fontsList.addItem("Monospace", "Courier New, Courier, monospace");
        fontsList.addChangeHandler(handler);

        return fontsList;
    }

    private ListBox createFontSize() {
        ListBox sizeList = new ListBox();
        sizeList.addItem(XX_SMALL);
        sizeList.addItem(X_SMALL);
        sizeList.addItem(SMALL);
        sizeList.addItem(MEDIUM);
        sizeList.addItem(LARGE);
        sizeList.addItem(X_LARGE);
        sizeList.addItem(XX_LARGE);
        sizeList.addChangeHandler(handler);

        return sizeList;
    }

    /**
     * Updates the status of all the stateful buttons.
     */
    private void updateStatus() {
        if (formatter != null) {
            bold.setDown(formatter.isBold());
            italic.setDown(formatter.isItalic());
            underline.setDown(formatter.isUnderlined());
            strikeThrough.setDown(formatter.isStrikethrough());

            String fontSize = queryCommandValue(richText, FONT_SIZE);
            String fontName = queryCommandValue(richText, FONT_NAME);

            selectFontSize(fontSize);
            selectFontName(fontName);
        }
    }

    private void selectFontName(String fontName) {
        for (int i = 0; i < fonts.getItemCount(); i++) {
            String value = fonts.getValue(i);
            if (value.equals(fontName)) {
                fonts.setSelectedIndex(i);
                break;
            }
        }
    }

    private void selectFontSize(String fontSize) {
        int index = 1;
        try {
            index = Integer.valueOf(fontSize) - 1;
        } catch (NumberFormatException e) {
        }
        this.fontSize.setSelectedIndex(index);
    }

    native String queryCommandValue(RichTextArea richText, String cmd) /*-{
        var impl = richText.@com.google.gwt.user.client.ui.RichTextArea::impl;
        return impl.@com.google.gwt.user.client.ui.impl.RichTextAreaImplStandard::queryCommandValue(Ljava/lang/String;)(cmd);
    }-*/;
}
