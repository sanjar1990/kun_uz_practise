package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtDTO {
    private String phone;
    private ProfileRole role;
    private String id;
    public JwtDTO(String phone, ProfileRole role) {
        this.phone = phone;
        this.role = role;
    }
}
