package com.example.dto.article;

import com.example.entity.attach.AttachEntity;
import com.example.entity.category.CategoryEntity;
import com.example.entity.profileEntity.ProfileEntity;
import com.example.entity.region_entity.RegionEntity;
import com.example.enums.ArticleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateArticleDTO {
    private String title;
    private String description;
    private String content;
    private Integer categoryId;
    private String attachId;
    private Integer regionId;
    private List<String>tagList;
    @NotEmpty(message = "Type is required")

    private List<Integer>articleTypeList;
}
