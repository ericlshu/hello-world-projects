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
 * Description :
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
