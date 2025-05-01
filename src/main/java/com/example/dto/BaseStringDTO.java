package com.example.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseStringDTO {
    private String id;
    private Boolean visible;
    private LocalDateTime createdDate;
    private String prtId;
}
