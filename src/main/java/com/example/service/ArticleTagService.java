package com.example.service;

import com.example.entity.article_tag.ArticleTagEntity;
import com.example.entity.tag.TagEntity;
import com.example.repository.ArticleTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTagService {
    @Autowired
    private ArticleTagRepository articleTagRepository;

    public void create(String id, List<String> tagList) {
        tagList.forEach(s->create(id, s));
    }
    private void create(String articleId, String tagId){
        ArticleTagEntity entity = new ArticleTagEntity();
        entity.setArticleId(articleId);
        entity.setTagId(tagId);
        entity.setVisible(Boolean.TRUE);
        articleTagRepository.save(entity);
    }

    public void merge(String articleId, List<String> newList) {
        List<String> oldList=articleTagRepository.getIdByArticleIdAndVisibleTrue(articleId);

        for (String a:newList) {
            if(!oldList.contains(a)){
                create(articleId, a);
            }
        }
        for(String a:oldList){
            if(!newList.contains(a)){
                articleTagRepository.deleteTag(articleId,a);
            }
        }
    }
    public List<String>getTagListByArticleId(String articleId){
        return articleTagRepository.getByArticleId(articleId);
    }
}
