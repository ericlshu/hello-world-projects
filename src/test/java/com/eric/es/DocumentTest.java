package com.eric.es;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description :
 *
 * @author Eric SHU
 */
@SpringBootTest
public class DocumentTest
{
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testGetSync() throws IOException
    {
        GetRequest getRequest = new GetRequest("book", "2");

        // 构建查询可选参数
        String[] includes = new String[]{"name", "price"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext context = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(context);

        // 同步查询
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);

        // 结果解析
        if (response.isExists())
        {
            String id = response.getId();
            long version = response.getVersion();
            Map<String, Object> source = response.getSource();

            System.out.println("id = " + id);
            System.out.println("version = " + version);
            System.out.println("source = " + source);
        }
    }

    @Test
    public void testGetAsync()
    {
        GetRequest getRequest = new GetRequest("book", "1");

        ActionListener<GetResponse> listener = new ActionListener<>()
        {
            // 查询成功时执行的方法
            @Override
            public void onResponse(GetResponse documentFields)
            {
                Map<String, Object> source = documentFields.getSource();
                System.out.println("source = " + source);
                System.out.println(documentFields.getSourceAsString());
                System.out.println(documentFields.getSourceAsBytes());
                System.out.println(documentFields.getSourceAsMap());
            }

            // 查询失败时执行的方法
            @Override
            public void onFailure(Exception e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };

        //执行异步请求
        client.getAsync(getRequest, RequestOptions.DEFAULT, listener);
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAdd() throws IOException
    {
        IndexRequest indexRequest = new IndexRequest("test_posts");
        indexRequest.id("1");

        // 构建方法1:Json字符串
        // String jsonString = "{\n" +
        //         "  \"user\":\"tomas J\",\n" +
        //         "  \"postDate\":\"2019-07-18\",\n" +
        //         "  \"message\":\"trying out es3\"\n" +
        //         "}";
        // indexRequest.source(jsonString, XContentType.JSON);

        // 构建方法2:Map对象
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "eric");
        jsonMap.put("postDate", "2022-06-12");
        jsonMap.put("message", "test index add");
        indexRequest.source(jsonMap);

        // 构建方法3:XContentBuilder
        // XContentBuilder builder = XContentFactory.jsonBuilder();
        // builder.startObject();
        // {
        //     builder.field("user", "tomas");
        //     builder.timeField("postDate", new Date());
        //     builder.field("message", "trying out es2");
        // }
        // builder.endObject();
        // indexRequest.source(builder);

        // 构建方法4:key-value
        // indexRequest.source("user", "tomas",
        //                     "postDate", new Date(),
        //                     "message", "trying out es2");

        // 设置超时时间
        indexRequest.timeout(TimeValue.timeValueSeconds(1));

        // 自己维护版本号
        // indexRequest.version(2);
        // indexRequest.versionType(VersionType.EXTERNAL);

        // 同步执行操作
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        String index = indexResponse.getIndex();
        String id = indexResponse.getId();
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println("id = " + id);
        System.out.println("index = " + index);
        System.out.println("result = " + result);

        if (DocWriteResponse.Result.CREATED.equals(result))
        {
            System.out.println("新增成功！");
        }

        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful())
        {
            System.out.println("处理成功的分片数少于总分片！");
        }
        if (shardInfo.getFailed() > 0)
        {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures())
            {
                System.out.println(failure.reason());
            }
        }

        // 异步执行操作
        // ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>()
        // {
        //     @Override
        //     public void onResponse(IndexResponse indexResponse)
        //     {
        //         System.out.println(indexResponse.getId());
        //         System.out.println(indexResponse.getResult());
        //     }
        //
        //     @Override
        //     public void onFailure(Exception e)
        //     {
        //         e.printStackTrace();
        //         throw new RuntimeException(e);
        //     }
        // };
        // client.indexAsync(indexRequest, RequestOptions.DEFAULT, listener);
        // try
        // {
        //     Thread.sleep(1000);
        // }
        // catch (InterruptedException e)
        // {
        //     throw new RuntimeException(e);
        // }
    }

    @Test
    public void testUpdate() throws IOException
    {
        UpdateRequest updateRequest = new UpdateRequest("test_posts", "3");

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "Tomas JJ");
        updateRequest.doc(jsonMap);

        // 设置超时时间
        updateRequest.timeout("1s");
        // 设置重试次数
        updateRequest.retryOnConflict(3);

        // 设置在继续更新之前，必须激活的分片数
        // updateRequest.waitForActiveShards(2);
        // 所有分片都是active状态，才更新
        // updateRequest.waitForActiveShards(ActiveShardCount.ALL);

        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.getVersion());
        System.out.println(updateResponse.getIndex());
        System.out.println(updateResponse.getResult());

        if (updateResponse.getResult() == DocWriteResponse.Result.NOOP)
        {
            System.out.println("数据未发生变更！");
        }
    }

    @Test
    public void testDelete() throws IOException
    {
        // 1.构建请求
        DeleteRequest deleteRequest = new DeleteRequest("test_posts", "3");

        // 2.执行操作
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);

        // 3.获取结果
        System.out.println(deleteResponse.getId());
        System.out.println(deleteResponse.getIndex());
        System.out.println(deleteResponse.getResult());
    }
}
