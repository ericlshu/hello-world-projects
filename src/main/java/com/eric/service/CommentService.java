package com.eric.service;

import com.eric.dao.CommentRepository;
import com.eric.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description :
 *
 * @author Eric SHU
 */
@Service
public class CommentService
{
    @Resource
    private CommentRepository commentRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 保存一个评论
     */
    public void saveComment(Comment comment)
    {
        //如果需要自定义主键，可以在这里指定主键；如果不指定主键，MongoDB会自动生成主键
        commentRepository.save(comment);
    }

    /**
     * 更新评论
     */
    public void updateComment(Comment comment)
    {
        commentRepository.save(comment);
    }

    /**
     * 根据id删除评论
     */
    public void deleteCommentById(String id)
    {
        commentRepository.deleteById(id);
    }

    /**
     * 查询所有评论
     */
    public List<Comment> findCommentList()
    {
        return commentRepository.findAll();
    }

    /**
     * 根据id查询评论
     */
    public Comment findCommentById(String id)
    {
        return commentRepository.findById(id).get();
    }

    public Page<Comment> findCommentListPageByParentId(String parentId, int page, int size)
    {
        return commentRepository.findByParentId(parentId, PageRequest.of(page - 1, size));
    }

    public void updateCommentLikeNum(String id)
    {
        //查询对象
        Query query = Query.query(Criteria.where("_id").is(id));
        //更新对象
        Update update = new Update();
        // 局部更新，相当于$set
        // update.set(key,value)
        // 递增$inc
        // update.inc("likeNum",1);
        update.inc("likeNum");
        //参数1：查询对象
        //参数2：更新对象
        //参数3：集合的名字或实体类的类型Comment.class
        mongoTemplate.updateFirst(query, update, "comment");
    }
}
