package com.example.repository;

import com.example.dto.FilterResultDTO;
import com.example.dto.comment.FilterCommentDTO;
import com.example.entity.comment.CommentEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CommentCustomRepository {
    @Autowired
    private EntityManager entityManager;
    public FilterResultDTO<CommentEntity>commentFilter(FilterCommentDTO dto, int page, int size) {
        StringBuilder selectBuilder=new StringBuilder("select c from CommentEntity c ");
        StringBuilder countBuilder=new StringBuilder("select count(c) from CommentEntity c ");
        StringBuilder whereBuilder=new StringBuilder("where 1=1");
        Map<String,Object>params=new HashMap<>();
        if(dto.getId()!=null && !dto.getId().isBlank()){
            whereBuilder.append(" and c.id=:id ");
            params.put("id",dto.getId());
        }
        if(dto.getCreatedDateFrom()!=null){
            whereBuilder.append(" and c.createdDate >= :createdDateFrom ");
            params.put("createdDateFrom",dto.getCreatedDateFrom().atStartOfDay());
        }
        if(dto.getCreatedDateTo()!=null){
            whereBuilder.append(" and c.createdDate <= :createdDateTo ");
            params.put("createdDateTo",dto.getCreatedDateTo().atTime(LocalTime.MAX));
        }
        if(dto.getUpdatedDateFrom()!=null){
            whereBuilder.append(" and c.updatedDate >= :updatedDateFrom ");
            params.put("updatedDateFrom",dto.getUpdatedDateFrom().atStartOfDay());
        }
        if(dto.getUpdatedDateTo()!=null){
            whereBuilder.append(" and c.updatedDate <= :updatedDateTo ");
            params.put("updatedDateTo",dto.getUpdatedDateTo().atTime(LocalTime.MAX));
        }
        if(dto.getProfileId()!=null && !dto.getProfileId().isBlank()){
            whereBuilder.append(" and c.profileId=:profileId ");
            params.put("profileId",dto.getProfileId());
        }
        if(dto.getContent()!=null && !dto.getContent().isBlank()){
            whereBuilder.append(" and lower(c.content) like :content ");
            params.put("content","%"+dto.getContent().toLowerCase()+"%");
        }
        if(dto.getArticleId()!=null && !dto.getArticleId().isBlank()){
            whereBuilder.append(" and c.articleId=:articleId ");
            params.put("articleId",dto.getArticleId());
        }
        if(dto.getReplyId()!=null && !dto.getReplyId().isBlank()){
            whereBuilder.append(" and c.replyId=:replyId ");
            params.put("replyId",dto.getReplyId());
        }
        if(dto.getVisible()!=null){
            whereBuilder.append(" and c.visible=:visible ");
            params.put("visible",dto.getVisible());
        }
        selectBuilder.append(whereBuilder);
        selectBuilder.append(" order by c.createdDate desc ");
        countBuilder.append(whereBuilder);
        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setFirstResult((page)*size);
        selectQuery.setMaxResults(size);
        Query countQuery=entityManager.createQuery(countBuilder.toString());
        for (Map.Entry<String,Object> param : params.entrySet()) {
            countQuery.setParameter(param.getKey(),param.getValue());
            selectQuery.setParameter(param.getKey(),param.getValue());
        }
        List<CommentEntity> resultList=selectQuery.getResultList();
        Long totalCount=(Long)countQuery.getSingleResult();
        return new FilterResultDTO<>(resultList,totalCount);
    }
}
