package com.eric.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-27 22:48
 * @since jdk-11.0.14
 */
public class CuratorTest1
{
    /**
     * 建立连接
     */
    @Test
    public void connect()
    {
        /*
         * connectString       list of servers to connect to
         * sessionTimeoutMs    session timeout
         * connectionTimeoutMs connection timeout
         * retryPolicy         retry policy to use
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "120.26.107.127:2181",
                60_000,
                15_000,
                retryPolicy);
        client.start();

        client = CuratorFrameworkFactory.builder()
                .connectString("120.26.107.127:2181")
                .sessionTimeoutMs(60_000)
                .connectionTimeoutMs(15_000)
                .retryPolicy(retryPolicy)
                .namespace("eric")
                .build();
        client.start();
    }
}
