package com.example.service;

import com.example.dto.article.ArticleDTO;
import com.example.dto.attach.AttachDTO;
import com.example.dto.saved_article.SavedArticleDTO;
import com.example.entity.article.ArticleEntity;
import com.example.entity.saved_article.SavedArticleEntity;
import com.example.repository.SavedArticleRepository;
import com.example.utility.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SavedArticleService {
    @Autowired
    private SavedArticleRepository savedArticleRepository;
    @Autowired
    private AttachService attachService;

    public Boolean saveArticle(String articleId) {
        SavedArticleEntity entity=new SavedArticleEntity();
        Optional<SavedArticleEntity> optional=savedArticleRepository
                .getByArticleIdAndProfileId(articleId,SpringSecurityUtil.getCurrentUserId());
        if(optional.isPresent()){
            entity=optional.get();
            if(entity.isVisible()){
                entity.setVisible(false);
            }else{
                entity.setVisible(true);
            }
        }else{
            entity.setArticleId(articleId);
            entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
            entity.setVisible(Boolean.TRUE);
        }
        savedArticleRepository.save(entity);
        return true;
    }

    public List<SavedArticleDTO> getByProfileId() {
        List<SavedArticleEntity>list=savedArticleRepository.getAllByProfileIdAndVisibleTrue(SpringSecurityUtil.getCurrentUserId());
        return list.stream().map(s->{
            SavedArticleDTO dto=new SavedArticleDTO();
            dto.setId(s.getId());
            ArticleDTO article=new ArticleDTO();
            article.setId(s.getArticle().getId());
            article.setTitle(s.getArticle().getTitle());
            article.setDescription(s.getArticle().getDescription());
            AttachDTO attachDTO=new AttachDTO();
            attachDTO.setId(s.getArticle().getAttach().getId());
            attachDTO.setUrl(attachService.getAttachUrl(s.getArticle().getAttach().getId()));
            article.setAttach(attachDTO);
            dto.setArticle(article);
            return dto;
        }).toList();
    }
}
