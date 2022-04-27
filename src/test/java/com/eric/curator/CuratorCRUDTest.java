package com.eric.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-27 22:48
 * @since jdk-11.0.14
 */
public class CuratorCRUDTest
{
    private CuratorFramework client;

    /******************************************************************************************************************/

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

    /******************************************************************************************************************/

    @Test
    public void testGet1() throws Exception
    {
        // 查询数据 get
        byte[] data = client.getData().forPath("/app1");
        System.out.println("data = " + new String(data));
    }

    @Test
    public void testGet2() throws Exception
    {
        // 查询子节点 ls
        List<String> nodeList = client.getChildren().forPath("/");
        System.out.println("nodeList = " + nodeList);
    }

    @Test
    public void testGet3() throws Exception
    {
        // 查询节点状态信息 ls -s
        Stat status = new Stat();
        System.out.println("status = " + status);
        client.getData().storingStatIn(status).forPath("/app1");
        System.out.println("status = " + status);
    }

    /******************************************************************************************************************/

    @Test
    public void testSet() throws Exception
    {
        client.setData().forPath("/app1", "修改数据".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testSetWithVersion() throws Exception
    {
        Stat status = new Stat();
        client.getData().storingStatIn(status).forPath("/app1");
        client.setData().withVersion(status.getVersion())
                .forPath("/app1", "修改数据with version".getBytes(StandardCharsets.UTF_8));
    }

    /******************************************************************************************************************/

    @Test
    public void testDelete() throws Exception
    {
        // 删除单个节点
        client.delete().forPath("/app1");
    }

    @Test
    public void testDeleteAll() throws Exception
    {
        // 删除带有子节点的节点
        client.delete().deletingChildrenIfNeeded().forPath("/app4");
    }

    @Test
    public void testDeleteGuaranteed() throws Exception
    {
        // 保证删除成功，防止网络抖动，自动重试
        client.delete().guaranteed().forPath("/app4");
    }

    @Test
    public void testDeleteCallback() throws Exception
    {
        // 删除回调操作
        client.delete().guaranteed().inBackground(
                (client, event) ->
                {
                    System.out.println("client = " + client);
                    System.out.println("event = " + event);

                }).forPath("/app1");
    }

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

    @After
    public void close()
    {
        if (client != null)
        {
            client.close();
        }
    }
}
