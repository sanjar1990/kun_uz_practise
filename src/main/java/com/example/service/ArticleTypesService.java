package com.example.service;

import com.example.entity.article_types.ArticleTypesEntity;
import com.example.repository.ArticleTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTypesService {
    @Autowired
    private ArticleTypesRepository articleTypesRepository;
    public void create(String articleId, List<Integer> articleTypes) {
        articleTypes.forEach(s->create(articleId, s));
    }
    private void create(String articleId, Integer articleTypeId) {
        ArticleTypesEntity entity = new ArticleTypesEntity();
        entity.setArticleId(articleId);
        entity.setArticleTypeId(articleTypeId);
        entity.setVisible(Boolean.TRUE);
        articleTypesRepository.save(entity);
    }

    public void merge(String articleId, List<Integer> newTypeList) {

        List<Integer>oldList=articleTypesRepository.selectArticleTypeList(articleId);
        for (Integer id : newTypeList) {
            if(!oldList.contains(id)) {
                create(articleId, id);
            }
        }
        for (Integer id : oldList) {
         if(!newTypeList.contains(id)) {
             articleTypesRepository.deleteByArticleId(articleId,id);
         }
        }
    }

}
