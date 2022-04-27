package com.eric.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-27 22:48
 * @since jdk-11.0.14
 */
public class CuratorTest1
{
    private CuratorFramework client;

    /**
     * 建立连接
     */
    @Before
    public void connect()
    {
        /*
         * connectString       list of servers to connect to
         * sessionTimeoutMs    session timeout
         * connectionTimeoutMs connection timeout
         * retryPolicy         retry policy to use
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        // CuratorFramework client = CuratorFrameworkFactory.newClient(
        //         "120.26.107.127:2181",
        //         60_000,
        //         15_000,
        //         retryPolicy);
        // client.start();

        client = CuratorFrameworkFactory.builder()
                .connectString("120.26.107.127:2181")
                .sessionTimeoutMs(60_000)
                .connectionTimeoutMs(15_000)
                .retryPolicy(retryPolicy)
                .namespace("eric")
                .build();
        client.start();
    }

    @Test
    public void testCreate1() throws Exception
    {
        // 如果创建节点，没有指定数据，则默认将当前客户端的ip作为数据存储
        String path = client.create().forPath("/app1");
        System.out.println("path = " + path);
    }

    /**
     * 创建节点并指定数据
     */
    @Test
    public void testCreate2() throws Exception
    {
        String path = client.create().forPath("/app2", "app2节点数据".getBytes(StandardCharsets.UTF_8));
        System.out.println("path = " + path);
    }

    /**
     * 创建节点并指定类型
     */
    @Test
    public void testCreate3() throws Exception
    {
        String path = client.create().withMode(CreateMode.EPHEMERAL).forPath("/app3");
        System.out.println("path = " + path);
    }

    /**
     * 创建多级节点
     */
    @Test
    public void testCreate4() throws Exception
    {
        // 如果父节点不存在则创建父节点
        String path = client.create().creatingParentContainersIfNeeded().forPath("/app4/p1");
        System.out.println("path = " + path);
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
