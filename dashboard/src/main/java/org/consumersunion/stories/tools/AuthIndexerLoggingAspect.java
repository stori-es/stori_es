package org.consumersunion.stories.tools;

import org.aspectj.lang.annotation.Aspect;

//
//import java.lang.reflect.Field;
//import java.util.Collection;
//import java.util.List;
//import java.util.logging.Logger;
//
//import org.apache.solr.client.solrj.response.QueryResponse;
//import org.apache.solr.common.SolrDocumentList;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.consumersunion.stories.server.solr.AuthIndexer;
//import org.consumersunion.stories.server.solr.ObservableSolrServer;
//
@Aspect
public class AuthIndexerLoggingAspect {
//    private static final Logger OBSERVABLE_LOGGER = Logger.getLogger(ObservableSolrServer.class.getName());
//
//    private ThreadLocal<Integer> totalSize = new ThreadLocal<Integer>() {
//        @Override
//        protected Integer initialValue() {
//            return 0;
//        }
//    };
//    private ThreadLocal<Long> startIndex = new ThreadLocal<Long>() {
//        @Override
//        protected Long initialValue() {
//            return 0L;
//        }
//    };
//
//    @Pointcut("bean(coreIndexer) && execution(* process(..))")
//    public void process() {
//    }
//
//    @Around(value = "process() && args(indexer)", argNames = "pjp,indexer")
//    public Object onProcess(ProceedingJoinPoint pjp, AuthIndexer indexer) throws Throwable {
//        String indexerName = indexer.getClass().getName();
//
//        startIndex.set(0L);
//        totalSize.set(getTotalSize(indexer));
//        Logger logger = Logger.getLogger(indexerName);
//        logger.info("Running " + indexerName);
//
//        Object result = pjp.proceed();
//
//        totalSize.set(0);
//        logger.info("Done");
//
//        return result;
//    }
//
//    @Pointcut("execution(* org.consumersunion.stories.server.solr.ObservableSolrServer.query(..))")
//    public void query() {
//    }
//
//    @Pointcut("execution(* org.consumersunion.stories.server.solr.ObservableSolrServer.add(..))")
//    public void add() {
//    }
//
//    @Pointcut("execution(* org.consumersunion.stories.server.solr.ObservableSolrServer.commit(..))")
//    public void commit() {
//    }
//
//    @Pointcut("within(org.consumersunion.stories.server.solr..*)")
//    public void withinSolrPackage() {
//    }
//
//    @AfterReturning(value = "query() && withinSolrPackage())", returning = "result")
//    public void afterQuery(QueryResponse result) throws Throwable {
//        SolrDocumentList results = result.getResults();
//
//        long start = startIndex.get();
//        long end = start + results.getNumFound();
//
//        OBSERVABLE_LOGGER.info(String.format("Queried documents %d to %d of %d", start, end, totalSize.get()));
//
//        startIndex.set(end);
//    }
//
//    @Before(value = "add() && withinSolrPackage() && args(documents)", argNames = "documents")
//    public void onAdd(Collection<?> documents) throws Throwable {
//        OBSERVABLE_LOGGER.info(String.format("Adding %d documents", documents.size()));
//    }
//
//    @Around(value = "commit() && withinSolrPackage()")
//    public Object onCommit(ProceedingJoinPoint pjp) throws Throwable {
//        OBSERVABLE_LOGGER.info("Committing..");
//
//        Object result = pjp.proceed();
//
//        OBSERVABLE_LOGGER.info("Committed.");
//
//        return result;
//    }
//
//    private Integer getTotalSize(AuthIndexer indexer) {
//        try {
//            Field idsField = AuthIndexer.class.getDeclaredField("ids");
//            idsField.setAccessible(true);
//            return ((List<Integer>) idsField.get(indexer)).size();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//            return 0;
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
}
// TODO : @SOLR
