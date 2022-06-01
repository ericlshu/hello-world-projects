package com.eric.dao;

import com.eric.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Description :
 *
 * @author Eric SHU
 */
public interface CommentRepository extends MongoRepository<Comment, String>
{
    Page<Comment> findByParentId(String parentId, Pageable pageable);
}
