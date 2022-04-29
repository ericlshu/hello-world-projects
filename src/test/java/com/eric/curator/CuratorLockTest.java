package com.eric.curator;

import com.eric.domain.Ticket12306;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-27 22:48
 * @since jdk-11.0.14
 */
public class CuratorLockTest
{
    public static void main(String[] args) throws InterruptedException
    {
        Ticket12306 ticket12306 = new Ticket12306();
        Thread t1 = new Thread(ticket12306, "携程");
        Thread t2 = new Thread(ticket12306, "飞猪");
        t1.start();
        t2.start();
    }
}
