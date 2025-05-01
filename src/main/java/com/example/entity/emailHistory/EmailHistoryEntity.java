package com.example.entity.emailHistory;

import com.example.entity.BaseStringEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "email_history")
public class EmailHistoryEntity extends BaseStringEntity {
    @Column(columnDefinition = "text")
    private String message;
    @Column()
    private String email;
}
