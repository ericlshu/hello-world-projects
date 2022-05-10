package com.hmdp.service.impl;

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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService
{
    @Resource
    private ISeckillVoucherService secKillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override

    public Result secKill(Long voucherId)
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

        return creatVoucherOrder(voucherId);
    }

    @Transactional
    public Result creatVoucherOrder(Long voucherId)
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
            voucherOrder.setVoucherId(orderId);
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


    /*@Transactional
    public Result creatVoucherOrder(Long voucherId)
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
            voucherOrder.setVoucherId(orderId);
            voucherOrder.setUserId(userId);
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);

            // 7. 返回结果
            return Result.ok(orderId);
        }
    }*/
}
