package com.example.repository;

import com.example.entity.article_like.ArticleLikeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends CrudRepository<ArticleLikeEntity,String> {
    Optional<ArticleLikeEntity> getByArticleIdAndProfileId(String articleId, String currentUserId);
    @Transactional
    @Modifying
    @Query("delete from ArticleLikeEntity where id=?1")
    int deleteByArticleId(String id);
}
