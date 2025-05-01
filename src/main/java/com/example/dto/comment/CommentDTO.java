package com.example.dto.comment;

import com.example.dto.BaseStringDTO;
import com.example.dto.article.ArticleDTO;
import com.example.dto.profileDTO.ProfileDTO;
import com.example.entity.article.ArticleEntity;
import com.example.entity.comment.CommentEntity;
import com.example.entity.profileEntity.ProfileEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO extends BaseStringDTO {
    private LocalDateTime updatedDate;
    private String profileId;
    private ProfileDTO profile;
    private String content;
    private String articleId;
    private ArticleDTO article;
    private String replyId;
    private CommentDTO reply;
}
