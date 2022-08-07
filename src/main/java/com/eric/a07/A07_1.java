package com.eric.a07;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/*
 * 初始化和销毁的执行顺序
 */
@SpringBootApplication
public class A07_1
{
    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = SpringApplication.run(A07_1.class, args);
        context.close();
    }

    @Bean(initMethod = "init3")
    public Bean1 bean1()
    {
        return new Bean1();
    }

    @Bean(destroyMethod = "destroy3")
    public Bean2 bean2()
    {
        return new Bean2();
    }
}
