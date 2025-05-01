package com.example.repository;

import com.example.entity.article_types.ArticleTypesEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleTypesRepository extends CrudRepository<ArticleTypesEntity,String > {

    @Transactional
    @Modifying
    @Query("update ArticleTypesEntity set visible=false where articleId=?1 and articleTypeId=?2")
    void deleteByArticleId(String articleId,Integer articleTypeId);
    @Query("select at.articleTypeId from ArticleTypesEntity as at where at.articleId=?1")
    List<Integer>selectArticleTypeList(String articleId);

}
