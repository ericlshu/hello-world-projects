package com.eric.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-27 22:48
 * @since jdk-11.0.14
 */
public class CuratorWatcherTest
{
    private CuratorFramework client;

    @Test
    public void testNodeCache() throws Exception
    {
        NodeCache nodeCache = new NodeCache(client, "/app1");
        nodeCache.getListenable().addListener(
                () ->
                {
                    System.out.println("Node changed ...");
                    byte[] data = nodeCache.getCurrentData().getData();
                    System.out.println("data = " + new String(data));
                });
        // 如果开启监听设置为true，则开启监听时加载缓冲数据
        nodeCache.start(true);
    }

    /**
     * 建立连接
     */
    @Before
    public void connect()
    {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        client = CuratorFrameworkFactory.builder()
                .connectString("120.26.107.127:2181")
                .sessionTimeoutMs(60_000)
                .connectionTimeoutMs(15_000)
                .retryPolicy(retryPolicy)
                .namespace("eric")
                .build();
        client.start();
    }

    @After
    public void close()
    {
        if (client != null)
        {
            client.close();
        }
    }
}
