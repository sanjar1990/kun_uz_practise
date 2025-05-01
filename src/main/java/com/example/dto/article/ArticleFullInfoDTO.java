package com.example.dto.article;

import com.example.dto.attach.AttachDTO;
import com.example.dto.category.CategoryDTO;
import com.example.dto.profileDTO.ProfileDTO;
import com.example.dto.region_dto.RegionDTO;
import com.example.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFullInfoDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private CategoryDTO category;
    private AttachDTO attach;
    private RegionDTO region;
    private ArticleStatus status;
    private Integer sharedCount;
    private Integer viewCount;
    private LocalDateTime publishedDate;
    private List<String> tagName;
}
