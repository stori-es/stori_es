import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.collection.FullCollectionIndexer;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchBuilder;
import org.consumersunion.stories.server.index.mappings.IndexMappingCreator;
import org.consumersunion.stories.server.index.profile.FullPersonIndexer;
import org.consumersunion.stories.server.index.story.FullStoryIndexer;
import org.consumersunion.stories.server.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

public class MainIndexer {
    public static void main(String[] args) throws Exception {
        try {
            run(args);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void run(String[] args) throws Exception {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();

        boolean indexCollections, indexStories, indexPeople;
        indexCollections = indexStories = indexPeople = false;
        if (args.length == 0) {
            indexCollections = indexStories = indexPeople = true;
        } else {
            for (int i = 0; i < args.length; i += 1) {
                if ("collections".equalsIgnoreCase(args[i])) {
                    indexCollections = true;
                    maybeDelete(context, "collectionIndexer");
                } else if ("stories".equalsIgnoreCase(args[i])) {
                    indexStories = true;
                    maybeDelete(context, "storyIndexer");
                } else if ("people".equalsIgnoreCase(args[i])) {
                    indexPeople = true;
                    maybeDelete(context, "profileIndexer");
                } else {
                    System.err.print(
                            "Unknown index indicated: " + args[0] + "; please specify 'collections', 'stories', or " +
                                    "'people'.");
                    System.exit(2);
                }
            }
        }

        IndexMappingCreator indexMappingCreator = context.getBean(IndexMappingCreator.class);
        indexMappingCreator.create();

        if (indexCollections) {
            System.out.print("Collection Indexation... ");
            FullCollectionIndexer indexer = context.getBean(FullCollectionIndexer.class);
            indexer.index();
            System.out.println("complete.");
        }
        if (indexStories) {
            System.out.print("Stories Indexation... ");
            FullStoryIndexer indexer = context.getBean(FullStoryIndexer.class);
            indexer.index();
            System.out.print("complete.");
        }
        if (indexPeople) {
            System.out.print("People Indexation... ");
            FullPersonIndexer indexer = context.getBean(FullPersonIndexer.class);
            indexer.index();
            System.out.print("complete.");
        }
    }

    private static void maybeDelete(ApplicationContext context, String beanName) {
        boolean delete = Boolean.parseBoolean(System.getProperty("delete", "false"));
        boolean deleteIndex = Boolean.parseBoolean(System.getProperty("deleteIndex", "false"));
        if (delete) {
            Indexer<?> indexer = (Indexer<?>) context.getBean(beanName);
            indexer.deleteByQuery(SearchBuilder.ofQuery(QueryBuilder.newMatchAll()));
        } else if (deleteIndex) {
            Indexer<?> indexer = (Indexer<?>) context.getBean(beanName);
            indexer.deleteIndex();
        }
    }
}
