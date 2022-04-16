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
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            // 2.1 准备DSL,构建BoolQuery
            sourceBuilder.query(buildBasicQuery(param));

            // 2.2 分页
            Integer size = param.getSize();
            Integer page = param.getPage();
            log.warn("current page : [{}]", page);
            log.warn("size of page : [{}]", size);
            sourceBuilder.from((page - 1) * size).size(size);

            // 2.3 按距离排序
            String location = param.getLocation();
            if (StringUtils.hasText(location))
            {
                log.warn("location     : [{}]", location);
                sourceBuilder.sort(SortBuilders
                                           .geoDistanceSort("location", new GeoPoint(location))
                                           .order(SortOrder.ASC)
                                           .unit(DistanceUnit.KILOMETERS));
            }

            // 2.4 高亮显示
            sourceBuilder.highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));

            // 3 发送查询请求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 4 解析响应结果
            return handleResponse(searchResponse);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建BoolQuery
     *
     * @param param 请求参数
     * @return BoolQuery
     */
    private QueryBuilder buildBasicQuery(RequestParam param)
    {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        String key = param.getKey();
        String city = param.getCity();
        String brand = param.getBrand();
        String starName = param.getStarName();
        Integer minPrice = param.getMinPrice();
        Integer maxPrice = param.getMaxPrice();

        log.warn("query key    : [{}]", key);
        log.warn("city         : [{}]", city);
        log.warn("brand        : [{}]", brand);
        log.warn("star name    : [{}]", starName);
        log.warn("min price    : [{}]", minPrice);
        log.warn("max price    : [{}]", maxPrice);

        // 关键字检索
        if (key == null || "".equals(key))
            boolQuery.must(QueryBuilders.matchAllQuery());
        else
            boolQuery.must(QueryBuilders.matchQuery("all", key));

        // 条件过滤
        if (StringUtils.hasText(city))
            boolQuery.filter(QueryBuilders.termQuery("city", city));
        if (StringUtils.hasText(brand))
            boolQuery.filter(QueryBuilders.termQuery("brand", brand));
        if (StringUtils.hasText(starName))
            boolQuery.filter(QueryBuilders.termQuery("starName", starName));
        if (minPrice != null && maxPrice != null)
            boolQuery.filter(QueryBuilders.rangeQuery("price")
                                     .gte(minPrice)
                                     .lte(maxPrice));

        // 算分函数查询
        return QueryBuilders
                .functionScoreQuery(
                        boolQuery,// 原始查询，boolQuery
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{// function数组
                                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                        QueryBuilders.termQuery("isAD", true),// 过滤条件
                                        ScoreFunctionBuilders.weightFactorFunction(10)   // 算分函数
                                )
                        });
    }

    private PageResult handleResponse(SearchResponse searchResponse) throws IOException
    {
        // 4 解析结果
        SearchHits searchHits = searchResponse.getHits();
        // 4.1 查询的总条数
        long total = searchHits.getTotalHits().value;
        log.warn("total hits   : [{}]", total);
        // 4.2 查询的结果数组
        SearchHit[] hits = searchHits.getHits();
        List<HotelDoc> hotels = new ArrayList<>();
        for (SearchHit hit : hits)
        {
            // 4.3 得到source
            String json = hit.getSourceAsString();
            // 4.4 转换json为对象
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            // 4.5 获取排序值
            Object[] sortValues = hit.getSortValues();
            if (sortValues.length > 0)
                hotelDoc.setDistance(sortValues[0]);
            // 4.6 处理高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (!CollectionUtils.isEmpty(highlightFields))
            {
                HighlightField highlightField = highlightFields.get("name");
                if (highlightField != null)
                    hotelDoc.setName(highlightField.getFragments()[0].string());
            }
            log.info("hotelDoc = [{}]", hotelDoc);
            hotels.add(hotelDoc);
        }
        return new PageResult(total, hotels);
    }
}
