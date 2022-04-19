package cn.itcast.account.service.impl;

import cn.itcast.account.entity.AccountFreeze;
import cn.itcast.account.mapper.AccountFreezeMapper;
import cn.itcast.account.mapper.AccountMapper;
import cn.itcast.account.service.AccountTCCService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-19 19:15
 * @since jdk-11.0.14
 */
@Slf4j
@Service
public class AccountTCCServiceImpl implements AccountTCCService
{
    @Resource
    private AccountMapper accountMapper;

    @Resource
    private AccountFreezeMapper freezeMapper;

    @Override
    @Transactional
    public void deduct(String userId, int money)
    {
        // 0.获取事务id
        String xid = RootContext.getXID();

        // 1.业务悬挂判断
        AccountFreeze freeze = freezeMapper.selectById(xid);
        if (freeze != null)
        {
            // 如果freeze中已有记录，则已经执行过cancel，拒绝业务
            return;
        }
        // 2.扣减可用余额
        accountMapper.deduct(userId, money);
        // 3.记录冻结金额，事务状态
        freezeMapper.insert(
                new AccountFreeze(
                        xid,
                        userId,
                        money,
                        AccountFreeze.State.TRY));
    }

    @Override
    public boolean confirm(BusinessActionContext context)
    {
        // 根据id删除冻结记录
        return freezeMapper.deleteById(context.getXid()) == 1;
    }

    @Override
    public boolean cancel(BusinessActionContext context)
    {
        // 0.查询冻结记录
        AccountFreeze accountFreeze = freezeMapper.selectById(context.getXid());
        // 1.空回滚判断
        if (accountFreeze == null)
        {
            // 证明try没有执行，需要执行空回滚
            freezeMapper.insert(
                    new AccountFreeze(
                            RootContext.getXID(),
                            context.getActionContext("userId").toString(),
                            0,
                            AccountFreeze.State.CANCEL));
            return true;
        }
        // 2.幂等判断
        else if (AccountFreeze.State.CANCEL == accountFreeze.getState())
        {
            // 已经执行过回滚，无需重复执行
            return true;
        }
        // 3.执行回滚
        else
        {
            // 3.1.恢复可用余额
            accountMapper.refund(accountFreeze.getUserId(), accountFreeze.getFreezeMoney());
            // 3.2.将冻结金额清零，状态改为CANCEL
            accountFreeze.setFreezeMoney(0);
            accountFreeze.setState(AccountFreeze.State.CANCEL);
            return freezeMapper.updateById(accountFreeze) == 1;
        }
    }
}
