package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.CollectionSortField;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.server.persistence.funcs.DeleteFunc;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class CollectionPersisterTest extends SpringTestCase {
    @Inject
    private CollectionPersister collectionPersister;
    @Inject
    private PersistenceService persistenceService;
    @Inject
    private BlockPersistenceHelper blockPersistenceHelper;
    @Inject
    private TagsPersistenceHelper tagsPersistenceHelper;

    public void testCreate() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM collection");
            if (!results.next()) {
                fail("No results");
            }

            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the udpate

            final Collection template = createCollectionTemplate();
            template.setPermalink("permalink-testCreate");
            template.setStories(createStoryLinks());
            final Collection collectionResult = collectionPersister.createCollection(template);

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM collection");
            if (!results.next()) {
                fail("No results");
            }
            final long postCount = results.getLong(1);
            assertEquals("New user count didn't match.", initialCount + 1, postCount);
            assertTrue("Bad ID", template.getId() > 0);
            assertEquals("Unexpected version", 1, template.getVersion());
            assertNotNull(collectionResult);

            results = conn.createStatement().executeQuery(
                    "SELECT * FROM collection_story WHERE collection=" + collectionResult.getId());
            if (!results.next()) {
                fail("No StoryLinks were persisted");
            }

            deleteCollection(template);
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            conn.close();
        }
    }

    public void testRetrieve() {
        Collection template = createCollectionTemplate();
        template = collectionPersister.create(template);

        Collection retrieved = collectionPersister.get(template.getId());

        assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
        assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
        assertEquals("Unexpected permalink.", template.getPermalink(), retrieved.getPermalink());
        assertEquals("Unexpected source count", template.getCollectionSources().size(),
                retrieved.getCollectionSources().size());
        for (Integer id : template.getCollectionSources()) {
            assertTrue("Did not find expected questionnaire ID '" + id + "'",
                    retrieved.getCollectionSources().contains(id));
        }
        assertEquals("Unexpected organization.", template.getOwner(), retrieved.getOwner());
        assertEquals("Unexpected public", template.isPublic(), retrieved.isPublic());
        assertEquals("Unexpected profile", template.getProfile(), retrieved.getProfile());
        deleteCollection(template);
    }

    public void testUpdate() {
        Collection template = createCollectionTemplate();
        template.setPermalink("link-testUpdate");
        template = collectionPersister.create(template);
        template.setOwner(2);
        template.setPermalink("my-link");
        template.setProfile(55);
        template.setPublic(false);
        template.setDeleted(false);
        template.getCollectionSources().add(51);
        Document bodyDocument = template.getBodyDocument();
        bodyDocument.setTitle("Collection Title 2");
        bodyDocument.setPrimaryAuthor(PersistersTestUtils.getRootProfileIdForOrganization(2));
        template.setStories(createStoryLinks());
        collectionPersister.updateCollection(template);
        assertEquals("Unexpected version.", 2, template.getVersion());

        final Collection retrieved = collectionPersister.get(template.getId());
        assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
        assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
        assertEquals("Unexpected permalink.", template.getPermalink(), retrieved.getPermalink());
        assertEquals("Unexpected source count", template.getCollectionSources().size(),
                retrieved.getCollectionSources().size());
        for (Integer id : template.getCollectionSources()) {
            assertTrue("Did not find expected questionnaire ID '" + id + "'",
                    retrieved.getCollectionSources().contains(id));
        }
        assertEquals("Unexpected organization.", template.getOwner(), retrieved.getOwner());
        assertEquals("Unexpected public", template.isPublic(), retrieved.isPublic());
        assertEquals("Unexpected profile", template.getProfile(), retrieved.getProfile());

        assertEquals("Unexpected story count", template.getStories().size(), retrieved.getStories().size());
        assertFalse("Unexpected story count", retrieved.getStories().isEmpty());
        deleteCollection(template);
    }

    public void testCollectionCountByStoryId() throws SQLException {
        final RetrievePagedCollectionsParams param = new RetrievePagedCollectionsParams.Builder()
                .withStart(0)
                .withLength(3)
                .withSortField(CollectionSortField.TITLE_A_Z)
                .withAscending(true)
                .withStoryId(6)
                .withAccessMode(ACCESS_MODE_ANY)
                .withPermissionMask(ROLE_READER)
                .withEffectiveId(1001)
                .build();
        assertEquals("Unexpected count countCollectionsByStory", 1,
                PersistenceUtil.process(new CollectionPersister.CountCollectionsByStory(param)).getNbCollections());
    }

    public void testPagedGetCollectionsByStory() {
        final RetrievePagedCollectionsParams params = new RetrievePagedCollectionsParams.Builder()
                .withStart(0)
                .withLength(4)
                .withSortField(CollectionSortField.TITLE_A_Z)
                .withAscending(true)
                .withStoryId(6)
                .withAccessMode(ACCESS_MODE_ANY)
                .withPermissionMask(ROLE_READER)
                .withEffectiveId(1001)
                .build();
        @SuppressWarnings("unchecked")
        final List<CollectionData> cl = collectionPersister.retrievePagedCollectionsByStory(params);
        checkTitleOrder(cl, true);

        assertEquals("There must be 1 collection for story 1", 1, cl.size());
        assertTrue("First Collection id does not match", cl.get(0).getId() == 36 || cl.get(0).getId() == 37);

        final RetrievePagedCollectionsParams params2 = new RetrievePagedCollectionsParams.Builder()
                .withStart(0)
                .withLength(4)
                .withSortField(CollectionSortField.TITLE_A_Z)
                .withAscending(false)
                .withStoryId(6)
                .withAccessMode(ACCESS_MODE_ANY)
                .withPermissionMask(ROLE_READER)
                .withEffectiveId(1001)
                .build();
        @SuppressWarnings("unchecked")
        final List<CollectionData> cl2 = collectionPersister.retrievePagedCollectionsByStory(params2);
        checkTitleOrder(cl2, false);

        assertEquals("There must be 1 collections for story 2", 1, cl2.size());
        assertTrue("First Collection id does not match", cl2.get(0).getId() == 37 || cl2.get(0).getId() == 36);
    }

	/* can't run this test until spring is set up for tests 
    @SuppressWarnings("unchecked")
	public void testPagedGetCollections() {
		final CollectionPersister persister = (CollectionPersister) PersistenceService
				.getPersisterFor(Collection.class);

		final RetrievePagedCollectionsParams params = new RetrievePagedCollectionsParams(0, 3,
				CollectionSortField.TITLE, true, "", ACCESS_MODE_ANY, null);
		final List<CollectionData> cl = (List<CollectionData>) PersistenceService.process(persister
				.retrievePagedFunc(params));
		checkTitleOrder(cl, true);

		final RetrievePagedCollectionsParams params2 = new RetrievePagedCollectionsParams(0, 3,
				CollectionSortField.TITLE, false, "", ACCESS_MODE_ANY, null);
		final List<CollectionData> cl2 = (List<CollectionData>) PersistenceService.process(persister
				.retrievePagedFunc(params2));
		checkTitleOrder(cl2, false);
	}
	*/

    private void checkTitleOrder(final List<CollectionData> cl, final boolean ascending) {
        final Iterator<CollectionData> it = cl.iterator();
        if (cl.size() < 1) {
            fail("Not enough results found, can't check the ordering");
        }

        CollectionData prev = it.next();
        while (it.hasNext()) {
            final CollectionData current = it.next();
            final int diff = prev.getTitle().compareTo(current.getTitle());
            final boolean matches = (diff < 0) == ascending;

            assertTrue("Wrong ordering", diff == 0 || matches);

            prev = current;
        }
    }

	/*
    @SuppressWarnings("unchecked")
	public void testPagedTagRetrieval() {
		final CollectionPersister persister = (CollectionPersister) PersistenceService
				.getPersisterFor(Collection.class);
		final EntityPersister entPersister = (EntityPersister) PersistenceService.getPersisterFor(Entity.class);

		Collection collection = createCollectionTemplate();
		collection.setPermalink("link-testPagedTagRetrieval");
		collection = (Collection) PersistenceService.process(persister.createFunc(collection));
		final List<String> tags = Arrays.asList("tag1", "tag2", "tag3", "tag4", "tag5");
		final UpdateTagsParams updateParams = new UpdateTagsParams(collection, tags);
		PersistenceService.process(entPersister.updateTags(updateParams));

		final RetrievePagedCollectionsParams params = new RetrievePagedCollectionsParams(0, 10,
				CollectionSortField.TITLE, true, "", ACCESS_MODE_ANY, null);
		final List<CollectionData> cds = (List<CollectionData>) PersistenceService.process(persister
				.retrievePagedFunc(params));
		CollectionData cd = null;
		for (final CollectionData t : cds) {
			if (t.getId() == collection.getId()) {
				cd = t;
				break;
			}
		}

		assertNotNull("Search page didn't contain expected collection", cd);
		final List<String> retrievedTags = cd.getTags();

		assertTrue("Unexpected amount of tags returned", retrievedTags.size() == 5);
		assertTrue("Test tag not found", retrievedTags.contains("tag1"));
		assertTrue("Test tag not found", retrievedTags.contains("tag5"));

		PersistenceService.process(persister.deleteFunc(collection));
	}*/

	/* solr is not integrated into the testing facility... so we can't do this test.
	@SuppressWarnings("unchecked")
	public void testTagRetrieval() {
		// The CollectionPersister uses solr, so we have to set up Spring
		final ApplicationContext ctx = new GenericApplicationContext();
		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((GenericApplicationContext) ctx);
		xmlReader.loadBeanDefinitions(new ClassPathResource("/META-INF/applicationContext.xml"));
		((GenericApplicationContext) ctx).refresh();
		ApplicationContextProvider appContext = new ApplicationContextProvider();
		appContext.setApplicationContext(ctx);
		
		final CollectionPersister persister = (CollectionPersister) PersistenceService
				.getPersisterFor(Collection.class);

		final EntityPersister entPersister = (EntityPersister) PersistenceService.getPersisterFor(Entity.class);

		final Collection collection = createCollectionTemplate();
		collection.setPermalink("link-testTagRetrieval");
		PersistenceService.process(new CollectionPersister.CreateCollectionFunc(collection));
		final List<String> tags = Arrays.asList("tag1", "tag2", "tag3", "tag4", "tag5");
		final UpdateTagsParams updateParams = new UpdateTagsParams(collection, tags);
		PersistenceService.process(new EntityPersister.UpdateTagsFunc(updateParams));

		final List<CollectionData> cds = (List<CollectionData>) PersistenceService.process(persister.
				retrievePagedFunc(new RetrievePagedCollectionsParams(0, 100, true, CollectionSortField.TITLE, true,
				AuthorizationService.ACCESS_MODE_ROOT, 2)));

		CollectionData cd = null;
		for (final CollectionData t : cds) {
			if (t.getId() == collection.getId()) {
				cd = t;
				break;
			}
		}

		assertNotNull("Search page didn't contain expected collection", cd);
		final List<String> retrievedTags = cd.getTags();

		assertTrue("Unexpected amount of tags returned", retrievedTags.size() == 5);
		assertTrue("Test tag not found", retrievedTags.contains("tag1"));
		assertTrue("Test tag not found", retrievedTags.contains("tag5"));

		PersistenceService.process(new DeleteCollectionFunc(collection));
	}
	*/

    public static Collection createCollectionTemplate() {
        final Collection template = new Collection();
        template.setOwner(2);
        template.setPermalink("link");
        template.setProfile(54);
        template.setPublic(true);
        template.getCollectionSources().add(33);
        Document bodyDocument = template.getBodyDocument();
        bodyDocument.setTitle("Collection Title");
        bodyDocument.setPrimaryAuthor(PersistersTestUtils.getRootProfileIdForOrganization(2));
        bodyDocument.addBlock(new TextImageBlock("Hey!"));
        template.setDeleted(false);

        return template;
    }

    private Set<StoryLink> createStoryLinks() {
        final Set<StoryLink> result = new HashSet<StoryLink>();

        final StoryLink sl = new StoryLink(6, true);
        result.add(sl);

        return result;
    }
	
	/* can't run this test until spring is set up for tests 
	public void testRetrieveCollectionsByUsers() throws SQLException {
		/*
		 * final Connection conn = PersistenceService.getConnection();
		 * conn.createStatement
		 * ().executeUpdate("DELETE FROM stories.acl_entry"); conn.commit();
		 * conn.createStatement().executeUpdate("INSERT INTO acl_entry " +
		 * "(id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES"
		 * + // the #2 acl_object_identity corresponds with collection #36,
		 * grant read to users 1 & 2 "(1, 2,0, 2,1,1,1,1)," +
		 * "(2, 2,1, 1,1,1,1,1)," + // acl_object_identity #3 is id 37
		 * "(3, 3,0, 1,1,1,1,1)"); conn.commit();
		 * /

		final CollectionPersister collectionPersister = new CollectionPersister();

		final User userWithAllPermissions = new User();
		userWithAllPermissions.setId(1);
		final Organization userWithPermissions = new Organization();
		userWithPermissions.setId(2);
		final User userWithoutPermissions = new User();
		userWithoutPermissions.setId(38);

		final RetrievePagedCollectionsParams requestUserWithAllAccess = new RetrievePagedCollectionsParams(0, 10,
				CollectionSortField.TITLE, true, "", ACCESS_MODE_ANY, userWithAllPermissions.getId());
		final RetrievePagedCollectionsParams requestUserWithoutAccess = new RetrievePagedCollectionsParams(0, 10,
				CollectionSortField.TITLE, true, "", ACCESS_MODE_EXPLICIT, userWithoutPermissions.getId());

		final RetrievePagedCollectionsParams requestUserWithoutAccessPublic = new RetrievePagedCollectionsParams(0, 10,
				CollectionSortField.TITLE, true, "", ACCESS_MODE_PUBLIC, userWithoutPermissions.getId());

		@SuppressWarnings("unchecked")
		final List<CollectionData> listUserAllPermisions = (List<CollectionData>) PersistenceService
				.process(collectionPersister.retrievePagedFunc(requestUserWithAllAccess));

		assertNotNull("List of collection data for User#1 must not be null", listUserAllPermisions);
		String collectionMembers = ""; // build useful description for when
										// there's a problem
		for (CollectionData data : listUserAllPermisions)
			collectionMembers += "(" + data.getId() + ")";
		// 4 and 5 are public
		// user 1 has read over 36
		// user is owner of 52
		// that's two public and one with direct permissions (out of a set of
		// something like 8)
		assertEquals("unexpected element count; " + collectionMembers, 4, listUserAllPermisions.size());
		for (final CollectionData data : listUserAllPermisions) {
			assertTrue("Collection id does not match; " + collectionMembers, data.getCollection().getId() == 4
					|| data.getCollection().getId() == 5 || data.getCollection().getId() == 52
					|| data.getCollection().getId() == 36);
		}

		@SuppressWarnings("unchecked")
		final List<CollectionData> listUserPublicAccess = (List<CollectionData>) PersistenceService
				.process(collectionPersister.retrievePagedFunc(requestUserWithoutAccessPublic));

		assertNotNull("List of collection data for User#2 must not be null", listUserPublicAccess);
		collectionMembers = ""; // build useful description for when there's a
								// problem
		for (CollectionData data : listUserPublicAccess)
			collectionMembers += "(" + data.getId() + ")";
		assertEquals("unexpected element count; " + collectionMembers, 2, listUserPublicAccess.size());
		for (final CollectionData data : listUserPublicAccess) {
			assertTrue("Collection id does not match; " + collectionMembers, data.getCollection().getId() == 4
					|| data.getCollection().getId() == 5);
		}

		@SuppressWarnings("unchecked")
		final List<CollectionData> listUserOutPermisions = (List<CollectionData>) PersistenceService
				.process(collectionPersister.retrievePagedFunc(requestUserWithoutAccess));

		assertNotNull("List of collection data for User#38 must not be null", listUserOutPermisions);
		collectionMembers = ""; // build useful description for when there's a
								// problem
		for (CollectionData data : listUserOutPermisions)
			collectionMembers += "(" + data.getId() + ")";
		assertEquals("unexpected element count; " + collectionMembers, 3, listUserOutPermisions.size());
		for (CollectionData data : listUserOutPermisions) {
			assertTrue("Collection id does not match; " + collectionMembers, data.getCollection().getId() == 40
					|| data.getCollection().getId() == 43 || data.getCollection().getId() == 44);
		}
	}*/

    void deleteCollection(final Collection collection) {
        persistenceService.process(
                new DeleteFunc<Collection>(collection) {
                    @Override
                    public Collection process() {
                        checkVersion();
                        try {
                            blockPersistenceHelper.deleteBlocks(input.getBodyDocument().getId(), conn);
                            deleteDocuments(conn, input.getId());
                            deleteStoryLinks();
                            deleteTags();
                            deleteSourcesAndTargets();

                            final PreparedStatement delete = conn.prepareStatement("DELETE FROM collection WHERE " +
                                    "id=?");
                            delete.setInt(1, input.getId());

                            final int updateCount = delete.executeUpdate();
                            if (updateCount != 1) {
                                throw new GeneralException("Unexpected delete count: " + updateCount);
                            }

                            deleteEntityRecordAndUpdateInput();

                            return input;
                        } catch (final SQLException e) {
                            throw new GeneralException(e);
                        }
                    }

                    private void deleteDocuments(Connection conn, int id) throws SQLException {
                        PreparedStatement preparedStatement = conn.prepareStatement(
                                "DELETE FROM document WHERE systemEntity=?");
                        preparedStatement.setInt(1, id);
                        preparedStatement.executeUpdate();
                    }

                    private void deleteSourcesAndTargets() throws SQLException {
                        final PreparedStatement delete = conn.prepareStatement(
                                "DELETE FROM collection_sources WHERE targetCollection=?");
                        delete.setInt(1, input.getId());

                        delete.executeUpdate();
                    }

                    protected void deleteStoryLinks() throws SQLException {
                        CollectionStoryLinkPersistenceHelper.deleteStoryLinks(input, conn);
                    }

                    protected void deleteTags() throws SQLException {
                        tagsPersistenceHelper.deleteTags(input, conn);
                        tagsPersistenceHelper.deleteAutoTags(input, conn);
                    }
                });
    }
}
