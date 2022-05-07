package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务实现类
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService
{
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendCode(String phone, HttpSession session)
    {
        // 1.校验手机号
        if (RegexUtils.isPhoneInvalid(phone))
        {
            // 2.不符合，返回错误信息
            return Result.fail("手机号格式错误！");
        }
        // 3.符合，生成验证码
        String code = RandomUtil.randomString(6);
        // 4.保存验证码到 session
        // session.setAttribute("code", code);
        // 4.保存验证码到 redis // set ke value ex 120
        stringRedisTemplate.opsForValue().set(
                RedisConstants.LOGIN_CODE_KEY + phone, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 5.发送验证码
        log.debug("发送短信验证码成功,验证码:{}", code);
        // 返回ok
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session)
    {
        // 1.校验手机号
        String phone = loginForm.getPhone();
        if (RegexUtils.isPhoneInvalid(phone))
        {
            // 2.不符合，返回错误信息
            return Result.fail("手机号格式错误！");
        }

        // 3.验证码校验
        // Object sessionCode = session.getAttribute("code");
        String redisCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
        String inputCode = loginForm.getCode();
        // if (sessionCode == null || !sessionCode.toString().equals(inputCode))
        if (redisCode == null || !redisCode.equals(inputCode))
        {
            // 4.1.不一致，报错
            return Result.fail("验证码错误！");
        }
        // 4.2.一致，根据手机号查询用户
        User user = query().eq("phone", phone).one();

        // 5.判断用户是否存在
        if (user == null)
        {
            // 6.不存在，创建新用户并保存
            user = createUserWithPhone(phone);
        }

        // 7.保存用户信息到session中
        // session.setAttribute("user", user);
        // 隐藏敏感信息
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // session.setAttribute("user", userDTO);

        // 7.1.随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);

        // 7.2.将User对象转为HashMap存储
        // stringRedisTemplate只能处理字符串类型数据，需要把HashMap中的long类型转成String类型
        CopyOptions copyOptions = CopyOptions.create()
                .setIgnoreNullValue(true)
                .setFieldValueEditor((fieldName, filedValue) -> filedValue.toString());
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(), copyOptions);

        // 7.3.存储token到redis
        String key = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(key, userMap);

        // 7.4.设置token有效期
        stringRedisTemplate.expire(key, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 8.返回token
        return Result.ok(token);
    }

    private User createUserWithPhone(String phone)
    {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        save(user);
        return user;
    }
}
