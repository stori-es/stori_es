package org.consumersunion.stories.server.spring;

import javax.inject.Named;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Strings;

@Configuration
public class SolrConfigurator {
    private static final String SOLR_BASE_URL = "http://localhost:8983/solr/";

    @Bean
    public HttpClient httpClient() {
        return new DecompressingHttpClient(new SystemDefaultHttpClient());
    }

    @Bean
    @Named("solrStoryServer")
    public SolrServer solrStoryServer(HttpClient httpClient) {
        return getSolrServer("stories", httpClient);
    }

    @Bean
    @Named("solrCollectionServer")
    public SolrServer solrCollectionServer(HttpClient httpClient) {
        return getSolrServer("collections", httpClient);
    }

    @Bean
    @Named("solrPersonServer")
    public SolrServer solrPersonServer(HttpClient httpClient) {
        return getSolrServer("people", httpClient);
    }

    private SolrServer getSolrServer(String core, HttpClient httpClient) {
        String baseUrl = getBaseUrl(core);

        return new HttpSolrServer(baseUrl, httpClient);
    }

    private String getBaseUrl(String core) {
        String baseUrl = System.getProperty("PARAM1", null);

        if (Strings.isNullOrEmpty(baseUrl)) {
            baseUrl = SOLR_BASE_URL + core;
        } else {
            baseUrl += "-" + core;
        }

        return baseUrl;
    }
}
