package com.eric.a05;

import com.alibaba.druid.pool.DruidDataSource;
import com.eric.a05.component.Bean2;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.eric.a05.component")
public class Config
{
    @Bean
    public Bean1 bean1()
    {
        return new Bean1();
    }

    public Bean2 bean2()
    {
        return new Bean2();
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource)
    {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean(initMethod = "init")
    public DruidDataSource dataSource()
    {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://192.168.3.33:3306/spring");
        dataSource.setUsername("eric");
        dataSource.setPassword("1234");
        return dataSource;
    }

    /*@Bean
    public MapperFactoryBean<Mapper1> mapper1(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<Mapper1> factory = new MapperFactoryBean<>(Mapper1.class);
        factory.setSqlSessionFactory(sqlSessionFactory);
        return factory;
    }

    @Bean
    public MapperFactoryBean<Mapper2> mapper2(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<Mapper2> factory = new MapperFactoryBean<>(Mapper2.class);
        factory.setSqlSessionFactory(sqlSessionFactory);
        return factory;
    }*/
}
