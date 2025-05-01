package com.example.dto.profileDTO;

import com.example.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileFilterPaginationDTO {
    private String name;
    private String surname;
    private String phone;
    private ProfileRole role;
    private LocalDate from;
    private LocalDate to;
}
