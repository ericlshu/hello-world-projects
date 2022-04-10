package cn.itcast.user.web;

import cn.itcast.user.config.PatternProperties;
import cn.itcast.user.pojo.User;
import cn.itcast.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 配置自动刷新
 * 方式一：在@Value注入的变量所在类上添加注解@RefreshScope
 */
@Slf4j
@RestController
@RequestMapping("/user")
// @RefreshScope
public class UserController
{
    @Resource
    private UserService userService;

    // @Value("${pattern.dateformat}")
    // private String dateformat;

    @Resource
    private PatternProperties patternProperties;

    @GetMapping("now")
    public String now()
    {
        // log.debug("dateformat : {}", dateformat);
        // return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
        log.debug("dateformat : {}", patternProperties.getDateformat());
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patternProperties.getDateformat()));
    }

    /**
     * 路径： /user/110
     *
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id)
    {
        return userService.queryById(id);
    }
}
