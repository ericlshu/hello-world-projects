package com.hmdp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description : 登陆检验拦截器
 *
 * @author Eric L SHU
 * @date 2022-05-07 18:12
 * @since jdk-11.0.14
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor
{
    /*
    private final StringRedisTemplate stringRedisTemplate;
    // LoginInterceptor是new出来而不是spring创建，所以需要手动注入stringRedisTemplate
    public LoginInterceptor(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate = stringRedisTemplate;
    }*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        log.info("登陆校验拦截器");

        // UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        // log.debug("user : {}", user);
        // if (user == null)
        // {
        //     response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //     return false;
        // }
        // UserHolder.saveUser(user);

        /*
        // 1.获取请求头中的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token))
        {
            // token为空，未登录，拦截返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 2.基于token获取redis中的user
        String key = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        if (userMap.isEmpty())
        {
            // user为空，未登录，拦截返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 3.将Hash对象转为DTO对象
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        log.debug("userDTO in redis : {}", userDTO);

        // 4.保存用户到ThreadLocal
        UserHolder.saveUser(userDTO);

        // 5.刷新token有限期
        stringRedisTemplate.expire(key, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        */

        // 1.判断是否需要拦截（ThreadLocal中是否有用户）
        if (UserHolder.getUser() == null)
        {
            // 没有用户需要拦截，设置状态码401并拦截
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        // 有用户，则放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        UserHolder.removeUser();
    }
}
