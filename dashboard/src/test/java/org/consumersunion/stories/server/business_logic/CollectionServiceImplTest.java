package org.consumersunion.stories.server.business_logic;

import java.util.ArrayList;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.DocumentsContainer;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class CollectionServiceImplTest {
    public static class MyModule extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(CollectionPersister.class);
        }
    }

    @Inject
    CollectionServiceImpl collectionService;
    @Inject
    CollectionIndexerService collectionIndexerService;
    @Inject
    CollectionPersister collectionPersister;

    @Test
    public void createCollection_willIndex() {
        // Given
        Collection collection = new Collection();
        collection.getBodyDocument().setTitle("some title");
        given(collectionPersister.createCollection(collection)).willReturn(collection);

        // When
        collectionService.createCollection(collection);

        // Then
        verify(collectionIndexerService).index(same(collection));
    }

    @Test
    public void getCollectionForRole_willGetDocuments(
            DocumentService documentService,
            AuthorizationService authService) {
        // given
        int aCollectionId = 1;
        CollectionData collectionData = Mockito.mock(CollectionData.class);
        Collection collection = Mockito.mock(Collection.class);
        ArrayList<Document> documents = new ArrayList<Document>();

        given(collectionData.getCollection()).willReturn(collection);
        given(documentService.getDocuments(Matchers.eq(collection))).willReturn(documents);
        given(collectionPersister.getCollectionData(Matchers.eq(aCollectionId))).willReturn(collectionData);
        given(authService.isUserAuthorized(eq(AuthConstants.ROLE_READER), same(collection))).willReturn(true);

        // when
        collectionService.getCollectionForRole(aCollectionId, AuthConstants.ROLE_READER);

        // then
        verify(documentService).getDocuments(collection);
        verify(collectionData).setDocuments(Matchers.any(DocumentsContainer.class));
    }
}
