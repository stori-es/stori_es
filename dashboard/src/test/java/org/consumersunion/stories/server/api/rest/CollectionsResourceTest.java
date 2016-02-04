package org.consumersunion.stories.server.api.rest;

import java.util.HashSet;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.CollectionsApiResponse;
import org.consumersunion.stories.common.shared.dto.post.CollectionPost;
import org.consumersunion.stories.common.shared.dto.post.CollectionPut;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.server.api.rest.converters.CollectionConverter;
import org.consumersunion.stories.server.api.rest.merger.CollectionPutMerger;
import org.consumersunion.stories.server.business_logic.CollectionService;
import org.consumersunion.stories.server.business_logic.TagsService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.common.collect.Sets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class CollectionsResourceTest {

    private static final int COLLECTION_ID = 3;

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(CollectionConverter.class);
            forceMock(CollectionPutMerger.class);
        }
    }

    private static final int ORGANIZATION_ID = 10;

    @Inject
    private CollectionsResource collectionsResource;
    @Inject
    private CollectionConverter collectionConverter;
    @Inject
    private UserService userService;
    @Inject
    private CollectionService collectionService;
    @Inject
    private TagsService tagsService;
    @Inject
    private CollectionPutMerger collectionPutMerger;

    @Test
    public void createCollection() throws Exception {
        CollectionPost collectionPost = new CollectionPost();
        HashSet<String> tags = Sets.newHashSet("a", "b");
        collectionPost.setTags(tags);
        Collection collection = spy(new Collection());
        Collection savedCollection = new Collection();
        given(collectionConverter.convert(same(collectionPost))).willReturn(collection);
        given(collectionService.createCollection(same(collection))).willReturn(savedCollection);
        given(userService.getContextOrganizationId()).willReturn(ORGANIZATION_ID);

        collectionsResource.createCollection(collectionPost);

        InOrder inOrder = Mockito.inOrder(collectionConverter, userService, collectionService, tagsService, collection);
        inOrder.verify(collectionConverter).convert(same(collectionPost));
        inOrder.verify(collection).setOwner(ORGANIZATION_ID);
        inOrder.verify(collectionService).createCollection(same(collection));
        inOrder.verify(tagsService).setTags(same(savedCollection), eq(tags));
    }

    @Test
    public void updateCollection() throws Exception {
        HashSet<String> tags = Sets.newHashSet("a", "b");
        CollectionPut collectionPut = new CollectionPut();
        collectionPut.setTags(tags);
        Collection collection = spy(new Collection());
        CollectionData collectionData = new CollectionData(collection, tags);
        given(collectionService.getCollectionData(COLLECTION_ID)).willReturn(collectionData);
        Collection savedCollection = new Collection();
        given(collectionService.updateCollection(same(collection))).willReturn(savedCollection);

        collectionsResource.updateCollection(COLLECTION_ID, collectionPut);

        InOrder inOrder =
                Mockito.inOrder(collectionConverter, collectionPutMerger, collectionService, tagsService, collection);
        inOrder.verify(collectionPutMerger).merge(same(collection), same(collectionPut));
        inOrder.verify(collectionService).updateCollection(same(collection));
        inOrder.verify(tagsService).setTags(same(savedCollection), eq(tags));
    }

    @Test
    public void getCollection() throws Exception {
        CollectionData collectionData = new CollectionData(new Collection(COLLECTION_ID, 1), new HashSet<String>());
        given(collectionService.getCollectionData(COLLECTION_ID)).willReturn(collectionData);

        Response response = collectionsResource.getCollection(COLLECTION_ID);

        CollectionsApiResponse apiResponse = (CollectionsApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(COLLECTION_ID);
    }

    @Test
    public void deleteCollection() throws Exception {
        collectionsResource.deleteCollection(COLLECTION_ID);

        verify(collectionService).deleteCollection(COLLECTION_ID);
    }
}
