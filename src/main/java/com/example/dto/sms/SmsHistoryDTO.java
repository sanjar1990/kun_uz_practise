package com.example.dto.sms;

import com.example.entity.BaseStringEntity;
import com.example.enums.SmsStatus;
import com.example.enums.SmsType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsHistoryDTO extends BaseStringEntity {
    private String smsCode;
    private SmsType smsType;
    private SmsStatus status;
    private String phone;
    private String smsText;
}
