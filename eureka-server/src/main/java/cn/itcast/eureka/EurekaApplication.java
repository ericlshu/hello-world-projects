package cn.itcast.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-10 12:08
 * @since jdk-11.0.14
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
