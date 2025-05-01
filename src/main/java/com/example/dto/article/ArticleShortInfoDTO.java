package com.example.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ArticleShortInfoDTO {
    private String id;
    private String title;
    private String description;
    private String attachId;
    private String imageUrl;
    private LocalDateTime publishedDate;

    public ArticleShortInfoDTO(String id, String title, String description, String attachId, LocalDateTime publishedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.attachId = attachId;
        this.publishedDate = publishedDate;
    }
}
