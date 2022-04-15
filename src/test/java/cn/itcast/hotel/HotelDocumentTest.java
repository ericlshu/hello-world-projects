package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
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

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-15 16:09
 * @since jdk-11.0.14
 */
@Slf4j
@SpringBootTest
public class HotelDocumentTest
{
    public static final String INDEX_NAME = "hotel";

    private RestHighLevelClient client;

    @Resource
    private IHotelService hotelService;

    @Test
    void testDocumentCRUD() throws IOException
    {
        // 1.根据id查询酒店数据
        Hotel hotel = hotelService.getById(61083L);
        // 2.转换为文档类型
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 3.将HotelDoc转json
        String json = JSON.toJSONString(hotelDoc);

        log.info(json);

        // 1.准备Request对象
        IndexRequest request = new IndexRequest(INDEX_NAME).id(hotelDoc.getId().toString());
        // 2.准备Json文档
        request.source(json, XContentType.JSON);
        // 3.发送请求
        client.index(request, RequestOptions.DEFAULT);
    }

    @BeforeEach
    void BeforeEach()
    {
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://110.40.224.64:9200")));
    }

    @AfterEach
    void AfterEach() throws IOException
    {
        this.client.close();
    }
}
