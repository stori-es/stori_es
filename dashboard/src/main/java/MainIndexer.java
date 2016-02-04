import org.consumersunion.stories.server.solr.CoreIndexer;
import org.consumersunion.stories.server.solr.collection.FullCollectionIndexer;
import org.consumersunion.stories.server.solr.person.FullPersonIndexer;
import org.consumersunion.stories.server.solr.story.FullStoryIndexer;
import org.consumersunion.stories.server.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

public class MainIndexer {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();

        CoreIndexer indexer = context.getBean(CoreIndexer.class);
        boolean indexCollections, indexStories, indexPeople;
        indexCollections = indexStories = indexPeople = false;
        if (args.length == 0) {
            indexCollections = indexStories = indexPeople = true;
        } else {
            for (int i = 0; i < args.length; i += 1) {
                if ("collections".equalsIgnoreCase(args[i])) {
                    indexCollections = true;
                } else if ("stories".equalsIgnoreCase(args[i])) {
                    indexStories = true;
                } else if ("people".equalsIgnoreCase(args[i])) {
                    indexPeople = true;
                } else {
                    System.err.print(
                            "Unknown index indicated: " + args[0] + "; please specify 'collections', 'stories', or " +
                                    "'people'.");
                    System.exit(2);
                }
            }
        }

        if (indexCollections) {
            System.out.print("Collection Indexation... ");
            indexer.processManual(context.getBean(FullCollectionIndexer.class));
            System.out.println("complete.");
        }
        if (indexStories) {
            System.out.print("Stories Indexation... ");
            indexer.processManual(context.getBean(FullStoryIndexer.class));
            System.out.print("complete.");
        }
        if (indexPeople) {
            System.out.print("People Indexation... ");
            indexer.processManual(context.getBean(FullPersonIndexer.class));
            System.out.print("complete.");
        }
        System.exit(0);
    }
}
