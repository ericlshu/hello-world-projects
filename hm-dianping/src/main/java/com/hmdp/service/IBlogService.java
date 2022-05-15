package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IBlogService extends IService<Blog>
{
    Result queryHotBlogs(Integer current);

    Result queryBolgById(Long id);

    Result likeBlog(Long id);

    Result queryBolgLikes(Long id);

    Result saveBlog(Blog blog);
}
