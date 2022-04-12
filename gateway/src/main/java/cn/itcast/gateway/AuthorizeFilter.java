package cn.itcast.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description : 过滤器执行顺序
 * <p>
 * --> 请求进入网关会碰到三类过滤器：当前路由的过滤器、DefaultFilter、GlobalFilter
 * --> 请求路由后，会将当前路由过滤器和DefaultFilter、GlobalFilter，合并到一个过滤器链（集合）中，排序后依次执行每个过滤器
 * <p>
 * --> 每一个过滤器都必须指定一个int类型的order值，order值越小，优先级越高，执行顺序越靠前。
 * --> GlobalFilter通过实现Ordered接口，或者添加@Order注解来指定order值，由我们自己指定。
 * --> 路由过滤器和defaultFilter的order由Spring指定，默认是按照声明顺序从1递增。
 * --> 当过滤器的order值一样时，会按照 defaultFilter > 路由过滤器 > GlobalFilter的顺序执行。
 *
 * @author Eric L SHU
 * @date 2022-04-12 22:10
 * @since jdk-11.0.14
 */
// @Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered
{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        // 1. 获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        // 2. 获取参数中的 authorization 参数
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String authorization = queryParams.getFirst("authorization");
        // 3. 判断参数值是否等于 admin
        if ("admin".equals(authorization))
        {
            // 4.1 是，放行
            return chain.filter(exchange);
        }
        else
        {
            // 4.2 否，拦截，并设置状态码
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder()
    {
        return -1;
    }
}
