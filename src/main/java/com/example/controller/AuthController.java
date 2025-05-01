package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.auth.AuthDTO;
import com.example.dto.auth.PhoneVerificationDTO;
import com.example.dto.auth.RegistrationDTO;
import com.example.dto.profileDTO.ProfileDTO;
import com.example.enums.Language;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
//    private final Logger log= LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO>login(@RequestBody AuthDTO dto,
                                               @RequestHeader(value = "Accept-Language", defaultValue = "uz")Language language){
        log.info("login authDTO: {}", dto);
        return ResponseEntity.ok(authService.login(dto,language));
    }
    @PostMapping("/registration/email")
    public ResponseEntity<ApiResponseDTO>emailRegistration(@Valid @RequestBody RegistrationDTO dto){
        log.warn("email registration dto: {}", dto);
        return ResponseEntity.ok(authService.emailRegistration(dto));
    }
    @PostMapping("/registration/phone")
    public ResponseEntity<ApiResponseDTO>phoneRegistration(@Valid @RequestBody RegistrationDTO dto){
        log.error("phone registration dto: {}", dto);
        return ResponseEntity.ok(authService.phoneRegistration(dto));
    }
    @GetMapping("/verification/email/{jwt}")
    public ResponseEntity<ApiResponseDTO>emailVerification(@PathVariable String jwt){
        return ResponseEntity.ok(authService.emailVerification(jwt));
    }
    @PostMapping("/verification/phone")
    public ResponseEntity<ApiResponseDTO>phoneVerification(@Valid @RequestBody PhoneVerificationDTO dto){
        return ResponseEntity.ok(authService.phoneVerification(dto));
    }
}
