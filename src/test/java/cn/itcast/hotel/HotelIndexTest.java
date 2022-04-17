package cn.itcast.hotel;

import cn.itcast.hotel.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-15 16:09
 * @since jdk-11.0.14
 */
@Slf4j
public class HotelIndexTest
{
    private RestHighLevelClient client;

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



    @Test
    void testCreateHotelIndex() throws IOException
    {
        System.out.println("client = " + client);
        // 1.创建Request对象
        CreateIndexRequest request = new CreateIndexRequest(AppConstants.INDEX_NAME);
        // 2.请求参数，MAPPING_TEMPLATE是静态常量字符串，内容是创建索引库的DSL语句
        request.source(AppConstants.MAPPING_TEMPLATE, XContentType.JSON);
        // 3.发起请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    @Test
    void testIndexCRUD() throws IOException
    {
        IndicesClient indices = client.indices();

        if (indices.exists(new GetIndexRequest(AppConstants.INDEX_NAME), RequestOptions.DEFAULT))
        {
            log.warn("[{}]索引库已存在，删除索引库。", AppConstants.INDEX_NAME);
            indices.delete(new DeleteIndexRequest(AppConstants.INDEX_NAME), RequestOptions.DEFAULT);
        }
        else
        {
            log.warn("[{}]索引库不存在，创建索引库。", AppConstants.INDEX_NAME);
            // 1.创建Request对象
            CreateIndexRequest request = new CreateIndexRequest(AppConstants.INDEX_NAME);
            // 2.请求参数，MAPPING_TEMPLATE是静态常量字符串，内容是创建索引库的DSL语句
            request.source(AppConstants.MAPPING_TEMPLATE, XContentType.JSON);
            // 3.发起请求
            client.indices().create(request, RequestOptions.DEFAULT);
        }
    }
}
