package com.example.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FilterCommentDTO {
    private String id;
    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
    private LocalDate updatedDateFrom;
    private LocalDate updatedDateTo;
    private String profileId;
    private String content;
    private String articleId;
    private String replyId;
    private Boolean visible;
}
