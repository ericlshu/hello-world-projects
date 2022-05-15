package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.SimpleRedisLock;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService
{
    @Resource
    private ISeckillVoucherService secKillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static
    {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("lua/secKill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    private final BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024);

    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init()
    {
        log.info("线程池异步提交！");
        // SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    private class VoucherOrderHandler implements Runnable
    {
        @Override
        public void run()
        {
            while (true)
            {
                try
                {
                    // 1.获取阻塞消息队列中的订单信息
                    // VoucherOrder voucherOrder = orderTasks.take();

                    String steamKey = "stream.orders";
                    String consumerGroupName = "g1";
                    String consumerName = "c1";


                    // 获取消息队列中的订单信息
                    // XREADGROUP GROUP g1 c1 COUNT 1 BLOCK 2000 STREAMS s1 0
                    List<MapRecord<String, Object, Object>> recordList = stringRedisTemplate.opsForStream().read(
                            Consumer.from(consumerGroupName, consumerName),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                            StreamOffset.create(steamKey, ReadOffset.lastConsumed())
                    );
                    if (recordList == null || recordList.isEmpty())
                    {
                        // 没有消息，继续下一次循环
                        continue;
                    }

                    // 解析数据
                    MapRecord<String, Object, Object> record = recordList.get(0);
                    Map<Object, Object> map = record.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(map, new VoucherOrder(), true);

                    // 2.创建订单
                    log.info("独立线程异步创建订单！");
                    createVoucherOrder(voucherOrder);
                    // 消息确认
                    stringRedisTemplate.opsForStream().acknowledge(steamKey, consumerGroupName, record.getId());
                }
                catch (Exception e)
                {
                    log.error("处理订单异常！", e);
                    handlePendingList();
                }
            }
        }
    }

    private void handlePendingList()
    {
        while (true)
        {
            try
            {
                String steamKey = "stream.orders";
                String consumerGroupName = "g1";
                String consumerName = "c1";

                // 获取pending-list中的订单信息
                List<MapRecord<String, Object, Object>> recordList = stringRedisTemplate.opsForStream().read(
                        Consumer.from(consumerGroupName, consumerName),
                        StreamReadOptions.empty().count(1),
                        StreamOffset.create(steamKey, ReadOffset.from("0"))
                );
                if (recordList == null || recordList.isEmpty())
                {
                    // 没有异常消息，结束循环
                    break;
                }

                // 解析数据
                MapRecord<String, Object, Object> record = recordList.get(0);
                Map<Object, Object> map = record.getValue();
                VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(map, new VoucherOrder(), true);

                // 创建订单
                createVoucherOrder(voucherOrder);
                // 消息确认
                stringRedisTemplate.opsForStream().acknowledge(steamKey, consumerGroupName, record.getId());
            }
            catch (Exception e)
            {
                log.error("处理订单异常！", e);
            }
        }
    }

    private void createVoucherOrder(VoucherOrder voucherOrder)
    {
        Long userId = voucherOrder.getUserId();
        Long voucherId = voucherOrder.getVoucherId();
        // 获取锁对象
        RLock redisLock = redissonClient.getLock("lock:order:" + userId);
        try
        {
            if (!redisLock.tryLock(1, TimeUnit.SECONDS))
            {
                log.error("请勿重复下单！");
                return;
            }

            Long count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if (count > 0)
            {
                log.error("当前用户已经购买过优惠券！");
                return;
            }

            log.info("扣减库存开始！");

            // 扣减库存
            boolean success = secKillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId)
                    // .eq("stock", voucher.getStock())    // CAS乐观锁解决超卖问题，但会造成失败率降低的问题
                    .gt("stock", 0)
                    .update();
            if (!success)
            {
                log.error("优惠券已抢完！");
                return;
            }
            log.info("扣减库存成功！");

            // 创建订单
            save(voucherOrder);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            redisLock.unlock();
        }
    }

    @Override
    public Result secKill(Long voucherId)
    {
        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("order");

        // 1.执行lua脚本
        int result = Objects.requireNonNull(stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString(),
                String.valueOf(orderId)
        )).intValue();

        // 2.判断结果
        if (result != 0)
        {
            // 2.1.不为0，代表没有购买资格
            if (result == 1)
                return Result.fail("优惠券已抢完！");
            else if (result == 2)
                return Result.fail("请勿重复下单！");
        }

        // 3.返回订单id
        return Result.ok(orderId);
    }

    public Result secKillBak2(Long voucherId)
    {
        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("order");

        // 1.执行lua脚本
        int result = Objects.requireNonNull(stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString()
        )).intValue();

        // 2.判断结果
        if (result != 0)
        {
            // 2.1.不为0，代表没有购买资格
            if (result == 1)
                return Result.fail("优惠券已抢完！");
            else if (result == 2)
                return Result.fail("请勿重复下单！");
        }
        else
        {
            // 2.2.有购买资格，把下单信息保存到阻塞队列
            VoucherOrder voucherOrder = new VoucherOrder();
            voucherOrder.setId(orderId);
            voucherOrder.setUserId(userId);
            voucherOrder.setVoucherId(voucherId);

            // 2.3.放入阻塞队列
            log.info("将优惠券订单信息放入阻塞队列！");
            orderTasks.add(voucherOrder);
        }

        // 3.返回订单id
        return Result.ok(orderId);
    }

    public Result secKillBak1(Long voucherId)
    {
        // 1. 查询优惠券
        SeckillVoucher voucher = secKillVoucherService.getById(voucherId);

        LocalDateTime currentTime = LocalDateTime.now();
        // 2. 判断是否开始
        if (currentTime.isBefore(voucher.getBeginTime()))
            return Result.fail("秒杀活动尚未开始！");

        // 3. 判断是否结束
        if (currentTime.isAfter(voucher.getEndTime()))
            return Result.fail("秒杀活动已经结束！");

        // 4. 判断库存是否充足
        if (voucher.getStock() < 1)
            return Result.fail("优惠券已抢完！");

        try
        {
            return creatVoucherOrder(voucherId);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Result creatVoucherOrder(Long voucherId) throws InterruptedException
    {
        // 添加一人一单校验功能
        Long userId = UserHolder.getUser().getId();

        RLock redisLock = redissonClient.getLock("lock:order:" + userId);

        if (!redisLock.tryLock(1, TimeUnit.SECONDS))
            return Result.fail("请勿重复下单！");

        long orderId;
        try
        {
            Long count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if (count > 0)
                return Result.fail("当前用户已经购买过优惠券！");

            // 5. 扣减库存
            boolean success = secKillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId)
                    // .eq("stock", voucher.getStock())    // CAS乐观锁解决超卖问题，但会造成失败率降低的问题
                    .gt("stock", 0)
                    .update();
            if (!success)
                return Result.fail("优惠券已抢完！");

            // 6. 创建订单
            orderId = redisIdWorker.nextId("order");

            VoucherOrder voucherOrder = new VoucherOrder();
            voucherOrder.setId(orderId);
            voucherOrder.setUserId(userId);
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);
        }
        finally
        {
            redisLock.unlock();
        }

        // 7. 返回结果
        return Result.ok(orderId);
    }

    @Transactional
    public Result creatVoucherOrderBak2(Long voucherId)
    {
        // 添加一人一单校验功能
        Long userId = UserHolder.getUser().getId();

        SimpleRedisLock simpleRedisLock = new SimpleRedisLock("order:" + userId, stringRedisTemplate);

        if (!simpleRedisLock.tryLock(300))
            return Result.fail("请勿重复下单！");

        long orderId;
        try
        {
            Long count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if (count > 0)
                return Result.fail("当前用户已经购买过优惠券！");

            // 5. 扣减库存
            boolean success = secKillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId)
                    // .eq("stock", voucher.getStock())    // CAS乐观锁解决超卖问题，但会造成失败率降低的问题
                    .gt("stock", 0)
                    .update();
            if (!success)
                return Result.fail("优惠券已抢完！");

            // 6. 创建订单
            orderId = redisIdWorker.nextId("order");

            VoucherOrder voucherOrder = new VoucherOrder();
            voucherOrder.setId(orderId);
            voucherOrder.setUserId(userId);
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);
        }
        finally
        {
            simpleRedisLock.unlock();
        }

        // 7. 返回结果
        return Result.ok(orderId);
    }

    @Transactional
    public Result creatVoucherOrderBak1(Long voucherId)
    {
        // 添加一人一单校验功能
        Long userId = UserHolder.getUser().getId();
        // 对方法加锁相当于对当前类加锁，并发场景下为串行执行
        // 对当前用户id加锁，保证不同的user使用同一事务
        // 使用intern()方法将字符串放入常量池，保证同一用户取到同一字符串对象
        synchronized (userId.toString().intern())
        {
            Long count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if (count > 0)
                return Result.fail("当前用户已经购买过优惠券！");

            // 5. 扣减库存
            boolean success = secKillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId)
                    // .eq("stock", voucher.getStock())    // CAS乐观锁解决超卖问题，但会造成失败率降低的问题
                    .gt("stock", 0)
                    .update();
            if (!success)
                return Result.fail("优惠券已抢完！");

            // 6. 创建订单
            long orderId = redisIdWorker.nextId("order");

            VoucherOrder voucherOrder = new VoucherOrder();
            voucherOrder.setId(orderId);
            voucherOrder.setUserId(userId);
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);

            // 7. 返回结果
            return Result.ok(orderId);
        }
    }
}
