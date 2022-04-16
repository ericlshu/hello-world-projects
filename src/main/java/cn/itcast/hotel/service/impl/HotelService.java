package cn.itcast.hotel.service.impl;

import cn.itcast.hotel.mapper.HotelMapper;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.RequestParam;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService
{
    public static final String INDEX_NAME = "hotel";

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public PageResult search(RequestParam param)
    {
        // 1 准备Request
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder sourceBuilder = searchRequest.source();
        try
        {
            // 2 准备DSL
            // 2.1 Query
            String key = param.getKey();
            if (key == null || "".equals(key))
                sourceBuilder.query(QueryBuilders.matchAllQuery());
            else
                sourceBuilder.query(QueryBuilders.matchQuery("all", key));

            // 2.2 分页
            Integer size = param.getSize();
            Integer page = param.getPage();
            log.warn("current page : [{}]", page);
            log.warn("size of page : [{}]", size);
            sourceBuilder.from((page - 1) * size).size(size);

            // 3. 发送查询请求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 4. 解析响应结果
            return handleResponse(searchResponse);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private PageResult handleResponse(SearchResponse searchResponse) throws IOException
    {
        // 4 解析结果
        SearchHits searchHits = searchResponse.getHits();
        // 4.1 查询的总条数
        long total = searchHits.getTotalHits().value;
        log.warn("searchResponse.searchHits.totalHits = [{}]", total);
        // 4.2 查询的结果数组
        SearchHit[] hits = searchHits.getHits();
        List<HotelDoc> hotels = new ArrayList<>();
        for (SearchHit hit : hits)
        {
            // 4.3 得到source
            String json = hit.getSourceAsString();
            // 4.4 转换json为对象
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            log.info("hotelDoc = [{}]", hotelDoc);
            hotels.add(hotelDoc);
        }
        return new PageResult(total, hotels);
    }
}
