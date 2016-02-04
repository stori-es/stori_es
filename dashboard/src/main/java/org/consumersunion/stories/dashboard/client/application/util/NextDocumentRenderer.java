package org.consumersunion.stories.dashboard.client.application.util;

import org.consumersunion.stories.common.shared.model.document.SubmitBlock.NextDocument;

import com.google.gwt.text.shared.AbstractRenderer;

public class NextDocumentRenderer extends AbstractRenderer<NextDocument> {
    @Override
    public String render(NextDocument document) {
        if (document != null) {
            return document.getTitle();
        } else {
            return "";
        }
    }
}
