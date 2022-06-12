package com.eric.es;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
}
