package cn.itcast.hotel;

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
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://110.40.224.64:9200")));
    }

    @AfterEach
    void AfterEach() throws IOException
    {
        this.client.close();
    }

    public static final String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"address\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"score\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"brand\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"city\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"starName\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"business\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"location\":{\n" +
            "        \"type\": \"geo_point\"\n" +
            "      },\n" +
            "      \"pic\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static final String INDEX_NAME = "hotel";

    @Test
    void testCreateHotelIndex() throws IOException
    {
        System.out.println("client = " + client);
        // 1.创建Request对象
        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        // 2.请求参数，MAPPING_TEMPLATE是静态常量字符串，内容是创建索引库的DSL语句
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        // 3.发起请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    @Test
    void testIndexCRUD() throws IOException
    {
        IndicesClient indices = client.indices();

        if (indices.exists(new GetIndexRequest(INDEX_NAME), RequestOptions.DEFAULT))
        {
            log.warn("[{}]索引库已存在，删除索引库。", INDEX_NAME);
            indices.delete(new DeleteIndexRequest(INDEX_NAME), RequestOptions.DEFAULT);
        }
        else
        {
            log.warn("[{}]索引库不存在，创建索引库。", INDEX_NAME);
            // 1.创建Request对象
            CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
            // 2.请求参数，MAPPING_TEMPLATE是静态常量字符串，内容是创建索引库的DSL语句
            request.source(MAPPING_TEMPLATE, XContentType.JSON);
            // 3.发起请求
            client.indices().create(request, RequestOptions.DEFAULT);
        }
    }
}
