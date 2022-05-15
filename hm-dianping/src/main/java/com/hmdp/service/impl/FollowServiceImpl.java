package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService
{
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Override
    public Result follow(Long followUserId, Boolean isFollow)
    {
        // 0.获取登录用户
        UserDTO userDTO = UserHolder.getUser();
        if (userDTO == null)
            return Result.fail("请先登陆再进行操作！");
        Long userId = userDTO.getId();
        String key = "follows:" + userId;
        // 1.判断到底是关注还是取关
        if (isFollow)
        {
            // 2.关注，新增数据
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            boolean save = save(follow);
            if (save)
                // 把关注用户的id，放入redis的set集合 sadd userId followerUserId
                stringRedisTemplate.opsForSet().add(key, followUserId.toString());
        }
        else
        {
            // 3.取关，删除数据
            boolean remove = remove(
                    new QueryWrapper<Follow>()
                            .eq("user_id", userId)
                            .eq("follow_user_id", followUserId));
            if (remove)
                // 把关注用户的id从Redis集合中移除
                stringRedisTemplate.opsForSet().remove(key, followUserId.toString());
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
        // 0.获取登录用户
        UserDTO userDTO = UserHolder.getUser();
        if (userDTO == null)
            return Result.fail("请先登陆再进行操作！");
        String currentUserKey = "follows:" + userDTO.getId();
        String targetUserKey = "follows:" + id;
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(currentUserKey, targetUserKey);
        if (intersect == null || intersect.isEmpty())
            return Result.ok(Collections.emptyList());
        List<Long> ids = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserDTO> userDTOList = userService.listByIds(ids)
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());
        return Result.ok(userDTOList);
    }
}
