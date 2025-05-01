package com.example.controller;

import com.example.dto.sms.SmsHistoryDTO;
import com.example.service.SmsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/smsHistory/admin")
public class SmsHistoryController {
    @Autowired
    private SmsHistoryService smsHistoryService;

    //     2. Get sms history by phone(id, phone,message,status,type(if necessary),created_date)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{phone}")
    public ResponseEntity<List<SmsHistoryDTO>> getHistoryByPhone(@PathVariable("phone") String phone) {

        return ResponseEntity.ok(smsHistoryService.getByPhone(phone));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getByDate")
    public ResponseEntity<PageImpl<SmsHistoryDTO>> getByDate(@RequestParam("from") LocalDate from,
                                                             @RequestParam("to") LocalDate to,
                                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(smsHistoryService.getByDate(page - 1, size, from, to));
    }
}
