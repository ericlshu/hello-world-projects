package com.eric;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-01 0:35
 * @since jdk-11.0.14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-consumer.xml")
public class ConsumerTest
{
    @Test
    public void test() throws InterruptedException
    {
        Thread.sleep(10_000);
    }
}
