package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.example.dto.ApiResponseDTO;
import com.example.dto.emailHistory.EmailHistoryDTO;
import com.example.entity.emailHistory.EmailHistoryEntity;
import com.example.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    //1. Create EmailHistory when email is send using application.
    public void sendEmailHistory(String email, String message) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setEmail(email);
        entity.setMessage(message);
        entity.setVisible(Boolean.TRUE);
        emailHistoryRepository.save(entity);
    }

    public ApiResponseDTO getByEmail(String email) {
        List<EmailHistoryDTO> list = emailHistoryRepository
                .getByEmailAndVisibleTrueOrderByCreatedDateDesc(email).stream().map(this::toDTO).toList();
        return new ApiResponseDTO(false, list);
    }

    private EmailHistoryDTO toDTO(EmailHistoryEntity entity) {
        EmailHistoryDTO dto = new EmailHistoryDTO();
        dto.setEmail(entity.getEmail());
        dto.setMessage(entity.getMessage());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public ApiResponseDTO getByDate(int page, Integer size, LocalDate from, LocalDate to) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<EmailHistoryEntity> pageObj = emailHistoryRepository.getByCreatedDateBetween(from.atStartOfDay(),
                LocalDateTime.of(to, LocalTime.MAX), pageable);
       List<EmailHistoryDTO> dtoList= pageObj.getContent().stream().map(this::toDTO).toList();
        return new ApiResponseDTO(false, new PageImpl<EmailHistoryDTO>(dtoList, pageable, pageObj.getTotalElements()));
    }

    public Optional<EmailHistoryEntity> getByEmailLast(String email) {
       return emailHistoryRepository.getByEmailLast(email);
    }
}
