package org.consumersunion.stories.server.spring;

import javax.inject.Named;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.consumersunion.stories.server.solr.ObservableSolrServer;
import org.consumersunion.stories.server.solr.SolrServer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

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
    public SolrServer solrStoryServer(HttpClient httpClient, BeanFactory beanFactory) {
        return (SolrServer) beanFactory.getBean("observableSolrServer", "stories", httpClient);
    }

    @Bean
    @Named("solrCollectionServer")
    public SolrServer solrCollectionServer(HttpClient httpClient, BeanFactory beanFactory) {
        return (SolrServer) beanFactory.getBean("observableSolrServer", "collections", httpClient);
    }

    @Bean
    @Named("solrPersonServer")
    public SolrServer solrPersonServer(HttpClient httpClient, BeanFactory beanFactory) {
        return (SolrServer) beanFactory.getBean("observableSolrServer", "people", httpClient);
    }

    @Bean(name = "observableSolrServer")
    @Scope("prototype")
    @Lazy
    public SolrServer getSolrServer(String core, HttpClient httpClient) {
        String baseUrl = getBaseUrl(core);

        return new ObservableSolrServer(baseUrl, httpClient);
    }

    @Bean
    public String core() {
        return "";
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
