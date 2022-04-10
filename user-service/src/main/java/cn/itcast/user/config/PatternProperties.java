package cn.itcast.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description : 配置自动刷新
 * 方式二：使用@ConfigurationProperties注解
 *
 * @author Eric L SHU
 * @date 2022-04-10 20:52
 * @since jdk-11.0.14
 */
@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties
{
    private String dateformat;
}
