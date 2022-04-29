package com.eric.domain;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-29 14:08
 * @since jdk-11.0.14
 */
public class Ticket12306 implements Runnable
{
    private int tickets = 10;

    private final InterProcessMutex lock;

    public Ticket12306()
    {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("120.26.107.127:2181")
                .sessionTimeoutMs(60_000)
                .connectionTimeoutMs(15_000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        lock = new InterProcessMutex(client, "/lock");
    }

    @Override
    public void run()
    {
        while (true)
        {
            //获取锁
            try
            {
                lock.acquire(3, TimeUnit.SECONDS);
                if (tickets > 0)
                {
                    System.out.println(Thread.currentThread() + ":" + tickets);
                    Thread.sleep(100);
                    tickets--;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                //释放锁
                try
                {
                    lock.release();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
