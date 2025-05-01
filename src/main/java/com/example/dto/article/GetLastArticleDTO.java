package com.example.dto.article;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetLastArticleDTO {
    private List<Integer> articleTypeList;
    private Integer limit;
    private String articleId;
}
