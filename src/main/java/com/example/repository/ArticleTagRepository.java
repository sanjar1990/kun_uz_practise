package com.example.repository;

import com.example.entity.article_tag.ArticleTagEntity;
import com.example.entity.tag.TagEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleTagRepository extends CrudRepository<ArticleTagEntity,String> {
    @Query("select tagId from ArticleTagEntity where visible=true")
    List<String>getIdByArticleIdAndVisibleTrue(String articleId);

   @Transactional
   @Modifying
   @Query("update ArticleTagEntity set visible=false where articleId=?1 and tagId=?2")
    void deleteTag(String articleId, String a);
    @Query("select t.name from ArticleTagEntity as at inner join at.tag as t where at.articleId=?1 and at.visible=true")
    List<String> getByArticleId(String articleId);
}
