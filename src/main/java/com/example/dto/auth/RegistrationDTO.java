package com.example.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
     @NotBlank(message = "Name should not be empty")
     @Size( min = 3, message = "length should be at least 3 character")
     private String name;
     private String surname;
     @Email(message = "email not valid")
     private String email;

     private String phone;
     private String password;
}
