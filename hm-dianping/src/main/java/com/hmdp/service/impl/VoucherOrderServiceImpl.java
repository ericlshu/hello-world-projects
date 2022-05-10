package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
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

    @Override
    @Transactional
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

        // 5. 扣减库存
        boolean success = secKillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId)
                .update();
        if (!success)
            return Result.fail("优惠券已抢完！");

        // 6. 创建订单
        long orderId = redisIdWorker.nextId("order");

        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setVoucherId(orderId);
        voucherOrder.setUserId(UserHolder.getUser().getId());
        voucherOrder.setVoucherId(voucherId);
        save(voucherOrder);

        // 7. 返回结果
        return Result.ok(orderId);
    }
}
