package cn.itcast.feign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-10 21:37
 * @since jdk-11.0.14
 */
@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties
{
    private String dateformat;
    private String envSharedValue;
    private String parameter;
    private String commonParam;
}
