package com.hmdp.utils;

import com.hmdp.entity.User;
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
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        log.info("用户请求拦截器");
        log.debug("pathInfo : {}", request.getPathInfo());
        User user = (User) request.getSession().getAttribute("user");
        log.debug("user : {}", user);
        if (user == null)
        {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        UserHolder.saveUser(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        UserHolder.removeUser();
    }
}
