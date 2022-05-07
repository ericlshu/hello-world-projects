package com.hmdp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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
        session.setAttribute("code", code);
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
        Object sessionCode = session.getAttribute("code");
        String inputCode = loginForm.getCode();
        if (sessionCode == null || !sessionCode.toString().equals(inputCode))
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
        session.setAttribute("user", user);
        // 7.1.随机生成token，作为登录令牌
        // 7.2.将User对象转为HashMap存储
        // 7.3.存储
        // 7.4.设置token有效期
        // 8.返回token

        return null;
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
