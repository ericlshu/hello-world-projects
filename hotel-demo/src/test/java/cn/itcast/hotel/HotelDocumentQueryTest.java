package cn.itcast.hotel;

import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.util.AppConstants;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

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
        // 2 组织DSL参数
        builder = searchRequest.source();
    }

    @Test
    void testMatchAll() throws IOException
    {
        execSearch(QueryBuilders.matchAllQuery());
    }

    @Test
    void testMatch() throws IOException
    {
        execSearch(QueryBuilders.matchQuery("all", "如家"));
    }

    @Test
    void testBoolQuery() throws IOException
    {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("city", "上海"));
        boolQuery.filter(QueryBuilders.rangeQuery("price").lte(250));
        execSearch(boolQuery);
    }

    @Test
    void testPageAndSort() throws IOException
    {
        int currentPage = 2, pageSize = 10;
        builder.sort("price", SortOrder.ASC);
        builder.from((currentPage - 1) * pageSize).size(pageSize);
        execSearch(QueryBuilders.matchAllQuery());
    }

    @Test
    void testHighlight() throws IOException
    {
        builder.highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
        execSearch(QueryBuilders.matchQuery("all", "如家"));
    }

    private void execSearch(QueryBuilder queryBuilder) throws IOException
    {
        // 2 设置查询条件即DSL参数
        builder.query(queryBuilder);
        // 3 发送请求，得到响应结果
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 4 解析结果
        SearchHits searchHits = searchResponse.getHits();
        // 4.1 查询的总条数
        long total = searchHits.getTotalHits().value;
        log.warn("searchResponse.searchHits.totalHits = [{}]", total);
        // 4.2 查询的结果数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits)
        {
            // 4.3 得到source
            String json = hit.getSourceAsString();
            // 4.4 转换json为对象
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            // 4.5 处理高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (!CollectionUtils.isEmpty(highlightFields))
            {
                HighlightField highlightField = highlightFields.get("name");
                if (highlightField != null)
                    hotelDoc.setName(highlightField.getFragments()[0].string());
            }
            log.info("hotelDoc = [{}]", hotelDoc);
        }
    }

    @AfterEach
    void AfterEach() throws IOException
    {
        this.client.close();
    }
}
