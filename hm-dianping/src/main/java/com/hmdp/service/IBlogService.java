package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IBlogService extends IService<Blog>
{
    Result queryBolgById(Long id);
    Result queryHotBlogs(Integer current);
    Result likeBlog(Long id);
}
