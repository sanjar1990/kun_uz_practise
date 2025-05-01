package com.example.dto.emailHistory;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailHistoryDTO {
    private String message;
    private String email;
    private LocalDateTime createdDate;
}
