package cn.itcast.order.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Description : 从request中获取一个名为origin的请求头，作为origin的值
 *
 * @author Eric L SHU
 * @date 2022-04-18 21:55
 * @since jdk-11.0.14
 */
@Slf4j
@Component
public class HeaderOriginParser implements RequestOriginParser
{
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest)
    {
        String originHeader = httpServletRequest.getHeader("origin");
        log.info("originHeader = " + originHeader);
        return StringUtils.hasText(originHeader) ? originHeader : "blank";
    }
}
