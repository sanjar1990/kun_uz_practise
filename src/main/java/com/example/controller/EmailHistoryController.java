package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.enums.ProfileRole;
import com.example.service.EmailHistoryService;
import com.example.utility.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/emailHistory")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;
//       2. Get EmailHistory by email
@PreAuthorize("hasRole('ROLE_ADMIN')")
@GetMapping()
    public ResponseEntity<ApiResponseDTO>getEmailByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(emailHistoryService.getByEmail(email));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getByDate")
    public ResponseEntity<ApiResponseDTO> getEmailBetweenDate(@RequestParam("from")LocalDate from,
                                                              @RequestParam("to") LocalDate to,
                                                              @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                              @RequestParam(value = "size", defaultValue = "10")Integer size){
        return ResponseEntity.ok(emailHistoryService.getByDate(page-1,size,from,to));
    }
}
