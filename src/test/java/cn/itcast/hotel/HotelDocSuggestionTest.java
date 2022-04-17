package cn.itcast.hotel;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-17 11:13
 * @since jdk-11.0.14
 */
@Slf4j
@SpringBootTest
public class HotelDocSuggestionTest
{
    public static final String INDEX_NAME = "hotel";

    private RestHighLevelClient client;
    private SearchRequest searchRequest;
    private SearchSourceBuilder builder;

    @BeforeEach
    void BeforeEach()
    {
        // 0 初始化RestClient对象
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://110.40.224.64:9200")));
        // 1 准备Request
        searchRequest = new SearchRequest(INDEX_NAME);
        // 2.1 准备SearchSourceBuilder
        builder = searchRequest.source();
    }

    @Test
    void testSuggestion() throws IOException
    {
        // 2.2 设置自动补全参数
        builder.suggest(new SuggestBuilder().addSuggestion("suggestions",
                                                           SuggestBuilders
                                                                   .completionSuggestion("suggestion")
                                                                   .prefix("hq")
                                                                   .skipDuplicates(true)
                                                                   .size(10)));
        // 3.发出请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // System.out.println("searchResponse = " + searchResponse);
        // 4.结果解析
        CompletionSuggestion suggestion = searchResponse.getSuggest().getSuggestion("suggestions");
        List<CompletionSuggestion.Entry.Option> options = suggestion.getOptions();
        for (CompletionSuggestion.Entry.Option option : options)
        {
            String result = option.getText().toString();
            System.out.println("result = " + result);
        }
    }

    @AfterEach
    void AfterEach() throws IOException
    {
        this.client.close();
    }
}
