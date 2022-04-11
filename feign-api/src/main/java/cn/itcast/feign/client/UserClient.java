package cn.itcast.feign.client;

import cn.itcast.feign.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-11 21:11
 * @since jdk-11.0.14
 */
@FeignClient("user-service")
public interface UserClient
{
    /**
     * 使用feign客户端获取user
     *
     * @param id user id
     * @return user
     */
    @GetMapping("/user/{id}")
    User findById(@PathVariable Long id);
}
