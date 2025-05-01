package com.example.service;

import com.example.entity.article_like.ArticleLikeEntity;
import com.example.enums.ArticleLiceStatus;
import com.example.repository.ArticleLikeRepository;
import com.example.utility.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
//@Transactional
public class ArticleLikeService {
    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    public Boolean like(String articleId) {
        Optional<ArticleLikeEntity> optional=getArticleLike(articleId,SpringSecurityUtil.getCurrentUserId());
        ArticleLikeEntity entity=new ArticleLikeEntity();
        if(optional.isPresent()){
            entity=optional.get();
            if(entity.getStatus().equals(ArticleLiceStatus.LIKE)){
                articleLikeRepository.delete(entity);
//                int n=articleLikeRepository.deleteByArticleId(entity.getId());
                return true;
            } {
                entity.setStatus(ArticleLiceStatus.LIKE);
            }
        }else{
            entity.setStatus(ArticleLiceStatus.LIKE);
            entity.setVisible(Boolean.TRUE);
            entity.setArticleId(articleId);
            entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        }
        articleLikeRepository.save(entity);
        return true;
    }
    public Boolean dislike(String articleId) {
        Optional<ArticleLikeEntity> optional=getArticleLike(articleId,SpringSecurityUtil.getCurrentUserId());
        ArticleLikeEntity entity=new ArticleLikeEntity();
        if(optional.isPresent()){
            entity=optional.get();
            if(entity.getStatus().equals(ArticleLiceStatus.DISLIKE)){
                System.out.println("IDDD:::"+entity.getId());
                int n=articleLikeRepository.deleteByArticleId(entity.getId());
                System.out.println("RESULT:::"+n);
                return n >0;
            }  else {
                entity.setStatus(ArticleLiceStatus.DISLIKE);
            }
        }else{
            entity.setStatus(ArticleLiceStatus.DISLIKE);
            entity.setVisible(Boolean.TRUE);
            entity.setArticleId(articleId);
            entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        }
        articleLikeRepository.save(entity);
        return true;
    }
    private Optional<ArticleLikeEntity>getArticleLike(String articleId, String profileId) {
        return articleLikeRepository.getByArticleIdAndProfileId(articleId, profileId);
    }


}
