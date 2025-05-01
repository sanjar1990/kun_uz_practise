package com.example.repository;

import com.example.dto.FilterResultDTO;
import com.example.dto.article.ArticleFilterPaginationDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ArticleCustomRepository {
    @Autowired
    private EntityManager entityManager;
    public FilterResultDTO<ArticleShortInfoDTO>articleShortInfoFilter(ArticleFilterPaginationDTO dto,Integer page,Integer Size) {
        StringBuilder selectBuilder=new StringBuilder("select new com.example.dto.article.ArticleShortInfoDTO(a.id,a.title,a.description," +
                "a.attachId,a.publishedDate)from ArticleEntity a");
        StringBuilder countBuilder=new StringBuilder("select count(a) from ArticleEntity a");
        StringBuilder stringBuilder=new StringBuilder(" where a.visible=true");
        Map<String,Object> params=new HashMap<>();
        if(dto.getId()!=null && !dto.getId().isEmpty()) {
            selectBuilder.append(" and a.id=:id");
            params.put("id",dto.getId());
        }
        if(dto.getTitle()!=null && !dto.getTitle().isEmpty()) {
            stringBuilder.append(" and a.title like :title");
            params.put("title","%"+dto.getTitle()+"%");
        }
        if(dto.getRegionId()!=null){
            stringBuilder.append(" and a.regionId=:regionId");
            params.put("regionId",dto.getRegionId());
        }
        if(dto.getCategoryId()!=null){
            stringBuilder.append(" and a.categoryId=:categoryId");
            params.put("categoryId",dto.getCategoryId());
        }
        if(dto.getCreatedDateFrom()!=null){
            stringBuilder.append(" and a.createdDate>=:cratedDateFrom");
            params.put("createdDateFrom",dto.getCreatedDateFrom().atStartOfDay());
        }
        if(dto.getCreatedDateTo()!=null){
            stringBuilder.append(" and a.createdDate<=:createdDateTo");
            params.put("createdDateTo", LocalDateTime.of(dto.getCreatedDateTo(), LocalTime.MAX));
        }
        if(dto.getPublishedDateFrom()!=null){
            stringBuilder.append(" and a.publishedDate>=:publishedDateFrom");
            params.put("publishedDateFrom",dto.getPublishedDateFrom().atStartOfDay());
        }
        if(dto.getPublishedDateTo()!=null){
            stringBuilder.append(" and a.publishedDate<=:publishedDateTo");
            params.put("publishedDateTo",LocalDateTime.of(dto.getPublishedDateTo(), LocalTime.MAX));
        }
        if(dto.getModeratorId()!=null){
            stringBuilder.append(" and a.moderatorId=:moderatorId");
            params.put("moderatorId",dto.getModeratorId());
        }
        if(dto.getPublisherId()!=null){
            stringBuilder.append(" and a.publisherId=:publisherId");
            params.put("publisherId",dto.getPublisherId());
        }
        if(dto.getStatus()!=null){
            stringBuilder.append(" and a.status=:status");
            params.put("status",dto.getStatus().name());
        }
        selectBuilder.append(stringBuilder);
        countBuilder.append(stringBuilder);
        selectBuilder.append(" order by a.createdDate desc");
        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setFirstResult(page*Size);
        selectQuery.setMaxResults(Size);
        Query countQuery = entityManager.createQuery(countBuilder.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
            selectQuery.setParameter(entry.getKey(), entry.getValue());
        }
        List<ArticleShortInfoDTO> resultList=selectQuery.getResultList();
        Long totalCount=(Long) countQuery.getSingleResult();
        return new FilterResultDTO<>(resultList,totalCount);
    }
}
