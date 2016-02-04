package org.consumersunion.stories.dashboard.client.application.util;

import org.consumersunion.stories.common.shared.model.document.Document;

import com.google.gwt.text.shared.AbstractRenderer;

public class DocumentRenderer extends AbstractRenderer<Document> {
    @Override
    public String render(Document document) {
        if (document != null) {
            return document.getTitle();
        } else {
            return "";
        }
    }
}
