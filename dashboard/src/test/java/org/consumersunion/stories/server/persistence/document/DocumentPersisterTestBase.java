package org.consumersunion.stories.server.persistence.document;

import org.consumersunion.stories.common.shared.model.document.Document;

import junit.framework.TestCase;

public abstract class DocumentPersisterTestBase extends TestCase {

    protected void compareDocuments(final Document expected, final Document found) {
        assertEquals("Unexpected permalink", expected.getVersion(), found.getVersion());
        compareUpdatedDocuments(expected, found);
    }

    protected void compareUpdatedDocuments(final Document expected, final Document found) {
        assertEquals("Unexpected permalink", expected.getPermalink(), found.getPermalink());
        assertEquals("Unexpected systemEntity", expected.getSystemEntity(), found.getSystemEntity());
        assertEquals("Unexpected permalink", expected.getSystemEntityRelation(), found.getSystemEntityRelation());
        assertEquals("Unexpected permalink", expected.getPrimaryAuthor(), found.getPrimaryAuthor());
        assertEquals("Unexpected permalink", expected.isPublic(), found.isPublic());
    }
}
