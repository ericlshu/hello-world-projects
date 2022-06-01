package com.eric.service;

import com.eric.domain.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description :
 *
 * @author Eric SHU
 */
@SpringBootTest
public class CommentServiceTest
{
    @Autowired
    private CommentService commentService;

    /**
     * 保存一个评论
     */
    @Test
    public void testSaveComment()
    {
        Comment comment = new Comment();
        comment.setArticleId("100000");
        comment.setContent("测试添加的数据");
        comment.setCreateTime(LocalDateTime.now());
        comment.setUserid("1003");
        comment.setNickname("凯撒大帝");
        comment.setState("1");
        comment.setLikeNum(0);
        comment.setReplyNum(0);
        commentService.saveComment(comment);
    }

    /**
     * 查询所有数据
     */
    @Test
    public void testFindAll()
    {
        List<Comment> list = commentService.findCommentList();
        System.out.println(list);
    }

    /**
     * 测试根据id查询
     */
    @Test
    public void testFindCommentById()
    {
        Comment comment = commentService.findCommentById("629738195edd342cbbccdc2a");
        System.out.println(comment);
    }

    /**
     * 测试根据父id查询子评论的分页列表
     */
    @Test
    public void testFindCommentListPageByParentId()
    {
        Page<Comment> pageResponse = commentService.findCommentListPageByParentId("3", 1, 2);
        System.out.println("----总记录数:" + pageResponse.getTotalElements());
        System.out.println("----当页数据:" + pageResponse.getContent());
    }

    /**
     * 点赞数+1
     */
    @Test
    public void testUpdateCommentLikeNum()
    {
        commentService.updateCommentLikeNum("62973e07cee32069a086939d");
    }
}
