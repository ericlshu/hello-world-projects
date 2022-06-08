package com.eric.es;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
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
    private RestHighLevelClient highLevelClient;

    @Test
    public void testGet() throws IOException
    {
        GetRequest getRequest = new GetRequest("book", "2");
        GetResponse response = highLevelClient.get(getRequest, RequestOptions.DEFAULT);

        String id = response.getId();
        long version = response.getVersion();
        Map<String, Object> source = response.getSource();

        System.out.println("id = " + id);
        System.out.println("version = " + version);
        System.out.println("source = " + source);
    }
}
