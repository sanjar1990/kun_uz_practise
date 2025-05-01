package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseIntDTO {
    private Integer id;
    private LocalDate createdDate;
    private Integer orderNum;
    private String nameEn;
    private String nameUz;
    private String nameRu;
    private String name;
    private String prtId;
    private Boolean visible;
}
