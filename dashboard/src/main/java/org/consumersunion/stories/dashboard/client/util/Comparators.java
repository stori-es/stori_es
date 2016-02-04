package org.consumersunion.stories.dashboard.client.util;

import java.util.Comparator;

import org.consumersunion.stories.common.shared.model.document.Document;

import static com.google.common.base.Strings.nullToEmpty;

public class Comparators {
    public static final Comparator<Document> NOTE_CREATED_TIME_COMPARATOR = new Comparator<Document>() {
        @Override
        public int compare(Document o1, Document o2) {
            return o2.getCreated().compareTo(o1.getCreated());
        }
    };

    public static final Comparator<Document> ATTACHMENT_TITLE_AZ_COMPARATOR = new Comparator<Document>() {
        @Override
        public int compare(Document o1, Document o2) {
            return nullToEmpty(o1.getTitle()).compareTo(nullToEmpty(o2.getTitle()));
        }
    };
}
