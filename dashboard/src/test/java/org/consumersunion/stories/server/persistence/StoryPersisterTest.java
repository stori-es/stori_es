package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.server.persistence.StoryPersister.StoryPagedRetrieveParams;

public class StoryPersisterTest extends SpringTestCase {
    @Inject
    private StoryPersister persister;
    @Inject
    private DocumentPersister documentPersister;
    @Inject
    private PersistenceService persistenceService;

    public void testCreate() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        final String byLine = "Created by someone";
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM story");
            if (!results.next()) {
                fail("No results");
            }

            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update

            final Story template = createTemplate();
            template.setByLine(byLine);

            final Story storyResult = (Story) PersistenceUtil.process(new StoryPersister.CreateStoryFunc(template));

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM story");
            if (!results.next()) {
                fail("No results");
            }
            final long postCount = results.getLong(1);

            assertEquals("New story count didn't match.", initialCount + 1, postCount);
            assertTrue("Bad ID", template.getId() > 0);
            assertEquals("Unexpected version", 1, template.getVersion());
            assertEquals("ByLine was not inserted properly", byLine, storyResult.getByLine());
            assertEquals("AnswerSetId was not persisted", (Integer) 6,
                    template.getOnlyDocument(SystemEntityRelation.ANSWER_SET));
            assertNotNull(storyResult);

            PersistenceUtil.process(new StoryPersister.DeleteStoryFunc(template, persister));
        } finally {
            conn.close();
        }
    }

    public void testRetrieve() {
        final Story template = createTemplate();
        PersistenceUtil.process(new StoryPersister.CreateStoryFunc(template));

        final Story retrieved = (Story)
                PersistenceUtil.process(new StoryPersister.RetrieveStoryFunc(template.getId()));

        assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
        assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
        assertEquals("Unexpected permalink.", template.getPermalink(), retrieved.getPermalink());
        assertEquals("Unexpected defaultContent", template.getDefaultContent(),
                retrieved.getDefaultContent());
        assertEquals("Unexpected owner", template.getOwner(), retrieved.getOwner());
        assertEquals("Unexpected ByLine", template.getByLine(), retrieved.getByLine());

        // MySQL ignores the millis of the timestamp.
        final long diff = Math.abs(template.getFirstPublished().getTime() - retrieved.getFirstPublished().getTime());
        assertTrue("Unexpected FirstPublished date", diff < 1000);

        PersistenceUtil.process(new StoryPersister.DeleteStoryFunc(template, persister));
    }

    public void testUpdate() {
        final Story template = createTemplate();
        PersistenceUtil.process(new StoryPersister.CreateStoryFunc(template));

        final String originalByLine = template.getByLine();
        template.setByLine("Another byline");
        template.setPermalink("perma2");

        PersistenceUtil.process(new StoryPersister.UpdateStoryFunc(template));
        assertEquals("Unexpected version.", 2, template.getVersion());

        final Story retrieved = (Story)
                PersistenceUtil.process(new StoryPersister.RetrieveStoryFunc(template.getId()));

        assertEquals("Unexpected ID.", template.getId(), retrieved.getId());
        assertEquals("Unexpected version.", template.getVersion(), retrieved.getVersion());
        assertEquals("Unexpected permalink.", template.getPermalink(), retrieved.getPermalink());
        assertEquals("Unexpected defaultContent", template.getDefaultContent(),
                retrieved.getDefaultContent());
        assertEquals("Unexpected owner", template.getOwner(), retrieved.getOwner());
        assertEquals("ByLine should not be updated", originalByLine, retrieved.getByLine());

        PersistenceUtil.process(new StoryPersister.DeleteStoryFunc(template, persister));
    }

    public void testDelete() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            final Story template = createTemplate();
            PersistenceUtil.process(new StoryPersister.CreateStoryFunc(template));

            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM story");
            if (!results.next()) {
                fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the udpate
            PersistenceUtil.process(new StoryPersister.DeleteStoryFunc(template, persister));

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM story");
            if (!results.next()) {
                fail("No results");
            }
            final long postCount = results.getLong(1);

            assertEquals("New collection count didn't match.", initialCount - 1, postCount);
            assertEquals("Bad ID", -1, template.getId());
            assertEquals("Bad ID", -1, template.getVersion());
        } finally {
            conn.close();
        }
    }

    /* This test requires Solr index to be run */
    public void _testSearchSpecificField() {
        StoryPagedRetrieveParams params = new StoryPagedRetrieveParams(0, 0, StorySortField.CREATED_OLD, false,
                "title:title6", ACCESS_MODE_ANY, 1, null, null, null, null, null, ROLE_READER, 0);

        List<StorySummary> stories = persistenceService.process(persister.getStoriesPagedFunc(params, false));

        assertEquals("Unexpected story count.", 1, stories.size());
        assertEquals("Unexpected story title.", "Story Title6", stories.get(0).getTitle());
    }

    /*Recent search is not used anywhere in the application; the function has been commented out in the service; I'm
    leaving this in because I'm not sure if the non-use is intentional or not
    public void testGetRecent() {
        final StoryPersister persister = (StoryPersister) PersistenceService.getPersisterFor(Story.class);
        final RecentStoriesSearchParams params = new RecentStoriesSearchParams(36, 10);

        @SuppressWarnings("unchecked")
        final List<StorySummary> summaries = (List<StorySummary>) PersistenceService.process(persister
                .retrieveRecentFunc(params));

        @SuppressWarnings("unchecked")
        final RetrieveFunc<Collection> collectionGetter = (RetrieveFunc<Collection>) PersistenceService.retrieve(
                Collection.class, 36);
        final Collection collection = (Collection) PersistenceService.process(collectionGetter);

        final List<Integer> approved = getApprovedStoriesIds(collection.getStories());
        final List<Integer> unApproved = getUnApprovedStoriesIds(collection.getStories());

        assertEquals("", 1, summaries.size());
        for (final StorySummary summ : summaries) {
            assertTrue("Story not found in approved list", approved.contains(new Integer(summ.getStory().getId())));
            assertFalse("Story found in unapproved list", unApproved.contains(new Integer(summ.getStory().getId())));
        }
    }

    private List<Integer> getApprovedStoriesIds(final Set<StoryLink> stories) {
        final List<Integer> r = new ArrayList<Integer>();

        for (final StoryLink link : stories) {
            if (link.getIsClearedForPublicInclusion())
                r.add(link.getStory());
        }
        return r;
    }

    private List<Integer> getUnApprovedStoriesIds(final Set<StoryLink> stories) {
        final List<Integer> r = new ArrayList<Integer>();

        for (final StoryLink link : stories) {
            if (link.getIsClearedForPublicInclusion() == false)
                r.add(link.getStory());
        }
        return r;
    }
    */
    private Story createTemplate() {
        final Story s = new Story(-1, -1);
        s.setDefaultContent(55);
        s.setFirstPublished(new Date());
        s.setOwner(1001);
        s.setPermalink("permalink");
        s.setPublished(true);
        s.setByLine("byline");
        s.addDocument(SystemEntityRelation.ANSWER_SET, 6);

        return s;
    }

    public void testLimitCharacters() {
        final String testString = " Hey my name is <single/><em>Zane</em>. I like cheese.";

        final String result1 = persister.limitCharacters(testString, 1);
        assertEquals("Hey...", result1);

        final String result2 = persister.limitCharacters(testString, 4);
        assertEquals("Hey...", result2);

        final String result3 = persister.limitCharacters(testString, 5);
        assertEquals("Hey my...", result3);

        final String result4 = persister.limitCharacters(testString, 16);
        assertEquals("Hey my name is Zane...", result4);
    }

	/*
     * SYSTWO-
	 * 
	 * @SuppressWarnings("unchecked") public void
	 * testGetStoriesSearchNotResults(){ //search for a string with no results
	 * (like 'xxxxxxxxxxxxxxxxxxxx' or something) final User user = new User();
	 * user.setId(1); final StoryPagedRetrieveParams searchParams = new
	 * StoryPagedRetrieveParams
	 * (0,10,StorySortField.STORYTELLER,true,"xxxxxxxxx",
	 * MineCallbackProvider.MINE,user,null); final StoryPersister persister =
	 * (StoryPersister) PersistenceService.getPersisterFor(Story.class); final
	 * List<StorySummary> stories = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * assertTrue("No results for search expected",(stories.size()==0)); }
	 */

    // @SuppressWarnings("unchecked")
    public void testGetStoriesSearchForString() {
        // search for a string where there would be results, but the access
        // limitations prevent it (create or uses a non-public story which the
        // testUser has no rights to in the DB, then test MINE, PUBLIC,
        // AUTHORIZED, and ANY limitations, enusre that the story is not
        // returned)
		/*
		 * final User user = new User(); user.setId(38);
		 * StoryPagedRetrieveParams searchParams = new
		 * StoryPagedRetrieveParams(0
		 * ,10,StorySortField.STORYTELLER,true,"title",
		 * MineCallbackProvider.MINE,user,null); StoryPersister persister =
		 * (StoryPersister) PersistenceService.getPersisterFor(Story.class);
		 * List<StorySummary> stories = (List<StorySummary>)
		 * PersistenceService.process
		 * (persister.getStoriesPagedFunc(searchParams));
		 * assertTrue("No results expected for MINE",(stories.size()==0));
		 * 
		 * user.setId(38); searchParams = new
		 * StoryPagedRetrieveParams(0,10,StorySortField
		 * .STORYTELLER,true,"title",MineCallbackProvider.AUTHORIZED,user,null);
		 * persister = (StoryPersister)
		 * PersistenceService.getPersisterFor(Story.class); stories =
		 * (List<StorySummary>)
		 * PersistenceService.process(persister.getStoriesPagedFunc
		 * (searchParams));
		 * assertTrue("No results expected for AUTHORIZED",(stories.size()==0));
		 */

        // TODO : This test uses now Solr and have to be updated.
    }

	/*
	 * SYSTWO-
	 * 
	 * @SuppressWarnings("unchecked") public void
	 * testGetStoriesSearchNonPublicStoryMineAny(){ //create or use a non-public
	 * story owned by the user; this is determined by the story.owner field;
	 * ensure that this is returned with the 'MINE' and 'ANY', but NOT with
	 * PUBLIC or AUTHORIZED final User user = new User(); user.setId(1);
	 * StoryPagedRetrieveParams searchParams = new
	 * StoryPagedRetrieveParams(0,10,
	 * StorySortField.STORYTELLER,true,"title",MineCallbackProvider
	 * .MINE,user,null); StoryPersister persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); List<StorySummary>
	 * stories = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * boolean result=false; for (final StorySummary story : stories) {
	 * if(story.getStory().getId()==8 || story.getStory().getId()==10){
	 * result=true; break; } }
	 * assertTrue("non-public story results expected for MINE",result);
	 * 
	 * user.setId(1); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"",MineCallbackProvider.ANY,user,null); persister =
	 * (StoryPersister) PersistenceService.getPersisterFor(Story.class); stories
	 * = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * assertTrue
	 * ("non-public story results expected for ANY",(stories.size()>0));
	 * 
	 * user.setId(2); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"title",MineCallbackProvider.PUBLIC,user,null);
	 * persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); stories =
	 * (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * result=false; for (final StorySummary story : stories) {
	 * if(story.getStory().getId()==8 || story.getStory().getId()==10){
	 * result=true; break; } }
	 * assertFalse("non-public story results expected for PUBLIC",result);
	 * 
	 * user.setId(2); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"title",MineCallbackProvider.AUTHORIZED,user,null);
	 * persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); stories =
	 * (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * result=false; for (final StorySummary story : stories) {
	 * if(story.getStory().getId()==8 || story.getStory().getId()==10){
	 * result=true; break; } }
	 * assertFalse("non-public story results expected for AUTHORIZED",result); }
	 */

	/*
	 * SYSTWO-
	 * 
	 * @SuppressWarnings("unchecked") public void
	 * testGetStoriesSearchPublicStoryMinePublic(){ // create or use a public
	 * story owned by the user; this is determined by the story.owner field;
	 * ensure that this is returned with the 'MINE', 'PUBLIC', and 'ANY', but
	 * NOT with AUTHORIZED final User user = new User(); user.setId(1);
	 * StoryPagedRetrieveParams searchParams = new
	 * StoryPagedRetrieveParams(0,10,
	 * StorySortField.STORYTELLER,true,"title",MineCallbackProvider
	 * .MINE,user,null); StoryPersister persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); List<StorySummary>
	 * stories = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * assertTrue("public story results expected for MINE",(stories.size()>0));
	 * 
	 * user.setId(38); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"title",MineCallbackProvider.PUBLIC,user,null);
	 * persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); stories =
	 * (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * assertTrue
	 * ("public story results expected for PUBLIC",(stories.size()>0));
	 * 
	 * user.setId(1); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"",MineCallbackProvider.ANY,user,null); persister =
	 * (StoryPersister) PersistenceService.getPersisterFor(Story.class); stories
	 * = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * assertTrue("public story results expected for ANY",(stories.size()>0));
	 * 
	 * user.setId(38); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"title",MineCallbackProvider.AUTHORIZED,user,null);
	 * persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); stories =
	 * (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * assertTrue
	 * ("public story results expected for AUTHORIZED",(stories.size()==0)); }
	 * 
	 * @SuppressWarnings("unchecked") public void
	 * testGetStoriesSearchPublicStoryAnyPublic(){ //create a public story not
	 * owned by the user, this should be returned with 'PUBLIC' or 'ANY', but
	 * not MINE or AUTHORIZED final User user = new User(); user.setId(38);
	 * StoryPagedRetrieveParams searchParams = new
	 * StoryPagedRetrieveParams(0,10,
	 * StorySortField.STORYTELLER,true,"title",MineCallbackProvider
	 * .PUBLIC,user,null); StoryPersister persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); List<StorySummary>
	 * stories = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * assertTrue
	 * ("public story results expected for PUBLIC",(stories.size()>0));
	 * 
	 * user.setId(38); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"",MineCallbackProvider.ANY,user,null); persister =
	 * (StoryPersister) PersistenceService.getPersisterFor(Story.class); stories
	 * = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * assertTrue("public story results expected for ANY",(stories.size()>0));
	 * 
	 * user.setId(38); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"title",MineCallbackProvider.MINE,user,null); persister
	 * = (StoryPersister) PersistenceService.getPersisterFor(Story.class);
	 * stories = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * boolean result=false; for (final StorySummary story : stories) {
	 * if(story.getStory().getId()==8 || story.getStory().getId()==10 ||
	 * story.getStory().getId()==12){ result=true; break; } }
	 * assertFalse("public story results expected for MINE",result);
	 * 
	 * user.setId(38); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"title",MineCallbackProvider.AUTHORIZED,user,null);
	 * persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); stories =
	 * (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * result=false; for (final StorySummary story : stories) {
	 * if(story.getStory().getId()==8 || story.getStory().getId()==10 ||
	 * story.getStory().getId()==12){ result=true; break; } }
	 * assertFalse("public story results expected for AUTHORIZED",result); }
	 */
	/*
	 * SYSTWO-
	 * 
	 * @SuppressWarnings("unchecked") public void
	 * testGetStoriesSearchNotResultsAuthorizedStory() throws SQLException {
	 * //create or use a story which the user is authorized to READ but which is
	 * not their story; this should be returned with 'AUTHORIZED' or 'ANY', but
	 * not 'MINE' or 'PUBLIC' final User user = new User(); user.setId(53);
	 * 
	 * StoryPagedRetrieveParams searchParams = new
	 * StoryPagedRetrieveParams(0,10,
	 * StorySortField.STORYTELLER,true,"title",MineCallbackProvider
	 * .AUTHORIZED,user,null); StoryPersister persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); List<StorySummary>
	 * stories = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * boolean result=false; for (final StorySummary story : stories) {
	 * if(story.getStory().getId()==8){ result=true; break; } }
	 * assertTrue("public story results expected for AUTHORIZED",result);
	 * 
	 * user.setId(2); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"title",MineCallbackProvider.MINE,user,null); persister
	 * = (StoryPersister) PersistenceService.getPersisterFor(Story.class);
	 * stories = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * result=false; for (final StorySummary story : stories) {
	 * if(story.getStory().getId()==8){ result=true; break; } }
	 * assertFalse("public story results expected for MINE",result);
	 * 
	 * user.setId(2); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"title",MineCallbackProvider.PUBLIC,user,null);
	 * persister = (StoryPersister)
	 * PersistenceService.getPersisterFor(Story.class); stories =
	 * (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * result=false; for (final StorySummary story : stories) {
	 * if(story.getStory().getId()==8){ result=true; break; } }
	 * assertFalse("public story results expected for PUBLIC",result);
	 * 
	 * user.setId(2); searchParams = new
	 * StoryPagedRetrieveParams(0,10,StorySortField
	 * .STORYTELLER,true,"",MineCallbackProvider.ANY,user,null); persister =
	 * (StoryPersister) PersistenceService.getPersisterFor(Story.class); stories
	 * = (List<StorySummary>)
	 * PersistenceService.process(persister.getStoriesPagedFunc(searchParams));
	 * assertTrue("public story results expected for ANY",(stories.size()>0)); }
	 */
}
