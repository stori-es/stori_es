package org.consumersunion.stories.server.solr;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.StreamingResponseCallback;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.request.RequestWriter;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;

public interface SolrServer {
    NamedList<Object> request(SolrRequest request) throws SolrServerException, IOException;

    NamedList<Object> request(SolrRequest request, ResponseParser processor)
            throws SolrServerException, IOException;

    ModifiableSolrParams getInvariantParams();

    String getBaseURL();

    void setBaseURL(String baseURL);

    ResponseParser getParser();

    void setParser(ResponseParser processor);

    HttpClient getHttpClient();

    void setConnectionTimeout(int timeout);

    void setSoTimeout(int timeout);

    void setFollowRedirects(boolean followRedirects);

    void setAllowCompression(boolean allowCompression);

    void setMaxRetries(int maxRetries);

    void setRequestWriter(RequestWriter requestWriter);

    UpdateResponse add(Iterator<SolrInputDocument> docIterator) throws SolrServerException, IOException;

    UpdateResponse addBeans(Iterator<?> beanIterator) throws SolrServerException, IOException;

    void shutdown();

    void setDefaultMaxConnectionsPerHost(int max);

    void setMaxTotalConnections(int max);

    UpdateResponse add(Collection<SolrInputDocument> docs) throws SolrServerException, IOException;

    UpdateResponse add(Collection<SolrInputDocument> docs, int commitWithinMs)
                    throws SolrServerException, IOException;

    UpdateResponse addBeans(Collection<?> beans) throws SolrServerException, IOException;

    UpdateResponse addBeans(Collection<?> beans, int commitWithinMs) throws SolrServerException, IOException;

    UpdateResponse add(SolrInputDocument doc) throws SolrServerException, IOException;

    UpdateResponse add(SolrInputDocument doc, int commitWithinMs) throws SolrServerException, IOException;

    UpdateResponse addBean(Object obj) throws IOException, SolrServerException;

    UpdateResponse addBean(Object obj, int commitWithinMs) throws IOException, SolrServerException;

    UpdateResponse commit() throws SolrServerException, IOException;

    UpdateResponse optimize() throws SolrServerException, IOException;

    UpdateResponse commit(boolean waitFlush, boolean waitSearcher) throws SolrServerException, IOException;

    UpdateResponse commit(boolean waitFlush, boolean waitSearcher, boolean softCommit)
                            throws SolrServerException, IOException;

    UpdateResponse optimize(boolean waitFlush, boolean waitSearcher) throws SolrServerException, IOException;

    UpdateResponse optimize(boolean waitFlush, boolean waitSearcher, int maxSegments)
                                    throws SolrServerException, IOException;

    UpdateResponse rollback() throws SolrServerException, IOException;

    UpdateResponse deleteById(String id) throws SolrServerException, IOException;

    UpdateResponse deleteById(String id, int commitWithinMs) throws SolrServerException, IOException;

    UpdateResponse deleteById(List<String> ids) throws SolrServerException, IOException;

    UpdateResponse deleteById(List<String> ids, int commitWithinMs) throws SolrServerException, IOException;

    UpdateResponse deleteByQuery(String query) throws SolrServerException, IOException;

    UpdateResponse deleteByQuery(String query, int commitWithinMs) throws SolrServerException, IOException;

    SolrPingResponse ping() throws SolrServerException, IOException;

    QueryResponse query(SolrParams params) throws SolrServerException;

    QueryResponse query(SolrParams params, SolrRequest.METHOD method) throws SolrServerException;

    QueryResponse queryAndStreamResponse(SolrParams params, StreamingResponseCallback callback)
                                            throws SolrServerException, IOException;

    DocumentObjectBinder getBinder();
}
