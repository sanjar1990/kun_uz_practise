package com.example.dto.article;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public interface ArticleFullInfoMapper {
   String getId();
   String getTitle();
   String getDescription();
    String getContent();
    Integer getSharedCount();
    Integer getViewCount();
    LocalDateTime getPublishedDate();
    String getAttachId();
    String getCategoryName();
    Integer getCategoryOrder();
    Integer getCategoryId();
    String getRegionName();
    Integer getRegionOrder();
    Integer getRegionId();

}
