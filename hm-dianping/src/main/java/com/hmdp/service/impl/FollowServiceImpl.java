package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService
{

    @Override
    public Result follow(Long followUserId, Boolean isFollow)
    {
        // 0.获取登录用户
        UserDTO userDTO = UserHolder.getUser();
        if (userDTO == null)
            return Result.fail("请先登陆再进行操作！");
        Long userId = userDTO.getId();
        // 1.判断到底是关注还是取关
        if (isFollow)
        {
            // 2.关注，新增数据
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            save(follow);
        }
        else
        {
            // 3.取关，删除数据
            remove(new QueryWrapper<Follow>()
                           .eq("user_id", userId)
                           .eq("follow_user_id", followUserId));

        }
        return Result.ok();
    }

    @Override
    public Result isFollow(Long followUserId)
    {
        // 1.获取登录用户
        Long userId = UserHolder.getUser().getId();
        // 2.查询是否关注 select count(*) from tb_follow where user_id = ? and follow_user_id = ?
        Long count = query().eq("user_id", userId).eq("follow_user_id", followUserId).count();
        // 3.判断
        return Result.ok(count > 0);
    }

    @Override
    public Result followCommons(Long id)
    {
        return null;
    }
}
