package com.example.dto.article;

import com.example.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleFilterPaginationDTO {
  private String id;
  private String title;
  private Integer regionId;
  private Integer categoryId;
  private LocalDate createdDateFrom;
  private  LocalDate createdDateTo;
  private LocalDate publishedDateFrom;
  private LocalDate publishedDateTo;
  private String moderatorId;
  private String publisherId;
  private ArticleStatus status;
}
