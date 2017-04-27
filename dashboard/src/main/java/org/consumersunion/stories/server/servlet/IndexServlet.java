package org.consumersunion.stories.server.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.business_logic.AuthorizationService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.collection.CollectionDocument;
import org.consumersunion.stories.server.index.collection.FullCollectionIndexer;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.consumersunion.stories.server.index.elasticsearch.search.Search;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchBuilder;
import org.consumersunion.stories.server.index.profile.FullPersonIndexer;
import org.consumersunion.stories.server.index.profile.ProfileDocument;
import org.consumersunion.stories.server.index.story.FullStoryIndexer;
import org.consumersunion.stories.server.index.story.StoryDocument;
import org.consumersunion.stories.server.persistence.AnswerSetPersister;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.consumersunion.stories.server.persistence.DocumentPersister;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;
import org.consumersunion.stories.server.util.ApplicationContextProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import static org.consumersunion.stories.common.shared.AuthConstants.OPERATION_INDEX;

public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final static Logger logger = Logger.getLogger(IndexServlet.class.getName());

    @Inject
    private AuthorizationService authService;
    @Inject
    private UserService userService;
    @Inject
    @Named("storyIndexer")
    private Indexer<StoryDocument> storyIndexer;
    @Inject
    @Named("profileIndexer")
    private Indexer<ProfileDocument> profileIndexer;
    @Inject
    @Named("collectionIndexer")
    private Indexer<CollectionDocument> collectionIndexer;
    @Inject
    private DocumentPersister documentPersister;
    @Inject
    private CollectionPersister collectionPersister;
    @Inject
    private SupportDataUtilsFactory supportDataUtilsFactory;
    @Inject
    private AnswerSetPersister answerSetPersister;

    @Override
    public void init(ServletConfig cfg) throws ServletException {
        super.init(cfg);

        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("No-Compress", "true");
        response.setContentType("text/plain");
        response.setHeader("Content-Encoding", "none");

        User user = userService.getLoggedInUser();
        if (user == null) {
            response.getWriter().println("Not logged in.");
            return;
        }

        if (!authService.isEntityAuthorized(user.getId(), OPERATION_INDEX, null)) {
            response.getWriter().println("Unauthorized.");
            return;
        }

        final Object lock = new Object();
        Thread bgThread = new Thread(new Runnable() {
            public void run() {
                try {
                    logger.log(Level.INFO, "Reindexing collections.");
                    Search searchAll = SearchBuilder.ofQuery(QueryBuilder.newMatchAll());
                    collectionIndexer.deleteByQuery(searchAll);
                    new FullCollectionIndexer(response, supportDataUtilsFactory, collectionPersister,
                            collectionIndexer).index();
                    logger.log(Level.INFO, "Reindexing stories.");
                    storyIndexer.deleteByQuery(searchAll);
                    new FullStoryIndexer(response, storyIndexer, documentPersister, answerSetPersister,
                            supportDataUtilsFactory).index();
                    logger.log(Level.INFO, "Reindexing people.");
                    profileIndexer.deleteByQuery(searchAll);
                    new FullPersonIndexer(profileIndexer, supportDataUtilsFactory, response).index();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error running re-index.", e);
                } finally {
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            }
        });

        bgThread.start();
        response.getWriter().println("Index started in separate thread. If you have access to the Elasticsearch " +
                "server, you can watch the progress there. The index order will be 'collections', then 'stories', " +
                "then 'people'. Each core will be cleared immediately before re-indexing. We will attempt to send " +
                "incremental updates to the browser window, but the HTTP server's compression filters may interefere." +
                " All the '-' below are to force the buffer to flush this message.\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n" +
                "-------------------------------------------------------------------------------------------------\n");
        response.flushBuffer();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
            }
        }
    }
}
