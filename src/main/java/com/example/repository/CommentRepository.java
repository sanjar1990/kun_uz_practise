package com.example.repository;

import com.example.entity.comment.CommentEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity,String>,
        PagingAndSortingRepository<CommentEntity,String> {
    Optional<CommentEntity> findByIdAndVisibleTrue(String id);
    @Transactional
    @Modifying
    @Query("update CommentEntity set visible=false where id=?1")
    int deleteComment(String id);
    Page<CommentEntity>getByArticleIdAndVisibleTrue(String articleId, Pageable pageable);
    Page<CommentEntity>getByVisibleTrue(Pageable pageable);
    List<CommentEntity>getAllByReplyIdAndVisibleTrue(String id);
}
