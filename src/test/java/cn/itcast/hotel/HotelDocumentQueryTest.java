package cn.itcast.hotel;

import cn.itcast.hotel.pojo.HotelDoc;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-15 16:09
 * @since jdk-11.0.14
 */
@Slf4j
@SpringBootTest
public class HotelDocumentQueryTest
{
    public static final String INDEX_NAME = "hotel";

    private RestHighLevelClient client;
    private SearchRequest searchRequest;
    private SearchSourceBuilder builder;

    @BeforeEach
    void BeforeEach()
    {
        // 0 初始化RestClient对象
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://110.40.224.64:9200")));
        // 1 准备Request
        searchRequest = new SearchRequest(INDEX_NAME);
        // 2 组织DSL参数
        builder = searchRequest.source();
    }

    @Test
    void testMatchAll() throws IOException
    {
        // 2  设置查询条件
        builder.query(QueryBuilders.matchAllQuery());
        execSearch();
    }

    @Test
    void testMatch() throws IOException
    {
        builder.query(QueryBuilders.matchQuery("all", "如家"));
        execSearch();
    }

    private void execSearch() throws IOException
    {
        // 3 发送请求，得到响应结果
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 4 解析结果
        SearchHits searchHits = searchResponse.getHits();
        // 4.1 查询的总条数
        long total = searchHits.getTotalHits().value;
        log.warn("total = [{}]", total);
        // 4.2 查询的结果数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits)
        {
            // 4.3 得到source
            String json = hit.getSourceAsString();
            // 4.4 转换json为对象
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            log.info("hotelDoc = [{}]", hotelDoc);
        }
    }

    @AfterEach
    void AfterEach() throws IOException
    {
        this.client.close();
    }
}
