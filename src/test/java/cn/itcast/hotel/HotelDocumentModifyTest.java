package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import cn.itcast.hotel.util.AppConstants;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-15 16:09
 * @since jdk-11.0.14
 */
@Slf4j
@SpringBootTest
public class HotelDocumentModifyTest
{
    private RestHighLevelClient client;

    @Resource
    private IHotelService hotelService;

    @Test
    void testCreateDocument() throws IOException
    {
        // 1.根据id查询酒店数据
        Hotel hotel = hotelService.getById(61083L);
        // 2.转换为文档类型
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 3.将HotelDoc转json
        String json = JSON.toJSONString(hotelDoc);

        log.info(json);

        // 1.准备Request对象
        IndexRequest request = new IndexRequest(AppConstants.INDEX_NAME).id(hotelDoc.getId().toString());
        // 2.准备Json文档
        request.source(json, XContentType.JSON);
        // 3.发送请求
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    void testGetDocument() throws IOException
    {
        // 1.准备Request
        GetRequest request = new GetRequest(AppConstants.INDEX_NAME, "61083");
        // 2.发送请求，得到响应
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        // 3.解析响应结果
        String json = response.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        log.warn("hotelDoc = {}", hotelDoc);
    }

    @Test
    void testUpdateDocument() throws IOException
    {
        // 1.准备Request
        UpdateRequest updateRequest = new UpdateRequest(AppConstants.INDEX_NAME, "61083");
        // 2.准备请求参数
        updateRequest.doc("price", "666", "starName", "四钻");
        // 3.发送请求
        client.update(updateRequest, RequestOptions.DEFAULT);
    }

    @Test
    void testDeleteDocument() throws IOException
    {
        // 1.准备Request
        DeleteRequest deleteResult = new DeleteRequest(AppConstants.INDEX_NAME, "61083");
        // 2.发送请求
        client.delete(deleteResult, RequestOptions.DEFAULT);
    }

    @Test
    void testBulkRequest() throws IOException
    {
        // 批量查询酒店数据
        List<Hotel> hotelList = hotelService.list();
        // 1.创建Request
        BulkRequest bulkRequest = new BulkRequest();
        // 2.准备参数，添加多个新增的Request
        for (Hotel hotel : hotelList)
        {
            // 2.1.转换为文档类型HotelDoc
            HotelDoc hotelDoc = new HotelDoc(hotel);
            // 2.2.创建新增文档的Request对象
            bulkRequest.add(new IndexRequest(AppConstants.INDEX_NAME)
                                    .id(hotelDoc.getId().toString())
                                    .source(JSON.toJSONString(hotelDoc), XContentType.JSON));
        }
        // 3.发送请求
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @BeforeEach
    void BeforeEach()
    {
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create(AppConstants.ELASTIC_SERVER)));
    }

    @AfterEach
    void AfterEach() throws IOException
    {
        this.client.close();
    }
}
