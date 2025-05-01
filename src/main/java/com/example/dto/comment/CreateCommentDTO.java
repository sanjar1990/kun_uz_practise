package com.example.dto.comment;

import com.example.entity.article.ArticleEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentDTO {
    @NotBlank(message = "comment is required")
    private String content;
    @NotBlank(message = "Article id is required")
    private String articleId;
    private String replyId;
}
