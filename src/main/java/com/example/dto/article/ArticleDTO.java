package com.example.dto.article;
import com.example.dto.attach.AttachDTO;
import com.example.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer categoryId;
    private String attachId;
    private Integer regionId;
    private String moderatorId;
    private String publisherId;
    private ArticleStatus status;
    private LocalDateTime createdDate;
    private Integer sharedCount;
    private Integer viewCount;
    private LocalDateTime publishedDate;
    private String attachUrl;
    private String categoryName;
    private Integer categoryOrd;
    private String regionName;
    private Integer regionOrder;
    private List<String> tagName;
    private AttachDTO attach;
}
