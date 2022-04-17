package cn.itcast.hotel;

import cn.itcast.hotel.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-17 11:13
 * @since jdk-11.0.14
 */
@Slf4j
@SpringBootTest
public class HotelDocAggregationTest
{
    private RestHighLevelClient client;
    private SearchRequest searchRequest;
    private SearchSourceBuilder builder;

    @BeforeEach
    void BeforeEach()
    {
        // 0 初始化RestClient对象
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create(AppConstants.ELASTIC_SERVER)));
        // 1 准备Request
        searchRequest = new SearchRequest(AppConstants.INDEX_NAME);
        // 2 准备SearchSourceBuilder
        builder = searchRequest.source();
    }

    @Test
    void testAgg() throws IOException
    {
        // 2 组织DSL请求参数
        // 2.1 清除文档数据
        builder.size(0);
        // 2.2 设置聚合参数
        builder.aggregation(AggregationBuilders
                                    .terms("brandAgg")
                                    .field("brand")
                                    .size(20));
        // 3.发出请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 4.解析结果
        log.debug("searchResponse = {}", searchResponse);
        // 4.1.根据聚合名称，获取聚合结果
        Terms brandAgg = searchResponse.getAggregations().get("brandAgg");
        // 4.2.获取buckets
        List<? extends Terms.Bucket> buckets = brandAgg.getBuckets();
        // 4.3.遍历
        for (Terms.Bucket bucket : buckets)
        {
            String key = bucket.getKeyAsString();
            log.debug("key = {}", key);
        }
    }

    @AfterEach
    void AfterEach() throws IOException
    {
        this.client.close();
    }
}
