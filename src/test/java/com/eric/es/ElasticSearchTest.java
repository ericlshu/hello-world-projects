package com.eric.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Description :
 *
 * @author Eric SHU
 */
public class ElasticSearchTest
{
    @Test
    public void test() throws IOException
    {
        // 获取客户端连接
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost(
                        "110.40.224.64",
                        9200,
                        HttpHost.DEFAULT_SCHEME_NAME)
        ));
        // 构建请求
        GetRequest getRequest = new GetRequest("book", "2");

        // 执行
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);

        // 获取结果
        String id = response.getId();
        long version = response.getVersion();
        Map<String, Object> source = response.getSource();

        System.out.println("id = " + id);
        System.out.println("version = " + version);
        System.out.println("source = " + source);
    }
}
