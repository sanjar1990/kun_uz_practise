package com.example.entity.smsHistory;

import com.example.entity.BaseStringEntity;
import com.example.enums.SmsStatus;
import com.example.enums.SmsType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_history")
public class SmsHistoryEntity extends BaseStringEntity {
    @Column(name = "sms_code")
    private String smsCode;
    @Column(name = "sms_type")
    @Enumerated(EnumType.STRING)
    private SmsType smsType;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SmsStatus status;
    @Column(name = "phone")
    private String phone;
    @Column(name = "sms_text", columnDefinition = "text")
    private String smsText;
}
