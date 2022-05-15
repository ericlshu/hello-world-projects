package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.Follow;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.hmdp.service.IFollowService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService
{
    @Resource
    private IUserService userService;

    @Resource
    private IFollowService followService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryHotBlogs(Integer current)
    {
        // 根据用户查询
        Page<Blog> page = this.query()
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 查询用户
        records.forEach(this::queryBlogDetails);
        return Result.ok(records);
    }

    @Override
    public Result queryBolgById(Long id)
    {
        // 1.查询blog
        Blog blog = getById(id);
        if (blog == null)
        {
            return Result.fail("笔记不存在！");
        }
        // 2.查询笔记blog相关信
        queryBlogDetails(blog);
        return Result.ok(blog);
    }

    @Override
    public Result likeBlog(Long id)
    {
        // 1.获取登录用户
        String userId = UserHolder.getUser().getId().toString();
        // 2.判断是否已经点赞
        String key = RedisConstants.BLOG_LIKED_KEY + id;
        // Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, userId);
        Double score = stringRedisTemplate.opsForZSet().score(key, userId);
        // 3.如果未点赞，可以点赞
        // if (BooleanUtil.isFalse(isLiked))
        if (score == null)
        {
            // 3.1.数据库点赞数 + 1
            boolean success = update().setSql("liked = liked + 1").eq("id", id).update();
            // 3.2.保存用户到Redis的set集合
            if (success)
                // 使用SortedSet代替Set实现点赞排行榜功能
                // stringRedisTemplate.opsForSet().add(key, userId);
                // zadd key value score
                stringRedisTemplate.opsForZSet().add(key, userId, System.currentTimeMillis());
        }
        // 4.如果已点赞，取消点赞
        else
        {
            // 4.1.数据库点赞数 -1
            boolean success = update().setSql("liked = liked - 1").eq("id", id).update();
            // 4.2.把用户从Redis的set集合移除
            if (success)
                // stringRedisTemplate.opsForSet().remove(key, userId);
                stringRedisTemplate.opsForZSet().remove(key, userId);
        }
        return Result.ok();
    }

    @Override
    public Result queryBolgLikes(Long id)
    {
        String key = RedisConstants.BLOG_LIKED_KEY + id;
        // 1.查询top5的点赞用户 zrange key 0 4
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        if (top5 == null || top5.isEmpty())
            return Result.ok(Collections.emptyList());
        // 2.解析出其中的用户id
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        // 3.根据用户id查询用户
        // List<UserDTO> userDTOList = userService.listByIds(ids)
        //         .stream()
        //         .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
        //         .collect(Collectors.toList());
        // 使用IN查询时，用以下API指定排序方式
        // WHERE id IN ( 5 , 1 ) ORDER BY FIELD(id, 5, 1)
        String idStr = StrUtil.join(",", ids);
        List<UserDTO> userDTOList = userService.query()
                .in("id", ids)
                .last("ORDER BY FIELD(id," + idStr + ")")
                .list()
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());
        // 4.返回结果
        return Result.ok(userDTOList);
    }

    @Override
    public Result saveBlog(Blog blog)
    {
        // 1. 获取登录用户
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        // 2. 保存探店博文
        boolean success = save(blog);
        if (!success)
            return Result.fail("新增笔记失败!");
        // 3. 查询笔记作者的所有粉丝
        List<Follow> follows = followService.query().eq("follow_user_id", user.getId()).list();
        // 4.推送笔记id给所有粉丝
        for (Follow follow : follows)
        {
            // 4.1.获取粉丝id
            Long userId = follow.getUserId();
            // 4.2.推送
            String key = RedisConstants.FEED_KEY + userId;
            stringRedisTemplate.opsForZSet().add(key, blog.getId().toString(), System.currentTimeMillis());

        }
        // 5. 返回id
        return Result.ok(blog.getId());
    }

    /**
     * 判断是否已经点赞
     *
     * @param blog 笔记对象
     */
    private void isBlogLiked(Blog blog)
    {
        // 1.获取登录用户
        String userId = UserHolder.getUser().getId().toString();
        // 2.判断是否已经点赞
        String key = RedisConstants.BLOG_LIKED_KEY + blog.getId();
        Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, userId);
        // 3.设置点赞标志位
        blog.setIsLike(BooleanUtil.isTrue(isLiked));
    }

    /**
     * 查询笔记blog相关信息
     *
     * @param blog 笔记对象
     */
    private void queryBlogDetails(Blog blog)
    {
        // 1 查询blog有关的用户
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());

        // 2 判断当前用户是否已经点赞
        // 2.1 获取登录用户
        UserDTO currentUser = UserHolder.getUser();
        if (currentUser == null)
            // 2.2 用户未登录，无需查询是否点赞
            return;
        String currentUserId = currentUser.getId().toString();
        // 2.3 判断是否已经点赞
        String key = RedisConstants.BLOG_LIKED_KEY + blog.getId();
        // Boolean isLiked = stringRedisTemplate.opsForSet().isMember(key, currentUserId);
        Double score = stringRedisTemplate.opsForZSet().score(key, currentUserId);
        // 2.4 设置点赞标志位
        // blog.setIsLike(BooleanUtil.isTrue(isLiked));
        blog.setIsLike(score != null);
    }
}
