package org.consumersunion.stories.server.solr;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrServer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("coreIndexer")
public class CoreIndexer {
    private final List<Indexer> pending = new ArrayList<Indexer>();
    private final List<Indexer> failed = new ArrayList<Indexer>();

    @Inject
    @Named("solrStoryServer")
    private SolrServer solrStoryServer;
    @Inject
    @Named("solrCollectionServer")
    private SolrServer solrCollectionServer;
    @Inject
    @Named("solrPersonServer")
    private SolrServer solrPersonServer;
    private List<IndexChecker> checkers;

    @Inject
    public CoreIndexer(List<IndexChecker> checkers) {
        this.checkers = checkers;
    }

    public void process(Indexer indexer) {
        try {
            pending.add(indexer);
            indexer.index(solrStoryServer, solrCollectionServer, solrPersonServer);
            pending.remove(indexer);
        } catch (Exception e) {
            failed.add(indexer);
            pending.remove(indexer);
            e.printStackTrace();
        }
    }

    public void processManual(Indexer indexer) {
        try {
            pending.add(indexer);
            indexer.index(solrStoryServer, solrCollectionServer, solrPersonServer);
            pending.remove(indexer);
        } catch (Exception e) {
            failed.add(indexer);
            pending.remove(indexer);
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 55 23 ? * *")
    public void checkIndexes() throws Exception {
        for (IndexChecker checker : checkers) {
            checker.check(solrStoryServer, solrCollectionServer, solrPersonServer);
        }
    }

    public SolrServer getSolrStoryServer() {
        return solrStoryServer;
    }

    public SolrServer getSolrPersonServer() {
        return solrPersonServer;
    }

    public SolrServer getSolrCollectionServer() {
        return solrCollectionServer;
    }
}
