package com.eric.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description :
 *
 * @author Eric SHU
 */
@Configuration
public class ElasticsearchConfig
{
    public static final String ELASTIC_SERVER = "http://110.40.224.64:9200";

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient()
    {
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(ELASTIC_SERVER)));
    }
}
