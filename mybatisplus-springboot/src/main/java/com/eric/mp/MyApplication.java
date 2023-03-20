package com.eric.mp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description :
 *
 * @author Eric L SHU
 * @ MapperScan("com.eric.mp.mapper")
 * @date 2022-03-15 20:44
 */
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(MyApplication.class, args);
    }
}
