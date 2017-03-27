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
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.RequestWriter;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;

public class ObservableSolrServer extends HttpSolrServer implements SolrServer {
    public ObservableSolrServer(String baseURL, HttpClient client) {
        super(baseURL, client);
    }

    @Override
    public NamedList<Object> request(SolrRequest request) throws SolrServerException, IOException {
        return super.request(request);
    }

    @Override
    public NamedList<Object> request(SolrRequest request, ResponseParser processor)
            throws SolrServerException, IOException {
        return super.request(request, processor);
    }

    @Override
    public ModifiableSolrParams getInvariantParams() {
        return super.getInvariantParams();
    }

    @Override
    public String getBaseURL() {
        return super.getBaseURL();
    }

    @Override
    public void setBaseURL(String baseURL) {
        super.setBaseURL(baseURL);
    }

    @Override
    public ResponseParser getParser() {
        return super.getParser();
    }

    @Override
    public void setParser(ResponseParser processor) {
        super.setParser(processor);
    }

    @Override
    public HttpClient getHttpClient() {
        return super.getHttpClient();
    }

    @Override
    public void setConnectionTimeout(int timeout) {
        super.setConnectionTimeout(timeout);
    }

    @Override
    public void setSoTimeout(int timeout) {
        super.setSoTimeout(timeout);
    }

    @Override
    public void setFollowRedirects(boolean followRedirects) {
        super.setFollowRedirects(followRedirects);
    }

    @Override
    public void setAllowCompression(boolean allowCompression) {
        super.setAllowCompression(allowCompression);
    }

    @Override
    public void setMaxRetries(int maxRetries) {
        super.setMaxRetries(maxRetries);
    }

    @Override
    public void setRequestWriter(RequestWriter requestWriter) {
        super.setRequestWriter(requestWriter);
    }

    @Override
    public UpdateResponse add(Iterator<SolrInputDocument> docIterator) throws SolrServerException, IOException {
        return super.add(docIterator);
    }

    @Override
    public UpdateResponse addBeans(Iterator<?> beanIterator) throws SolrServerException, IOException {
        return super.addBeans(beanIterator);
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public void setDefaultMaxConnectionsPerHost(int max) {
        super.setDefaultMaxConnectionsPerHost(max);
    }

    @Override
    public void setMaxTotalConnections(int max) {
        super.setMaxTotalConnections(max);
    }

    @Override
    public UpdateResponse add(Collection<SolrInputDocument> docs) throws SolrServerException, IOException {
        return super.add(docs);
    }

    @Override
    public UpdateResponse add(Collection<SolrInputDocument> docs, int commitWithinMs)
            throws SolrServerException, IOException {
        return super.add(docs, commitWithinMs);
    }

    @Override
    public UpdateResponse addBeans(Collection<?> beans) throws SolrServerException, IOException {
        return super.addBeans(beans);
    }

    @Override
    public UpdateResponse addBeans(Collection<?> beans, int commitWithinMs) throws SolrServerException, IOException {
        return super.addBeans(beans, commitWithinMs);
    }

    @Override
    public UpdateResponse add(SolrInputDocument doc) throws SolrServerException, IOException {
        return super.add(doc);
    }

    @Override
    public UpdateResponse add(SolrInputDocument doc, int commitWithinMs) throws SolrServerException, IOException {
        return super.add(doc, commitWithinMs);
    }

    @Override
    public UpdateResponse addBean(Object obj) throws IOException, SolrServerException {
        return super.addBean(obj);
    }

    @Override
    public UpdateResponse addBean(Object obj, int commitWithinMs) throws IOException, SolrServerException {
        return super.addBean(obj, commitWithinMs);
    }

    @Override
    public UpdateResponse commit() throws SolrServerException, IOException {
        return super.commit();
    }

    @Override
    public UpdateResponse optimize() throws SolrServerException, IOException {
        return super.optimize();
    }

    @Override
    public UpdateResponse commit(boolean waitFlush, boolean waitSearcher) throws SolrServerException, IOException {
        return super.commit(waitFlush, waitSearcher);
    }

    @Override
    public UpdateResponse commit(boolean waitFlush, boolean waitSearcher, boolean softCommit)
            throws SolrServerException, IOException {
        return super.commit(waitFlush, waitSearcher, softCommit);
    }

    @Override
    public UpdateResponse optimize(boolean waitFlush, boolean waitSearcher) throws SolrServerException, IOException {
        return super.optimize(waitFlush, waitSearcher);
    }

    @Override
    public UpdateResponse optimize(boolean waitFlush, boolean waitSearcher, int maxSegments)
            throws SolrServerException, IOException {
        return super.optimize(waitFlush, waitSearcher, maxSegments);
    }

    @Override
    public UpdateResponse rollback() throws SolrServerException, IOException {
        return super.rollback();
    }

    @Override
    public UpdateResponse deleteById(String id) throws SolrServerException, IOException {
        return super.deleteById(id);
    }

    @Override
    public UpdateResponse deleteById(String id, int commitWithinMs) throws SolrServerException, IOException {
        return super.deleteById(id, commitWithinMs);
    }

    @Override
    public UpdateResponse deleteById(List<String> ids) throws SolrServerException, IOException {
        return super.deleteById(ids);
    }

    @Override
    public UpdateResponse deleteById(List<String> ids, int commitWithinMs) throws SolrServerException, IOException {
        return super.deleteById(ids, commitWithinMs);
    }

    @Override
    public UpdateResponse deleteByQuery(String query) throws SolrServerException, IOException {
        return super.deleteByQuery(query);
    }

    @Override
    public UpdateResponse deleteByQuery(String query, int commitWithinMs) throws SolrServerException, IOException {
        return super.deleteByQuery(query, commitWithinMs);
    }

    @Override
    public SolrPingResponse ping() throws SolrServerException, IOException {
        return super.ping();
    }

    @Override
    public QueryResponse query(SolrParams params) throws SolrServerException {
        return super.query(params);
    }

    @Override
    public QueryResponse query(SolrParams params, SolrRequest.METHOD method) throws SolrServerException {
        return super.query(params, method);
    }

    @Override
    public QueryResponse queryAndStreamResponse(SolrParams params, StreamingResponseCallback callback)
            throws SolrServerException, IOException {
        return super.queryAndStreamResponse(params, callback);
    }

    @Override
    public DocumentObjectBinder getBinder() {
        return super.getBinder();
    }
}
