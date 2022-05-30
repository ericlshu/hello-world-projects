package com.eric;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

/**
 * Description :
 *
 * @author Eric SHU
 */
public class Generator
{
    public static void main(String[] args)
    {
        AutoGenerator autoGenerator = new AutoGenerator();

        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.3.33:3306/spring");
        dataSource.setUsername("eric");
        dataSource.setPassword("1234");
        autoGenerator.setDataSource(dataSource);

        autoGenerator.execute();
    }
}
