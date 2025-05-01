package com.example.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneVerificationDTO {
    private String phone;
    private String code;
}
